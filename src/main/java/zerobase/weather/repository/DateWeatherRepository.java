package zerobase.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.weather.domain.DateWeather;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DateWeatherRepository extends JpaRepository<DateWeather, LocalDate> {  // <DateWeather 엔티티 사용, ID 형식>
    List<DateWeather> findAllByDate(LocalDate localDate);  // Date에 따라서 그 날의 DateWeather 값을 가져오는 함수
}
