package org.example.spartascheduleplus.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.http.HttpServletRequest;
import org.example.spartascheduleplus.dto.api.ErrorResponseDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice // @ControllerAdvice + @ResponseBody
public class ApiExceptionHandler {
    /**
     * [예외] 유효하지 않은 URL 경로로 접근하려는 경우 호출되는 메서드
     * @param exception 유효하지 않은 URL 입력 시 발생한 예외 객체
     * @return 에러 메시지와 에러 상태코드 반환
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundUrlException(
            NoHandlerFoundException exception)
    {
        ErrorResponseDto responseDto = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getRequestURL(),
                "유효하지 않은 URL 입니다. 다시 한번 확인해주세요.",
                null
        );
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
    }

    /**
     * [예외] 요청 데이터 유효성 검사 실패시 호출되는 메서드
     * @param exception 유효성 검사 실패로 발생한 예외 객체
     * @return 유효성 검사 실패한 필드와 메시지, 에러 상태 코드 반환
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest httpRequest)
    {
        // 에러 필드 메시지 리스트
        List<ErrorResponseDto.FieldError> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorResponseDto.FieldError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        ErrorResponseDto responseDto = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                httpRequest.getRequestURI(),
                "유효성 검사에 실패하였습니다.",
                fieldErrors
        );
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * [예외] 서비스 또는 컨트롤러에서 예외를 던졌을 경우 호출되는 메서드
     * @param exception 호출된 예외 객체
     * @return 에러 메시지와 에러 상태코드 반환
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDto> handleResponseStatusException(
            ResponseStatusException exception,
            HttpServletRequest httpRequest)
    {
        // 예외 상태코드로 HttpStatus 객체 생성
        HttpStatus status = HttpStatus.resolve(exception.getStatusCode().value());
        if (status == null) {
            status = HttpStatus.BAD_REQUEST; // null 일 경우, 기본 값 설정
        }


        // 기본 메시지 설정
        String message = "유효하지 않은 요청입니다.";

        // 상태 코드에 따라 메시지 설정
        switch (status) {
            case FORBIDDEN -> message = "접근 권한이 없습니다.";
            case NOT_FOUND -> message = "요청하신 데이터를 찾을 수 없습니다.";
            case CONFLICT -> message = "충돌되는 요청입니다.";
        }

        // 에러 필드 메시지 리스트
        List<ErrorResponseDto.FieldError> fieldErrors = List.of(
                new ErrorResponseDto.FieldError("error",
                        exception.getReason()
                )
        );

        ErrorResponseDto responseDto = new ErrorResponseDto(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                httpRequest.getRequestURI(),
                message,
                fieldErrors
        );
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
    }

    /**
     * [예외] 파라미터 타입을 전달 받았을 경우 호출되는 메서드
     * @param exception 파라미터 타입을 전달 받았을 경우에 발생한 예외 객체
     * @return 에러 메시지와 에러코드 반환
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatchException(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest httpRequest)
    {
        // 에러 필드 메시지 리스트
        List<ErrorResponseDto.FieldError> fieldErrors = List.of(
                new ErrorResponseDto.FieldError(
                        "error",
                        String.format("파라미터 '%s'의 타입이 올바르지 않습니다",
                                exception.getName()
                        )
                )
        );

        ErrorResponseDto responseDto = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                httpRequest.getRequestURI(),
                "유효하지 않은 입력 값입니다.",
                fieldErrors
        );
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * [예외] 사용자 요청 JSON 을 제대로 읽을 수 없을 때 호출되는 메서드
     * @param exception 요청 JSON 을 읽을 수 없을 때 호출되는 예외 객체
     * @return 에러 메시지와 에러코드 반환
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidJson(
            HttpMessageNotReadableException exception,
            HttpServletRequest httpRequest)
    {
        // FIXME: 추후 정확히 이해하고 갈 부분
        // 요청 JSON 에서 필드 값이 존재하지 않을 경우 호출되는 메서드
        Throwable cause = exception.getCause();
        if (cause instanceof UnrecognizedPropertyException unrecognizedPropertyException) {
            return handleUnrecognizedPropertyException(unrecognizedPropertyException, httpRequest);
        }

        // 에러 필드 메시지 리스트
        List<ErrorResponseDto.FieldError> fieldErrors = List.of(
                new ErrorResponseDto.FieldError(
                        "error",
                        "유효하지 않은 입력 값입니다."
                )
        );

        ErrorResponseDto responseDto = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                httpRequest.getRequestURI(),
                "RequestBody JSON 형식이 올바르지 않습니다. 형식을 다시 확인해주세요.",
                fieldErrors
        );
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * [예외] 요청한 정보가 이미 존재할 때 호출되는 메서드 (ex. 이메일 중복)
     * @param exception 중복한 값이 있을 때 호출되는 예외 객체
     * @param httpRequest Http 요청 객체
     * @return API 에러 응답 객체 반환
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolation(
            DataIntegrityViolationException exception,
            HttpServletRequest httpRequest
    ) {
        // 기본 메시지 설정
        String message = "중복된 값이 존재합니다.";
        Throwable rootCause = exception.getRootCause();
        if(rootCause != null ){
            message = rootCause.getMessage();
        }

        // 에러 필드 메시지 리스트
        List<ErrorResponseDto.FieldError> fieldErrors = List.of(
                new ErrorResponseDto.FieldError("error", message)
        );

        ErrorResponseDto responseDto = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                httpRequest.getRequestURI(),
                "요청이 충돌하였습니다.",
                fieldErrors
        );

        return new ResponseEntity<>(responseDto, HttpStatus.CONFLICT);
    }

    /**
     * 🚀 UnrecognizedPropertyException 예외를 처리하는 메서드
     * 요청 JSON 에서 유효하지 않으면 발생하는 예외이다.
     * @param exception JSON 파싱 중 발생한 예외 객체
     * @param httpRequest Http 요청 객체
     * @return API 에러 응답 객체
     */
    private ResponseEntity<ErrorResponseDto> handleUnrecognizedPropertyException(
            UnrecognizedPropertyException exception,
            HttpServletRequest httpRequest)
    {
        // 유효하지 않은 필드
        String invalidField = exception.getPropertyName();

        // 에러 필드 메시지 리스트
        List<ErrorResponseDto.FieldError> fieldErrors = List.of(
                new ErrorResponseDto.FieldError(
                        invalidField,
                        String.format("유효하지 않은 필드 '%s'가 포함되어 있습니다.", invalidField)
                )
        );

        ErrorResponseDto responseDto = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                httpRequest.getRequestURI(),
                "유효하지 않은 입력 값입니다.",
                fieldErrors
        );
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

}

