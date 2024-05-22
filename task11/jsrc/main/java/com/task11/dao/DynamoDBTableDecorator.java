package com.task11.dao;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.stream.Collectors;

public class DynamoDBTableDecorator<T> {

    private final DynamoDbTable<T> decorated;

    public DynamoDBTableDecorator(DynamoDbTable<T> decorated) {
        this.decorated = decorated;
    }

    public T getItem(T keyItem) {
        return decorated.getItem(keyItem);
    }

    public List<T> getAllItem() {
        return decorated.scan().stream()
                .flatMap(itemPage -> itemPage.items().stream())
                .collect(Collectors.toList());
    }

    public PageIterable<T> query(QueryConditional queryConditional) {
        return decorated.query(queryConditional);
    }

    public void putItem(T item) {
        decorated.putItem(item);
    }

}
