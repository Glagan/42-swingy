package org.glagan.Artefact.Validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.glagan.Artefact.ArtefactSlot;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = CheckSlotValidator.class)
@Documented
public @interface CheckSlot {
    String message() default "contains an item that can't fit in this slot";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    ArtefactSlot value();

    @Target({ FIELD, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ArtefactSlot[] value();
    }
}
