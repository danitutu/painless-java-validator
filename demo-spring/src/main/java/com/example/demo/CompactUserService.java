package com.example.demo;

import com.github.danitutu.painlessjavavalidator.ValidationException;
import com.github.danitutu.painlessjavavalidator.Violation;
import com.github.danitutu.painlessjavavalidator.ViolationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.danitutu.painlessjavavalidator.ValidationEngine.validateAllAndStopIfViolations;
import static com.github.danitutu.painlessjavavalidator.ValidationEngine.validateFindFirstAndStopIfViolation;
import static com.github.danitutu.painlessjavavalidator.ValidationRule.*;

@Service
public class CompactUserService {

    @Autowired
    private UserRepository userRepository;

    public User updateUser(User input) {
        // we are interested in returning all error at once
        validateAllAndStopIfViolations(
                notBlank("input.firstName", input.getFirstName()),
                notBlank("input.lastName", input.getLastName()),
                lengthBetween("input.firstName", input.getFirstName(), 2, 50),
                lengthBetween("input.lastName", input.getLastName(), 2, 50)
        );

        User user = userRepository.findById(input.getId())
                .orElseThrow(() -> new ValidationException(
                        Violation.of(
                                "input.id",
                                "validation.error.user.not.found",
                                "User cannot be found."
                        )
                ));

        // we would like to stop the processing if the following error occurs
        validateFindFirstAndStopIfViolation(userFullNameIsUnique(input));

        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setAge(input.getAge());

        return userRepository.save(user);
    }

    private ViolationProvider userFullNameIsUnique(User user) {
        return isFalse(
                () -> userRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName()).isPresent(),
                Violation.of(
                        "general",
                        "validation.error.user.duplicate.name",
                        "The provided name is already used."
                ));
    }
}