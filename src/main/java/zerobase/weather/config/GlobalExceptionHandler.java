package zerobase.weather.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice   // 전역 예외처리
public class GlobalExceptionHandler {   // 이 클래스에 전역 예외가 모임.


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)   // 클라이언트 -> 서버 API 호출 시점에서 에러 발생시 반환
    @ExceptionHandler(Exception.class)   // 모인 전역 예외들을 처리 (모든 익셉션에 대해서 동작시킴)
    public Exception handleAllException() {
        System.out.println("error from GlobalExceptionHandler");
        return new Exception();
    }
}
