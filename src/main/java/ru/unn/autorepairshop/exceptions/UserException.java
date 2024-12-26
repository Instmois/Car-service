package ru.unn.autorepairshop.exceptions;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    @Getter
    public enum CODE {

        NO_SUCH_USER_ID("Нет пользователя с таким id"),

        NO_SUCH_USER_EMAIL("Нет пользователя с таким email"),

        EMAIL_IN_USE("Такой email уже используется"),

        USER_IS_NOT_A_MANAGER("У пользователя нет роли менеджера"),

        USER_IS_NOT_A_CLIENT("У пользователя нет роли клиента");

        final String codeDescription;

        CODE(String codeDescription) {
            this.codeDescription = codeDescription;
        }

        public UserException get() {
            return new UserException(this, this.codeDescription);
        }

        public UserException get(String msg) {
            return new UserException(this, this.codeDescription + " : " + msg);
        }

        public UserException get(Throwable e) {
            return new UserException(this, this.codeDescription + " : " + e.getMessage());
        }

        public UserException get(Throwable e, String msg) {
            return new UserException(this, e, this.codeDescription + " : " + msg);
        }
    }

    private final UserException.CODE code;

    private UserException(UserException.CODE code, String msg) {
        this(code, null, msg);
    }

    private UserException(UserException.CODE code, Throwable e, String msg) {
        super(msg, e);
        this.code = code;
    }

}
