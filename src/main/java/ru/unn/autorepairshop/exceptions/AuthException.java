package ru.unn.autorepairshop.exceptions;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException{
    @Getter
    public enum CODE {

        JWT_VALIDATION_ERROR("Ошибка валидации JWT"),

        INVALID_REPEAT_PASSWORD("Пароли не совпадают"),

        REFRESH_TOKEN_IS_NULL("Refresh token is null"),

        EMAIL_IN_USE("Этот email уже используется");

        final String codeDescription;

        CODE(String codeDescription) {
            this.codeDescription = codeDescription;
        }

        public AuthException get() {
            return new AuthException(this, this.codeDescription);
        }

        public AuthException get(String msg) {
            return new AuthException(this, this.codeDescription + " : " + msg);
        }

        public AuthException get(Throwable e) {
            return new AuthException(this, this.codeDescription + " : " + e.getMessage());
        }

        public AuthException get(Throwable e, String msg) {
            return new AuthException(this, e, this.codeDescription + " : " + msg);
        }
    }

    private final CODE code;

    private AuthException(CODE code, String msg) {
        this(code, null, msg);
    }

    private AuthException(CODE code, Throwable e, String msg) {
        super(msg, e);
        this.code = code;
    }

}
