package com.webcont.lumen_project_1.service;

import com.webcont.lumen_project_1.model.User;
import com.webcont.lumen_project_1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        // Set default role as USER if not specified
        if (user.getRole() == null) {
            user.setRole(User.Role.USER);
        }
        // Set default balance
        if (user.getBalance() == null) {
            user.setBalance(0.0);
        }
        return userRepository.save(user);
    }

    // Admin operations
    public List<User> getAllUsers() {
        List<User> users = userRepository.findByRole(User.Role.USER);
        // Clear passwords for security and avoid lazy loading issues
        users.forEach(user -> {
            user.setPassword(null);
            user.setRecharges(null); // Avoid lazy loading serialization issues
        });
        return users;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(updatedUser.getName());
            user.setPhone(updatedUser.getPhone());
            user.setAddress(updatedUser.getAddress());
            if (updatedUser.getBalance() != null) {
                user.setBalance(updatedUser.getBalance());
            }
            return userRepository.save(user);
        }
        throw new RuntimeException("User not found with id: " + id);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public List<User> searchUsers(String searchTerm) {
        return userRepository.searchUsersByNameOrEmail(searchTerm);
    }

    public Long getTotalUserCount() {
        return userRepository.countTotalUsers();
    }

    public User addBalance(Long userId, Double amount) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();
        user.setBalance(user.getBalance() + amount);
        return userRepository.save(user);
    }

    public List<User> getUsersByRole(User.Role role) {
        return userRepository.findByRole(role);
    }
}
