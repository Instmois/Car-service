package ru.unn.autorepairshop.controller.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.unn.autorepairshop.exceptions.AppointmentException;
import ru.unn.autorepairshop.exceptions.AuthException;
import ru.unn.autorepairshop.exceptions.UserException;
import ru.unn.autorepairshop.exceptions.VehicleException;
import ru.unn.autorepairshop.exceptions.message.ErrorMessage;
import ru.unn.autorepairshop.exceptions.message.ValidationErrorMessage;
import ru.unn.autorepairshop.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static ru.unn.autorepairshop.utils.StringUtil.trimToFirstDot;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> handleAuthException(AccessDeniedException ex) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String message = ex.getMessage() + "для пользователя с email: %s"
                .formatted(email);

        return ResponseEntity
                .status(FORBIDDEN)
                .body(new ErrorMessage(FORBIDDEN.name(), message));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorMessage> handleAuthException(AuthException e) {
        AuthException.CODE code = e.getCode();
        HttpStatus status = switch (code) {
            case JWT_VALIDATION_ERROR -> HttpStatus.UNAUTHORIZED;
            case INVALID_REPEAT_PASSWORD,
                    REFRESH_TOKEN_IS_NULL,
                    INVALID_OLD_PASSWORD,
                    OLD_PASSWORD_EQUALS_TO_NEW_PASSWORD -> HttpStatus.BAD_REQUEST;
            case EMAIL_IN_USE -> HttpStatus.CONFLICT;
        };
        String codeStr = code.toString();
        return ResponseEntity
                .status(status)
                .body(new ErrorMessage(codeStr, e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorMessage> handleAuthException(AuthenticationException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<ErrorMessage> handleAuthException(HttpClientErrorException.Unauthorized e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorMessage(HttpStatus.UNAUTHORIZED.name(), e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorMessage> handleValidationException(MethodArgumentNotValidException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String codeStr = "INCORRECT_INPUT";

        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));

        ValidationErrorMessage errorMessage = new ValidationErrorMessage(
                codeStr,
                "Ошибка валидации",
                errors
        );

        return ResponseEntity
                .status(status)
                .body(errorMessage);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ValidationErrorMessage> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String codeStr = "INCORRECT_INPUT";

        Map<String, String> errors = new HashMap<>();
        errors.put(e.getName(), StringUtil.shortenStringToSemicolon(e.getMessage()));

        ValidationErrorMessage errorMessage = new ValidationErrorMessage(
                codeStr,
                "Ошибка валидации: неправильный тип аргумента",
                errors
        );

        return ResponseEntity
                .status(status)
                .body(errorMessage);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorMessage> handleConstraintViolationException(ConstraintViolationException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String codeStr = "INCORRECT_INPUT";

        Map<String, String> errors = e.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        violation -> trimToFirstDot(violation.getPropertyPath().toString()),
                        ConstraintViolation::getMessage
                ));

        ValidationErrorMessage errorMessage = new ValidationErrorMessage(
                codeStr,
                "Ошибка валидации",
                errors
        );

        return ResponseEntity
                .status(status)
                .body(errorMessage);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorMessage> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String codeStr = "MISSING_PARAMETER";

        String message = String.format("Параметр '%s' отсутствует", e.getParameterName());

        ErrorMessage errorMessage = new ErrorMessage(
                codeStr,
                message
        );

        return ResponseEntity.status(status).body(errorMessage);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorMessage> handleUserException(UserException e) {
        UserException.CODE code = e.getCode();
        HttpStatus status = switch (code) {
            case NO_SUCH_USER_ID, NO_SUCH_USER_EMAIL -> HttpStatus.NOT_FOUND;
            case USER_IS_NOT_A_CLIENT, USER_IS_NOT_A_MANAGER -> HttpStatus.BAD_REQUEST;
            case EMAIL_IN_USE -> HttpStatus.CONFLICT;
        };
        String codeStr = code.toString();
        return ResponseEntity
                .status(status)
                .body(new ErrorMessage(codeStr, e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(VehicleException.class)
    public ResponseEntity<ErrorMessage> handleVehicleException(VehicleException e) {
        VehicleException.CODE code = e.getCode();
        HttpStatus status = switch (code) {
            case NO_SUCH_VEHICLE_BY_LICENSE_PLATE -> HttpStatus.NOT_FOUND;
        };
        String codeStr = code.toString();
        return ResponseEntity
                .status(status)
                .body(new ErrorMessage(codeStr, e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AppointmentException.class)
    public ResponseEntity<ErrorMessage> handleAppointmentException(AppointmentException e) {
        AppointmentException.CODE code = e.getCode();
        HttpStatus status = switch (code) {
            case REPETITION_OF_SERVICE_TYPES,
                    SIMILAR_WORKS_EXIST,
                    WRONG_DATE,
                    APPOINTMENT_IS_NOT_IN_PROGRESS,
                    PART_ORDERS_IS_NOT_DELIVERED,
                    APPOINTMENT_IS_DONE,
                    MECHANIC_IS_NOT_ASSIGNED -> HttpStatus.BAD_REQUEST;
            case APPOINTMENT_IS_NOT_EXIST -> HttpStatus.NOT_FOUND;
            case CAR_IS_ALREADY_OCCUPIED -> FORBIDDEN;
        };
        String codeStr = code.toString();
        return ResponseEntity
                .status(status)
                .body(new ErrorMessage(codeStr, e.getMessage()));
    }

}