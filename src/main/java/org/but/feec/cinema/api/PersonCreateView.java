package org.but.feec.cinema.api;

import java.util.Arrays;

public class PersonCreateView {

    private String email;
    private String givenName;
    private String nickname;
    private String familyName;
    private char[] pwd;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public char[] getPwd() {
        return pwd;
    }

    public void setPwd(char[] pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "PersonCreateView{" +
                "email='" + email + '\'' +
                ", givenName='" + givenName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", familyName='" + familyName + '\'' +
                ", pwd=" + Arrays.toString(pwd) +
                '}';
    }
}
