/**
 * MongoDB Collection Validator Update Script for Alignify Backend
 * 
 * This script adds JSON Schema validation to existing collections using db.runCommand with collMod.
 * It reads all schema definitions from MongoConfig.java and applies them to existing collections.
 * 
 * Usage:
 * 1. Connect to your MongoDB instance
 * 2. Switch to your target database: use alignify_db
 * 3. Run this script: load('scripts/update-collection-validators.js')
 * 
 * Or run directly from command line:
 * mongosh alignify_db --file scripts/update-collection-validators.js
 */

// Database name - can be changed as needed
const DATABASE_NAME = "alignify_db";

print("🚀 Starting Alignify Collection Validator Update...");
print("📋 Database: " + DATABASE_NAME);
print("⏰ Timestamp: " + new Date().toISOString());

// Use the specified database
db = db.getSiblingDB(DATABASE_NAME);

// Helper function to update collection validation using collMod
function updateCollectionValidator(collectionName, validationSchema, validationLevel = "strict", validationAction = "error") {
    print(`\n📦 Updating validator for collection: ${collectionName}`);
    
    try {
        // Check if collection exists
        const collections = db.getCollectionNames();
        if (!collections.includes(collectionName)) {
            print(`  ⚠️  Collection '${collectionName}' does not exist, creating it first...`);
            db.createCollection(collectionName);
        }
        
        // Update collection validator
        const result = db.runCommand({
            collMod: collectionName,
            validator: {
                $jsonSchema: validationSchema
            },
            validationLevel: validationLevel,
            validationAction: validationAction
        });
        
        if (result.ok === 1) {
            print(`  ✅ Successfully updated validator for: ${collectionName}`);
        } else {
            print(`  ❌ Failed to update validator for ${collectionName}: ${result.errmsg || 'Unknown error'}`);
        }
    } catch (e) {
        print(`  ❌ Error updating validator for ${collectionName}: ${e.message}`);
    }
}

// 1. Users Collection
print("\n=== 1. USERS COLLECTION ===");
updateCollectionValidator("users", {
    bsonType: "object",
    required: ["name", "email", "password", "roleId"],
    properties: {
        name: {
            bsonType: "string",
            description: "must be a string and is required"
        },
        email: {
            bsonType: "string",
            pattern: "^.+@.+\\..+$",
            description: "must be a valid email and is required"
        },
        avatarUrl: {
            bsonType: "string",
            pattern: "^https?://.+$",
            description: "must be a valid URL if provided"
        },
        backgroundUrl: {
            bsonType: "string",
            description: "can be a string if provided"
        },
        password: {
            bsonType: "string",
            description: "must be a string and is required"
        },
        roleId: {
            bsonType: "string",
            description: "must be a string and is required"
        },
        isActive: {
            bsonType: "bool",
            description: "must be a boolean if provided"
        },
        createdAt: {
            bsonType: "date",
            description: "must be a date if provided"
        },
        permissionIds: {
            bsonType: "array",
            items: {
                bsonType: "string"
            },
            description: "must be an array of strings if provided"
        },
        userPlanId: {
            bsonType: "string",
            description: "must be a string if provided"
        }
    }
});

// 2. Roles Collection
print("\n=== 2. ROLES COLLECTION ===");
updateCollectionValidator("roles", {
    bsonType: "object",
    required: ["roleName"],
    properties: {
        roleName: {
            bsonType: "string",
            description: "must be a string and is required"
        }
    }
});

// 3. Categories Collection
print("\n=== 3. CATEGORIES COLLECTION ===");
updateCollectionValidator("categories", {
    bsonType: "object",
    required: ["categoryName"],
    properties: {
        categoryName: {
            bsonType: "string",
            description: "must be a string and is required"
        }
    }
});

// 4. Influencers Collection
print("\n=== 4. INFLUENCERS COLLECTION ===");
updateCollectionValidator("influencers", {
    bsonType: "object",
    properties: {
        _id: {
            bsonType: "objectId"
        },
        DoB: {
            bsonType: "date",
            description: "date of birth"
        },
        gender: {
            bsonType: "string",
            enum: ["MALE", "FEMALE", "OTHER", "LGBT", "NONE"],
            description: "must be one of the allowed gender values"
        },
        bio: {
            bsonType: "string",
            description: "biography text"
        },
        socialMediaLinks: {
            bsonType: "array",
            items: {
                bsonType: "object",
                required: ["platform", "url", "follower"],
                properties: {
                    platform: {
                        bsonType: "string",
                        enum: ["FACEBOOK", "TIKTOK", "YOUTUBE", "INSTAGRAM"],
                        description: "must be a valid social media platform"
                    },
                    url: {
                        bsonType: "string",
                        description: "social media profile URL"
                    },
                    follower: {
                        bsonType: "int",
                        description: "number of followers"
                    }
                }
            }
        },
        rating: {
            bsonType: "double",
            description: "influencer rating"
        },
        categoryIds: {
            bsonType: "array",
            items: {
                bsonType: "string"
            },
            description: "array of category IDs"
        },
        follower: {
            bsonType: "int",
            description: "total follower count"
        },
        isPublic: {
            bsonType: "bool",
            description: "whether profile is public"
        },
        createdAt: {
            bsonType: "date",
            description: "creation timestamp"
        }
    }
});

// 5. Brands Collection
print("\n=== 5. BRANDS COLLECTION ===");
updateCollectionValidator("brands", {
    bsonType: "object",
    properties: {
        _id: {
            bsonType: "objectId"
        },
        bio: {
            bsonType: "string",
            description: "brand biography"
        },
        contacts: {
            bsonType: "array",
            items: {
                bsonType: "object",
                properties: {
                    contact_type: {
                        bsonType: "string",
                        description: "type of contact"
                    },
                    contact_infor: {
                        bsonType: "string",
                        description: "contact information"
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
                        enum: ["FACEBOOK", "TIKTOK", "YOUTUBE", "INSTAGRAM"],
                        description: "social media platform"
                    },
                    url: {
                        bsonType: "string",
                        description: "social media URL"
                    },
                    follower: {
                        bsonType: "int",
                        description: "follower count"
                    }
                }
            }
        },
        categoryIds: {
            bsonType: "array",
            items: {
                bsonType: "string"
            },
            description: "array of category IDs"
        },
        establishDate: {
            bsonType: "date",
            description: "brand establishment date"
        },
        createdAt: {
            bsonType: "date",
            description: "creation timestamp"
        },
        totalCampaign: {
            bsonType: "int",
            description: "total number of campaigns"
        }
    }
});

// 6. Admins Collection
print("\n=== 6. ADMINS COLLECTION ===");
updateCollectionValidator("admins", {
    bsonType: "object",
    properties: {
        _id: {
            bsonType: "objectId"
        },
        createdAt: {
            bsonType: "date",
            description: "creation timestamp"
        }
    }
});

// 7. Galleries Collection
print("\n=== 7. GALLERIES COLLECTION ===");
updateCollectionValidator("galleries", {
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
            },
            description: "array of image URLs and is required"
        },
        createdAt: {
            bsonType: "date",
            description: "creation timestamp"
        }
    }
});

// 8. Gallery Images Collection
print("\n=== 8. GALLERY IMAGES COLLECTION ===");
updateCollectionValidator("galleryImages", {
    bsonType: "object",
    required: ["imageUrl"],
    properties: {
        imageUrl: {
            bsonType: "string",
            pattern: "^https?://.+$",
            description: "must be a valid image URL and is required"
        },
        createdAt: {
            bsonType: "date",
            description: "creation timestamp"
        }
    }
});

// 9. OTPs Collection
print("\n=== 9. OTPS COLLECTION ===");
updateCollectionValidator("otps", {
    bsonType: "object",
    required: ["otpCode", "email"],
    properties: {
        email: {
            bsonType: "string",
            description: "email address and is required"
        },
        otpCode: {
            bsonType: "string",
            pattern: "^[A-Z0-9]{6}$",
            description: "must be a 6-character alphanumeric OTP code and is required"
        },
        requestCount: {
            bsonType: "int",
            description: "number of OTP requests"
        },
        attemptCount: {
            bsonType: "int",
            description: "number of verification attempts"
        },
        createdAt: {
            bsonType: "date",
            description: "creation timestamp"
        }
    }
});

// 10. Account Verifieds Collection
print("\n=== 10. ACCOUNT VERIFIEDS COLLECTION ===");
updateCollectionValidator("accountVerifieds", {
    bsonType: "object",
    required: ["email"],
    properties: {
        email: {
            bsonType: "string",
            description: "verified email address and is required"
        },
        createdAt: {
            bsonType: "date",
            description: "verification timestamp"
        }
    }
});

// 11. Content Postings Collection
print("\n=== 11. CONTENT POSTINGS COLLECTION ===");
updateCollectionValidator("contentPostings", {
    bsonType: "object",
    required: ["contentName", "content", "userId", "imageUrl"],
    properties: {
        userId: {
            bsonType: "string",
            description: "user ID who created the content and is required"
        },
        contentName: {
            bsonType: "string",
            description: "content title and is required"
        },
        content: {
            bsonType: "string",
            description: "content body and is required"
        },
        imageUrl: {
            bsonType: "string",
            description: "content image URL and is required"
        },
        categoryIds: {
            bsonType: "array",
            items: {
                bsonType: "string"
            },
            description: "array of category IDs"
        },
        createdDate: {
            bsonType: "date",
            description: "content creation date"
        },
        isPublic: {
            bsonType: "bool",
            description: "whether content is public"
        },
        commentCount: {
            bsonType: "int",
            description: "number of comments"
        },
        likeCount: {
            bsonType: "int",
            description: "number of likes"
        }
    }
});

// 12. Likes Collection
print("\n=== 12. LIKES COLLECTION ===");
updateCollectionValidator("likes", {
    bsonType: "object",
    required: ["userId", "contentId", "createdAt"],
    properties: {
        userId: {
            bsonType: "string",
            description: "user who liked the content and is required"
        },
        contentId: {
            bsonType: "string",
            description: "content that was liked and is required"
        },
        createdAt: {
            bsonType: "date",
            description: "like timestamp and is required"
        }
    }
});

// 13. Comments Collection
print("\n=== 13. COMMENTS COLLECTION ===");
updateCollectionValidator("comments", {
    bsonType: "object",
    required: ["userId", "contentId", "content"],
    properties: {
        userId: {
            bsonType: "string",
            description: "user who made the comment and is required"
        },
        contentId: {
            bsonType: "string",
            description: "content being commented on and is required"
        },
        content: {
            bsonType: "string",
            description: "comment text and is required"
        },
        createdAt: {
            bsonType: "date",
            description: "comment timestamp"
        }
    }
});

// 14. Campaigns Collection
print("\n=== 14. CAMPAIGNS COLLECTION ===");
updateCollectionValidator("campaigns", {
    bsonType: "object",
    required: ["brandId", "campaignName", "content", "budget", "imageUrl", "status", "campaignRequirements", "influencerRequirements", "startAt", "dueAt", "influencerCountExpected", "joinedInfluencerIds", "applicationTotal", "appliedInfluencerIds"],
    properties: {
        brandId: {
            bsonType: "string",
            description: "brand ID that owns the campaign and is required"
        },
        campaignName: {
            bsonType: "string",
            description: "campaign name and is required"
        },
        content: {
            bsonType: "string",
            description: "campaign description and is required"
        },
        imageUrl: {
            bsonType: "string",
            description: "campaign image URL and is required"
        },
        categoryIds: {
            bsonType: "array",
            items: {
                bsonType: "string"
            },
            description: "array of category IDs"
        },
        status: {
            bsonType: "string",
            enum: ["DRAFT", "RECRUITING", "PENDING", "PARTICIPATING", "COMPLETED"],
            description: "campaign status and is required"
        },
        createdAt: {
            bsonType: "date",
            description: "campaign creation timestamp"
        },
        startAt: {
            bsonType: "date",
            description: "campaign start date and is required"
        },
        dueAt: {
            bsonType: "date",
            description: "campaign due date and is required"
        },
        budget: {
            bsonType: "int",
            description: "campaign budget and is required"
        },
        campaignRequirements: {
            bsonType: "array",
            items: {
                bsonType: "object",
                required: ["platform", "post_type", "quantity", "details"],
                properties: {
                    platform: { 
                        bsonType: "string",
                        description: "social media platform"
                    },
                    post_type: { 
                        bsonType: "string",
                        description: "type of post required"
                    },
                    quantity: { 
                        bsonType: "int",
                        description: "number of posts required"
                    },
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
            },
            description: "campaign requirements and is required"
        },
        influencerRequirements: {
            bsonType: "array",
            items: {
                bsonType: "object",
                required: ["platform", "followers"],
                properties: {
                    platform: { 
                        bsonType: "string",
                        description: "social media platform"
                    },
                    followers: { 
                        bsonType: "int",
                        description: "minimum follower count"
                    }
                }
            },
            description: "influencer requirements and is required"
        },
        influencerCountExpected: {
            bsonType: "int",
            description: "expected number of influencers and is required"
        },
        joinedInfluencerIds: {
            bsonType: "array",
            items: {
                bsonType: "string"
            },
            description: "array of joined influencer IDs and is required"
        },
        applicationTotal: {
            bsonType: "int",
            description: "total number of applications and is required"
        },
        appliedInfluencerIds: {
            bsonType: "array",
            items: {
                bsonType: "string"
            },
            description: "array of applied influencer IDs and is required"
        },
        contractUrl: {
            bsonType: "string",
            description: "contract document URL"
        }
    }
});

// 15. Applications Collection
print("\n=== 15. APPLICATIONS COLLECTION ===");
updateCollectionValidator("applications", {
    bsonType: "object",
    required: ["campaignId", "influencerId", "brandId", "limited", "status", "cv_url"],
    properties: {
        campaignId: {
            bsonType: "string",
            description: "campaign ID and is required"
        },
        influencerId: {
            bsonType: "string",
            description: "influencer ID and is required"
        },
        brandId: {
            bsonType: "string",
            description: "brand ID and is required"
        },
        cv_url: {
            bsonType: "string",
            description: "CV/portfolio URL and is required"
        },
        limited: {
            bsonType: "int",
            description: "limitation value and is required"
        },
        status: {
            bsonType: "string",
            enum: ["PENDING", "ACCEPTED", "REJECTED"],
            description: "application status and is required"
        },
        createdAt: {
            bsonType: "date",
            description: "application timestamp"
        }
    }
});

// 16. Invitations Collection
print("\n=== 16. INVITATIONS COLLECTION ===");
updateCollectionValidator("invitations", {
    bsonType: "object",
    required: ["brandId", "influencerId", "campaignId", "message", "status"],
    properties: {
        campaignId: {
            bsonType: "string",
            description: "campaign ID and is required"
        },
        influencerId: {
            bsonType: "string",
            description: "influencer ID and is required"
        },
        brandId: {
            bsonType: "string",
            description: "brand ID and is required"
        },
        message: {
            bsonType: "string",
            description: "invitation message and is required"
        },
        status: {
            bsonType: "string",
            enum: ["PENDING", "ACCEPTED", "REJECTED"],
            description: "invitation status and is required"
        },
        createdAt: {
            bsonType: "date",
            description: "invitation timestamp"
        }
    }
});

// 17. Campaign Trackings Collection
print("\n=== 17. CAMPAIGN TRACKINGS COLLECTION ===");
updateCollectionValidator("campaignTrackings", {
    bsonType: "object",
    required: ["campaignId", "brandId", "influencerId", "platformRequirementTracking", "process", "status", "createdAt"],
    properties: {
        _id: {
            bsonType: "objectId"
        },
        campaignId: {
            bsonType: "string",
            description: "campaign ID and is required"
        },
        brandId: {
            bsonType: "string",
            description: "brand ID and is required"
        },
        influencerId: {
            bsonType: "string",
            description: "influencer ID and is required"
        },
        platformRequirementTracking: {
            bsonType: "array",
            items: {
                bsonType: "object",
                required: ["platform", "post_type", "quantity", "details"],
                properties: {
                    platform: {
                        bsonType: "string",
                        description: "social media platform"
                    },
                    post_type: {
                        bsonType: "string",
                        description: "type of post"
                    },
                    quantity: {
                        bsonType: "int",
                        description: "number of posts"
                    },
                    details: {
                        bsonType: "array",
                        items: {
                            bsonType: "object",
                            required: ["post_type"],
                            properties: {
                                post_type: {
                                    bsonType: "string",
                                    description: "post type"
                                },
                                like: {
                                    bsonType: "int",
                                    description: "like count"
                                },
                                comment: {
                                    bsonType: "int",
                                    description: "comment count"
                                },
                                share: {
                                    bsonType: "int",
                                    description: "share count"
                                },
                                view: {
                                    bsonType: "int",
                                    description: "view count"
                                },
                                postUrl: {
                                    bsonType: ["string", "null"],
                                    description: "post URL, can be null"
                                },
                                status: {
                                    bsonType: ["string", "null"],
                                    enum: [null, "PENDING", "ACCEPTED", "REJECTED"],
                                    description: "post status, can be null"
                                },
                                uploadedAt: {
                                    bsonType: ["date", "null"],
                                    description: "upload timestamp, can be null"
                                }
                            }
                        }
                    }
                }
            },
            description: "platform requirement tracking and is required"
        },
        process: {
            bsonType: "double",
            minimum: 0,
            maximum: 100,
            description: "completion percentage and is required"
        },
        status: {
            bsonType: "string",
            enum: ["PENDING", "COMPLETED"],
            description: "tracking status and is required"
        },
        createdAt: {
            bsonType: "date",
            description: "creation timestamp and is required"
        }
    }
});

// 18. Messages Collection
print("\n=== 18. MESSAGES COLLECTION ===");
updateCollectionValidator("messages", {
    bsonType: "object",
    required: ["message", "chatRoomId", "userId", "name", "readBy"],
    properties: {
        userId: {
            bsonType: "string",
            description: "user ID who sent the message and is required"
        },
        chatRoomId: {
            bsonType: "string",
            description: "chat room ID and is required"
        },
        name: {
            bsonType: "string",
            description: "sender name and is required"
        },
        message: {
            bsonType: "string",
            description: "message content and is required"
        },
        tempId: {
            bsonType: "string",
            description: "temporary message ID"
        },
        sendAt: {
            bsonType: "date",
            description: "message send timestamp"
        },
        readBy: {
            bsonType: "array",
            items: {
                bsonType: "string"
            },
            description: "array of user IDs who read the message and is required"
        }
    }
});

// 19. Chat Rooms Collection
print("\n=== 19. CHAT ROOMS COLLECTION ===");
updateCollectionValidator("chatRooms", {
    bsonType: "object",
    required: ["roomOwnerId", "roomAvatarUrl", "roomName", "members"],
    properties: {
        _id: {
            bsonType: "objectId"
        },
        roomName: {
            bsonType: "string",
            description: "chat room name and is required"
        },
        roomAvatarUrl: {
            bsonType: "string",
            description: "chat room avatar URL and is required"
        },
        members: {
            bsonType: "array",
            items: {
                bsonType: "string"
            },
            description: "array of member user IDs and is required"
        },
        roomOwnerId: {
            bsonType: "string",
            description: "room owner user ID and is required"
        },
        createdAt: {
            bsonType: "date",
            description: "room creation timestamp"
        }
    }
});

// 20. Notifications Collection
print("\n=== 20. NOTIFICATIONS COLLECTION ===");
updateCollectionValidator("notifications", {
    bsonType: "object",
    required: ["userId", "name", "content", "avatarUrl"],
    properties: {
        userId: {
            bsonType: "string",
            description: "target user ID and is required"
        },
        name: {
            bsonType: "string",
            description: "notification sender name and is required"
        },
        avatarUrl: {
            bsonType: "string",
            description: "sender avatar URL and is required"
        },
        content: {
            bsonType: "string",
            description: "notification content and is required"
        },
        createdAt: {
            bsonType: "date",
            description: "notification timestamp"
        },
        isRead: {
            bsonType: "bool",
            description: "whether notification is read"
        }
    }
});

// 21. User Bans Collection
print("\n=== 21. USER BANS COLLECTION ===");
updateCollectionValidator("userBans", {
    bsonType: "object",
    required: ["reasonId"],
    properties: {
        roleId: {
            bsonType: "string",
            description: "role ID of banned user"
        },
        reasonId: {
            bsonType: "string",
            description: "ban reason ID and is required"
        },
        createdAt: {
            bsonType: "date",
            description: "ban timestamp"
        }
    }
});

// 22. Reasons Collection
print("\n=== 22. REASONS COLLECTION ===");
updateCollectionValidator("reasons", {
    bsonType: "object",
    required: ["title", "description"],
    properties: {
        title: {
            bsonType: "string",
            description: "reason title and is required"
        },
        description: {
            bsonType: "string",
            description: "reason description and is required"
        }
    }
});

// 23. Permissions Collection
print("\n=== 23. PERMISSIONS COLLECTION ===");
updateCollectionValidator("permissions", {
    bsonType: "object",
    required: ["permissionName", "permissionDescription"],
    properties: {
        permissionName: {
            bsonType: "string",
            enum: ["posting", "comment", "all"],
            description: "permission name and is required"
        },
        permissionDescription: {
            bsonType: "string",
            description: "permission description and is required"
        }
    }
});

// 24. Plan Permissions Collection
print("\n=== 24. PLAN PERMISSIONS COLLECTION ===");
updateCollectionValidator("planPermissions", {
    bsonType: "object",
    required: ["planPermissionName", "limited"],
    properties: {
        planPermissionName: {
            bsonType: "string",
            enum: ["search_result", "campaign_members", "campaign_invitation", "campaign_apply"],
            description: "plan permission name and is required"
        },
        limited: {
            bsonType: "number",
            description: "permission limit and is required"
        }
    }
});

// 25. Plans Collection
print("\n=== 25. PLANS COLLECTION ===");
updateCollectionValidator("plans", {
    bsonType: "object",
    required: ["planName", "description", "roleId", "planPermissionIds", "price", "planType", "planCount"],
    properties: {
        planName: {
            bsonType: "string",
            description: "plan name and is required"
        },
        description: {
            bsonType: "string",
            description: "plan description and is required"
        },
        roleId: {
            bsonType: "string",
            description: "target role ID and is required"
        },
        planPermissionIds: {
            bsonType: "array",
            items: {
                bsonType: "string"
            },
            description: "array of plan permission IDs and is required"
        },
        permissionIds: {
            bsonType: "array",
            items: {
                bsonType: "string"
            },
            description: "array of permission IDs"
        },
        price: {
            bsonType: "number",
            description: "plan price and is required"
        },
        isPopular: {
            bsonType: "boolean",
            description: "whether plan is popular"
        },
        isActive: {
            bsonType: "boolean",
            description: "whether plan is active"
        },
        discount: {
            bsonType: "number",
            description: "plan discount percentage"
        },
        planType: {
            bsonType: "string",
            enum: ["one_month", "monthly", "one_year"],
            description: "plan type and is required"
        },
        planCount: {
            bsonType: "number",
            description: "plan count and is required"
        }
    }
});

// 26. User Plans Collection
print("\n=== 26. USER PLANS COLLECTION ===");
updateCollectionValidator("userPlans", {
    bsonType: "object",
    required: ["userId", "planId", "createdAt", "status"],
    properties: {
        userId: {
            bsonType: "string",
            description: "user ID and is required"
        },
        planId: {
            bsonType: "string",
            description: "plan ID and is required"
        },
        status: {
            bsonType: "string",
            enum: ["PENDING", "FAILED", "SUCCESS"],
            description: "user plan status and is required"
        },
        createdAt: {
            bsonType: "date",
            description: "creation timestamp and is required"
        },
        completedAt: {
            bsonType: "date",
            description: "completion timestamp"
        }
    }
});

// 27. Assistant Messages Collection
print("\n=== 27. ASSISTANT MESSAGES COLLECTION ===");
updateCollectionValidator("assistantMessages", {
    bsonType: "object",
    required: ["roomId", "senderId", "senderType", "messageType", "content", "createdAt"],
    properties: {
        roomId: {
            bsonType: "string",
            description: "room ID and is required"
        },
        senderId: {
            bsonType: "string",
            description: "sender ID and is required"
        },
        senderType: {
            bsonType: "string",
            enum: ["USER", "ASSISTANT"],
            description: "sender type and is required"
        },
        messageType: {
            bsonType: "string",
            enum: ["TEXT", "CAMPAIGN_RECOMMENDATIONS"],
            description: "message type and is required"
        },
        content: {
            bsonType: "string",
            description: "message content and is required"
        },
        createdAt: {
            bsonType: "date",
            description: "creation timestamp and is required"
        }
    }
});

print("\n✅ Collection validator update completed!");
print("📊 Total collections processed: 27");
print("🎯 All schemas are now enforced with strict validation");
print("⚠️  Note: Existing documents that don't match the schema will need to be updated manually");
print("🔍 You can verify validators by running: db.runCommand({listCollections: 1})");