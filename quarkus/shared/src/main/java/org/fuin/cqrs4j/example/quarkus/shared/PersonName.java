package org.fuin.cqrs4j.example.quarkus.shared;

import jakarta.json.bind.adapter.JsonbAdapter;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import org.fuin.objects4j.common.ConstraintViolationException;
import org.fuin.objects4j.common.Immutable;
import org.fuin.objects4j.core.AbstractStringValueObject;
import org.fuin.objects4j.ui.Label;
import org.fuin.objects4j.ui.ShortLabel;
import org.fuin.objects4j.ui.Tooltip;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Name of a person.
 */
@ShortLabel(bundle = "ddd-cqrs-4-java-example", key = "PersonName.slabel", value = "PNAME")
@Label(bundle = "ddd-cqrs-4-java-example", key = "PersonName.label", value = "Person's name")
@Tooltip(bundle = "ddd-cqrs-4-java-example", key = "PersonName.tooltip", value = "Name of a person")
@Immutable
public final class PersonName extends AbstractStringValueObject {

    private static final long serialVersionUID = 1000L;

    /**
     * Max length of a person's name.
     */
    public static final int MAX_LENGTH = 100;

    @NotNull
    @PersonNameStr
    private String value;

    /**
     * Protected default constructor for deserialization.
     */
    protected PersonName() {
        super();
    }

    /**
     * Constructor with mandatory data.
     *
     * @param value Value.
     */
    public PersonName(final String value) {
        super();
        PersonName.requireArgValid("value", value);
        this.value = value;
    }

    @Override
    public final String asBaseType() {
        return value;
    }

    @Override
    public final String toString() {
        return value;
    }

    /**
     * Verifies that a given string can be converted into the type.
     *
     * @param value Value to validate.
     * @return Returns <code>true</code> if it's a valid type else <code>false</code>.
     */
    public static boolean isValid(final String value) {
        if (value == null) {
            return true;
        }
        if (value.length() == 0) {
            return false;
        }
        final String trimmed = value.trim();
        if (trimmed.length() > PersonName.MAX_LENGTH) {
            return false;
        }
        return true;
    }

    /**
     * Verifies if the argument is valid and throws an exception if this is not the case.
     *
     * @param name  Name of the value for a possible error message.
     * @param value Value to check.
     * @throws ConstraintViolationException The value was not valid.
     */
    public static void requireArgValid(@NotNull final String name, @NotNull final String value) throws ConstraintViolationException {

        if (!PersonName.isValid(value)) {
            throw new ConstraintViolationException("The argument '" + name + "' is not valid: '" + value + "'");
        }

    }

    /**
     * Ensures that the string can be converted into the type.
     */
    @Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = {Validator.class})
    @Documented
    public static @interface PersonNameStr {

        String message()

                default "{org.fuin.cqrs4j.example.javasecdi.PersonName.message}";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

    }

    /**
     * Validates if a string is compliant with the type.
     */
    public static final class Validator implements ConstraintValidator<PersonNameStr, String> {

        @Override
        public final void initialize(final PersonNameStr annotation) {
            // Not used
        }

        @Override
        public final boolean isValid(final String value, final ConstraintValidatorContext context) {
            return PersonName.isValid(value);
        }

    }

    /**
     * Converts the value object from/to string.
     */
    public static final class Adapter implements JsonbAdapter<PersonName, String> {

        @Override
        public final String adaptToJson(final PersonName obj) throws Exception {
            if (obj == null) {
                return null;
            }
            return obj.asBaseType();
        }

        @Override
        public final PersonName adaptFromJson(final String str) throws Exception {
            if (str == null) {
                return null;
            }
            return new PersonName(str);
        }

    }

}
