package com.task10.web.table.api;

import java.util.Objects;

public class CreateTableResponse {
    private int id;

    public CreateTableResponse() {
    }

    public CreateTableResponse(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        CreateTableResponse that = (CreateTableResponse) object;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CreateTableResponse{" +
                "id=" + id +
                '}';
    }
}
