package zerobase.weather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration   // config 파일임을 명시
@EnableSwagger2   // 스웨거를 활성화 시킴
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("zerobase.weather"))  // basePackage 기준으로 많이 함.
                .paths(PathSelectors.any())   // any(): DiaryController 에 있는 모든 API (5개)가 나오게 하겠다.
                .build().apiInfo(apiInfo());
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()   // apiInfo 를 빌드하는 객체
                .title("날씨 일기 프로젝트 :")
                .description("날씨 일기를 CRUD 할 수 있는 백엔드 API 입니다")
                .version("2.0")
                .build();
    }
}
