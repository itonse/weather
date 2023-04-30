package zerobase.weather.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "memo")   // DB 테이블과의 매핑 (JPA가 관리), JPA에서 사용할 엔티티 이름은 memo
public class Memo {
    @Id   // PK는 id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // 기본적인 키 생성을 DB에게 맡김(자동증가).
    private int id;
    private String text;
}
