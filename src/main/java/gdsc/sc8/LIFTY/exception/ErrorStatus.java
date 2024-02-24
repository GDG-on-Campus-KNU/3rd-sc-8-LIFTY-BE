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
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    WRONG_LOGIN_INFO_EXCEPTION(HttpStatus.BAD_REQUEST, "로그인 정보가 잘못되었습니다."),
    ALREADY_EXIST_EMAIL_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),
    NO_IMAGE(HttpStatus.BAD_REQUEST, "이미지가 없습니다."),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "올바르지 않은 파일 형식입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "만료된 토큰입니다."),

    /*
     * 401 Unauthorized
     */

    /*
     * 403 Forbidden
     */

    /*
     * 404 Not Found
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    KEYWORD_NOT_FOUND(HttpStatus.NOT_FOUND,"키워드가 존재하지 않습니다."),

    /*
     * 500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    RECEIVE_RESPONSE_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,"생성모델 호출에 실패했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
