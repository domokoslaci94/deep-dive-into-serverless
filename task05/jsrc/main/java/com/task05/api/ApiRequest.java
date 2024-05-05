package com.task05.api;

import java.util.Map;
import java.util.Objects;

public class ApiRequest {
    private int principalId;
    private Map<String, String> content;

    public ApiRequest() {
    }

    public ApiRequest(int principalId, Map<String, String> content) {
        this.principalId = principalId;
        this.content = content;
    }

    public int getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(int principalId) {
        this.principalId = principalId;
    }

    public Map<String, String> getContent() {
        return content;
    }

    public void setContent(Map<String, String> content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiRequest that = (ApiRequest) o;
        return principalId == that.principalId && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(principalId, content);
    }

    @Override
    public String toString() {
        return "ApiRequest{" +
                "principalId=" + principalId +
                ", content=" + content +
                '}';
    }
}
