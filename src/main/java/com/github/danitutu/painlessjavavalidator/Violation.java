package com.github.danitutu.painlessjavavalidator;

import java.util.Map;
import java.util.Objects;

public class Violation {
  /**
   * Path to field. Each level is separated by a colon. Example: <code>user.account.email</code>
   */
  private final String field;
  /**
   * A message describing the validation result. A good message handling approach is to let the
   * clients decide how they should handle a message. For that it would be best to return something
   * like a message key and then the client can translate it in different languages or display the
   * message and use the parameters however they like.
   *
   * <p>Examples: <br>
   * <code>
   * validation.error.user.account.email.duplicate
   * validation.warning.engine.previous.configuration.clear
   * </code>
   */
  private final String message;
  /**
   * A field where the message can be explained. This can be used for giving details about a the
   * message, maybe explain it, or even give something like a default translation for it.
   */
  private final String details;
  /**
   * Anything related to the violation can be added here. One good purpose of the field is to send
   * the parameters of the message. For that you can use the key as identifier.
   */
  private final Map<String, Object> attributes;

  private Violation(String field, String message, String details, Map<String, Object> attributes) {
    this.field = field;
    this.message = message;
    this.details = details;
    this.attributes = attributes;
  }

  public static Violation of(String field, String message, String details) {
    return of(field, message, details, null);
  }

  public static Violation of(
          String field, String message, String details, Map<String, Object> attributes) {
    return new Violation(field, message, details, attributes);
  }

  public String getField() {
    return field;
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
  public int hashCode() {
    return Objects.hash(field, message, details, attributes);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Violation violation = (Violation) o;
    return Objects.equals(field, violation.field)
            && Objects.equals(message, violation.message)
            && Objects.equals(details, violation.details)
            && Objects.equals(attributes, violation.attributes);
  }
}
