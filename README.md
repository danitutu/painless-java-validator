# Painless Java Validator

A very simple Java validation tool that helps you implement validations. 

### Why another Java validation tool?

- easy to use, extend and customize
- no hidden magic
- lightweight, very small size
- written in plain Java
- framework agnostic
- parametrized messages
- predefined validators


- validations can be implemented in the business layer 
- data required by the validations can be shared with the rest of the business logic  
- validations can happen in the same transaction
- easy to achieve i18n on the client side

### Compatibility

Java 8+.

### Usage

```java
class Example {
    public void saveUser(User input) {
        List<Violation> validationResult = validate(asList(
                notNull("firstName", input.getFirstName()),
                notNull("lastName", input.getLastName()),
                notBlank("firstName", input.getFirstName()),
                notBlank("lastName", input.getLastName())
        )); 
        
        if (!inputFormatViolations.isEmpty()) {
            throw new ValidationException(validationResult);
        }
    
        // ...
    }
}
```

The `validate` function is part of the `ValidationEngine` 
and it receives a list of validation rules. Two predefined validation 
functions will be used as rules: `notNull` and `notBlank`. The predefined validation rules
can be found in `ValidationRule` class.
The `validate` function will execute each validation rule and returns a result containing 
the `Violation`s. Next a check will be made to see if there are any errors. 
If errors are present then a `ValidationException` exception will be raised 
which receives the validation result as input. 

### Best practices and solutions 

- Keep the validations in grouped where it makes sense and make use of the validation engine. The grouped validation 
can also be extracted into functions.
- Fail fast - if there is no reason to proceed with the business logic or other 
validations after some violations occurred, throw the validation exceptions
- Always keep the field format validations in the first part of your code and proceed to business validations only after those pass
- If violation messages need to be categorized then the `message` field can be used for that purpose: 
the messages can have the following format: `validation.errors.` and `validation.warnings.`
- If general messages (messages that cannot be attached to any field) will be needed then one new fields can be 
added regardless of the input; for example, when doing form validation in UI you may need to display a message at the top
of the form; for that you can add: `Violation.of("general", "validation.error.some.message", "Message details")`  
- Put complex validations in separate functions and give them names that describe their behaviour
- Some validation can be redundant; frequent redundant validations can be encountered in and between null, empty and blank checks, in this case blank covers the other two   

### Complex example

The following example is just to illustrate more situations and how they can be handled. 
The cleaner solution can be found below.
```java
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User updateUser(User input) {
        // basic example of ValidationEngine usage
        List<Violation> inputFormatViolations = validate(asList(
                notNull("input.firstName", input.getFirstName()),
                notNull("input.lastName", input.getLastName()),
                notBlank("input.firstName", input.getFirstName()),
                notBlank("input.lastName", input.getLastName())
        ));

        // "input." prefix is not required
        // nesting fields: "input.address.street"
        // lists naming suggestion: "input.addresses[]" or "input.addresses[0]"  

        if (!inputFormatViolations.isEmpty()) {
            // if basic format validation fails then stop
            throw new ValidationException(inputFormatViolations);
        }

        // other business logic logic
        // ...

        List<Violation> lengthValidations = validate(asList(
                lengthBetween("input.firstName", input.getFirstName(), 2, 50),
                lengthBetween("input.firstName", input.getFirstName(), 2, 50)
        ));

        if (!lengthValidations.isEmpty()) {
            // fail fast - there is no reason to go onward
            throw new ValidationException(lengthValidations);
        }

        // throwing validation errors inline
        User user = userRepository.findById(input.getId())
                .orElseThrow(() -> new ValidationException(
                        Violation.of(
                                "input.id",
                                "validation.error.user.not.found",
                                "User cannot be found."
                        )
                ));

        // custom complex validation - hide implementation by using a function
        validate(firstNameAndLastNameUniqueness(user));

        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setAge(input.getAge());

        return userRepository.save(user);
    }

    private Supplier<Optional<Violation>> firstNameAndLastNameUniqueness(User user) {
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
@Service
public class UserService {

    public User updateUserCompact(User input) {
        List<Violation> inputFormatViolations = validate(asList(
                // lengthBetween also requires a value to be present and so notNull and notBlank are redundant
                lengthBetween("input.firstName", input.getFirstName(), 2, 50),
                lengthBetween("input.firstName", input.getFirstName(), 2, 50)
        ));

        if (!inputFormatViolations.isEmpty()) {
            throw new ValidationException(inputFormatViolations);
        }

        User user = userRepository.findById(input.getId())
                .orElseThrow(() -> new ValidationException(
                        Violation.of(
                                "input.id",
                                "validation.error.user.not.found",
                                "User cannot be found."
                        )
                ));

        validate(firstNameAndLastNameUniqueness(user));

        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setAge(input.getAge());

        return userRepository.save(user);
    }

    private Supplier<Optional<Violation>> firstNameAndLastNameUniqueness(User user) {
        return () -> userRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName())
                .map(u -> Violation.of(
                        "general",
                        "validation.error.user.duplicate.name",
                        "The provided name is already used."
                ));
    }

}
```
