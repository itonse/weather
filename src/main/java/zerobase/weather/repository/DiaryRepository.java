package zerobase.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.weather.domain.Diary;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer> {   // <<다이어리 객체를 가지고 연결, id값 타입>
    List<Diary> findAllByDate(LocalDate date);   // date 날짜에 해당되는 그 날의 모든 일기를 가져온다.

    List<Diary> findAllByDateBetween(LocalDate startDate, LocalDate endDate);  // 알아서 쿼리를 짜줌. start 날짜 ~ end 날짜 의 데이터를 모두 찾아준다.
}