package org.glagan.Artefact.Validator;

import org.glagan.Artefact.Artefact;
import org.glagan.Artefact.ArtefactSlot;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CheckSlotValidator implements ConstraintValidator<CheckSlot, Artefact> {
    private ArtefactSlot slot;

    @Override
    public void initialize(CheckSlot constraintAnnotation) {
        this.slot = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Artefact object, ConstraintValidatorContext constraintContext) {
        if (object == null) {
            return true;
        }

        boolean isValid = object.getSlot().equals(slot);

        if (!isValid) {
            constraintContext.disableDefaultConstraintViolation();
            constraintContext.buildConstraintViolationWithTemplate(
                    "{org.glagan.Artefact.Validator.CheckSlot.message}")
                    .addConstraintViolation();
        }

        return isValid;
    }
}