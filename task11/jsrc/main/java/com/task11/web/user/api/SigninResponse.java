package com.task11.web.user.api;

import java.util.Objects;

public class SigninResponse {
    private String accessToken;

    public SigninResponse() {
    }

    public SigninResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        SigninResponse that = (SigninResponse) object;
        return Objects.equals(accessToken, that.accessToken);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(accessToken);
    }

    @Override
    public String toString() {
        return "SigninResponse{" +
                "accessToken='" + accessToken + '\'' +
                '}';
    }
}
