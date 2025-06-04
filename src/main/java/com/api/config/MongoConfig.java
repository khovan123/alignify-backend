package com.api.config;

import com.api.model.*;
import com.api.repository.*;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class MongoConfig {

    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @PostConstruct
    public void init() {
        MongoDatabase db = mongoClient.getDatabase(databaseName);
        this.create_usersCollection(db);
        this.create_influencersCollection(db);
        this.create_brandsCollection(db);
//        this.create_rolesCollection(db);
//        this.create_categoriesCollection(db);
        this.create_adminsCollection(db);
        this.create_galleriesCollection(db);
        this.create_galleryImagesCollection(db);
        this.create_otpsCollection(db);
        this.create_accountVerifiedsCollection(db);
        this.create_contentPostingsCollection(db);
        this.create_likesCollection(db);
        this.create_campaignsCollection(db);
    }

    public void create_usersCollection(MongoDatabase db) {
        if (db.getCollection("users") != null) {
            db.getCollection("users").drop();
        }

        Document jsonSchema = Document.parse("""
        {
              "bsonType": "object",
              "required": ["name", "email", "password", "location", "roleId"],
              "properties": {
              "name": {
                "bsonType": "string"
                },
                "email": {
                  "bsonType": "string",
                  "pattern": "^.+@.+\\\\..+$"
                },
                "password": {
                  "bsonType": "string",
                  "minLength": 6
                },
                "location": {
                  "bsonType": "string"
                },
                "roleId": {
                  "bsonType": "string"
                },
                "isActive": {
                  "bsonType": "bool"
                },
                "createdAt": {
                  "bsonType": "date"
                }                                            
              }
        }
        """);

        ValidationOptions validationOptions = new ValidationOptions()
                .validator(new Document("$jsonSchema", jsonSchema));

        CreateCollectionOptions options = new CreateCollectionOptions()
                .validationOptions(validationOptions);

        db.createCollection("users", options);
    }

    public void create_influencersCollection(MongoDatabase db) {
        if (db.getCollection("influencers") != null) {
            db.getCollection("influencers").drop();
        }
        Document jsonSchema = Document.parse("""
        {
          "bsonType": "object",
          "properties": {
            "_id": {
              "bsonType": "objectId"
            },
            "avatarUrl": {
              "bsonType": "string"
            },
            "DoB": {
              "bsonType": "date"
            },
            "gender": {
              "bsonType": "string",
              "enum" : ["MALE", "FEMALE", "LGBT", "NONE"]                               
            },
            "bio": {
              "bsonType": "string"
            },
            "socialMediaLinks": {
              "bsonType": "object",
              "additionalProperties": {
                "bsonType": "string"
              }
            },
            "rating": {
              "bsonType": "double"
            },
            "categoryIds": {
              "bsonType": "array",
              "items": {
                "bsonType": "string"
              }
            },
            "followerIds": {
              "bsonType": "array",
              "items": {
                "bsonType": "string"
              }
            },
            "isPublic": {
              "bsonType": "bool"
            },
            "createdAt": {
              "bsonType": "date"
            }   
          }
        }
        """);

        ValidationOptions validationOptions = new ValidationOptions()
                .validator(new Document("$jsonSchema", jsonSchema));

        CreateCollectionOptions options = new CreateCollectionOptions()
                .validationOptions(validationOptions);

        db.createCollection("influencers", options);
    }

    public void create_rolesCollection(MongoDatabase db) {
        if (db.getCollection("roles") != null) {
            db.getCollection("roles").drop();
        }
        Document jsonSchema = Document.parse("""
        {
              "bsonType": "object",
              "required": ["roleName"],
              "properties": {
              "roleName": {
                  "bsonType": "string"
                }               
              }
        }
        """);
        ValidationOptions validationOptions = new ValidationOptions()
                .validator(new Document("$jsonSchema", jsonSchema));

        CreateCollectionOptions options = new CreateCollectionOptions()
                .validationOptions(validationOptions);

        db.createCollection("roles", options);

        if (roleRepository.count() == 0) {
            Role adminRole = roleRepository.save(new Role("ADMIN"));
            Role brandRole = roleRepository.save(new Role("BRAND"));
            Role influencerRole = roleRepository.save(new Role("INFLUENCER"));
            EnvConfig.ADMIN_ROLE_ID = adminRole.getRoleId();
            EnvConfig.BRAND_ROLE_ID = brandRole.getRoleId();
            EnvConfig.INFLUENCER_ROLE_ID = influencerRole.getRoleId();
        }
    }

    public void create_categoriesCollection(MongoDatabase db) {
        if (db.getCollection("categories") != null) {
            db.getCollection("categories").drop();
        }
        Document jsonSchema = Document.parse("""
        {
              "bsonType": "object",
              "required": ["categoryName"],
              "properties": {
                "categoryName": {
                  "bsonType": "string"
                }               
              }
        }
        """);
        ValidationOptions validationOptions = new ValidationOptions()
                .validator(new Document("$jsonSchema", jsonSchema));

        CreateCollectionOptions options = new CreateCollectionOptions()
                .validationOptions(validationOptions);

        db.createCollection("categories", options);

        if (categoryRepository.count() == 0) {
            categoryRepository.saveAll(List.of(
                    new Category("thời trang"),
                    new Category("mỹ phẩm"),
                    new Category("công nghệ"),
                    new Category("nghệ thuật"),
                    new Category("thể thao"),
                    new Category("ăn uống"),
                    new Category("du lịch"),
                    new Category("lối sống"),
                    new Category("âm nhạc"),
                    new Category("trò chơi điện tử"),
                    new Category("handmade"),
                    new Category("phong tục và văn hóa"),
                    new Category("khởi nghiệp"),
                    new Category("kĩ năng mềm"),
                    new Category("mẹ và bé")
            ));
        }
    }

    public void create_brandsCollection(MongoDatabase db) {
        if (db.getCollection("brands") != null) {
            db.getCollection("brands").drop();
        }
        Document jsonSchema = Document.parse("""
        {
              "bsonType": "object",
              "properties": {
                "_id": {
                  "bsonType": "objectId",
                },
                "avatarUrl": {
                  "bsonType": "string",
                },
                "bio": {
                  "bsonType": "string",
                },
                "contacts": {
                  "bsonType": "object",
                  "additionalProperties": {
                    "bsonType": "string"
                  }
                },
                "socialMediaLinks": {
                  "bsonType": "object",
                  "additionalProperties": {
                    "bsonType": "string"
                  }
                },
                "establishDate": {
                  "bsonType": "date",
                },
                "createdAt": {
                  "bsonType": "date"
                }   
              }
        }
        """);
        ValidationOptions validationOptions = new ValidationOptions()
                .validator(new Document("$jsonSchema", jsonSchema));

        CreateCollectionOptions options = new CreateCollectionOptions()
                .validationOptions(validationOptions);

        db.createCollection("brands", options);
    }

    public void create_adminsCollection(MongoDatabase db) {
        if (db.getCollection("admins") != null) {
            db.getCollection("admins").drop();
        }

        Document jsonSchema = Document.parse("""
        {
              "bsonType": "object",
              "required": ["name", "email", "password", "roleId"],
              "properties": {
              "name": {
                "bsonType": "string"
                },
                "email": {
                  "bsonType": "string",
                  "pattern": "^.+@.+\\\\..+$"
                },
                "password": {
                  "bsonType": "string",
                  "minLength": 6
                },
                "roleId": {
                  "bsonType": "string"
                },
                "createdAt": {
                  "bsonType": "date"
                }                                            
              }
        }
        """);

        ValidationOptions validationOptions = new ValidationOptions()
                .validator(new Document("$jsonSchema", jsonSchema));

        CreateCollectionOptions options = new CreateCollectionOptions()
                .validationOptions(validationOptions);

        db.createCollection("admins", options);
    }

    public void create_galleriesCollection(MongoDatabase db) {
        if (db.getCollection("galleries") != null) {
            db.getCollection("galleries").drop();
        }
        Document jsonSchema = Document.parse("""
        {
              "bsonType": "object",
              "properties": {
                "_id": {
                  "bsonType": "objectId",
                },
                "images": {
                  "bsonType": "array",
                  "items": {
                    "bsonType": "string"
                  }
                },
                "createdAt": {
                  "bsonType": "date"
                }   
              }
        }
        """);
        ValidationOptions validationOptions = new ValidationOptions()
                .validator(new Document("$jsonSchema", jsonSchema));

        CreateCollectionOptions options = new CreateCollectionOptions()
                .validationOptions(validationOptions);

        db.createCollection("galleries", options);
    }

    public void create_galleryImagesCollection(MongoDatabase db) {
        if (db.getCollection("galleryImages") != null) {
            db.getCollection("galleryImages").drop();
        }
        Document jsonSchema = Document.parse("""
        {
              "bsonType": "object",
              "required": ["imageUrl"],                                                                                                                                      
              "properties": {
                "imageUrl": {
                  "bsonType": "string",
                },
                "createdAt": {
                  "bsonType": "date"
                }   
              }
        }
        """);
        ValidationOptions validationOptions = new ValidationOptions()
                .validator(new Document("$jsonSchema", jsonSchema));

        CreateCollectionOptions options = new CreateCollectionOptions()
                .validationOptions(validationOptions);

        db.createCollection("galleryImages", options);
    }

    public void create_otpsCollection(MongoDatabase db) {
        if (db.getCollection("otps") != null) {
            db.getCollection("otps").drop();
        }
        Document jsonSchema = Document.parse("""
        {
              "bsonType": "object",
              "required": ["otpCode","email"],                                                                                                                                      
              "properties": {
                "email": {
                  "bsonType": "string",
                },
                "otpCode": {
                  "bsonType": "string",
                  "pattern": "^[A-Z0-9]{6}$",          
                },            
                "requestCount" :{
                  "bsonType": "int",
                },  
                "attemptCount" :{
                  "bsonType": "int",
                },                                          
                "createdAt": {
                  "bsonType": "date"
                }   
              }
        }
        """);
        ValidationOptions validationOptions = new ValidationOptions()
                .validator(new Document("$jsonSchema", jsonSchema));

        CreateCollectionOptions options = new CreateCollectionOptions()
                .validationOptions(validationOptions);

        db.createCollection("otps", options);
    }

    @PostConstruct
    public void initIndexes() {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = database.getCollection("otps");

        boolean ttlIndexExists = false;
        for (Document index : collection.listIndexes()) {
            if ("createdAt_ttl".equals(index.getString("name"))) {
                ttlIndexExists = true;
                break;
            }
        }

        if (!ttlIndexExists) {
            Document indexKeys = new Document("createdAt", 1);
            IndexOptions indexOptions = new IndexOptions()
                    .name("createdAt_ttl")
                    .expireAfter(180L, java.util.concurrent.TimeUnit.SECONDS);
            collection.createIndex(indexKeys, indexOptions);
        }
    }

    public void create_accountVerifiedsCollection(MongoDatabase db) {
        if (db.getCollection("accountVerifieds") != null) {
            db.getCollection("accountVerifieds").drop();
        }
        Document jsonSchema = Document.parse("""
        {
              "bsonType": "object",
              "required": ["email"],                                                                                                                                      
              "properties": {      
                "email": {
                  "bsonType": "string"
                },
                "createdAt": {
                  "bsonType": "date"
                }   
              }
        }
        """);
        ValidationOptions validationOptions = new ValidationOptions()
                .validator(new Document("$jsonSchema", jsonSchema));

        CreateCollectionOptions options = new CreateCollectionOptions()
                .validationOptions(validationOptions);

        db.createCollection("accountVerifieds", options);
    }

    public void create_contentPostingsCollection(MongoDatabase db) {
        if (db.getCollection("contentPostings") != null) {
            db.getCollection("contentPostings").drop();
        }

        Document jsonSchema = Document.parse("""
    {
        "bsonType": "object",
        "required": ["userId", "content"],
        "properties": {
            "contentId": {
                "bsonType": "string"
            },
            "userId": {
                "bsonType": "string"
            },
            "content": {
                "bsonType": "string"
            },
            "imageUrl": {
                "bsonType": "string"
            },
            "categoryIds": {
                "bsonType": "array",
                "items": {
                    "bsonType": "string"
                }
            },
            "timestamp": {
                "bsonType": "date"
            },
            "isPublic": {
                "bsonType": "bool"
            },
            "commentIds": {
                "bsonType": "array",
                "items": {
                    "bsonType": "string"
                }
            },
            "like": {
                "bsonType": "int"
            }
                                                     }
        }
    
    """);

        ValidationOptions validationOptions = new ValidationOptions()
                .validator(new Document("$jsonSchema", jsonSchema));

        CreateCollectionOptions options = new CreateCollectionOptions()
                .validationOptions(validationOptions);

        db.createCollection("contentPostings", options);
    }

    public void create_likesCollection(MongoDatabase db) {
        if (db.getCollection("likes") != null) {
            db.getCollection("likes").drop();
        }

        Document jsonSchema = Document.parse("""
    {
        "bsonType": "object",
        "required": ["userId", "contentId", "createdAt"],
        "properties": {
            "userId": {
                "bsonType": "string"
            },
            "contentId": {
                "bsonType": "string"
            },
            "createdAt": {
                "bsonType": "date"
            }
        }
    }
    """);

        ValidationOptions validationOptions = new ValidationOptions()
                .validator(new Document("$jsonSchema", jsonSchema));

        CreateCollectionOptions options = new CreateCollectionOptions()
                .validationOptions(validationOptions);

        db.createCollection("likes", options);

//        MongoCollection<Document> likesCollection = db.getCollection("likes");
//        likesCollection.createIndex(Indexes.ascending("contentId"));
//        likesCollection.createIndex(Indexes.ascending("userId"));
//        likesCollection.createIndex(Indexes.compoundIndex(Indexes.ascending("contentId"), Indexes.ascending("userId")));
    }
    
    public void create_campaignsCollection(MongoDatabase db) {
        if (db.getCollection("campaigns") != null) {
            db.getCollection("campaigns").drop();
        }

        Document jsonSchema = Document.parse("""
    {
        "bsonType": "object",
        "required": ["userId", "content"],
        "properties": {
            "campaignId": {
                "bsonType": "string"
            },
            "userId": {
                "bsonType": "string"
            },
            "content": {
                "bsonType": "string"
            },
            "imageUrl": {
                "bsonType": "string"
            },
            "categoryIds": {
                "bsonType": "array",
                "items": {
                    "bsonType": "string"
                }
            },
            "timestamp": {
                "bsonType": "date"
            },
            "isPublic": {
                "bsonType": "bool"
            }
            
            }
        }
    
    """);

        ValidationOptions validationOptions = new ValidationOptions()
                .validator(new Document("$jsonSchema", jsonSchema));

        CreateCollectionOptions options = new CreateCollectionOptions()
                .validationOptions(validationOptions);

        db.createCollection("campaigns", options);
    }
}
