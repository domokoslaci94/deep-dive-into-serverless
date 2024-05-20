package com.task10.web.reservation.api;

import com.task10.dao.entity.ReservationEntity;

import java.util.List;
import java.util.Objects;

public class AllReservationsResponse {
    private List<ReservationEntity> reservations;

    public AllReservationsResponse() {
    }

    public AllReservationsResponse(List<ReservationEntity> reservations) {
        this.reservations = reservations;
    }

    public List<ReservationEntity> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationEntity> reservations) {
        this.reservations = reservations;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        AllReservationsResponse that = (AllReservationsResponse) object;
        return Objects.equals(reservations, that.reservations);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(reservations);
    }

    @Override
    public String toString() {
        return "AllReservationsResponse{" +
                "reservations=" + reservations +
                '}';
    }
}
