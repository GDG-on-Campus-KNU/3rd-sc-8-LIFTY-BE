package gdsc.sc8.LIFTY.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus {

    /*
     * 400 Bad Request
     */
    WRONG_LOGIN_INFO_EXCEPTION(HttpStatus.BAD_REQUEST, "로그인 정보가 잘못되었습니다."),
    ALREADY_EXIST_EMAIL_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),

    /*
     * 401 Unauthorized
     */

    /*
     * 403 Forbidden
     */

    /*
     * 404 Not Found
     */

    /*
     * 500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatus() {
        return httpStatus.value();
    }
}
