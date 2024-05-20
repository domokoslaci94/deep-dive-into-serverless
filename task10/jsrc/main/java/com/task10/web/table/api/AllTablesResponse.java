package com.task10.web.table.api;

import com.task10.dao.entity.TableEntity;

import java.util.List;
import java.util.Objects;

public class AllTablesResponse {
    private List<TableEntity> tables;

    public AllTablesResponse() {
    }

    public AllTablesResponse(List<TableEntity> tables) {
        this.tables = tables;
    }

    public List<TableEntity> getTables() {
        return tables;
    }

    public void setTables(List<TableEntity> tables) {
        this.tables = tables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllTablesResponse that = (AllTablesResponse) o;
        return Objects.equals(tables, that.tables);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tables);
    }

    @Override
    public String toString() {
        return "AllTablesResponse{" +
                "tables=" + tables +
                '}';
    }
}

