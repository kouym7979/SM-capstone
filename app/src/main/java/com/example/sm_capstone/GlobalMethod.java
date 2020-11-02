package com.example.sm_capstone;

import android.app.Activity;
import android.app.Application;

import org.aviran.cookiebar2.CookieBar;

//Application을 상속받아 전역함수나 변수들을 모아놓는 클래스(프젝당 하나만 존재가능하고 모든 전역객체는 여기에 작성하시면 됩니다)
public class GlobalMethod extends Application {
    public void LoginBlank(Activity a){
        CookieBar.build(a)
                .setCookiePosition(CookieBar.BOTTOM)
                .setBackgroundColor(R.color.default_bg_color)
                .setMessage("아이디 비밀번호를 입력하세요")
                .show();
    }
    public void LoginFail(Activity a){
        CookieBar.build(a)
                .setCookiePosition(CookieBar.BOTTOM)
                .setBackgroundColor(R.color.warningColor)
                .setMessage("아이디, 비밀번호를 확인하세요")
                .show();
    }
}