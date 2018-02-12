package com.worldwide.practice.data.model;

import com.google.gson.annotations.SerializedName;

/** Created by Anand on 10-02-2018. */
public class User {
    @SerializedName("avatar_url")
    private String avatarUrl;

    private String name;
    private String email;

    @SerializedName("public_repos")
    private String publicRepos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPublicRepos() {
        return publicRepos;
    }

    public void setPublicRepos(String publicRepos) {
        this.publicRepos = publicRepos;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
