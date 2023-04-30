package zerobase.weather.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;

@RestController   // 기본 Controller + http응답을 보낼 때 상태코드(200 ok 등)를 컨트롤러에서 지정을 해서 내려줄 수 있게끔 하는 기능
public class DiaryController {
    private final DiaryService diaryService;  // 의존하는 클래스

    public DiaryController(DiaryService diaryService) {  // diaryService 가져오도록 생성자 만듦
        this.diaryService = diaryService;
    }

    @PostMapping("/create/diary")  // @PostMapping 어노테이션이 달린 API의 테스트를 하기 좋은 프로그램: Postman
    void createDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,  // // @RequestParam: 요청을 보낼때 넣어주는 매개변수 -> (포맷 형식의)LocalDate
                     @RequestBody String text) {      // @RequestBody: 바디값으로 보내주어야 할것 -> 텍스트
        diaryService.createDiary(date, text);   // DiaryController -> diaryService 로 요청 던지기
    }
}
