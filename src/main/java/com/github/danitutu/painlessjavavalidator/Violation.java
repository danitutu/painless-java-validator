package com.github.danitutu.painlessjavavalidator;

import java.util.Map;
import java.util.Objects;

public class Violation {
    /**
     * Path to field. Each level is separated by a colon.
     * Example: <code>user.account.email</code>
     */
    private final String fieldPath;
    /**
     * A message describing the validation result. A good message handling approach is to let the clients
     * decide how they should handle a message. For that it would be best to return something
     * like a message key and then the client can translate it in different languages or
     * display the message and use the parameters however they like.
     * <p>
     * Examples: <br/>
     * <code>
     * validation.error.user.account.email.duplicate
     * validation.warning.engine.previous.configuration.clear
     * </code>
     * </p>
     */
    private final String message;
    /**
     * A field where the message can be explained. This can be used for giving details about a
     * the message, maybe explain it, or even give something like a default translation for it.
     */
    private final String details;
    /**
     * Anything related to the violation can be added here. One good purpose of the field is to
     * send the parameters of the message. For that you can use the key as identifier.
     */
    private final Map<String, Object> attributes;

    private Violation(String fieldPath, String message, String details, Map<String, Object> attributes) {
        this.fieldPath = fieldPath;
        this.message = message;
        this.details = details;
        this.attributes = attributes;
    }

    public static Violation of(String fieldPath, String message, String details, Map<String, Object> attributes) {
        return new Violation(fieldPath, message, details, attributes);
    }

    public static Violation of(String fieldPath, String message, String details) {
        return of(fieldPath, message, details, null);
    }

    public String getFieldPath() {
        return fieldPath;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Violation violation = (Violation) o;
        return Objects.equals(fieldPath, violation.fieldPath) &&
                Objects.equals(message, violation.message) &&
                Objects.equals(details, violation.details) &&
                Objects.equals(attributes, violation.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldPath, message, details, attributes);
    }
}
