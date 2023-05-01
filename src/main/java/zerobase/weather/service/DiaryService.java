package zerobase.weather.service;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.WeatherApplication;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)  // 아래에 있는 메소드들이 트랜잭션으로 동작하게 됨 (readOnly 만 가능)
public class DiaryService {

    @Value("${openweathermap.key}")  // application.properties 에 들있는 이것의 값을 가져와서
    private String apiKey;   // 이 객체에 값을 넣음.

    private final DiaryRepository diaryRepository;  // 의존성
    private final DateWeatherRepository dateWeatherRepository;
    private static final Logger logger = LoggerFactory.getLogger(WeatherApplication.class);  // 로거 생성

    public DiaryService(DiaryRepository diaryRepository, DateWeatherRepository dateWeatherRepository) {  // 생성자
        this.diaryRepository = diaryRepository;
        this.dateWeatherRepository = dateWeatherRepository;
    }

    @Transactional   // DB 연결 작업이라 어노테이션 붙임
    @Scheduled(cron = "0 0 1 * * *")   // 5초 간격으로 매분 매시에 동작
    public void saveWeatherDate(){   // 매일 새벽 1시 마다 날씨 데이터를 저장할 함수
        logger.info("오늘도 날씨 데이터 잘 가져옴");
        dateWeatherRepository.save(getWeatherFromApi());   // getWeatherFromApi 를 통해 데이터를 가져와서 DB에 넣기
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)   // 최고 격리수준
    public void createDiary(LocalDate date, String text) {   // 컨트롤러에서 받아온 객체 사용
        logger.info("started to create diary");   // info 레벨의 로그 작성
        // 날씨 데이터 가져오기 (API 에서 가져오기 or DB 에서 가져오기)
        DateWeather dateWeather = getDataWeather(date);   // API 나 DB에서 알아서 원하는 날짜의 DataWeather 값 가져옴

        // 파싱된 데이터 + 일기 값 우리 db에 넣기
        Diary nowDiary = new Diary();  // 빈 다이어리를 만들어서 차차 값 채우기 (도메인의 @NoArgsConstructor 를 추가해서 객체 생성)
        nowDiary.setDateWeather(dateWeather);   // 다이어리에 dateWeather(date, weather, icon, temperature) 값이 채워짐
        nowDiary.setText(text);
        nowDiary.setDate(date);
        diaryRepository.save(nowDiary);   // 데이터를 DB에 넣음
        logger.info("end to create diary");
    }

    private DateWeather getWeatherFromApi() {    // API 에서 (당일)날씨 데이터 가져오기
        // open weather map 에서 날씨 데이터 가져오기
        String weatherData = getWeatherString();

        // 받아온 날씨 json 파싱하기
        Map<String, Object> parsedWeather = parseWeather(weatherData);
        DateWeather dateWeather = new DateWeather();   // 객체 생성
        dateWeather.setDate(LocalDate.now());   // 날씨를 가져온 시점의 날짜를 넣어주기
        dateWeather.setWeather(parsedWeather.get("main").toString());  // 값들 넣어주기
        dateWeather.setIcon(parsedWeather.get("icon").toString());
        dateWeather.setTemperature((Double) parsedWeather.get("temp"));

        return dateWeather;  // 객체 반환
    }

    private DateWeather getDataWeather(LocalDate date) {   // DB에서 원하는 날짜의 DataWeather 가져오기
        List<DateWeather> dateWeatherListFromDB = dateWeatherRepository.findAllByDate(date);
        if (dateWeatherListFromDB.size() == 0) {  // 가져온 데이터가 없으면
            // 새로 api에서 날씨 정보를 가져와야한다.
            // 정책상 현재 날씨를 가져오도록 하기
            return getWeatherFromApi();   // API 에서 (당일)날씨 데이터 가져오기
        } else {   // 저장된 그 날의 날씨 데이터가 있으면
            return dateWeatherListFromDB.get(0);   // 가져오기
        }

    }

    @Transactional(readOnly = true)  // (읽기전용)
    public List<Diary> readDiary(LocalDate date) {    // 해당 날짜의 모든 날씨 일기 조회
//        if (date.isAfter(LocalDate.ofYearDay(3050, 1))) {   // 너무 먼 미래의 날짜이면
//            throw new InvalidDate();   // 커스텀 익셉션 활용해서 처리
//        }
        logger.debug("read diary");    // debug 레벨의 로그 작성
        return diaryRepository.findAllByDate(date);  // 레포지토리를 통해 DB를 조회함
    }

    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {   // 기간 날짜들의 모든 날씨 일기 조회
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    public void updateDiary(LocalDate date, String text) {
        Diary nowDiary = diaryRepository.getFirstByDate(date);   // 그 날짜의 날씨 일기 중에 하나만 가져오기
        nowDiary.setText(text); // 그 날씨 일기의 모든 것은 그대로, 텍스트만 수정
        diaryRepository.save(nowDiary);   // DB에 수정된 것을 반영 (덮어쓰기)
    }

    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }

    private String getWeatherString() {
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apiKey;
        try{

            URL url = new URL(apiUrl);  // 스트링 -> URL 변환해서 저장
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();  // 위의 url을 Http 형식으로 연결 시킴.
            connection.setRequestMethod("GET");   // GET 요청을 보냄
            int responseCode = connection.getResponseCode();   // 받아온 응답코드(200, 400 등)를 저장
            BufferedReader br;  // 속도 향상
            if (responseCode == 200) {  // 성공 200 ok
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));  // 응답객체 가져옴
            } else {  // 문제 있음 (400, 500 등)
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));  // 어떤 오류인지 보기 위헤 에러를 가져옴
            }
            String inputLine;
            StringBuilder response = new StringBuilder();
            while((inputLine = br.readLine()) != null) {  // br을 한줄한줄 읽으면서
                response.append(inputLine);  // 그 값들이 쌓이게 됨.
            }
            br.close();
            return response.toString();   // response를 String으로 변환해서 리턴
        }catch (Exception e){
            return "failed to get response";
        }
    }

    private Map<String, Object> parseWeather(String jsonString) {   // json 스트링을 받와와서 json 형태로 변환을 함
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);  // jsonObject 에 파싱한 결과값을 담음
        } catch (ParseException e) {  // 파싱 작업이 잘못되었을 경우
            throw new RuntimeException(e);  // 예외 던지기
        }
        Map<String, Object> resultMap = new HashMap<>();  // 여기에 필요한 정보 3개를 담음

        JSONObject mainData = (JSONObject) jsonObject.get("main");
        resultMap.put("temp", mainData.get("temp"));  // main.temp 담음
        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");  // weather 값은 리스트 형식이다
        JSONObject weatherData = (JSONObject) weatherArray.get(0);  // 리스트에 값이 하나라서, 0번째 객체 가져옴
        resultMap.put("main", weatherData.get("main"));  // weather.main 담음
        resultMap.put("icon", weatherData.get("icon"));  // weather.icon 담음
        return resultMap;   // 담은 해쉬맵 리턴
    }
}
