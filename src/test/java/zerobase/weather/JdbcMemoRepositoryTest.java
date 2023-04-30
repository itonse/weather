package zerobase.weather;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;
import zerobase.weather.repository.JdbcMemoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@Transactional    // DB 테스트를 할 때 많이 사용되는 어노테이션 (테스트를 할 때 실제 DB의 정보 변경이 되지 않도록하는). 주석처리하면 테스트 내용으로 DB에서 변경이 됨.
public class JdbcMemoRepositoryTest {

    @Autowired
    JdbcMemoRepository jdbcMemoRepository;

    @Test
    void insertMemoTest() {    // jdbc 레포지토리가 정상 동작하는 것을 확인
        //given 주어진 것
        Memo newMemo = new Memo(2, "insertMemoTest");  // 메모 객체 생성

        //when  한 것
        jdbcMemoRepository.save(newMemo);   // 위의 메모 객체를 저장

        //then 결과는 이럴 것
        Optional<Memo> result = jdbcMemoRepository.findByID(2);   // 2번 id로 find 했을 때 나오는 메모 객체
        assertEquals(result.get().getText(), "insertMemoTest");
    }

    @Test
    void findAllMemoTest() {
        //given
        List<Memo> memoList = jdbcMemoRepository.findAll();

        //when
        System.out.println(memoList);

        //then
        assertNotNull(memoList);
    }
}
