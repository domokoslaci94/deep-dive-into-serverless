package com.task11.dao.entity;

import com.task11.config.ApplicationContext;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.util.Objects;

@DynamoDbBean
public class TableEntity {

    private int id;
    private int number;
    private int places;
    private boolean isVip;
    private int minOrder;

    public TableEntity() {
    }

    public TableEntity(int id, int number, int places, boolean isVip, int minOrder) {
        this.id = id;
        this.number = number;
        this.places = places;
        this.isVip = isVip;
        this.minOrder = minOrder;
    }

    @DynamoDbPartitionKey
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = {ApplicationContext.TABLES_INDEX_NAME})
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public boolean getIsVip() {
        return isVip;
    }

    public void setIsVip(boolean isVip) {
        this.isVip = isVip;
    }

    public int getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(int minOrder) {
        this.minOrder = minOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableEntity that = (TableEntity) o;
        return id == that.id && number == that.number && places == that.places && isVip == that.isVip && minOrder == that.minOrder;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, places, isVip, minOrder);
    }

    @Override
    public String toString() {
        return "TableEntity{" +
                "id=" + id +
                ", number=" + number +
                ", places=" + places +
                ", isVip=" + isVip +
                ", minOrder=" + minOrder +
                '}';
    }
}
