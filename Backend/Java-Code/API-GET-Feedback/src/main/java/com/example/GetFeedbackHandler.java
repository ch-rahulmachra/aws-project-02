package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

public class GetFeedbackHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    private static final String TABLE_NAME = "FeedbackTable";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DynamoDbClient dynamoDbClient = DynamoDbClient.create();

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Scan the entire DynamoDB table
            ScanRequest scanRequest = ScanRequest.builder()
                    .tableName(TABLE_NAME)
                    .build();

            ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);

            List<Map<String, Object>> feedbackList = new ArrayList<>();

            for (Map<String, AttributeValue> item : scanResponse.items()) {
                Map<String, Object> feedback = new HashMap<>();
                feedback.put("user_id", item.get("user_id").s());
                feedback.put("timestamp", item.get("timestamp").s());
                feedback.put("rating", Integer.parseInt(item.get("rating").n()));
                feedback.put("comment", item.get("comment").s());
                if (item.containsKey("sentiment")) {
                    feedback.put("sentiment", item.get("sentiment").s());
                }
                feedbackList.add(feedback);
            }

            // Convert the list to JSON string
            String jsonResponse = objectMapper.writeValueAsString(feedbackList);

            // Success response with CORS header
            response.put("statusCode", 200);
            response.put("body", jsonResponse);
            response.put("headers", Map.of(
                "Content-Type", "application/json",
                "Access-Control-Allow-Origin", "*"
            ));

        } catch (Exception e) {
            context.getLogger().log("Error: " + e.getMessage());

            // Error response with CORS header
            response.put("statusCode", 500);
            response.put("body", "{\"message\": \"Internal server error\"}");
            response.put("headers", Map.of(
                "Content-Type", "application/json",
                "Access-Control-Allow-Origin", "*"
            ));
        }

        return response;
    }
}