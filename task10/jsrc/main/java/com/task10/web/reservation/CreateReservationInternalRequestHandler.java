package com.task10.web.reservation;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.task10.dao.DynamoDBTableDecorator;
import com.task10.dao.entity.ReservationEntity;
import com.task10.dao.entity.TableEntity;
import com.task10.web.InternalRequestHandler;
import com.task10.web.reservation.api.CreateReservationResponse;
import com.task10.web.util.ObjectMapperDecorator;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CreateReservationInternalRequestHandler implements InternalRequestHandler {

    private static final String REQUEST_METHOD = "POST";
    private static final String REQUEST_PATH = "^/reservations$";
    private final DynamoDBTableDecorator<ReservationEntity> reservations;
    private final DynamoDbIndex<ReservationEntity> reservationsIndex;
    private final DynamoDbIndex<TableEntity> tablesIndex;
    private final ObjectMapperDecorator objectMapper;

    public CreateReservationInternalRequestHandler(DynamoDBTableDecorator<ReservationEntity> reservations,
                                                   DynamoDbIndex<ReservationEntity> reservationsIndex,
                                                   DynamoDbIndex<TableEntity> tablesIndex,
                                                   ObjectMapperDecorator objectMapper) {
        this.reservations = reservations;
        this.reservationsIndex = reservationsIndex;
        this.tablesIndex = tablesIndex;
        this.objectMapper = objectMapper;
    }

    private static String generateId() {
        return UUID.randomUUID().toString();
    }

    private static void verifyTimeSlot(List<ReservationEntity> reservationsForDate, ReservationEntity reservation) {
        reservationsForDate.stream()
                .filter(reservationEntity -> isOverlappingReservation(reservationEntity, reservation))
                .findFirst()
                .ifPresent(reservationEntity -> {
                    System.out.printf("CreateReservationInternalRequestHandler: Found overlapping reservation: %s%n", reservationEntity);
                    throw new IllegalArgumentException("Found overlapping reservation");
                });
    }

    private static boolean isOverlappingReservation(ReservationEntity reservationEntity, ReservationEntity reservation) {
        LocalTime bookedReservationStart = LocalTime.parse(reservationEntity.getSlotTimeStart());
        LocalTime bookedReservationEnd = LocalTime.parse(reservationEntity.getSlotTimeEnd());
        LocalTime newReservationStart = LocalTime.parse(reservation.getSlotTimeStart());
        LocalTime newReservationEnd = LocalTime.parse(reservation.getSlotTimeEnd());

        return newReservationStart.isBefore(bookedReservationEnd) && bookedReservationStart.isBefore(newReservationEnd);
    }

    @Override
    public APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request) {
        System.out.printf("CreateReservationRequestHandler: Received request: %s%n", request);
        ReservationEntity reservation = objectMapper.readValue(request.getBody(), ReservationEntity.class);

        TableEntity table = getTableByTableNumber(reservation.getTableNumber());
        System.out.printf("CreateReservationInternalRequestHandler: Found table: %s%n", table);

        String reservationDate = reservation.getDate();
        int tableNumber = table.getNumber();
        List<ReservationEntity> reservationsForDate = getReservationsForDateForTable(reservationDate, tableNumber);
        System.out.printf("CreateReservationInternalRequestHandler: Reservations for tableNumber: %d reservations: %s%n", tableNumber, reservationsForDate);

        verifyTimeSlot(reservationsForDate, reservation);

        reservation.setReservationId(generateId());
        reservations.putItem(reservation);

        System.out.printf("CreateReservationInternalRequestHandler: Saved reservation: %s%n", reservation);
        CreateReservationResponse responseBody = new CreateReservationResponse(reservation.getReservationId());
        return new APIGatewayProxyResponseEvent()
                .withBody(objectMapper.writeValueAsString(responseBody))
                .withStatusCode(200);
    }

    private List<ReservationEntity> getReservationsForDateForTable(String date, int tableNumber) {
        return reservationsIndex.query(QueryConditional.keyEqualTo(Key.builder()
                        .partitionValue(date)
                        .sortValue(tableNumber)
                        .build()))
                .stream()
                .flatMap(reservationEntityPage -> reservationEntityPage.items().stream())
                .collect(Collectors.toList());
    }

    private TableEntity getTableByTableNumber(int tableNumber) {
        return tablesIndex.query(QueryConditional.keyEqualTo(Key.builder()
                        .partitionValue(tableNumber)
                        .build()))
                .stream()
                .flatMap(tableEntityPage -> tableEntityPage.items().stream())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Table not found"));
    }

    @Override
    public String getRequestPath() {
        return REQUEST_PATH;
    }

    @Override
    public String getRequestMethod() {
        return REQUEST_METHOD;
    }

}
