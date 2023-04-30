package zerobase.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.weather.domain.Diary;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer> {   // <<다이어리 객체를 가지고 연결, id값 타입>
}
