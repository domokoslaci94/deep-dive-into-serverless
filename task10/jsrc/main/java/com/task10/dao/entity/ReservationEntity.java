package com.task10.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.task10.config.ApplicationContext;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.util.Objects;

@DynamoDbBean
public class ReservationEntity {

    @JsonIgnore
    private String reservationId;
    private int tableNumber;
    private String clientName;
    private String phoneNumber;
    private String date;
    private String slotTimeStart;
    private String slotTimeEnd;

    @DynamoDbPartitionKey
    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    @DynamoDbSecondarySortKey(indexNames = {ApplicationContext.RESERVATIONS_INDEX_NAME})
    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = {ApplicationContext.RESERVATIONS_INDEX_NAME})
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSlotTimeStart() {
        return slotTimeStart;
    }

    public void setSlotTimeStart(String slotTimeStart) {
        this.slotTimeStart = slotTimeStart;
    }

    public String getSlotTimeEnd() {
        return slotTimeEnd;
    }

    public void setSlotTimeEnd(String slotTimeEnd) {
        this.slotTimeEnd = slotTimeEnd;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ReservationEntity that = (ReservationEntity) object;
        return tableNumber == that.tableNumber && Objects.equals(reservationId, that.reservationId) && Objects.equals(clientName, that.clientName) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(date, that.date) && Objects.equals(slotTimeStart, that.slotTimeStart) && Objects.equals(slotTimeEnd, that.slotTimeEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId, tableNumber, clientName, phoneNumber, date, slotTimeStart, slotTimeEnd);
    }

    @Override
    public String toString() {
        return "ReservationEntity{" +
                "reservationId='" + reservationId + '\'' +
                ", tableNumber=" + tableNumber +
                ", clientName='" + clientName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", date='" + date + '\'' +
                ", slotTimeStart='" + slotTimeStart + '\'' +
                ", slotTimeEnd='" + slotTimeEnd + '\'' +
                '}';
    }
}
