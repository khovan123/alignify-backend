# MongoDB Collection Examples

This file shows examples of the generated collections and their data structure after running the database generation scripts.

## Sample Collection Structure

### Roles Collection
```javascript
// Query: db.roles.find().pretty()
{
  "_id": ObjectId("68485dcedda6867ca0d23e89"),
  "roleName": "ADMIN"
}
{
  "_id": ObjectId("68485dcedda6867ca0d23e8a"),
  "roleName": "BRAND"
}
{
  "_id": ObjectId("68485dcedda6867ca0d23e8b"),
  "roleName": "INFLUENCER"
}
```

### Categories Collection
```javascript
// Query: db.categories.find().limit(5).pretty()
{
  "_id": ObjectId("..."),
  "categoryName": "thời trang"
}
{
  "_id": ObjectId("..."),
  "categoryName": "mỹ phẩm"
}
{
  "_id": ObjectId("..."),
  "categoryName": "công nghệ"
}
// ... 12 more categories
```

### Users Collection (Schema Example)
```javascript
// Example user document structure
{
  "_id": ObjectId("..."),
  "name": "John Doe",
  "email": "john@example.com",
  "password": "hashed_password",
  "roleId": "68485dcedda6867ca0d23e8b",
  "avatarUrl": "https://example.com/avatar.jpg",
  "isActive": true,
  "createdAt": ISODate("2025-01-15T10:30:00Z"),
  "permissionIds": ["..."],
  "userPlanId": "..."
}
```

### Campaigns Collection (Schema Example)
```javascript
// Example campaign document structure
{
  "_id": ObjectId("..."),
  "brandId": "...",
  "campaignName": "Summer Fashion Campaign",
  "content": "Promote our summer collection...",
  "imageUrl": "https://example.com/campaign.jpg",
  "categoryIds": ["category_id_1", "category_id_2"],
  "status": "RECRUITING",
  "budget": 50000,
  "startAt": ISODate("2025-02-01T00:00:00Z"),
  "dueAt": ISODate("2025-03-31T23:59:59Z"),
  "campaignRequirements": [
    {
      "platform": "INSTAGRAM",
      "post_type": "POST",
      "quantity": 3,
      "details": [
        {
          "post_type": "STORY",
          "like": 1000,
          "comment": 50,
          "share": 25,
          "view": 10000
        }
      ]
    }
  ],
  "influencerRequirements": [
    {
      "platform": "INSTAGRAM",
      "followers": 10000
    }
  ],
  "influencerCountExpected": 5,
  "joinedInfluencerIds": [],
  "applicationTotal": 0,
  "appliedInfluencerIds": [],
  "createdAt": ISODate("2025-01-15T10:30:00Z")
}
```

## Collection List and Counts

After generation, you should see these collections:

```javascript
// Query: db.getCollectionNames()
[
  "accountVerifieds",
  "admins", 
  "applications",
  "assistantMessages",
  "campaignTrackings",
  "campaigns",
  "categories",        // 15 documents
  "chatRooms",
  "comments",
  "contentPostings",
  "galleries",
  "galleryImages",
  "influencers",
  "invitations",
  "likes",
  "messages",
  "notifications",
  "otps",
  "permissions",       // 3 documents
  "planPermissions",
  "plans",
  "reasons",
  "roles",            // 3 documents
  "userBans",
  "userPlans",
  "users"
]
```

## Indexes Created

### Users Collection Indexes
```javascript
// Query: db.users.getIndexes()
[
  { "v": 2, "key": { "_id": 1 }, "name": "_id_" },
  { "v": 2, "key": { "email": 1 }, "name": "email_1", "unique": true },
  { "v": 2, "key": { "roleId": 1 }, "name": "roleId_1" }
]
```

### Campaigns Collection Indexes
```javascript
// Query: db.campaigns.getIndexes()
[
  { "v": 2, "key": { "_id": 1 }, "name": "_id_" },
  { "v": 2, "key": { "brandId": 1 }, "name": "brandId_1" },
  { "v": 2, "key": { "status": 1 }, "name": "status_1" },
  { "v": 2, "key": { "categoryIds": 1 }, "name": "categoryIds_1" }
]
```

### TTL Index Example (OTPs)
```javascript
// Query: db.otps.getIndexes()
[
  { "v": 2, "key": { "_id": 1 }, "name": "_id_" },
  { "v": 2, "key": { "email": 1 }, "name": "email_1" },
  { 
    "v": 2, 
    "key": { "createdAt": 1 }, 
    "name": "createdAt_1",
    "expireAfterSeconds": 300  // 5 minutes TTL
  }
]
```

## Validation Schema Examples

### Users Collection Validation
```javascript
// Query: db.runCommand({collMod: "users", validator: {}})
{
  "$jsonSchema": {
    "bsonType": "object",
    "required": ["name", "email", "password", "roleId"],
    "properties": {
      "name": { "bsonType": "string" },
      "email": { 
        "bsonType": "string",
        "pattern": "^.+@.+\\..+$"
      },
      "password": { "bsonType": "string" },
      "roleId": { "bsonType": "string" },
      "isActive": { "bsonType": "bool" },
      "createdAt": { "bsonType": "date" }
    }
  }
}
```

### Campaigns Collection Validation
```javascript
{
  "$jsonSchema": {
    "bsonType": "object",
    "required": ["brandId", "campaignName", "content", "budget", "status"],
    "properties": {
      "status": {
        "bsonType": "string",
        "enum": ["DRAFT", "RECRUITING", "PENDING", "PARTICIPATING", "COMPLETED"]
      },
      "budget": { "bsonType": "int" },
      "startAt": { "bsonType": "date" },
      "dueAt": { "bsonType": "date" }
    }
  }
}
```

## Verification Queries

Use these queries to verify your database was created correctly:

```javascript
// Check all collections exist
db.getCollectionNames().length  // Should be 27

// Check default data
db.roles.countDocuments()       // Should be 3
db.categories.countDocuments()  // Should be 15
db.permissions.countDocuments() // Should be 3

// Check specific role IDs match EnvConfig
db.roles.findOne({roleName: "ADMIN"})._id.toString()     // 68485dcedda6867ca0d23e89
db.roles.findOne({roleName: "BRAND"})._id.toString()     // 68485dcedda6867ca0d23e8a
db.roles.findOne({roleName: "INFLUENCER"})._id.toString() // 68485dcedda6867ca0d23e8b

// Check indexes were created
db.users.getIndexes().length     // Should be 3
db.campaigns.getIndexes().length // Should be 4

// Test validation (should fail)
db.users.insertOne({name: "test"}) // Missing required fields

// Test validation (should succeed)
db.users.insertOne({
  name: "Test User",
  email: "test@example.com", 
  password: "password123",
  roleId: "68485dcedda6867ca0d23e8b"
})
```

## Database Size

After generation with default data only:
- Total collections: 27
- Total documents: ~21 (3 roles + 15 categories + 3 permissions)
- Database size: ~few KB (mostly empty collections with schemas)

The database is ready to receive your application data!