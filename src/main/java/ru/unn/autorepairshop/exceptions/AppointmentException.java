package ru.unn.autorepairshop.exceptions;

import lombok.Getter;

@Getter
public class AppointmentException extends RuntimeException {

    @Getter
    public enum CODE {

        REPETITION_OF_SERVICE_TYPES("Типы сервисных работ повторяются"),

        SIMILAR_WORKS_EXIST("Уже выполняются подобные работы"),

        APPOINTMENT_IS_NOT_EXIST("Нет заявки с таким id"),

        CAR_IS_ALREADY_OCCUPIED("Это машина принадлежит другому пользователю");

        final String codeDescription;

        CODE(String codeDescription) {
            this.codeDescription = codeDescription;
        }

        public AppointmentException get() {
            return new AppointmentException(this, this.codeDescription);
        }

        public AppointmentException get(String msg) {
            return new AppointmentException(this, this.codeDescription + " : " + msg);
        }

        public AppointmentException get(Throwable e) {
            return new AppointmentException(this, this.codeDescription + " : " + e.getMessage());
        }

        public AppointmentException get(Throwable e, String msg) {
            return new AppointmentException(this, e, this.codeDescription + " : " + msg);
        }
    }

    private final AppointmentException.CODE code;

    private AppointmentException(AppointmentException.CODE code, String msg) {
        this(code, null, msg);
    }

    private AppointmentException(AppointmentException.CODE code, Throwable e, String msg) {
        super(msg, e);
        this.code = code;
    }

}
