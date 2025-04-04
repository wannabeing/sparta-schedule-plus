package org.example.spartascheduleplus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResponseExceptionProvider {
    /**
     * 🚀[예외] NotFound 상태와 메시지를 반환하는 메서드
     * @param message 사용자에게 전달하는 메시지
     * @return 예외를 반환
     */
    public static ResponseStatusException notFound(String message) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    }

    /**
     * 🚀[예외] Conflict 상태와 메시지를 반환하는 메서드
     * @param message 사용자에게 전달하는 메시지
     * @return 예외를 반환
     */
    public static ResponseStatusException conflict(String message) {
        return new ResponseStatusException(HttpStatus.CONFLICT, message);
    }

    /**
     * 🚀[예외] Unauthorized 상태와 메시지를 반환하는 메서드
     * @param message 사용자에게 전달하는 메시지
     * @return 예외를 반환
     */
    public static ResponseStatusException unauthorized(String message) {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, message);
    }

    /**
     * 🚀[예외] Forbidden 상태와 메시지를 반환하는 메서드
     * @param message 사용자에게 전달하는 메시지
     * @return 예외를 반환
     */
    public static ResponseStatusException forbidden(String message) {
        return new ResponseStatusException(HttpStatus.FORBIDDEN, message);
    }
}
