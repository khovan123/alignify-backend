/**
 * MongoDB Database Generation Script for Alignify Backend
 * 
 * This script creates all collections with their validation schemas
 * based on the MongoConfig.java file from the Spring Boot application.
 * 
 * Usage:
 * 1. Connect to your MongoDB instance
 * 2. Switch to your target database: use alignify_db
 * 3. Run this script: load('scripts/generate-database.js')
 * 
 * Or run directly from command line:
 * mongosh alignify_db --file scripts/generate-database.js
 */

// Database name - can be changed as needed
const DATABASE_NAME = "alignify_db";

print("🚀 Starting Alignify Database Generation...");
print("📋 Database: " + DATABASE_NAME);
print("⏰ Timestamp: " + new Date().toISOString());

// Use the specified database
db = db.getSiblingDB(DATABASE_NAME);

// Helper function to drop collection if exists and create with validation
function createCollectionWithValidation(collectionName, validationSchema) {
    print(`\n📦 Creating collection: ${collectionName}`);
    
    // Drop collection if it exists
    try {
        if (db.getCollection(collectionName).stats().ok) {
            db.getCollection(collectionName).drop();
            print(`  ✅ Dropped existing collection: ${collectionName}`);
        }
    } catch (e) {
        // Collection doesn't exist, continue
    }
    
    // Create collection with validation
    try {
        db.createCollection(collectionName, {
            validator: {
                $jsonSchema: validationSchema
            }
        });
        print(`  ✅ Created collection with validation: ${collectionName}`);
    } catch (e) {
        print(`  ❌ Error creating collection ${collectionName}: ${e.message}`);
    }
}

// 1. Create Users Collection
createCollectionWithValidation("users", {
    bsonType: "object",
    required: ["name", "email", "password", "roleId"],
    properties: {
        name: {
            bsonType: "string"
        },
        email: {
            bsonType: "string",
            pattern: "^.+@.+\\..+$"
        },
        avatarUrl: {
            bsonType: "string",
            pattern: "^https?://.+$"
        },
        backgroundUrl: {
            bsonType: "string"
        },
        password: {
            bsonType: "string"
        },
        roleId: {
            bsonType: "string"
        },
        isActive: {
            bsonType: "bool"
        },
        createdAt: {
            bsonType: "date"
        },
        permissionIds: {
            bsonType: "array",
            items: {
                bsonType: "string"
            }
        },
        userPlanId: {
            bsonType: "string"
        }
    }
});

// 2. Create Roles Collection
createCollectionWithValidation("roles", {
    bsonType: "object",
    required: ["roleName"],
    properties: {
        roleName: {
            bsonType: "string"
        }
    }
});

// Insert default roles
print("\n📝 Inserting default roles...");
const roles = [
    { _id: ObjectId("68485dcedda6867ca0d23e89"), roleName: "ADMIN" },
    { _id: ObjectId("68485dcedda6867ca0d23e8a"), roleName: "BRAND" },
    { _id: ObjectId("68485dcedda6867ca0d23e8b"), roleName: "INFLUENCER" }
];

try {
    db.roles.insertMany(roles);
    print("  ✅ Default roles inserted successfully");
} catch (e) {
    print(`  ❌ Error inserting roles: ${e.message}`);
}

// 3. Create Categories Collection
createCollectionWithValidation("categories", {
    bsonType: "object",
    required: ["categoryName"],
    properties: {
        categoryName: {
            bsonType: "string"
        }
    }
});

// Insert default categories
print("\n📝 Inserting default categories...");
const categories = [
    { categoryName: "thời trang" },
    { categoryName: "mỹ phẩm" },
    { categoryName: "công nghệ" },
    { categoryName: "nghệ thuật" },
    { categoryName: "thể thao" },
    { categoryName: "ăn uống" },
    { categoryName: "du lịch" },
    { categoryName: "lối sống" },
    { categoryName: "âm nhạc" },
    { categoryName: "trò chơi điện tử" },
    { categoryName: "handmade" },
    { categoryName: "phong tục và văn hóa" },
    { categoryName: "khởi nghiệp" },
    { categoryName: "kĩ năng mềm" },
    { categoryName: "mẹ và bé" }
];

try {
    db.categories.insertMany(categories);
    print("  ✅ Default categories inserted successfully");
} catch (e) {
    print(`  ❌ Error inserting categories: ${e.message}`);
}

// 4. Create Influencers Collection
createCollectionWithValidation("influencers", {
    bsonType: "object",
    properties: {
        _id: {
            bsonType: "objectId"
        },
        DoB: {
            bsonType: "date"
        },
        gender: {
            bsonType: "string",
            enum: ["MALE", "FEMALE", "OTHER", "LGBT", "NONE"]
        },
        bio: {
            bsonType: "string"
        },
        socialMediaLinks: {
            bsonType: "array",
            items: {
                bsonType: "object",
                required: ["platform", "url", "follower"],
                properties: {
                    platform: {
                        bsonType: "string",
                        enum: ["FACEBOOK", "TIKTOK", "YOUTUBE", "INSTAGRAM"]
                    },
                    url: {
                        bsonType: "string"
                    },
                    follower: {
                        bsonType: "int"
                    }
                }
            }
        },
        rating: {
            bsonType: "double"
        },
        categoryIds: {
            bsonType: "array",
            items: {
                bsonType: "string"
            }
        },
        follower: {
            bsonType: "int"
        },
        isPublic: {
            bsonType: "bool"
        },
        createdAt: {
            bsonType: "date"
        }
    }
});

// 5. Create Brands Collection
createCollectionWithValidation("brands", {
    bsonType: "object",
    properties: {
        _id: {
            bsonType: "objectId"
        },
        bio: {
            bsonType: "string"
        },
        contacts: {
            bsonType: "array",
            items: {
                bsonType: "object",
                properties: {
                    contact_type: {
                        bsonType: "string"
                    },
                    contact_infor: {
                        bsonType: "string"
                    }
                }
            }
        },
        socialMediaLinks: {
            bsonType: "array",
            items: {
                bsonType: "object",
                required: ["platform", "url", "follower"],
                properties: {
                    platform: {
                        bsonType: "string",
                        enum: ["FACEBOOK", "TIKTOK", "YOUTUBE", "INSTAGRAM"]
                    },
                    url: {
                        bsonType: "string"
                    },
                    follower: {
                        bsonType: "int"
                    }
                }
            }
        },
        categoryIds: {
            bsonType: "array",
            items: {
                bsonType: "string"
            }
        },
        establishDate: {
            bsonType: "date"
        },
        createdAt: {
            bsonType: "date"
        },
        totalCampaign: {
            bsonType: "int"
        }
    }
});

// 6. Create Admins Collection
createCollectionWithValidation("admins", {
    bsonType: "object",
    properties: {
        _id: {
            bsonType: "objectId"
        },
        createdAt: {
            bsonType: "date"
        }
    }
});

// 7. Create Campaigns Collection
createCollectionWithValidation("campaigns", {
    bsonType: "object",
    required: ["brandId", "campaignName", "content", "budget", "imageUrl", "status", "campaignRequirements", "influencerRequirements", "startAt", "dueAt", "influencerCountExpected", "joinedInfluencerIds", "applicationTotal", "appliedInfluencerIds"],
    properties: {
        brandId: {
            bsonType: "string"
        },
        campaignName: {
            bsonType: "string"
        },
        content: {
            bsonType: "string"
        },
        imageUrl: {
            bsonType: "string"
        },
        categoryIds: {
            bsonType: "array",
            items: {
                bsonType: "string"
            }
        },
        status: {
            bsonType: "string",
            enum: ["DRAFT", "RECRUITING", "PENDING", "PARTICIPATING", "COMPLETED"]
        },
        createdAt: {
            bsonType: "date"
        },
        startAt: {
            bsonType: "date"
        },
        dueAt: {
            bsonType: "date"
        },
        budget: {
            bsonType: "int"
        },
        campaignRequirements: {
            bsonType: "array",
            items: {
                bsonType: "object",
                required: ["platform", "post_type", "quantity", "details"],
                properties: {
                    platform: { bsonType: "string" },
                    post_type: { bsonType: "string" },
                    quantity: { bsonType: "int" },
                    details: {
                        bsonType: "array",
                        items: {
                            bsonType: "object",
                            properties: {
                                post_type: { bsonType: "string" },
                                like: { bsonType: "int" },
                                comment: { bsonType: "int" },
                                share: { bsonType: "int" },
                                view: { bsonType: "int" }
                            }
                        }
                    }
                }
            }
        },
        influencerRequirements: {
            bsonType: "array",
            items: {
                bsonType: "object",
                required: ["platform", "followers"],
                properties: {
                    platform: { bsonType: "string" },
                    followers: { bsonType: "int" }
                }
            }
        },
        influencerCountExpected: {
            bsonType: "int"
        },
        joinedInfluencerIds: {
            bsonType: "array",
            items: {
                bsonType: "string"
            }
        },
        applicationTotal: {
            bsonType: "int"
        },
        appliedInfluencerIds: {
            bsonType: "array",
            items: {
                bsonType: "string"
            }
        },
        contractUrl: {
            bsonType: "string"
        }
    }
});

// 8. Create Applications Collection
createCollectionWithValidation("applications", {
    bsonType: "object",
    required: ["campaignId", "influencerId", "brandId", "limited", "status", "cv_url"],
    properties: {
        campaignId: {
            bsonType: "string"
        },
        influencerId: {
            bsonType: "string"
        },
        brandId: {
            bsonType: "string"
        },
        cv_url: {
            bsonType: "string"
        },
        limited: {
            bsonType: "int"
        },
        status: {
            bsonType: "string",
            enum: ["PENDING", "ACCEPTED", "REJECTED"]
        },
        createdAt: {
            bsonType: "date"
        }
    }
});

// 9. Create Invitations Collection
createCollectionWithValidation("invitations", {
    bsonType: "object",
    required: ["brandId", "influencerId", "campaignId", "message", "status"],
    properties: {
        campaignId: {
            bsonType: "string"
        },
        influencerId: {
            bsonType: "string"
        },
        brandId: {
            bsonType: "string"
        },
        message: {
            bsonType: "string"
        },
        status: {
            bsonType: "string",
            enum: ["PENDING", "ACCEPTED", "REJECTED"]
        },
        createdAt: {
            bsonType: "date"
        }
    }
});

// 10. Create Campaign Trackings Collection
createCollectionWithValidation("campaignTrackings", {
    bsonType: "object",
    required: ["campaignId", "brandId", "influencerId", "platformRequirementTracking", "process", "status", "createdAt"],
    properties: {
        _id: {
            bsonType: "objectId"
        },
        campaignId: {
            bsonType: "string"
        },
        brandId: {
            bsonType: "string"
        },
        influencerId: {
            bsonType: "string"
        },
        platformRequirementTracking: {
            bsonType: "array",
            items: {
                bsonType: "object",
                required: ["platform", "post_type", "quantity", "details"],
                properties: {
                    platform: {
                        bsonType: "string"
                    },
                    post_type: {
                        bsonType: "string"
                    },
                    quantity: {
                        bsonType: "int"
                    },
                    details: {
                        bsonType: "array",
                        items: {
                            bsonType: "object",
                            required: ["post_type"],
                            properties: {
                                post_type: {
                                    bsonType: "string"
                                },
                                like: {
                                    bsonType: "int"
                                },
                                comment: {
                                    bsonType: "int"
                                },
                                share: {
                                    bsonType: "int"
                                },
                                view: {
                                    bsonType: "int"
                                },
                                postUrl: {
                                    bsonType: ["string", "null"]
                                },
                                status: {
                                    bsonType: ["string", "null"],
                                    enum: [null, "PENDING", "ACCEPTED", "REJECTED"]
                                },
                                uploadedAt: {
                                    bsonType: ["date", "null"]
                                }
                            }
                        }
                    }
                }
            }
        },
        process: {
            bsonType: "double",
            minimum: 0,
            maximum: 100
        },
        status: {
            bsonType: "string",
            enum: ["PENDING", "COMPLETED"]
        },
        createdAt: {
            bsonType: "date"
        }
    }
});

// 11. Create Content Postings Collection
createCollectionWithValidation("contentPostings", {
    bsonType: "object",
    required: ["contentName", "content", "userId", "imageUrl"],
    properties: {
        userId: {
            bsonType: "string"
        },
        contentName: {
            bsonType: "string"
        },
        content: {
            bsonType: "string"
        },
        imageUrl: {
            bsonType: "string"
        },
        categoryIds: {
            bsonType: "array",
            items: {
                bsonType: "string"
            }
        },
        createdDate: {
            bsonType: "date"
        },
        isPublic: {
            bsonType: "bool"
        },
        commentCount: {
            bsonType: "int"
        },
        likeCount: {
            bsonType: "int"
        }
    }
});

// 12. Create Likes Collection
createCollectionWithValidation("likes", {
    bsonType: "object",
    required: ["userId", "contentId", "createdAt"],
    properties: {
        userId: {
            bsonType: "string"
        },
        contentId: {
            bsonType: "string"
        },
        createdAt: {
            bsonType: "date"
        }
    }
});

// 13. Create Comments Collection
createCollectionWithValidation("comments", {
    bsonType: "object",
    required: ["userId", "contentId", "content"],
    properties: {
        userId: {
            bsonType: "string"
        },
        contentId: {
            bsonType: "string"
        },
        content: {
            bsonType: "string"
        },
        createdAt: {
            bsonType: "date"
        }
    }
});

// 14. Create Chat Rooms Collection
createCollectionWithValidation("chatRooms", {
    bsonType: "object",
    required: ["roomOwnerId", "roomAvatarUrl", "roomName", "members"],
    properties: {
        _id: {
            bsonType: "objectId"
        },
        roomName: {
            bsonType: "string"
        },
        roomAvatarUrl: {
            bsonType: "string"
        },
        members: {
            bsonType: "array",
            items: {
                bsonType: "string"
            }
        },
        roomOwnerId: {
            bsonType: "string"
        },
        createdAt: {
            bsonType: "date"
        }
    }
});

// 15. Create Messages Collection
createCollectionWithValidation("messages", {
    bsonType: "object",
    required: ["message", "chatRoomId", "userId", "name", "readBy"],
    properties: {
        userId: {
            bsonType: "string"
        },
        chatRoomId: {
            bsonType: "string"
        },
        name: {
            bsonType: "string"
        },
        message: {
            bsonType: "string"
        },
        tempId: {
            bsonType: "string"
        },
        sendAt: {
            bsonType: "date"
        },
        readBy: {
            bsonType: "array",
            items: {
                bsonType: "string"
            }
        }
    }
});

// 16. Create Notifications Collection
createCollectionWithValidation("notifications", {
    bsonType: "object",
    required: ["userId", "name", "content", "avatarUrl"],
    properties: {
        userId: {
            bsonType: "string"
        },
        name: {
            bsonType: "string"
        },
        avatarUrl: {
            bsonType: "string"
        },
        content: {
            bsonType: "string"
        },
        createdAt: {
            bsonType: "date"
        },
        isRead: {
            bsonType: "bool"
        }
    }
});

// 17. Create Permissions Collection
createCollectionWithValidation("permissions", {
    bsonType: "object",
    required: ["permissionName", "permissionDescription"],
    properties: {
        permissionName: {
            bsonType: "string",
            enum: ["posting", "comment", "all"]
        },
        permissionDescription: {
            bsonType: "string"
        }
    }
});

// Insert default permissions
print("\n📝 Inserting default permissions...");
const permissions = [
    { permissionName: "posting", permissionDescription: "Permission to create and manage posts" },
    { permissionName: "comment", permissionDescription: "Permission to comment on posts" },
    { permissionName: "all", permissionDescription: "Full access permissions" }
];

try {
    db.permissions.insertMany(permissions);
    print("  ✅ Default permissions inserted successfully");
} catch (e) {
    print(`  ❌ Error inserting permissions: ${e.message}`);
}

// 18. Create Plan Permissions Collection
createCollectionWithValidation("planPermissions", {
    bsonType: "object",
    required: ["planPermissionName", "limited"],
    properties: {
        planPermissionName: {
            bsonType: "string",
            enum: ["search_result", "campaign_members", "campaign_invitation", "campaign_apply"]
        },
        limited: {
            bsonType: "number"
        }
    }
});

// 19. Create Plans Collection
createCollectionWithValidation("plans", {
    bsonType: "object",
    required: ["planName", "description", "roleId", "planPermissionIds", "price", "planType", "planCount"],
    properties: {
        planName: {
            bsonType: "string"
        },
        description: {
            bsonType: "string"
        },
        roleId: {
            bsonType: "string"
        },
        planPermissionIds: {
            bsonType: "array",
            items: {
                bsonType: "string"
            }
        },
        permissionIds: {
            bsonType: "array",
            items: {
                bsonType: "string"
            }
        },
        price: {
            bsonType: "number"
        },
        isPopular: {
            bsonType: "boolean"
        },
        isActive: {
            bsonType: "boolean"
        },
        discount: {
            bsonType: "number"
        },
        planType: {
            bsonType: "string",
            enum: ["one_month", "monthly", "one_year"]
        },
        planCount: {
            bsonType: "number"
        }
    }
});

// 20. Create User Plans Collection
createCollectionWithValidation("userPlans", {
    bsonType: "object",
    required: ["userId", "planId", "createdAt", "status"],
    properties: {
        userId: {
            bsonType: "string"
        },
        planId: {
            bsonType: "string"
        },
        status: {
            bsonType: "string",
            enum: ["PENDING", "FAILED", "SUCCESS"]
        },
        createdAt: {
            bsonType: "date"
        },
        completedAt: {
            bsonType: "date"
        }
    }
});

// 21. Create Galleries Collection
createCollectionWithValidation("galleries", {
    bsonType: "object",
    required: ["images"],
    properties: {
        _id: {
            bsonType: "objectId"
        },
        images: {
            bsonType: "array",
            items: {
                bsonType: "string"
            }
        },
        createdAt: {
            bsonType: "date"
        }
    }
});

// 22. Create Gallery Images Collection
createCollectionWithValidation("galleryImages", {
    bsonType: "object",
    required: ["imageUrl"],
    properties: {
        imageUrl: {
            bsonType: "string",
            pattern: "^https?://.+$"
        },
        createdAt: {
            bsonType: "date"
        }
    }
});

// 23. Create OTPs Collection
createCollectionWithValidation("otps", {
    bsonType: "object",
    required: ["otpCode", "email"],
    properties: {
        email: {
            bsonType: "string"
        },
        otpCode: {
            bsonType: "string",
            pattern: "^[A-Z0-9]{6}$"
        },
        requestCount: {
            bsonType: "int"
        },
        attemptCount: {
            bsonType: "int"
        },
        createdAt: {
            bsonType: "date"
        }
    }
});

// 24. Create Account Verifieds Collection
createCollectionWithValidation("accountVerifieds", {
    bsonType: "object",
    required: ["email"],
    properties: {
        email: {
            bsonType: "string"
        },
        createdAt: {
            bsonType: "date"
        }
    }
});

// 25. Create User Bans Collection
createCollectionWithValidation("userBans", {
    bsonType: "object",
    required: ["reasonId"],
    properties: {
        roleId: {
            bsonType: "string"
        },
        reasonId: {
            bsonType: "string"
        },
        createdAt: {
            bsonType: "date"
        }
    }
});

// 26. Create Reasons Collection
createCollectionWithValidation("reasons", {
    bsonType: "object",
    required: ["title", "description"],
    properties: {
        title: {
            bsonType: "string"
        },
        description: {
            bsonType: "string"
        }
    }
});

// 27. Create Assistant Messages Collection
createCollectionWithValidation("assistantMessages", {
    bsonType: "object",
    required: ["roomId", "senderId", "senderType", "messageType", "content", "createdAt"],
    properties: {
        roomId: {
            bsonType: "string"
        },
        senderId: {
            bsonType: "string"
        },
        senderType: {
            bsonType: "string",
            enum: ["USER", "ASSISTANT"]
        },
        messageType: {
            bsonType: "string",
            enum: ["TEXT", "CAMPAIGN_RECOMMENDATIONS"]
        },
        content: {
            bsonType: "string"
        },
        createdAt: {
            bsonType: "date"
        }
    }
});

// Create useful indexes for better performance
print("\n🔍 Creating indexes...");

try {
    // User indexes
    db.users.createIndex({ "email": 1 }, { unique: true });
    db.users.createIndex({ "roleId": 1 });
    
    // Campaign indexes
    db.campaigns.createIndex({ "brandId": 1 });
    db.campaigns.createIndex({ "status": 1 });
    db.campaigns.createIndex({ "categoryIds": 1 });
    
    // Application indexes
    db.applications.createIndex({ "campaignId": 1, "influencerId": 1 }, { unique: true });
    db.applications.createIndex({ "brandId": 1 });
    
    // Invitation indexes
    db.invitations.createIndex({ "campaignId": 1, "influencerId": 1 }, { unique: true });
    
    // Message and chat indexes
    db.messages.createIndex({ "chatRoomId": 1, "sendAt": -1 });
    db.chatRooms.createIndex({ "members": 1 });
    
    // Notification indexes
    db.notifications.createIndex({ "userId": 1, "createdAt": -1 });
    db.notifications.createIndex({ "isRead": 1 });
    
    // Content and interaction indexes
    db.contentPostings.createIndex({ "userId": 1, "createdDate": -1 });
    db.likes.createIndex({ "userId": 1, "contentId": 1 }, { unique: true });
    db.comments.createIndex({ "contentId": 1, "createdAt": -1 });
    
    // OTP and verification indexes
    db.otps.createIndex({ "email": 1 });
    db.otps.createIndex({ "createdAt": 1 }, { expireAfterSeconds: 300 }); // 5 minutes TTL
    db.accountVerifieds.createIndex({ "email": 1 }, { unique: true });
    
    print("  ✅ Indexes created successfully");
} catch (e) {
    print(`  ❌ Error creating indexes: ${e.message}`);
}

// Show database statistics
print("\n📊 Database Statistics:");
print("Collections created: " + db.getCollectionNames().length);

db.getCollectionNames().forEach(function(collection) {
    const count = db.getCollection(collection).countDocuments();
    print(`  - ${collection}: ${count} documents`);
});

print("\n🎉 Alignify Database Generation Completed Successfully!");
print("📋 Database: " + db.getName());
print("⏰ Completed at: " + new Date().toISOString());
print("\n📝 Next steps:");
print("1. Verify collections were created correctly");
print("2. Configure your application to connect to this database");
print("3. Start your Spring Boot application");
print("4. Begin populating with application data");