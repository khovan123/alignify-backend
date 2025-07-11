package com.api.config;

import java.util.Arrays;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.ValidationOptions;

import jakarta.annotation.PostConstruct;

@Configuration
public class MongoConfig {

  @Autowired
  private MongoClient mongoClient;

  // @Autowired
  // private RoleRepository roleRepository;

  // @Autowired
  // private CategoryRepository categoryRepository;

  @Value("${spring.data.mongodb.database}")
  private String databaseName;

  @Bean
  public MongoCustomConversions mongoCustomConversions() {
    return new MongoCustomConversions(Arrays.asList(
        new ZonedDateTimeToDateConverter(),
        new DateToZonedDateTimeConverter()));
  }

  @PostConstruct
  public void init() {
    // MongoDatabase db = mongoClient.getDatabase(databaseName);
    // this.create_usersCollection(db);
    // this.create_influencersCollection(db);
    // this.create_brandsCollection(db);
    // this.create_rolesCollection(db);
    // this.create_categoriesCollection(db);
    // this.create_adminsCollection(db);
    // this.create_galleriesCollection(db);
    // this.create_galleryImagesCollection(db);
    // this.create_otpsCollection(db);
    // this.create_accountVerifiedsCollection(db);
    // this.create_campaignsCollection(db);
    // this.create_contentPostingsCollection(db);
    // this.create_likesCollection(db);
    // this.create_applicationsCollection(db);
    // this.create_campaignTrackingsCollection(db);
    // this.create_commentsCollection(db);
    // this.create_chatRoomsCollection(db);
    // this.create_messagesCollection(db);
    // this.create_notificationsCollection(db);
    // this.create_reasonsCollection(db);
    // this.create_userBansCollection(db);
    // this.create_permissionsCollection(db);
    // this.create_planPermissionsCollection(db);
    // this.create_userPlansCollection(db);
    // this.create_plansCollection(db);
    // this.create_invitationsCollection(db);
  }

  public void create_usersCollection(MongoDatabase db) {
    if (db.getCollection("users") != null) {
      db.getCollection("users").drop();
    }

    Document jsonSchema = Document.parse(
        """
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
                    "avatarUrl": {
                      "bsonType": "string",
                      "pattern": "^https?://.+$"
                        },
                    "backgroundUrl": {
                      "bsonType": "string"
                        },
                    "password": {
                      "bsonType": "string",
                    },
                    "roleId": {
                      "bsonType": "string"
                    },
                    "isActive": {
                      "bsonType": "bool"
                    },
                    "createdAt": {
                      "bsonType": "date"
                    },
                    "permissionIds": {
                      "bsonType": "array",
                      "items":{
                        "bsonType": "string"
                      }
                    },
                    "userPlanId": {
                      "bsonType": "string"
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

  public void create_permissionsCollection(MongoDatabase db) {
    if (db.getCollection("permissions") != null) {
      db.getCollection("permissions").drop();
    }
    Document jsonSchema = Document.parse(
        """
            {
                  "bsonType": "object",
                  "required": ["permissionName","permissionDescription"],
                  "properties": {
                    "permissionName": {
                      "bsonType": "string",
                      "enum": ["posting", "comment", "all"]
                    },
                    "permissionDescription": {
                      "bsonType": "string"
                    }
                  }
            }
            """);
    ValidationOptions validationOptions = new ValidationOptions()
        .validator(new Document("$jsonSchema", jsonSchema));

    CreateCollectionOptions options = new CreateCollectionOptions()
        .validationOptions(validationOptions);

    db.createCollection("permissions", options);
  }

  public void create_planPermissionsCollection(MongoDatabase db) {
    if (db.getCollection("planPermissions") != null) {
      db.getCollection("planPermissions").drop();
    }
    Document jsonSchema = Document.parse(
        """
            {
                  "bsonType": "object",
                  "required": ["planPermissionName","roleId","limited"],
                  "properties": {
                    "planPermissionName": {
                      "bsonType": "string",
                      "enum": ["search_result","campaign_members","campaign_invitation","campaign_apply"]
                    },
                    "limited": {
                      "bsonType": "number"
                    }
                    "roleId": {
                      "bsonType": "string"
                    }
                  }
            }
            """);
    ValidationOptions validationOptions = new ValidationOptions()
        .validator(new Document("$jsonSchema", jsonSchema));

    CreateCollectionOptions options = new CreateCollectionOptions()
        .validationOptions(validationOptions);

    db.createCollection("planPermissions", options);
  }

  public void create_plansCollection(MongoDatabase db) {
    if (db.getCollection("plans") != null) {
      db.getCollection("plans").drop();
    }
    Document jsonSchema = Document.parse(
        """
            {
                  "bsonType": "object",
                  "required": ["planName","description","roleId","planPermissionIds","price","planType","planCount"],
                  "properties": {
                    "planName": {
                      "bsonType": "string",
                    },
                    "description": {
                      "bsonType": "string",
                    },
                    "roleId": {
                      "bsonType": "string",
                    },
                    "planPermissionIds": {
                      "bsonType": "array",
                      "items": {
                        "bsonType": "string"
                      }
                    },
                    "permissionIds": {
                      "bsonType": "array",
                      "items": {
                        "bsonType": "string"
                      }
                    },
                    "price": {
                      "bsonType": "number"
                    }
                    "discount": {
                      "bsonType": "number"
                    },
                    "planType": {
                      "bsonType": "string",
                      "enum": ["one_month","monthly","one_year"]
                    },
                    "planCount": {
                      "bsonType": "number"
                    },
                  }
            }
            """);
    ValidationOptions validationOptions = new ValidationOptions()
        .validator(new Document("$jsonSchema", jsonSchema));

    CreateCollectionOptions options = new CreateCollectionOptions()
        .validationOptions(validationOptions);

    db.createCollection("plans", options);
  }

  public void create_userPlansCollection(MongoDatabase db) {
    if (db.getCollection("userPlans") != null) {
      db.getCollection("userPlans").drop();
    }
    Document jsonSchema = Document.parse(
        """
            {
                  "bsonType": "object",
                  "required": ["userId","planId", "createdAt", "autoPaid"],
                  "properties": {
                    "userId": {
                      "bsonType": "string",
                    },
                    "planId": {
                      "bsonType": "string"
                    },
                    "createdAt": {
                      "bsonType": "date"
                    },
                    "autoPaid": {
                      "bsonType": "bool"
                    }
                  }
            }
            """);
    ValidationOptions validationOptions = new ValidationOptions()
        .validator(new Document("$jsonSchema", jsonSchema));

    CreateCollectionOptions options = new CreateCollectionOptions()
        .validationOptions(validationOptions);

    db.createCollection("userPlans", options);
  }

  public void create_influencersCollection(MongoDatabase db) {
    if (db.getCollection("influencers") != null) {
      db.getCollection("influencers").drop();
    }
    Document jsonSchema = Document.parse(
        """
            {
              "bsonType": "object",
              "properties": {
                "_id": {
                  "bsonType": "objectId"
                },
                "DoB": {
                  "bsonType": "date"
                },
                "gender": {
                  "bsonType": "string",
                  "enum" : ["MALE", "FEMALE", "OTHER", "LGBT", "NONE"]
                },
                "bio": {
                  "bsonType": "string"
                },
                "socialMediaLinks": {
                  "bsonType": "array",
                  "items": {
                    "bsonType": "object",
                    "required": ["platform","url", "follower"],
                    "properties": {
                      "platform":{
                        "bsonType": "string",
                        "enum":["FACEBOOK", "TIKTOK", "YOUTUBE", "INSTAGRAM"]
                        },
                      "url": {
                        "bsonType": "string"
                        },
                      "follower": {
                        "bsonType": "int"
                      }
                    }
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
                "follower": {
                  "bsonType": "int",
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
    Document jsonSchema = Document.parse(
        """
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

    // if (roleRepository.count() == 0) {
    // Role adminRole = roleRepository.save(new Role("ADMIN"));
    // Role brandRole = roleRepository.save(new Role("BRAND"));
    // Role influencerRole = roleRepository.save(new Role("INFLUENCER"));
    // EnvConfig.ADMIN_ROLE_ID = adminRole.getRoleId();
    // EnvConfig.BRAND_ROLE_ID = brandRole.getRoleId();
    // EnvConfig.INFLUENCER_ROLE_ID = influencerRole.getRoleId();
    // }
  }

  public void create_categoriesCollection(MongoDatabase db) {
    if (db.getCollection("categories") != null) {
      db.getCollection("categories").drop();
    }
    Document jsonSchema = Document.parse(
        """
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

    // if (categoryRepository.count() == 0) {
    // categoryRepository.saveAll(
    // List.of(
    // new Category("thời trang"),
    // new Category("mỹ phẩm"),
    // new Category("công nghệ"),
    // new Category("nghệ thuật"),
    // new Category("thể thao"),
    // new Category("ăn uống"),
    // new Category("du lịch"),
    // new Category("lối sống"),
    // new Category("âm nhạc"),
    // new Category("trò chơi điện tử"),
    // new Category("handmade"),
    // new Category("phong tục và văn hóa"),
    // new Category("khởi nghiệp"),
    // new Category("kĩ năng mềm"),
    // new Category("mẹ và bé")));
    // }
  }

  public void create_brandsCollection(MongoDatabase db) {
    if (db.getCollection("brands") != null) {
      db.getCollection("brands").drop();
    }
    Document jsonSchema = Document.parse(
        """
            {
                  "bsonType": "object",
                  "properties": {
                    "_id": {
                      "bsonType": "objectId",
                    },
                    "bio": {
                      "bsonType": "string",
                    },
                   "contacts": {
                            bsonType: 'array',
                            items: {
                              bsonType: 'object',
                              properties: {
                                contact_type: {
                                  bsonType: 'string'
                                },
                                contact_infor: {
                                  bsonType: 'string'
                                }
                              }
                            }
                          },
                    "socialMediaLinks": {
                      "bsonType": "array",
                      "items": {
                        "bsonType": "object",
                        "required": ["platform","url", "follower"],
                        "properties": {
                          "platform":{
                            "bsonType": "string"
                            "enum":["FACEBOOK", "TIKTOK", "YOUTUBE", "INSTAGRAM"]
                          },
                          "url": {
                            "bsonType": "string"
                            },
                          "follower": {
                            "bsonType": "int"
                          }
                        }
                      }
                    },
                    "categoryIds": {
                      "bsonType": "array",
                      "items": {
                        "bsonType": "string"
                       }
                    },
                    "establishDate": {
                      "bsonType": "date",
                    },
                    "createdAt": {
                      "bsonType": "date"
                    },
                    "totalCampaign": {
                      "bsonType": "int",
                    },
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

    Document jsonSchema = Document.parse(
        """
            {
                  "bsonType": "object",
                  "properties": {
                    "_id": {
                      "bsonType": "objectId",
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
    Document jsonSchema = Document.parse(
        """
            {
                  "bsonType": "object",
                  "required": ["images"],
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
    Document jsonSchema = Document.parse(
        """
            {
                  "bsonType": "object",
                  "required": ["imageUrl"],
                  "properties": {
                    "imageUrl": {
                      "bsonType": "string",
                      "pattern": "^https?://.+$"
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
    Document jsonSchema = Document.parse(
        """
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
    Document jsonSchema = Document.parse(
        """
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

    Document jsonSchema = Document.parse(
        """
            {
                "bsonType": "object",
                "required": ["contentName","content", "userId", "imageUrl"],
                "properties": {
                    "userId": {
                        "bsonType": "string"
                    },
                    "contentName": {
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
                    "createdDate": {
                        "bsonType": "date"
                    },
                    "isPublic": {
                        "bsonType": "bool"
                    },
                    "commentCount": {
                        "bsonType": "int"
                    },
                    "likeCount": {
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

    Document jsonSchema = Document.parse(
        """
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
  }

  public void create_commentsCollection(MongoDatabase db) {
    if (db.getCollection("comments") != null) {
      db.getCollection("comments").drop();
    }

    Document jsonSchema = Document.parse(
        """
            {
                "bsonType": "object",
                "required": ["userId","contentId", "content"],
                "properties": {
                     "userId": {
                        "bsonType": "string"
                    },
                    "contentId": {
                        "bsonType": "string"
                    },
                    "content": {
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

    db.createCollection("comments", options);
  }

  public void create_campaignsCollection(MongoDatabase db) {
    if (db.getCollection("campaigns") != null) {
      db.getCollection("campaigns").drop();
    }

    Document jsonSchema = Document.parse(
        """
            {
                "bsonType": "object",
                "required": ["brandId", "campaignName", "content", "budget", "imageUrl", "status", "campaignRequirements", "influencerRequirements", "startAt" , "dueAt", "influencerCountExpected", "joinedInfluencerIds", "applicationTotal", "appliedInfluencerIds"],
                "properties": {
                    "brandId": {
                        "bsonType": "string"
                    },
                    "campaignName": {
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
                    "status": {
                        "bsonType": "string",
                        "enum": ["DRAFT", "RECRUITING", "PENDING", "PARTICIPATING", "COMPLETED"]
                    },
                    "createdAt": {
                        "bsonType": "date"
                    },
                    "startAt":{
                      "bsonType": "date"
                    },
                    "dueAt": {
                        "bsonType": "date"
                    },
                    "budget": {
                        "bsonType": "int"
                    },
                   "campaignRequirements": {
                      "bsonType": "array",
                      "items": {
                        "bsonType": "object",
                        "required": ["platform", "post_type", "quantity", "details"],
                        "properties": {
                          "platform": { "bsonType": "string" },
                          "post_type": { "bsonType": "string" },
                          "quantity": { "bsonType": "int" },
                          "details": {
                            "bsonType": "array",
                            "items": {
                              "bsonType": "object",
                              "properties": {
                                "post_type": { "bsonType": "string" },
                                "like": { "bsonType": "int" },
                                "comment": { "bsonType": "int" },
                                "share": { "bsonType": "int" }
                              }
                            }
                          }
                        }
                      }
                    },
                    "influencerRequirements": {
                        "bsonType": "array",
                        "items": {
                            "bsonType": "object",
                            "required": ["platform", "followers"],
                            "properties": {
                                "platform": { "bsonType": "string" },
                                "followers": { "bsonType": "int" }
                            }
                        }
                    },
                    "influencerCountExpected": {
                        "bsonType": "int"
                    },
                    "joinedInfluencerIds": {
                        "bsonType": "array",
                        "items": {
                          "bsonType": "string"
                        }
                    },
                    "applicationTotal": {
                        "bsonType": "int"
                    },
                    "appliedInfluencerIds": {
                        "bsonType": "array",
                        "items": {
                            "bsonType": "string"
                        }
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

  public void create_applicationsCollection(MongoDatabase db) {
    if (db.getCollection("applications") != null) {
      db.getCollection("applications").drop();
    }
    Document jsonSchema = Document.parse(
        """
            {
                  "bsonType": "object",
                  "required": ["campaignId", "influencerId", "brandId", "limited", "status"],
                  "properties": {
                    "campaignId": {
                      "bsonType": "string"
                    },
                    "influencerId":{
                       "bsonType": "string"
                    },
                    "brandId":{
                       "bsonType": "string"
                    },
                    "limited": {
                      "bsonType": "int"
                    },
                    "status": {
                      "bsonType": "string",
                      "enum": ["PENDING","ACCEPTED","REJECTED"]
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

    db.createCollection("applications", options);
  }

  public void create_invitationsCollection(MongoDatabase db) {
    if (db.getCollection("invitations") != null) {
      db.getCollection("invitations").drop();
    }
    Document jsonSchema = Document.parse(
        """
            {
                  "bsonType": "object",
                  "required": ["brandId", "influencerId", "campaignId", "message", "status"],
                  "properties": {
                    "campaignId": {
                      "bsonType": "string"
                    },
                    "influencerId":{
                       "bsonType": "string"
                    },
                    "brandId":{
                       "bsonType": "string"
                    },
                    "message": {
                      "bsonType": "string"
                    },
                    "status": {
                      "bsonType": "string",
                      "enum": ["PENDING","ACCEPTED","REJECTED"]
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

    db.createCollection("invitations", options);
  }

  public void create_campaignTrackingsCollection(MongoDatabase db) {
    if (db.getCollection("campaignTrackings") != null) {
      db.getCollection("campaignTrackings").drop();
    }

    Document jsonSchema = Document.parse(
        """
            {
                "bsonType": "object",
                "required": ["campaignId", "brandId", "influencerId", "platformRequirementTracking", "process", "status", "createdAt"],
                "properties": {
                    "_id": {
                        "bsonType": "objectId"
                    },
                    "campaignId": {
                        "bsonType": "string"
                    },
                    "brandId": {
                        "bsonType": "string"
                    },
                    "influencerId": {
                        "bsonType": "string"
                    },
                    "platformRequirementTracking": {
                        "bsonType": "array",
                        "items": {
                            "bsonType": "object",
                            "required": ["platform", "post_type", "quantity", "details"],
                            "properties": {
                                "platform": {
                                    "bsonType": "string"
                                },
                                "post_type": {
                                    "bsonType": "string"
                                },
                                "quantity": {
                                    "bsonType": "int"
                                },
                                "details": {
                                    "bsonType": "array",
                                    "items": {
                                        "bsonType": "object",
                                        "required": ["post_type", "like", "comment", "share"],
                                        "properties": {
                                            "post_type": {
                                                "bsonType": "string"
                                            },
                                            "like": {
                                                "bsonType": "int"
                                            },
                                            "comment": {
                                                "bsonType": "int"
                                            },
                                            "share": {
                                                "bsonType": "int"
                                            },
                                            "postUrl": {
                                                "bsonType": ["string", "null"],
                                                "pattern": "^https?://.+$"
                                            },
                                            "status": {
                                                "bsonType": ["string", "null"],
                                                "enum": [null, "PENDING", "ACCEPTED", "REJECTED"]
                                            },
                                            "uploadedAt": {
                                                "bsonType": ["date", "null"]
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    "process": {
                        "bsonType": "double",
                        "minimum": 0,
                        "maximum": 100
                    },
                    "status": {
                        "bsonType": "string",
                        "enum": ["PENDING", "COMPLETED"]
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

    db.createCollection("campaignTrackings", options);
  }

  public void create_messagesCollection(MongoDatabase db) {
    if (db.getCollection("messages") != null) {
      db.getCollection("messages").drop();
    }
    Document jsonSchema = Document.parse(
        """
            {
                  "bsonType": "object",
                  "required": ["message", "chatRoomId", "userId", "name", "readBy"],
                  "properties": {
                    "userId": {
                      "bsonType": "string"
                    },
                    "chatRoomId":{
                       "bsonType": "string"
                    },
                    "name":{
                      "bsonType": "string"
                    },
                    "message":{
                       "bsonType": "string"
                    },
                    "tempId": {
                      "bsonType": "string"
                    },
                    "sendAt": {
                      "bsonType": "date"
                    },
                    "readBy": {
                      "bsonType": "array",
                      "items": {
                        "bsonType": "string"
                      }
                    }
                  }
            }
            """);
    ValidationOptions validationOptions = new ValidationOptions()
        .validator(new Document("$jsonSchema", jsonSchema));

    CreateCollectionOptions options = new CreateCollectionOptions()
        .validationOptions(validationOptions);

    db.createCollection("messages", options);
  }

  public void create_chatRoomsCollection(MongoDatabase db) {
    if (db.getCollection("chatRooms") != null) {
      db.getCollection("chatRooms").drop();
    }
    Document jsonSchema = Document.parse(
        """
            {
                  "bsonType": "object",
                  "required": ["roomOwnerId", "roomAvatarUrl", "roomName", "members"],
                  "properties": {
                    "_id": {
                      "bsonType": "objectId"
                    },
                    "roomName": {
                      "bsonType": "string"
                    },
                    "roomAvatarUrl":{
                      "bsonType": "string"
                   },
                    "members": {
                      "bsonType": "array",
                      "items": {
                          "bsonType": "string"
                      }
                    },
                    "roomOwnerId":{
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

    db.createCollection("chatRooms", options);
  }

  public void create_notificationsCollection(MongoDatabase db) {
    if (db.getCollection("notifications") != null) {
      db.getCollection("notifications").drop();
    }

    Document jsonSchema = Document.parse(
        """
            {
                "bsonType": "object",
                "required": ["userId", "name", "content", "avatarUrl"],
                "properties": {
                    "userId": {
                        "bsonType": "string"
                    },
                    "name": {
                        "bsonType": "string"
                    },
                    "avatarUrl":{
                      "bsonType": "string"
                    },
                    "content": {
                        "bsonType": "string"
                    },
                    "createdAt": {
                        "bsonType": "date"
                    },
                    "isRead": {
                        "bsonType": "bool"
                    }
                }
            }
            """);

    ValidationOptions validationOptions = new ValidationOptions()
        .validator(new Document("$jsonSchema", jsonSchema));

    CreateCollectionOptions options = new CreateCollectionOptions()
        .validationOptions(validationOptions);

    db.createCollection("notifications", options);
  }

  public void create_userBansCollection(MongoDatabase db) {
    if (db.getCollection("userBans") != null) {
      db.getCollection("userBans").drop();
    }

    Document jsonSchema = Document.parse(
        """
            {
                "bsonType": "object",
                "required": ["reasonId"],
                "properties": {
                    "roleId": {
                      "bsonType": "string"
                    },
                    "reasonId": {
                        "bsonType": "string"
                    },
                    "createdAt": {
                        "bsonType": "date"
                    },
                }
            }
            """);

    ValidationOptions validationOptions = new ValidationOptions()
        .validator(new Document("$jsonSchema", jsonSchema));

    CreateCollectionOptions options = new CreateCollectionOptions()
        .validationOptions(validationOptions);

    db.createCollection("userBans", options);
  }

  public void create_reasonsCollection(MongoDatabase db) {
    if (db.getCollection("reasons") != null) {
      db.getCollection("reasons").drop();
    }

    Document jsonSchema = Document.parse(
        """
            {
                "bsonType": "object",
                "required": ["title", "description"],
                "properties": {
                    "title": {
                        "bsonType": "string"
                    },
                    "description": {
                        "bsonType": "string"
                    },
                }
            }
            """);

    ValidationOptions validationOptions = new ValidationOptions()
        .validator(new Document("$jsonSchema", jsonSchema));

    CreateCollectionOptions options = new CreateCollectionOptions()
        .validationOptions(validationOptions);

    db.createCollection("reasons", options);
  }

  // public void create_campaignTrackingsCollection(MongoDatabase db) {
  // if (db.getCollection("campaignTrackings") != null) {
  // db.getCollection("campaignTrackings").drop();
  // }
  //
  // Document jsonSchema = Document.parse(
  // """
  // {
  // "bsonType": "object",
  // "required": ["campaignId", "brandId", "influencerId",
  // "campaignRequirementTracking"],
  // "properties": {
  // "_id": {
  // "bsonType": "objectId"
  // }
  // "campaignId": {
  // "bsonType": "string"
  // },
  // "brandId": {
  // "bsonType": "string"
  // },
  // "influencerId": {
  // "bsonType": "string"
  // },
  // "campaignRequirementTracking": {
  // "bsonType": "object",
  // "additionalProperties": {
  // "bsonType": "array",
  // "items": {
  // "bsonType": "object",
  // "properties": {
  // "index": {
  // "bsonType": "int"
  // },
  // "imageUrl": {
  // "bsonType": "string",
  // "pattern": "^https?://.+$"
  // },
  // "postUrl": {
  // "bsonType": "string",
  // "pattern": "^https?://.+$"
  // },
  // "status": {
  // "bsonType": "string",
  // "enum": ["PENDING", "ACCEPTED", "REJECTED"]
  // },
  // "uploadedAt": {
  // "bsonType": "date"
  // }
  // }
  // }
  // }
  // },
  // "process": {
  // "bsonType": "double",
  // "minimum": 0,
  // "maximum": 100
  // },
  // "createdAt": {
  // "bsonType": "date"
  // }
  // }
  // }
  // """);
  //
  // ValidationOptions validationOptions = new ValidationOptions()
  // .validator(new Document("$jsonSchema", jsonSchema));
  //
  // CreateCollectionOptions options = new CreateCollectionOptions()
  // .validationOptions(validationOptions);
  //
  // db.createCollection("campaignTrackings", options);
  // }
}