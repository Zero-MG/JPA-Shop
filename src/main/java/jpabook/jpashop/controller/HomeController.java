package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {
    /**
     * Logger는 'org.slf4j.Logger'를 사용하여 객체선언을 해주고 사용하는데,
     * '@Slf4j'라는 어노테이션을 쓰면 편리하다.
     */

    @RequestMapping("/")
    public String home() {
        log.info("home contoller");
        return "home";
    }

}
