package zerobase.weather.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.domain.Diary;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;


@RestController   // 기본 Controller + http응답을 보낼 때 상태코드(200 ok 등)를 컨트롤러에서 지정을 해서 내려줄 수 있게끔 하는 기능
public class DiaryController {
    private final DiaryService diaryService;  // 의존하는 클래스

    public DiaryController(DiaryService diaryService) {  // diaryService 가져오도록 생성자 만듦
        this.diaryService = diaryService;
    }

    @ApiOperation(value = "일기 텍스트와 날씨를 이용해서 DB에 일기 저장", notes = "이것은 노트")
    @PostMapping("/create/diary")  // @PostMapping 어노테이션이 달린 API의 테스트를 하기 좋은 프로그램: Postman
    void createDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,  // // @RequestParam: 요청을 보낼때 넣어주는 매개변수 -> (포맷 형식의)LocalDate
                     @RequestBody String text) {      // @RequestBody: 바디값으로 보내주어야 할것 -> 텍스트
        diaryService.createDiary(date, text);   // DiaryController -> diaryService 로 요청 던지기
    }

    @ApiOperation("선택한 날짜의 모든 일기 데이터를 가져옵니다")
    @GetMapping("/read/diary")   // 해당 날짜의 날씨 일기 조회
    List<Diary> readDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {  // 날짜 하루
        return diaryService.readDiary(date);   // 날짜를 통해서 조회하기
    }

    @ApiOperation("선택한 기간중의 모든 일기 데이터를 가져옵니다")
    @GetMapping("/read/diaries")  // 기간 날짜들의 날씨 일기 조회
    List<Diary> readDiaries(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "조회할 기간의 첫번째날", example = "2020-02-02") LocalDate startDate,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "조회할 기간의 마지막날", example = "2020-02-02") LocalDate endDate){   // start 날짜 부터 end 날짜 까지
        return diaryService.readDiaries(startDate, endDate);
    }

    @PutMapping("/update/diary")  // 날씨 일기 수정
    void updateDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                    @RequestBody String text) {  // 해당 날짜, 수정 대상 일기
        diaryService.updateDiary(date, text);
    }

    @DeleteMapping("/delete/diary")  // 날씨 일기 삭제
    void deleteDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date ){    // 해당 날짜의 모든 날씨 일기 삭제
        diaryService.deleteDiary(date);
    }
}
