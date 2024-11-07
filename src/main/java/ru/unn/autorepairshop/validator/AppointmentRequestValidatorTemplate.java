package ru.unn.autorepairshop.validator;

import ru.unn.autorepairshop.domain.dto.request.AppointmentCreateRequestDto;
import ru.unn.autorepairshop.exceptions.AppointmentException;

public abstract class AppointmentRequestValidatorTemplate {

    public final void validate(AppointmentCreateRequestDto request, String email) {
        validateUserOwnership(request, email);
        validateNoSimilarWorksExist(request);
    }

    protected abstract void validateUserOwnership(AppointmentCreateRequestDto request, String email);

    protected abstract void validateNoSimilarWorksExist(AppointmentCreateRequestDto request);

    protected void throwValidationError(AppointmentException.CODE code) {
        throw code.get();
    }

}
