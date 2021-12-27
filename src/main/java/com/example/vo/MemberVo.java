package com.example.vo;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberVo extends CommonVo {
    @NotEmpty
    private int memNo;
    @Email
    private String id;
    private String pwd;
    private String name;
    private String nick;
    private String gender;
    private String hp;
    private String birth;
    private String joinDate;
    private String state;
    private int boardCount;
    private int commentCount;
    private int visitCount;
    private int grade;

    public MemberVo(int memNo, String state) {
        super();
        this.memNo = memNo;
        this.state = state;
    }

    public MemberVo(int memNo, int grade) {
        super();
        this.memNo = memNo;
        this.grade = grade;
    }
}