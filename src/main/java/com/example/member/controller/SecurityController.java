package com.example.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SecurityController {

    // 로그인 GET
    @GetMapping("/loginForm")
    public String login() {
        return "page/member_login";
    }

    // 로그인 Post
    @PostMapping("/loginForm")
    public String login2() {
        return "page/member_login";
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(){
        return "/";
    }


//    @GetMapping("/loginSuccess")
//    @ResponseBody
//    public String loginSuccess(){
//
//        String pwd = passwordEncoder.encode("1111");
//        System.out.println("pwd"+pwd);
//
//        return "로그인에 성공하셨습니다.";
//    }
//
//    @GetMapping("/loginFail")
//    @ResponseBody
//    public String loginfail(){
//
//        String pwd = passwordEncoder.encode("1111");
//        System.out.println("pwd"+pwd);
//
//        return "로그인에 실패하셨습니다.";
//    }

    /*권한 test*/
    @GetMapping("/intranet/1")
    @ResponseBody
    public String admin(){
        return "admin 페이지입니다.";
    }

    @GetMapping("/member/1")
    @ResponseBody
    public String member(){
        return "member 페이지입니다.";
    }

    @GetMapping("/grade4/1")
    @ResponseBody
    public String grade4(){
        return "grade4 페이지입니다.";
    }

    @GetMapping("/grade3/1")
    @ResponseBody
    public String grade3(){
        return "grade3 페이지입니다.";
    }

    @GetMapping("/grade2/1")
    @ResponseBody
    public String grade2(){
        return "grade2 페이지입니다.";
    }

    @GetMapping("/grade1/1")
    @ResponseBody
    public String grade1(){
        return "grade1 페이지입니다.";
    }
}