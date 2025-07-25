#!/bin/bash
# ==============================================================================
# MongoDB Initialization Script for Alignify Backend
# ==============================================================================

set -e

echo "Creating Alignify database and collections..."

# Switch to alignify database
mongosh <<EOF
use alignify

// Create collections with validation schemas
db.createCollection("users", {
   validator: {
      \$jsonSchema: {
         bsonType: "object",
         required: ["email", "password", "name", "roleId"],
         properties: {
            email: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            password: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            name: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            roleId: {
               bsonType: "string",
               description: "must be a string and is required"
            }
         }
      }
   }
})

db.createCollection("campaigns")
db.createCollection("applications")
db.createCollection("invitations")
db.createCollection("contents")
db.createCollection("chat_rooms")
db.createCollection("chat_messages")
db.createCollection("notifications")
db.createCollection("roles")
db.createCollection("brands")
db.createCollection("influencers")
db.createCollection("reports")
db.createCollection("payment_tracking")

// Create indexes for better performance
db.users.createIndex({ "email": 1 }, { unique: true })
db.users.createIndex({ "roleId": 1 })
db.campaigns.createIndex({ "brandId": 1 })
db.campaigns.createIndex({ "status": 1 })
db.applications.createIndex({ "campaignId": 1 })
db.applications.createIndex({ "influencerId": 1 })
db.invitations.createIndex({ "campaignId": 1 })
db.invitations.createIndex({ "influencerId": 1 })
db.chat_messages.createIndex({ "chatRoomId": 1 })
db.chat_messages.createIndex({ "createdAt": 1 })
db.notifications.createIndex({ "userId": 1 })
db.notifications.createIndex({ "isRead": 1 })

// Insert default roles
db.roles.insertMany([
   {
      "_id": "ADMIN",
      "name": "Admin",
      "description": "System administrator with full access",
      "permissions": ["ALL"]
   },
   {
      "_id": "BRAND",
      "name": "Brand",
      "description": "Brand user who can create campaigns",
      "permissions": ["CREATE_CAMPAIGN", "MANAGE_CAMPAIGN", "INVITE_INFLUENCER", "VIEW_APPLICATIONS"]
   },
   {
      "_id": "INFLUENCER",
      "name": "Influencer",
      "description": "Influencer user who can apply to campaigns",
      "permissions": ["APPLY_CAMPAIGN", "CREATE_CONTENT", "VIEW_INVITATIONS", "UPDATE_PROGRESS"]
   }
])

echo "Database initialization completed successfully!"
EOF