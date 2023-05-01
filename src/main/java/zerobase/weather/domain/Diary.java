package zerobase.weather.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity  // 엔티티로 설정 (by JPA)
@Getter
@Setter
@NoArgsConstructor  // 다른 패키지의 다른 클래스에서 new 객체로 생성 가능
//@AllArgsConstructor  // 한번에 Diary의 모든 컬럼 넣기
public class Diary {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)  // id 값을 자동생성 가능하도록 설정
    private int id;
    private String weather;
    private String icon;
    private double temperature;
    private String text;
    private LocalDate date;

    public void setDateWeather(DateWeather dateWeather) {   // dateWeather 객체를 가져와서 객체 내부의 값들을 다이어리 안에 넣어줌
        this.date = dateWeather.getDate();
        this.weather = dateWeather.getWeather();
        this.icon = dateWeather.getIcon();
        this.temperature = dateWeather.getTemperature();
    }
}
