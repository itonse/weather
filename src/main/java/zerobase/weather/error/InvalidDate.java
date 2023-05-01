package zerobase.weather.error;

public class InvalidDate extends RuntimeException{   // 예외상황을 위한 커스텀 익셉션 클래스
    private static final String MESSAGE = "너무 과거 혹은 미래의 날짜입니다.";
    public InvalidDate() {   // 이 클래스가 호출될 때 아래의 메세지와 함께 반환.
        super(MESSAGE);

    }
}
