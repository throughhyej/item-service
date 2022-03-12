package hello.itemservice.controller;

import hello.itemservice.domain.SaveItemForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/item")
public class ValidationItemApiController {

    /**
     * CASE 1: json 데이터와 객체 타입이 불일치하여 컨트롤러가 호출되지 않음
     * CASE 2: json 데이터와 객체 타입 일치 + 검증에서 에러 발생
     * CASE 3: 정상 동작
     */

    @PostMapping("/add")
    public Object add(@RequestBody @Validated SaveItemForm form, BindingResult bindingResult) {
        log.info("::::: API 호출 :::::");
        if (bindingResult.hasErrors()) {
            log.info("::::: bindingResult ::::: => {}", bindingResult);
            return bindingResult.getAllErrors();
        }
        log.info("::::: form ::::: => {}", form);
        return form;
    }

}
