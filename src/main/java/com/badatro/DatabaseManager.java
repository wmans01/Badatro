package com.badatro;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.WriteConcern;
import org.bson.Document;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class DatabaseManager {
    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> usersCollection;
    
    public DatabaseManager() {
        // Load database configuration
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find database.properties");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading database configuration", e);
        }

        String uri = props.getProperty("mongodb.uri");

        // Configure MongoDB Atlas connection settings
        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new com.mongodb.ConnectionString(uri))
            .serverApi(ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build())
            .writeConcern(WriteConcern.MAJORITY)
            .applyToSocketSettings(builder -> 
                builder.connectTimeout(5000, TimeUnit.MILLISECONDS)
                       .readTimeout(5000, TimeUnit.MILLISECONDS))
            .applyToClusterSettings(builder -> 
                builder.serverSelectionTimeout(5000, TimeUnit.MILLISECONDS))
            .build();

        // Connect to MongoDB Atlas
        try {
            mongoClient = MongoClients.create(settings);
            // Get database name from connection string
            String dbName = new com.mongodb.ConnectionString(uri).getDatabase();
            if (dbName == null || dbName.trim().isEmpty()) {
                throw new RuntimeException("Database name not found in connection string");
            }
            database = mongoClient.getDatabase(dbName);
            usersCollection = database.getCollection("users");
            
            // Create indexes for better performance
            usersCollection.createIndex(new Document("username", 1), new com.mongodb.client.model.IndexOptions().unique(true));
            usersCollection.createIndex(new Document("highScore", -1));
            
            // Test the connection
            database.runCommand(new Document("ping", 1));
            System.out.println("Successfully connected to MongoDB Atlas!");
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to MongoDB Atlas", e);
        }
    }
    
    public boolean registerUser(String username, String password) {
        try {
            // Check if username already exists
            if (findUser(username) != null) {
                return false;
            }
            
            // Create new user
            User user = new User(username, hashPassword(password));
            usersCollection.insertOne(user.toDocument());
            return true;
        } catch (Exception e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }
    
    public User loginUser(String username, String password) {
        try {
            User user = findUser(username);
            if (user != null && user.getPasswordHash().equals(hashPassword(password))) {
                return user;
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error logging in user: " + e.getMessage());
            return null;
        }
    }
    
    public User findUser(String username) {
        try {
            Document doc = usersCollection.find(Filters.eq("username", username)).first();
            return doc != null ? User.fromDocument(doc) : null;
        } catch (Exception e) {
            System.err.println("Error finding user: " + e.getMessage());
            return null;
        }
    }
    
    public void updateUser(User user) {
        try {
            com.mongodb.client.result.UpdateResult result = usersCollection.replaceOne(
                Filters.eq("username", user.getUsername()),
                user.toDocument()
            );
            
            if (result.getModifiedCount() == 0) {
                // If no document was modified, try to insert it
                usersCollection.insertOne(user.toDocument());
            }
            
            // Verify the update was successful
            User updatedUser = findUser(user.getUsername());
            if (updatedUser == null || updatedUser.getCurrentMoney() != user.getCurrentMoney()) {
                throw new RuntimeException("Failed to verify user data was saved correctly");
            }
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            throw new RuntimeException("Failed to save user data", e);
        }
    }
    
    public List<User> getLeaderboard(int limit) {
        try {
            List<User> leaderboard = new ArrayList<>();
            usersCollection.find()
                .sort(Sorts.descending("highScore"))
                .limit(limit)
                .forEach(doc -> leaderboard.add(User.fromDocument(doc)));
            return leaderboard;
        } catch (Exception e) {
            System.err.println("Error getting leaderboard: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
} 