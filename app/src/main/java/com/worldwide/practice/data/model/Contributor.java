package com.worldwide.practice.data.model;

/** Created by Anand on 11-02-2018. */
public class Contributor {

    private String login;

    private String avatar_url;
    private int contributions;

    public Contributor() {}

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public int getContributions() {
        return contributions;
    }

    public void setContributions(int contributions) {
        this.contributions = contributions;
    }
}
