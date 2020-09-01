package com.example.demo;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {
    public Optional<User> findById(UUID id) {
        return Optional.of(new User());
    }

    public Optional<User> findByFirstNameAndLastName(String firstName, String lastName) {
        if (firstName.equals("firstName") && lastName.equals("lastName")) {
            return Optional.of(new User());
        }
        return Optional.empty();
    }

    public User save(User user) {
        return user;
    }
}
