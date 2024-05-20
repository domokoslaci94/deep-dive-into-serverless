package com.task10.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task10.dao.DynamoDBTableDecorator;
import com.task10.dao.entity.ReservationEntity;
import com.task10.dao.entity.TableEntity;
import com.task10.web.InternalRequestHandler;
import com.task10.web.RequestRouter;
import com.task10.web.reservation.CreateReservationInternalRequestHandler;
import com.task10.web.reservation.GetReservationInternalRequestHandler;
import com.task10.web.table.handler.AllTablesInternalRequestHandler;
import com.task10.web.table.handler.CreateTableInternalHandler;
import com.task10.web.table.handler.SingleTableInternalRequestHandler;
import com.task10.web.user.SigninInternalRequestHandler;
import com.task10.web.user.SignupInternalRequestHandler;
import com.task10.web.util.ObjectMapperDecorator;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolClientsRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolsRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserPoolClientDescription;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserPoolDescriptionType;

import java.util.Set;

public class ApplicationContext {

    public static final String TEMP_PASSWORD = "TempPassword123!";
    public static final String TABLES_INDEX_NAME = "tables_index";
    public static final String RESERVATIONS_INDEX_NAME = "reservations_index";
    private static final String TABLES_TABLE_NAME = System.getenv("tables_table");
    private static final String RESERVATIONS_TABLE_NAME = System.getenv("reservations_table");
    private static final String BOOKING_USERPOOL_NAME = System.getenv("booking_userpool");
    private static final DynamoDbTable<TableEntity> TABLES_TABLE = configureTablesTable();
    private static final DynamoDbIndex<TableEntity> TABLES_INDEX = TABLES_TABLE.index(TABLES_INDEX_NAME);
    private static final DynamoDBTableDecorator<TableEntity> TABLES_TABLE_DECORATOR = new DynamoDBTableDecorator<>(TABLES_TABLE);
    private static final DynamoDbTable<ReservationEntity> RESERVATIONS_TABLE = configureReservationsTable();
    private static final DynamoDBTableDecorator<ReservationEntity> RESERVATIONS_TABLE_DECORATOR = new DynamoDBTableDecorator<>(RESERVATIONS_TABLE);
    private static final DynamoDbIndex<ReservationEntity> RESERVATIONS_INDEX = RESERVATIONS_TABLE.index(RESERVATIONS_INDEX_NAME);
    private static final ObjectMapperDecorator OBJECT_MAPPER = new ObjectMapperDecorator(new ObjectMapper());
    private static final CognitoIdentityProviderClient COGNITO_CLIENT = CognitoIdentityProviderClient.create();
    private static final String BOOKING_USERPOOL_ID = getUserPoolId();
    public static final String BOOKING_CLIENT_ID = getClientId();
    public static final RequestRouter REQUEST_ROUTER = new RequestRouter(
            configureInternalRequestHandlers()
    );

    private ApplicationContext() {
    }

    private static Set<InternalRequestHandler> configureInternalRequestHandlers() {
        return Set.of(
                new CreateReservationInternalRequestHandler(RESERVATIONS_TABLE_DECORATOR, RESERVATIONS_INDEX, TABLES_INDEX, OBJECT_MAPPER),
                new GetReservationInternalRequestHandler(RESERVATIONS_TABLE_DECORATOR, OBJECT_MAPPER),
                new AllTablesInternalRequestHandler(TABLES_TABLE_DECORATOR, OBJECT_MAPPER),
                new CreateTableInternalHandler(TABLES_TABLE_DECORATOR, OBJECT_MAPPER),
                new SingleTableInternalRequestHandler(TABLES_TABLE_DECORATOR, OBJECT_MAPPER),
                new SignupInternalRequestHandler(OBJECT_MAPPER, COGNITO_CLIENT, BOOKING_USERPOOL_ID, BOOKING_CLIENT_ID),
                new SigninInternalRequestHandler(OBJECT_MAPPER, COGNITO_CLIENT, BOOKING_USERPOOL_ID, BOOKING_CLIENT_ID)
        );
    }

    private static DynamoDbTable<TableEntity> configureTablesTable() {
        return DynamoDbEnhancedClient.create()
                .table(TABLES_TABLE_NAME, TableSchema.fromClass(TableEntity.class));
    }

    private static DynamoDbTable<ReservationEntity> configureReservationsTable() {
        return DynamoDbEnhancedClient.create()
                .table(RESERVATIONS_TABLE_NAME, TableSchema.fromClass(ReservationEntity.class));
    }

    private static String getUserPoolId() {
        String userPoolId = COGNITO_CLIENT.listUserPools(ListUserPoolsRequest.builder().build()).userPools().stream()
                .filter(pool -> pool.name().equals(BOOKING_USERPOOL_NAME))
                .findFirst()
                .map(UserPoolDescriptionType::id)
                .orElseThrow(() -> new RuntimeException("User pool not found"));

        System.out.println("User pool id: " + userPoolId);
        return userPoolId;
    }

    private static String getClientId() {
        String clientId = COGNITO_CLIENT.listUserPoolClients(ListUserPoolClientsRequest.builder()
                        .userPoolId(BOOKING_USERPOOL_ID)
                        .build())
                .userPoolClients()
                .stream()
                .findFirst()
                .map(UserPoolClientDescription::clientId)
                .orElseThrow(() -> new RuntimeException("Client id not found"));

        System.out.println("Client id: " + clientId);
        return clientId;
    }
}
