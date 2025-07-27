package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

public class FeedbackHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    private static final String TABLE_NAME = "FeedbackTable"; // DynamoDB table
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DynamoDbClient dynamoDbClient = DynamoDbClient.create();

    // Replace with your actual RDS credentials
    private static final String JDBC_URL = "jdbc:postgresql://feedback-db.cjgickeiynwa.ap-south-1.rds.amazonaws.com:5432/feedback_db";
    private static final String DB_USER = "admin1";
    private static final String DB_PASSWORD = "password";

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Parse request body
            String bodyString = (String) input.get("body");
            Map<String, Object> body = objectMapper.readValue(bodyString, Map.class);

            String userId = (String) body.get("user_id");
            int rating = (Integer) body.get("rating");
            String comment = (String) body.get("comment");
            String timestamp = Instant.now().toString();

            // 🔍 Determine sentiment based on rating
            String sentiment;
            if (rating >= 4) {
                sentiment = "POSITIVE";
            } else if (rating == 3) {
                sentiment = "NEUTRAL";
            } else {
                sentiment = "NEGATIVE";
            }

            // 1️⃣ Save to DynamoDB
            Map<String, AttributeValue> item = new HashMap<>();
            item.put("user_id", AttributeValue.fromS(userId));
            item.put("timestamp", AttributeValue.fromS(timestamp));
            item.put("rating", AttributeValue.fromN(String.valueOf(rating)));
            item.put("comment", AttributeValue.fromS(comment));
            item.put("sentiment", AttributeValue.fromS(sentiment)); // ✅ Include sentiment

            PutItemRequest putItemRequest = PutItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .item(item)
                    .build();

            dynamoDbClient.putItem(putItemRequest);

            // 2️⃣ Save to RDS
            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                String sql = "INSERT INTO feedback (user_id, rating, comment, sentiment, submitted_at) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, userId);
                    ps.setInt(2, rating);
                    ps.setString(3, comment);
                    ps.setString(4, sentiment); // ✅ Insert sentiment
                    ps.setTimestamp(5, java.sql.Timestamp.from(Instant.now())); // ✅ correct data type
                    ps.executeUpdate();
                }
            }

            // ✅ Success
            response.put("statusCode", 200);
            response.put("body", "{\"message\": \"Feedback stored in DynamoDB and RDS!\"}");

            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            response.put("headers", headers);

        } catch (Exception e) {
            context.getLogger().log("Error: " + e.getMessage());
            response.put("statusCode", 500);
            response.put("body", "{\"message\": \"Internal server error\"}");
        }

        return response;
    }
}