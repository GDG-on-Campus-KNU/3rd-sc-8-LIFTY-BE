package gdsc.sc8.LIFTY.exception.model;

import gdsc.sc8.LIFTY.exception.ErrorStatus;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorStatus errorStatus;

    public CustomException(ErrorStatus errorStatus, String message) {
        super(message);
        this.errorStatus = errorStatus;
    }

    public int getHttpStatus() {
        return errorStatus.getHttpStatus();
    }

}
