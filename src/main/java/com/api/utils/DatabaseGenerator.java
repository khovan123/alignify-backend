package com.api.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.api.config.MongoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * MongoDB Database Generator Utility
 * 
 * This standalone Spring Boot application creates the complete Alignify database
 * structure by executing all the collection creation methods from MongoConfig.
 * 
 * Usage:
 * 1. Configure your MongoDB connection in application.properties
 * 2. Run: mvn spring-boot:run -Dspring-boot.run.main-class=com.api.utils.DatabaseGenerator
 * 3. Or compile and run: java -jar target/alignify-backend-1.0.0.jar --spring.main.web-application-type=none
 * 
 * The application will:
 * - Connect to MongoDB using the configured connection string
 * - Create all collections with validation schemas
 * - Insert default data for roles and categories
 * - Exit cleanly after completion
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ComponentScan(basePackages = {"com.api.config", "com.api.utils"})
public class DatabaseGenerator implements CommandLineRunner {

    @Autowired
    private MongoClient mongoClient;

    @Value("${spring.data.mongodb.database:alignify_db}")
    private String databaseName;

    public static void main(String[] args) {
        System.setProperty("spring.main.web-application-type", "none");
        SpringApplication.run(DatabaseGenerator.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Starting Alignify Database Generation...");
        System.out.println("📋 Database: " + databaseName);
        System.out.println("⏰ Timestamp: " + java.time.Instant.now());
        
        try {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoConfig mongoConfig = new MongoConfig();
            
            // Create all collections with validation schemas
            System.out.println("\n📦 Creating collections with validation schemas...");
            
            createCollection(database, mongoConfig, "users", () -> mongoConfig.create_usersCollection(database));
            createCollection(database, mongoConfig, "roles", () -> mongoConfig.create_rolesCollection(database));
            createCollection(database, mongoConfig, "categories", () -> mongoConfig.create_categoriesCollection(database));
            createCollection(database, mongoConfig, "influencers", () -> mongoConfig.create_influencersCollection(database));
            createCollection(database, mongoConfig, "brands", () -> mongoConfig.create_brandsCollection(database));
            createCollection(database, mongoConfig, "admins", () -> mongoConfig.create_adminsCollection(database));
            createCollection(database, mongoConfig, "campaigns", () -> mongoConfig.create_campaignsCollection(database));
            createCollection(database, mongoConfig, "applications", () -> mongoConfig.create_applicationsCollection(database));
            createCollection(database, mongoConfig, "invitations", () -> mongoConfig.create_invitationsCollection(database));
            createCollection(database, mongoConfig, "campaignTrackings", () -> mongoConfig.create_campaignTrackingsCollection(database));
            createCollection(database, mongoConfig, "contentPostings", () -> mongoConfig.create_contentPostingsCollection(database));
            createCollection(database, mongoConfig, "likes", () -> mongoConfig.create_likesCollection(database));
            createCollection(database, mongoConfig, "comments", () -> mongoConfig.create_commentsCollection(database));
            createCollection(database, mongoConfig, "chatRooms", () -> mongoConfig.create_chatRoomsCollection(database));
            createCollection(database, mongoConfig, "messages", () -> mongoConfig.create_messagesCollection(database));
            createCollection(database, mongoConfig, "notifications", () -> mongoConfig.create_notificationsCollection(database));
            createCollection(database, mongoConfig, "permissions", () -> mongoConfig.create_permissionsCollection(database));
            createCollection(database, mongoConfig, "planPermissions", () -> mongoConfig.create_planPermissionsCollection(database));
            createCollection(database, mongoConfig, "plans", () -> mongoConfig.create_plansCollection(database));
            createCollection(database, mongoConfig, "userPlans", () -> mongoConfig.create_userPlansCollection(database));
            createCollection(database, mongoConfig, "galleries", () -> mongoConfig.create_galleriesCollection(database));
            createCollection(database, mongoConfig, "galleryImages", () -> mongoConfig.create_galleryImagesCollection(database));
            createCollection(database, mongoConfig, "otps", () -> mongoConfig.create_otpsCollection(database));
            createCollection(database, mongoConfig, "accountVerifieds", () -> mongoConfig.create_accountVerifiedsCollection(database));
            createCollection(database, mongoConfig, "userBans", () -> mongoConfig.create_userBansCollection(database));
            createCollection(database, mongoConfig, "reasons", () -> mongoConfig.create_reasonsCollection(database));
            createCollection(database, mongoConfig, "assistantMessages", () -> mongoConfig.create_assistantMessagesCollection(database));
            
            // Create indexes for better performance
            System.out.println("\n🔍 Creating indexes for optimal performance...");
            createIndexes(database);
            
            // Insert default data
            System.out.println("\n📝 Inserting default data...");
            insertDefaultData(database);
            
            // Show statistics
            System.out.println("\n📊 Database Statistics:");
            showDatabaseStats(database);
            
            System.out.println("\n🎉 Alignify Database Generation Completed Successfully!");
            System.out.println("📋 Database: " + database.getName());
            System.out.println("⏰ Completed at: " + java.time.Instant.now());
            
            System.out.println("\n📝 Next steps:");
            System.out.println("1. Verify collections were created correctly");
            System.out.println("2. Configure your application to connect to this database");  
            System.out.println("3. Start your Spring Boot application");
            System.out.println("4. Begin populating with application data");
            
        } catch (Exception e) {
            System.err.println("❌ Error during database generation: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private void createCollection(MongoDatabase database, MongoConfig mongoConfig, String collectionName, Runnable createMethod) {
        try {
            System.out.printf("  📦 Creating collection: %s", collectionName);
            createMethod.run();
            System.out.println(" ✅");
        } catch (Exception e) {
            System.out.println(" ❌");
            System.err.println("    Error: " + e.getMessage());
        }
    }
    
    private void createIndexes(MongoDatabase database) {
        try {
            // User indexes
            database.getCollection("users").createIndex(new org.bson.Document("email", 1), 
                new com.mongodb.client.model.IndexOptions().unique(true));
            database.getCollection("users").createIndex(new org.bson.Document("roleId", 1));
            
            // Campaign indexes
            database.getCollection("campaigns").createIndex(new org.bson.Document("brandId", 1));
            database.getCollection("campaigns").createIndex(new org.bson.Document("status", 1));
            database.getCollection("campaigns").createIndex(new org.bson.Document("categoryIds", 1));
            
            // Application indexes
            database.getCollection("applications").createIndex(
                new org.bson.Document("campaignId", 1).append("influencerId", 1),
                new com.mongodb.client.model.IndexOptions().unique(true));
            database.getCollection("applications").createIndex(new org.bson.Document("brandId", 1));
            
            // Invitation indexes
            database.getCollection("invitations").createIndex(
                new org.bson.Document("campaignId", 1).append("influencerId", 1),
                new com.mongodb.client.model.IndexOptions().unique(true));
            
            // Message and chat indexes
            database.getCollection("messages").createIndex(
                new org.bson.Document("chatRoomId", 1).append("sendAt", -1));
            database.getCollection("chatRooms").createIndex(new org.bson.Document("members", 1));
            
            // Notification indexes
            database.getCollection("notifications").createIndex(
                new org.bson.Document("userId", 1).append("createdAt", -1));
            database.getCollection("notifications").createIndex(new org.bson.Document("isRead", 1));
            
            // Content and interaction indexes
            database.getCollection("contentPostings").createIndex(
                new org.bson.Document("userId", 1).append("createdDate", -1));
            database.getCollection("likes").createIndex(
                new org.bson.Document("userId", 1).append("contentId", 1),
                new com.mongodb.client.model.IndexOptions().unique(true));
            database.getCollection("comments").createIndex(
                new org.bson.Document("contentId", 1).append("createdAt", -1));
            
            // OTP and verification indexes
            database.getCollection("otps").createIndex(new org.bson.Document("email", 1));
            database.getCollection("otps").createIndex(new org.bson.Document("createdAt", 1),
                new com.mongodb.client.model.IndexOptions().expireAfter(300L, java.util.concurrent.TimeUnit.SECONDS));
            database.getCollection("accountVerifieds").createIndex(new org.bson.Document("email", 1),
                new com.mongodb.client.model.IndexOptions().unique(true));
            
            System.out.println("  ✅ Indexes created successfully");
        } catch (Exception e) {
            System.out.println("  ❌ Error creating indexes: " + e.getMessage());
        }
    }
    
    private void insertDefaultData(MongoDatabase database) {
        try {
            // Insert default roles (matching EnvConfig role IDs)
            java.util.List<org.bson.Document> roles = java.util.Arrays.asList(
                new org.bson.Document("_id", new org.bson.types.ObjectId("68485dcedda6867ca0d23e89"))
                    .append("roleName", "ADMIN"),
                new org.bson.Document("_id", new org.bson.types.ObjectId("68485dcedda6867ca0d23e8a"))
                    .append("roleName", "BRAND"),
                new org.bson.Document("_id", new org.bson.types.ObjectId("68485dcedda6867ca0d23e8b"))
                    .append("roleName", "INFLUENCER")
            );
            
            database.getCollection("roles").insertMany(roles);
            System.out.println("  ✅ Default roles inserted");
            
            // Insert default categories
            java.util.List<org.bson.Document> categories = java.util.Arrays.asList(
                new org.bson.Document("categoryName", "thời trang"),
                new org.bson.Document("categoryName", "mỹ phẩm"), 
                new org.bson.Document("categoryName", "công nghệ"),
                new org.bson.Document("categoryName", "nghệ thuật"),
                new org.bson.Document("categoryName", "thể thao"),
                new org.bson.Document("categoryName", "ăn uống"),
                new org.bson.Document("categoryName", "du lịch"),
                new org.bson.Document("categoryName", "lối sống"),
                new org.bson.Document("categoryName", "âm nhạc"),
                new org.bson.Document("categoryName", "trò chơi điện tử"),
                new org.bson.Document("categoryName", "handmade"),
                new org.bson.Document("categoryName", "phong tục và văn hóa"),
                new org.bson.Document("categoryName", "khởi nghiệp"),
                new org.bson.Document("categoryName", "kĩ năng mềm"),
                new org.bson.Document("categoryName", "mẹ và bé")
            );
            
            database.getCollection("categories").insertMany(categories);
            System.out.println("  ✅ Default categories inserted");
            
            // Insert default permissions
            java.util.List<org.bson.Document> permissions = java.util.Arrays.asList(
                new org.bson.Document("permissionName", "posting")
                    .append("permissionDescription", "Permission to create and manage posts"),
                new org.bson.Document("permissionName", "comment")
                    .append("permissionDescription", "Permission to comment on posts"),
                new org.bson.Document("permissionName", "all")
                    .append("permissionDescription", "Full access permissions")
            );
            
            database.getCollection("permissions").insertMany(permissions);
            System.out.println("  ✅ Default permissions inserted");
            
        } catch (Exception e) {
            System.out.println("  ❌ Error inserting default data: " + e.getMessage());
        }
    }
    
    private void showDatabaseStats(MongoDatabase database) {
        try {
            java.util.List<String> collectionNames = new java.util.ArrayList<>();
            database.listCollectionNames().into(collectionNames);
            
            System.out.println("Collections created: " + collectionNames.size());
            
            for (String collectionName : collectionNames) {
                long count = database.getCollection(collectionName).countDocuments();
                System.out.printf("  - %s: %d documents%n", collectionName, count);
            }
        } catch (Exception e) {
            System.out.println("  ❌ Error getting database stats: " + e.getMessage());
        }
    }
}