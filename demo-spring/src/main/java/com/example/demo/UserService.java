package com.example.demo;

import com.github.danitutu.painlessjavavalidator.ValidationException;
import com.github.danitutu.painlessjavavalidator.Violation;
import com.github.danitutu.painlessjavavalidator.ViolationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.github.danitutu.painlessjavavalidator.ValidationEngine.*;
import static com.github.danitutu.painlessjavavalidator.ValidationException.stopIfViolations;
import static com.github.danitutu.painlessjavavalidator.ValidationRule.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User updateUser(User input) {
        // basic example of ValidationEngine usage
        List<Violation> inputFormatViolations = validateAll(
                notNull("input.firstName", input.getFirstName()),
                notNull("input.lastName", input.getLastName()),
                notBlank("input.firstName", input.getFirstName()),
                notBlank("input.lastName", input.getLastName())
        );

        stopIfViolations(inputFormatViolations);

        // other business logic logic
        // ...

        List<Violation> lengthValidations = validateFindFirst(
                lengthBetween("input.firstName", input.getFirstName(), 2, 50),
                lengthBetween("input.lastName", input.getLastName(), 2, 50)
        );

        // similar to what stopIfViolations does
        if (!lengthValidations.isEmpty()) {
            // fail fast - there is no reason to go onward
            throw new ValidationException(lengthValidations);
        }

        // inline validation
        User user = userRepository.findById(input.getId())
                .orElseThrow(() -> new ValidationException(
                        Violation.of(
                                "input.id",
                                "validation.error.user.not.found",
                                "User cannot be found."
                        )
                ));

        // custom complex validation (contains lots of code) - hide implementation by using a function
        validateFindFirstAndStopIfViolation(userFullNameIsUnique(input));

        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setAge(input.getAge());

        return userRepository.save(user);
    }

    private ViolationProvider userFullNameIsUnique(User user) {
        return () -> userRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName())
                .map(u -> Violation.of(
                        "general",
                        "validation.error.user.duplicate.name",
                        "The provided name is already used."
                ));
    }

}