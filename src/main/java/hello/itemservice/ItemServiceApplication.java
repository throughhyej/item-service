package hello.itemservice;

import hello.itemservice.validatior.ItemValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

}


//@SpringBootApplication
//public class ItemServiceApplication implements WebMvcConfigurer {
//
//	public static void main(String[] args) {
//		SpringApplication.run(ItemServiceApplication.class, args);
//	}
//
//	// 검증기 전역 적용 (전역으로는 잘 사용하지 않음)
//	@Override
//	public Validator getValidator() {
//		return new ItemValidator();
//	}
//
//}