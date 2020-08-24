# Painless Java Validator

A very simple Java validation tool that helps you implement validations.

* [Why another Java validation tool?](#why-another-java-validation-tool)  
* [Compatibility](#compatibility)  
* [Usage](#usage)  
    * [Spring Web](#spring-web)
* [Docs](#docs)
    * [Validation Engine](#validationengine)
    * [Violation](#violation)
    * [Validation Rule](#validationrule)
    * [Violation Provider](#violationprovider)
    * [Validation Exception](#validationexception)
* [Best practices and solutions](#best-practices-and-solutions)
* [Complex example](#complex-example)
    

### Why another Java validation tool?

- easy to use, extend and customize
- no hidden magic
- lightweight, very small size
- written in plain Java
- framework agnostic
- parametrized messages
- predefined validators
- predefined validation modes

- validations can be implemented in the business layer 
- data required by the validations can be shared with the rest of the business logic  
- validations can happen in the same transaction
- easy to achieve i18n on the client side

### Compatibility

Java 8+.

### Usage

Add the library to your project dependencies:

Maven:

```xml
<dependency>
    <groupId>com.github.danitutu</groupId>
    <artifactId>painless-java-validator</artifactId>
    <version>1.0.0</version>
</dependency>
```

```java
import static com.github.danitutu.painlessjavavalidator.ValidationEngine.validateAllAndStopIfViolations;
import static com.github.danitutu.painlessjavavalidator.ValidationRule.*;

class Example {
    public void saveUser(User input) {
        validateAllAndStopIfViolations(
                notNull("firstName", input.getFirstName()),
                notNull("lastName", input.getLastName()),
                notBlank("firstName", input.getFirstName()),
                notBlank("lastName", input.getLastName())
        );
    
        // ...
    }
}
```

The `validateAllAndStopIfViolations` function is part of the 
`ValidationEngine` and it receives a list of validation rules. 
It will execute all validation rules and it will throw a 
`ValidationException` if any violations are found. Two 
predefined validation functions will be used as rules: 
`notNull` and `notBlank`. The result of the validation 
function is a list of `Violation`. 

### Integration

#### Spring Web

Adding the following `@ControllerAdvice` in your application will make sure each 
time a validation error appear, it will get handled by the framework and will return 
a BAD REQUEST response code. In the body there will be a `validationResult` 
containing the `Violation`s and a `type` containing the `VALIDATION-EXCEPTION` value. 
The purpose of the type is to be able to identify what problem has been encountered 
in this BAD REQUEST response.

```java
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ValidationException.class)
    protected ResponseEntity<Object> handleValidationException(
            ValidationException ex, WebRequest request) {
        List<Violation> violations = ex.getViolations();
        Map<String, Object> map = new HashMap<>();
        map.put("type", "VALIDATION-EXCEPTION");
        map.put("validationResult", violations);
        return handleExceptionInternal(
                ex,
                map,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request);
    }
}
```

If you need to customize the result of a `ValidationException` then the following snippet
may come in handy:

```java
@RestController
public class ApiController {
    @ExceptionHandler(value = {ValidationException.class})
    public List<Violation> handleConstraintViolation(ValidationException ex, WebRequest request) {
        return ex.getViolations();
    }
}
```

### Components

#### `ValidationEngine`
Handles the validation process and has two main ways of working:
1. "Execute all" rules and return the result using `validateAll` 
(or throw exception if violations are found using 
`validateAllAndStopIfViolations`)
2. "Find first" will search for the first violation and return 
it using `validateFindFirst` (or throw exception if violation 
is found using `validateFindFirstAndStopIfViolation`)

#### `Violation`
POJO used for transporting the data regarding the violation. 
Usually it is passed to the `ValidationException` when the 
process needs to be stopped. 

#### `ValidationRule`
Contains all predefined, general purpose rules. Rules return 
either `Violation`s either `ViolationProvider`s. The validation
logic can be found in the ones that return `Violation`s. 

#### `ViolationProvider`
A wrapper used as return type that gets well along with the 
`ValidationEngine`. It is also useful for cases when custom 
validations need to be implemented and they need to be used
inside the `ValidationEngine`.

#### `ValidationException`
Used to stop the execution and provide details about violations
to its clients.

### Practices and solutions 

- Group validations together and use the validation engine
- Fail fast - if a violation occurs and from the business 
point of view it doesn't make sense to continue then throw
exception
- Apply basic field content validations (in general the 
rules from the `ValidationRule` class) at the beginning 
of the method
- Put complex validations in separate functions in order 
to improve readability
- Categorize messages using the `message` field: 
`validation.errors.` and `validation.warnings.`. This 
case should be handled by the developer in a completely 
different way since warnings should not stop the execution 
flow. A warning can appear even in case of successful 
operations. One way to achieve this effect is to use 
a class where data and warnings can be added.
- Use a `general` violation message field when the 
validation is not meant for any of the existing fields 
or it's related to multiple fields; for example, when 
doing form validation in UI you may need to display a 
message at the top of the form; that can be done using
`Violation.of("general", "validation.error.some.message", 
"Message details")`  
- Avoid redundant validations (like null, empty and blank 
since blank covers the first two)
- Field naming suggestions:
    - maintain the level of nesting by separating each 
    field level by colon: `input.address.street`
    - `input.` prefix not required
    - lists or arrays: `input.addresses[]` or 
    `input.addresses[0]`

### Complex example

The following example is used to illustrate more usage scenarios 
and how they can be handled. A better approach can be found below.
```java
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
        validateFindFirstAndStopIfViolation(firstNameAndLastNameUniqueness(input));

        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setAge(input.getAge());

        return userRepository.save(user);
    }

    private ViolationProvider firstNameAndLastNameUniqueness(User user) {
        return () -> userRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName())
                .map(u -> Violation.of(
                        "general",
                        "validation.error.user.duplicate.name",
                        "The provided name is already used."
                ));
    }

}
```

A simplified version of the previous example:

```java
import com.github.danitutu.painlessjavavalidator.ValidationException;
import com.github.danitutu.painlessjavavalidator.Violation;
import com.github.danitutu.painlessjavavalidator.ViolationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.danitutu.painlessjavavalidator.ValidationEngine.validateAllAndStopIfViolations;
import static com.github.danitutu.painlessjavavalidator.ValidationEngine.validateFindFirstAndStopIfViolation;
import static com.github.danitutu.painlessjavavalidator.ValidationRule.lengthBetween;

@Service
public class CompactUserService {

    @Autowired
    private UserRepository userRepository;

    public User updateUser(User input) {
        // we are interested in returning all error at once
        validateAllAndStopIfViolations(
                // lengthBetween also requires a value to be present and so notNull and notBlank are redundant
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
        validateFindFirstAndStopIfViolation(firstNameAndLastNameUniqueness(input));

        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setAge(input.getAge());

        return userRepository.save(user);
    }

    private ViolationProvider firstNameAndLastNameUniqueness(User user) {
        return () -> userRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName())
                .map(u -> Violation.of(
                        "general",
                        "validation.error.user.duplicate.name",
                        "The provided name is already used."
                ));
    }
}
```

### Contributing

#### Deploying to sonatype

Beside the source code configuration the following setup will be needed

- gpg or pgp2 on system path
    - a key is required
    - in case you don't have a key
        - `gpg --gen-key`
        - `gpg --list-keys`
        - `gpg --keyserver hkp://pool.sks-keyservers.net --send-keys C6EED57A`
        - `gpg --keyserver hkp://pool.sks-keyservers.net --recv-keys C6EED57A`
- maven configuration `path/to/maven/config/.m2/settings.xml`

```xml
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>your-jira-id</username>
      <password>your-jira-pwd</password>
    </server>
  </servers>
  <profiles>
    <profile>
      <id>ossrh</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
        <gpg.executable>gpg</gpg.executable>
        <gpg.passphrase>the_pass_phrase</gpg.passphrase>
      </properties>
    </profile>
  </profiles>
</settings>
```

Then

- set new version: `mvn versions:set -DnewVersion=1.2.3`
- deploy:
    - SNAPSHOT: `mvn clean deploy`
    - RELEASE: `mvn clean deploy -P release`