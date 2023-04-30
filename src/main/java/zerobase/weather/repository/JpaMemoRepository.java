package zerobase.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.weather.domain.Memo;

@Repository   // 레퍼지토리는 DB와 연동하는 코드 작성하는 곳
public interface JpaMemoRepository extends JpaRepository<Memo, Integer> {
    // JAVA에서 ORM 개념을 활용할 때 쓸 함수들이 JpaRepository에 담겨있음 <Memo 클래스를 가지고 연결, id값 형식>
    // Jdbc 처럼 쿼리문이나 불필요한 코드 작성할 필요 없이 깔끔.
}
