package zerobase.weather.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zerobase.weather.domain.Memo;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcMemoRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired   // 알아서 application.properties 에서 dataSource 를 가져옴
    public JdbcMemoRepository(DataSource dataSource) {   // 생성자
        jdbcTemplate = new JdbcTemplate(dataSource);    // jdbcTemplate 객체 생성
    }

    // jdbc 를 활용해서 spring boot에 있는 memo 라는 객체를 쿼리문을 활용해서 mySQL DB에 넣어줌.
    public Memo save(Memo memo) {   // save 함수: 클래스 값을 저장하면 mySQL 에 클래스 값이 저장되고, 반환값은 Memo
        // Jdbc 는 쿼리문 직접 작성
        String sql = "insert into memo values(?,?)";   // memo 테이블에 insert하는 '쿼리문 작성'
        jdbcTemplate.update(sql, memo.getId(), memo.getText());   // jdbcTemplate 에 업데이트
        return memo;   // save 해준 memo
    }

    public List<Memo> findAll() {  // 전체 메모들을 찾아와서 반환
        String sql = "select * from memo";   // sql문
        return jdbcTemplate.query(sql, memoRowMapper());
            // jdbc 템플릿이 mySQL DB에 가서 위의 sql 쿼리를 던지고, 반환된 객체들을(RS) memoRowMapper를 이용해 Memo 객체로 가져옴.
    }

    public Optional<Memo> findByID(int id) {    // Optional: id로 찾은 객체가 없는 경우, null값 처리
        String sql = "select * from memo where id = ?";  // id를 가지고 select 해오기
        return jdbcTemplate.query(sql, memoRowMapper(), id).stream().findFirst();
    }

    private RowMapper<Memo> memoRowMapper() {   // ResultSet을 Spring boot 의 Memo 클래스 형식으로 매핑해 주는 것
        // jdbc를 통해서 mySQL DB에서 데이터를 가져오면, 가져온 데이터의 값의 형식은 ResultSet 이다.
        // ResultSet 형식: {id = 1, text  = 'this is memo~'}
        return (rs, rowNum) -> new Memo(    // 매핑작업 (rs는 ResultSet)
                rs.getInt("id"),
                rs.getString("text")
        );
    }

}
