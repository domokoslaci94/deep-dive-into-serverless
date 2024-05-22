package com.task11.web.reservation.api;

import java.util.Objects;

public class CreateReservationResponse {

    private String reservationId;

    public CreateReservationResponse() {
    }

    public CreateReservationResponse(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        CreateReservationResponse that = (CreateReservationResponse) object;
        return Objects.equals(reservationId, that.reservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(reservationId);
    }

    @Override
    public String toString() {
        return "CreateReservationResponse{" +
                "reservationId='" + reservationId + '\'' +
                '}';
    }
}
