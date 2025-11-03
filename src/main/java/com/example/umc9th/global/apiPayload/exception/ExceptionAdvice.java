package com.example.umc9th.global.apiPayload.exception;


import com.example.umc9th.global.apiPayload.ApiResponse;
import com.example.umc9th.global.apiPayload.code.ErrorReasonDTO;
import com.example.umc9th.global.apiPayload.code.status.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@RestControllerAdvice(annotations = {RestController.class}) // 전역 예외 처리기임을 선언하고, @RestController가 붙은 클래스들만 대상으로
// ResponseEntityExceptionHandler: Spring MVC의 기본 예외 처리
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    /**
     * @Validated 어노테이션으로 인한 유효성 검사 실패 시 (주로 @RequestParam, @PathVariable) 이 핸들러가 호출
     */
    @ExceptionHandler
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
        // 여러 에러 메세지 중 첫번째 메세지만
        String errorMessage = e.getConstraintViolations().stream()
                .map(constraintViolation -> constraintViolation.getMessage())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ConstraintViolationException 추출 도중 에러 발생"));

        return handleExceptionInternalConstraint(e, ErrorStatus.valueOf(errorMessage), HttpHeaders.EMPTY,request);
    }

    /**
     * @Valid 어노테이션으로 인한 유효성 검사 실패 시 (주로 @RequestBody DTO) 이 핸들러가 호출
     * Spring의 기본 핸들러를 오버라이드
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        // 유효성 검사에 실패한 필드와 에러 메시지를 저장할 Map
        Map<String, String> errors = new LinkedHashMap<>();
        // 예외 객체(e)의 BindingResult에서 모든 필드 에러(FieldErrors)를 가져와 스트림으로 처리
        e.getBindingResult().getFieldErrors().stream()
                .forEach(fieldError -> {
                    String fieldName = fieldError.getField(); // 에러가 발생한 필드 이름
                    String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse(""); // 에러에 설정된 기본 메세지
                    // Map에 필드 이름(key)과 에러 메시지(value)를 저장
                    // 만약 이미 동일한 필드 이름(key)이 존재하면, 기존 메시지 뒤에 새 메시지
                    errors.merge(fieldName, errorMessage, (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", " + newErrorMessage);
                });

        return handleExceptionInternalArgs(e,HttpHeaders.EMPTY,ErrorStatus.valueOf("_BAD_REQUEST"),request,errors);
    }

    /**
     * 위에서 명시적으로 처리되지 않은 모든 Exception을 처리하는 핸들러
     */
    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        // 발생한 예외의 전체 스택 트레이스(호출 경로)를 서버 로그에 출력 (디버깅 용도)
        e.printStackTrace();

        // _INTERNAL_SERVER_ERROR (HTTP 500) 상태를 기반으로 표준 응답생성
        return handleExceptionInternalFalse(e, ErrorStatus._INTERNAL_SERVER_ERROR, HttpHeaders.EMPTY, ErrorStatus._INTERNAL_SERVER_ERROR.getHttpStatus(),request, e.getMessage());
    }


    /**
     * 개발자가 직접 정의한 'GeneralException' (커스텀 비즈니스 예외)을 처리하는 핸들러
     */
    @ExceptionHandler(value = GeneralException.class)
    public ResponseEntity onThrowException(GeneralException generalException, HttpServletRequest request) {
        // 예외 객체(generalException)로부터 미리 정의된 에러 이유(ErrorReasonDTO)를 가져옴
        ErrorReasonDTO errorReasonHttpStatus = generalException.getErrorReasonHttpStatus();

        // 가져온 에러 이유를 기반으로 표준 응답(ResponseEntity)을 생성하여 반환
        return handleExceptionInternal(generalException,errorReasonHttpStatus,null,request);
    }

    /**
     * [Helper 메소드] GeneralException 처리를 위한 표준 응답 ResponseEntity를 생성
     */
    private ResponseEntity<Object> handleExceptionInternal(Exception e, ErrorReasonDTO reason,
                                                           HttpHeaders headers, HttpServletRequest request) {

        // ApiResponse의 정적 메소드 onFailure를 호출하여, 표준 실패 응답 본문(body)을 생성
        // data 필드는 null로 설정
        ApiResponse<Object> body = ApiResponse.onFailure(reason.getCode(),reason.getMessage(),null);
//        e.printStackTrace();

        // HttpServletRequest를 WebRequest로 래핑(Wrapping)
        WebRequest webRequest = new ServletWebRequest(request);

        // 부모 클래스(ResponseEntityExceptionHandler)의 handleExceptionInternal 메소드를 호출
        return super.handleExceptionInternal(
                e, // 원본 예외 객체
                body, // 표준 응답 본문 (ApiResponse)
                headers, // HTTP 헤더 (이 경우 null이 전달됨)
                reason.getHttpStatus(), // ErrorReasonDTO에 정의된 HTTP 상태 코드
                webRequest // 래핑된 WebRequest 객체
        );
    }

    /**
     * [Helper 메소드] 예측하지 못한 'Exception' 처리를 위한 표준 응답 ResponseEntity를 생성
     */
    private ResponseEntity<Object> handleExceptionInternalFalse(Exception e, ErrorStatus errorCommonStatus,
                                                                HttpHeaders headers, HttpStatus status, WebRequest request, String errorPoint) {
        // ApiResponse.onFailure를 호출하여 표준 실패 응답 본문(body)을 생성
        // data 필드에 어떤 에러인지 식별 가능한 메시지(errorPoint)를 담음
        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getCode(),errorCommonStatus.getMessage(),errorPoint);

        // 부모 클래스의 handleExceptionInternal 메소드를 호출
        return super.handleExceptionInternal(
                e, // 원본 예외 객체
                body, // 표준 응답 본문 (ApiResponse)
                headers, // HTTP 헤더
                status, // HTTP 상태 코드 (e.g., 500)
                request // WebRequest 객체
        );
    }

    /**
     * [Helper 메소드] MethodArgumentNotValidException (DTO 유효성 검사) 처리를 위한 표준 응답 ResponseEntity를 생성합니다.
     */
    private ResponseEntity<Object> handleExceptionInternalArgs(Exception e, HttpHeaders headers, ErrorStatus errorCommonStatus,
                                                               WebRequest request, Map<String, String> errorArgs) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getCode(),errorCommonStatus.getMessage(),errorArgs);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorCommonStatus.getHttpStatus(),
                request
        );
    }

    /**
     * [Helper 메소드] ConstraintViolationException (파라미터 유효성 검사) 처리를 위한 표준 응답 ResponseEntity를 생성합니다.
     */
    private ResponseEntity<Object> handleExceptionInternalConstraint(Exception e, ErrorStatus errorCommonStatus,
                                                                     HttpHeaders headers, WebRequest request) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getCode(), errorCommonStatus.getMessage(), null);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorCommonStatus.getHttpStatus(),
                request
        );
    }
}
