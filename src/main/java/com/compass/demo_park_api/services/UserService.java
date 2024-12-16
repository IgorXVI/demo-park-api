package com.compass.demo_park_api.services;

import com.compass.demo_park_api.entities.User;
import com.compass.demo_park_api.errors.ConfirmPasswordError;
import com.compass.demo_park_api.errors.NotFoundError;
import com.compass.demo_park_api.errors.UserNameUniqueError;
import com.compass.demo_park_api.errors.WrongPasswordError;
import com.compass.demo_park_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User save(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new UserNameUniqueError();
        }
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(NotFoundError::new);
    }

    public void updatePassword(
            Long id,
            String password,
            String newPassword,
            String confirmNewPassword
    ) {
        if (!newPassword.equals(confirmNewPassword)) {
            throw new ConfirmPasswordError();
        }

        User user = userRepository.findById(id).orElseThrow(NotFoundError::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new WrongPasswordError();
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(NotFoundError::new);
    }

    public User.Role getRoleByUsername(String username) {
        return userRepository.findRoleByUsername(username);
    }
}
