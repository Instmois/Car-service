package ru.unn.autorepairshop.exceptions;

import lombok.Getter;

@Getter
public class VehicleException extends RuntimeException{

    @Getter
    public enum CODE {

        NO_SUCH_VEHICLE_BY_LICENSE_PLATE("Транспорт с таким номером не найден");

        final String codeDescription;

        CODE(String codeDescription) {
            this.codeDescription = codeDescription;
        }

        public VehicleException get() {
            return new VehicleException(this, this.codeDescription);
        }

        public VehicleException get(String msg) {
            return new VehicleException(this, this.codeDescription + " : " + msg);
        }

        public VehicleException get(Throwable e) {
            return new VehicleException(this, this.codeDescription + " : " + e.getMessage());
        }

        public VehicleException get(Throwable e, String msg) {
            return new VehicleException(this, e, this.codeDescription + " : " + msg);
        }
    }

    private final CODE code;

    private VehicleException(CODE code, String msg) {
        this(code, null, msg);
    }

    private VehicleException(CODE code, Throwable e, String msg) {
        super(msg, e);
        this.code = code;
    }

}
