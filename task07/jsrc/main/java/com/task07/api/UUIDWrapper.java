package com.task07.api;

import java.util.List;
import java.util.Objects;

public class UUIDWrapper {

    private List<String> ids;

    public UUIDWrapper() {
    }

    public UUIDWrapper(List<String> ids) {
        this.ids = ids;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UUIDWrapper that = (UUIDWrapper) o;
        return Objects.equals(ids, that.ids);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ids);
    }

    @Override
    public String toString() {
        return "UUIDWrapper{" +
                "ids=" + ids +
                '}';
    }
}
