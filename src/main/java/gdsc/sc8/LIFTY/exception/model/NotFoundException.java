package gdsc.sc8.LIFTY.exception.model;

import gdsc.sc8.LIFTY.exception.ErrorStatus;

public class NotFoundException extends CustomException{

    public NotFoundException(ErrorStatus errorStatus,
        String message) {
        super(errorStatus, message);
    }

}
