package com.changcai.buyer;

/**
 * Created by lufeisong on 2018/1/11.
 */

public class TestUserInfo {
    String imToken;
    String grade;
    String account;

    public String getImToken() {
        return imToken;
    }

    public void setImToken(String imToken) {
        this.imToken = imToken;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "TestUserInfo{" +
                "imToken='" + imToken + '\'' +
                ", grade='" + grade + '\'' +
                ", account='" + account + '\'' +
                '}';
    }
}
