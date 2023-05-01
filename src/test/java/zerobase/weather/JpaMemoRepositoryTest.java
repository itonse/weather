package zerobase.weather;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;
import zerobase.weather.repository.JpaMemoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional  // DB 테스트를 할 때 많이 사용되는 어노테이션 (테스트를 할 때 실제 DB의 정보 변경이 되지 않도록하는-> 모두 롤백). 주석처리하면 테스트 내용으로 DB에서 변경이 됨.
public class JpaMemoRepositoryTest {    // 일반적으로 테스트 코드에는 @Transactional 붙여서 모두 롤백 처리

    @Autowired  // 불러오기
    JpaMemoRepository jpaMemoRepository;

    @Test
    void insertMemoTest() {
        //given 주어진 것
        Memo newMemo = new Memo(10, "this is jpa memo");

        //when  한 것
        jpaMemoRepository.save(newMemo);   // save는 jpaMemoRepository 에 있는 함수

        //then 결과는 이럴 것
        List<Memo> memoList = jpaMemoRepository.findAll();
        assertTrue(memoList.size() > 0);
    }

    @Test
    void findByIdTest() {
        //given
        Memo newMemo = new Memo(999, "jpa");   // 메모의 id는 자동증가라서, 11로 설정한 것은 의미 없음.
        //when
        Memo memo = jpaMemoRepository.save(newMemo);
        System.out.println(memo.getId());
        //then
        Optional<Memo> result = jpaMemoRepository.findById(memo.getId());
        assertEquals(result.get().getText(), "jpa");
    }
}
