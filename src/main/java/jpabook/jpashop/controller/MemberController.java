package jpabook.jpashop.controller;

import jpabook.jpashop.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    public String creatForm(Model model) {
        return "";
    }

}
