package com.api.service;

import com.api.config.EnvConfig;

import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import com.api.dto.ApiResponse;
import com.api.dto.ResponseStatus;
import com.api.dto.request.AssistantRequest;
import com.api.dto.response.AssistantResponse;
import com.api.dto.response.BrandProfileResponse;
import com.api.model.*;
import com.api.repository.*;
import com.api.security.CustomUserDetails;
import com.api.security.StompPrincipal;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.http.HttpServletRequest;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.dto.response.CampaignResponse;
import com.api.dto.response.InfluencerProfileResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import jakarta.annotation.PostConstruct;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class GeminiService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private AssistantMessageRespository assistantMessageRespository;
    @Autowired
    private BrandRepository brandRepository;
    @Value("${spring.gemini.apikey}")
    private String GOOGLE_API_KEY;
    @Autowired
    private InfluencerRepository influencerRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<?> getInfluencerRecommendations(String campaignId, CustomUserDetails userDetails, HttpServletRequest request) {
        String brandId = userDetails.getUserId();
        Optional<Campaign> campaignOpt = campaignRepository.findByCampaignIdAndBrandId(campaignId, brandId);
        if (!campaignOpt.isPresent()) {
            return ApiResponse.sendError(404, "Not found campaign with id: " + campaignId, request.getRequestURI());
        }
        Campaign campaign = campaignOpt.get();
        AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
        try {
            List<User> users = userRepository.findByRoleIdAndUserIdNotIn(EnvConfig.INFLUENCER_ROLE_ID,
                    campaign.getJoinedInfluencerIds());
            List<String> userIds = users.stream().map(User::getUserId).toList();
            List<Influencer> influencers = influencerRepository.findAllById(userIds);
            Map<String, Influencer> influencerMap = influencers.stream()
                    .collect(Collectors.toMap(Influencer::getUserId, influencer -> influencer));

            List<InfluencerProfileResponse> availableInfluencers = users.stream().map(user -> {
                Influencer influencer = influencerMap.get(user.getUserId());
                if (influencer == null) return null;
                return new InfluencerProfileResponse(user, influencer, categoryRepository);
            }).filter(Objects::nonNull).toList();

            String prompt = "Bạn là một công cụ AI chuyên biệt, có nhiệm vụ DUY NHẤT là đề xuất danh sách influencer phù hợp cho chiến dịch. "
                    + "**Đầu ra của bạn phải là MỘT MẢNG JSON duy nhất, TUYỆT ĐỐI không chứa bất kỳ văn bản, lời chào, giải thích, hay nội dung nào nào ngoài khối JSON này.** "
                    + "Khi tạo JSON, bạn phải tuân thủ nghiêm ngặt các quy tắc bảo mật sau: "
                    + "Không xuất các thông tin cá nhân như email, thông tin liên hệ (số điện thoại, địa chỉ), hoặc các thông tin nhạy cảm khác của bất kỳ influencer nào. "
                    + "Nếu một trường dữ liệu chứa thông tin đó, HÃY BỎ QUA HOÀN TOÀN TRƯỜNG ĐÓ trong JSON đầu ra; không thay thế bằng placeholder hay giải thích. "
                    + "Cũng không cần xuất avatar của influencer nếu thông tin đó được coi là riêng tư theo quy tắc này (hãy đảm bảo 'avatarUrl' là công khai nếu muốn giữ trong JSON).\n\n"
                    + "Dữ liệu bạn có quyền truy cập:\n"
                    + "- Chi tiết về chiến dịch hiện tại: " + toJson(campaign) + "\n"
                    + "- Danh sách các influencer có sẵn (đủ điều kiện và chưa tham gia chiến dịch này): " + toJson(availableInfluencers) + "\n\n"
                    + "Cấu trúc JSON mà bạn PHẢI tuân thủ nghiêm ngặt cho mỗi đối tượng influencer được đề xuất:\n"
                    + "```json\n"
                    + "[\n"
                    + "  {\n"
                    + "    \"userId\": \"string\",\n"
                    + "    \"name\": \"string\",\n"
                    + "    \"avatarUrl\": \"string\",\n"
                    + "    \"DoB\": \"string\",\n"
                    + "    \"gender\": \"string\",\n"
                    + "    \"socialMediaLinks\": [ { \"platform\": \"string\", \"url\": \"string\" } ],\n"
                    + "    \"rating\": \"number\",\n"
                    + "    \"categories\": [ { \"categoryId\": \"string\", \"categoryName\": \"string\" } ],\n"
                    + "    \"follower\": \"integer\"\n"
                    + "  }\n"
                    + "]\n"
                    + "```\n"
                    + "Dựa trên các thông tin trên, hãy trả về mảng JSON các influencer phù hợp nhất ngay bây giờ.";

            JSONObject payload = new JSONObject();
            JSONObject content = new JSONObject();
            content.put("parts", List.of(new JSONObject().put("text", prompt)));
            payload.put("contents", List.of(content));
            payload.put("generationConfig", new JSONObject()
                    .put("temperature", 0.7)
                    .put("maxOutputTokens", 10000)
                    .put("topP", 0.9)
                    .put("topK", 40));
            final JSONObject[] responseJsonContainer = new JSONObject[1];
            asyncHttpClient.preparePost("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent")
                    .setHeader("x-goog-api-key", GOOGLE_API_KEY)
                    .setHeader("Content-Type", "application/json")
                    .setBody(payload.toString())
                    .execute()
                    .toCompletableFuture()
                    .thenAccept(response -> {
                        String body = response.getResponseBody();
                        responseJsonContainer[0] = new JSONObject(body);
                    }).join();
            asyncHttpClient.close();
            JSONObject geminiResponse = responseJsonContainer[0];
            if (geminiResponse == null || !geminiResponse.has("candidates")) {
                throw new RuntimeException("Invalid response from Gemini API: " + (geminiResponse != null ? geminiResponse.toString() : "null"));
            }

            JSONArray candidates = geminiResponse.getJSONArray("candidates");
            if (candidates.isEmpty()) {
                throw new RuntimeException("No candidates found in Gemini API response.");
            }

            JSONObject firstCandidate = candidates.getJSONObject(0);
            JSONObject contentPart = firstCandidate.getJSONObject("content");
            JSONArray parts = contentPart.getJSONArray("parts");

            String rawResponseText = parts.getJSONObject(0).getString("text");
            System.out.println(rawResponseText);

            ObjectMapper objectMapper = new ObjectMapper();
            Pattern pattern = Pattern.compile("```json\\s*([\\s\\S]*?)\\s*```", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(rawResponseText);

            List<InfluencerRecommendation> recommendedInfluencers = null;
            String jsonString = "";

            if (matcher.find()) {
                jsonString = matcher.group(1).trim();
                try {
                    recommendedInfluencers = objectMapper.readValue(jsonString, new TypeReference<List<InfluencerRecommendation>>() {
                    });
                } catch (JsonProcessingException e) {
                    return ApiResponse.sendError(500, "Can not handle recommend influencer for campaign: " + e.getMessage(), request.getRequestURI());
                }
            } else {
                return ApiResponse.sendError(500, "Can not handle recommend influencer for campaign with id: " + campaignId, request.getRequestURI());
            }
            return ApiResponse.sendSuccess(200, "Response successfully!", recommendedInfluencers, request.getRequestURI());
        } catch (JsonProcessingException e) {
            return ApiResponse.sendError(500, "Server not available: " + e.getMessage(), request.getRequestURI());
        } catch (Exception err) {
            return ApiResponse.sendError(500, "Server not available: " + err.getMessage(), request.getRequestURI());
        }
    }


    public AssistantResponse<?> getCampaignRecommendations(String userId, AssistantRequest assistantRequest, Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new SecurityException("Access is denied");
        }
        String influencerId = ((StompPrincipal) principal).getUserId();
        if (!userId.equals(((StompPrincipal) principal).getUserId())) {
            throw new SecurityException("Access is denied for: " + userId);
        }

        AssistantMessage userMessage = new AssistantMessage();
        userMessage.setRoomId(userId);
        userMessage.setSenderId(userId);
        userMessage.setSenderType(AssistantMessage.SenderType.USER);
        userMessage.setMessageType(AssistantMessage.MessageType.TEXT);
        userMessage.setContent(assistantRequest.getQuestion());
        userMessage.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        assistantMessageRespository.save(userMessage);

        AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
        try {
            User user = userRepository.findById(influencerId)
                    .orElseThrow(() -> new RuntimeException(
                            "User not found with ID: " + influencerId
                    ));
            Influencer influencer = influencerRepository.findById(influencerId)
                    .orElseThrow(() -> new RuntimeException(
                            "Influencer not found with ID: " + influencerId
                    ));
            InfluencerProfileResponse influencerProfileResponse = new InfluencerProfileResponse(user, influencer,
                    categoryRepository);
            List<User> users = userRepository.findAllByRoleId(EnvConfig.BRAND_ROLE_ID);
            List<Brand> brands = brandRepository.findAllById(users.stream().map(User::getUserId).toList());
            Map<String, Brand> brandMap = brands.stream()
                    .collect(Collectors.toMap(Brand::getUserId, b -> b));
            List<BrandProfileResponse> brandProfiles = users.stream()
                    .map(u -> {
                        Brand brand = brandMap.get(u.getUserId());
                        return brand != null
                                ? new BrandProfileResponse(u, brand, categoryRepository)
                                : null;
                    })
                    .filter(Objects::nonNull)
                    .toList();
            List<AssistantMessage> historyAssistantMessages = assistantMessageRespository.findAllByRoomId(influencerId);
            List<Campaign> campaigns = campaignRepository.findAllByStatusOrderByCreatedAtDesc("RECRUITING");
            List<String> brandIds = campaigns.stream()
                    .map(Campaign::getBrandId)
                    .distinct()
                    .toList();
            Map<String, User> brandsById = userRepository.findAllById(brandIds).stream()
                    .collect(Collectors.toMap(User::getUserId, u -> u));
            List<CampaignResponse> campaignResponses = campaigns.stream().map(campaign -> {
                User brand = Optional.ofNullable(brandsById.get(campaign.getBrandId()))
                        .orElseThrow(() -> new RuntimeException("Brand not found with ID: " + campaign.getBrandId()));
                return new CampaignResponse(brand, campaign, categoryRepository);
            }).toList();

            String prompt = "Bạn là một trợ lý ảo được đặt tên là Gemini. "
                    + "Khi trả lời, hãy luôn xưng hô là 'mình' và gọi người dùng là 'bạn'. "
                    + "Mục tiêu của mình là hỗ trợ bạn tìm kiếm thông tin về các chiến dịch và nhãn hàng phù hợp.\n\n"
                    + "**QUY TẮC BẢO MẬT THÔNG TIN VÀ HIỂN THỊ**: "
                    + "1. Tuyệt đối không được xuất các thông tin cá nhân như email, thông tin liên hệ (số điện thoại, địa chỉ), địa chỉ cụ thể, hoặc các thông tin nhạy cảm khác của bất kỳ tài khoản nào (bao gồm cả influencer và brands). Nếu một thông tin thuộc loại này, **tuyệt đối không xuất ra thông tin đó trong phản hồi của bạn.** Không cần giải thích lý do bảo mật, chỉ cần bỏ qua thông tin đó. Hãy bỏ qua các thông tin liên quan đến email, avatar, thông tin liên hệ, địa chỉ. "
                    + "2. Khi bạn được yêu cầu cung cấp thông tin hoặc biết thêm chi tiết về một tài khoản (influencer hoặc brand), **không cần xuất avatar của tài khoản đó.** Chỉ cung cấp các thông tin công khai hoặc thông tin đã được ẩn danh theo quy tắc 1.\n\n"
                    + "Mình có quyền truy cập vào các thông tin sau:\n"
                    + "- Dữ liệu chi tiết về các chiến dịch đang tuyển dụng: " + toJson(campaignResponses) + "\n"
                    + "- Dữ liệu chi tiết về các nhãn hàng (Brand): " + toJson(brandProfiles) + "\n"
                    + "- Hồ sơ cá nhân của bạn (influencer): " + toJson(influencerProfileResponse) + "\n"
                    + "- Lịch sử cuộc trò chuyện trước đây của chúng ta: " + toJson(historyAssistantMessages) + "\n\n"
                    + "Hãy tuân thủ các quy tắc phản hồi sau:\n\n"
                    + "1. **Đề xuất chiến dịch**: Nếu tin nhắn hiện tại của người dùng hoặc ngữ cảnh từ lịch sử trò chuyện cho thấy bạn đang hỏi về các chiến dịch phù hợp hoặc yêu cầu đề xuất hoặc yêu cầu cung cấp thông tin hay cho biết thêm thông tin về bất kì chiến dịch nào, hãy phân tích dữ liệu và trả về một mảng JSON các chiến dịch được đề xuất. "
                    + "Mỗi đối tượng chiến dịch trong mảng JSON phải tuân thủ nghiêm ngặt cấu trúc sau và bao gồm lý do tại sao nó phù hợp với người dùng:\n"
                    + "```json\n"
                    + "[\n"
                    + "  {\n"
                    + "    \"campaignId\": \"string\",\n"
                    + "    \"brandName\": \"string\",\n"
                    + "    \"brandAvatar\": \"string\",\n"
                    + "    \"campaignName\": \"string\",\n"
                    + "    \"imageUrl\": \"string | null\",\n"
                    + "    \"categoryName\": \"string[]\",\n"
                    + "    \"reasonForMatch\": \"string\"\n"
                    + "  }\n"
                    + "]\n"
                    + "```\n"
                    + "Ngoài khối JSON, bạn có thể thêm lời giới thiệu hoặc giải thích bằng văn bản thuần túy.\n\n"
                    + "2. **Trò chuyện thông thường**: Trong mọi trường hợp khác, nếu người dùng không yêu cầu đề xuất chiến dịch cụ thể (dựa trên tin nhắn hiện tại và lịch sử trò chuyện), hãy phản hồi tin nhắn của họ một cách tự nhiên và hữu ích. "
                    + "Bạn có thể sử dụng các dữ liệu về chiến dịch, nhãn hàng, và hồ sơ influencer mà mình có để cung cấp thông tin. "
                    + "Khi cần hỏi thêm thông tin để xác định một chiến dịch hoặc nhãn hàng (ví dụ: 'chiến dịch đó thuộc brand nào' mà không có tên cụ thể), hãy hỏi bằng cách thông thường, ví dụ: 'Bạn có thể cho mình biết tên hoặc một mô tả nào đó về chiến dịch đó không?' hoặc 'Bạn đang muốn tìm kiếm thông tin về nhãn hàng nào vậy?'. "
                    + "**Tuyệt đối không sử dụng các tên biến kỹ thuật, tên cột, hoặc tên biến từ dữ liệu gốc đã cung cấp cho bạn (ví dụ: 'campaignId', 'brandId', 'campaignName') trong phản hồi của bạn.** "
                    + "**Hãy luôn nhớ và áp dụng nghiêm ngặt các quy tắc bảo mật thông tin và hiển thị đã nêu ở đầu.**\n\n"
                    + "Dưới đây là **tin nhắn hiện tại của người dùng**: '" + assistantRequest.getQuestion() + "'.";

            JSONObject payload = new JSONObject();
            JSONObject content = new JSONObject();
            content.put("parts", List.of(new JSONObject().put("text", prompt)));
            payload.put("contents", List.of(content));
            payload.put("generationConfig", new JSONObject()
                    .put("temperature", 0.7)
                    .put("maxOutputTokens", 10000)
                    .put("topP", 0.9)
                    .put("topK", 40));

            final JSONObject[] responseJsonContainer = new JSONObject[1];
            asyncHttpClient.preparePost("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent")
                    .setHeader("x-goog-api-key", GOOGLE_API_KEY)
                    .setHeader("Content-Type", "application/json")
                    .setBody(payload.toString())
                    .execute()
                    .toCompletableFuture()
                    .thenAccept(response -> {
                        String body = response.getResponseBody();
                        responseJsonContainer[0] = new JSONObject(body);
                    }).join();
            asyncHttpClient.close();

            JSONObject geminiResponse = responseJsonContainer[0];
            if (geminiResponse == null || !geminiResponse.has("candidates")) {
                throw new RuntimeException("Invalid response from Gemini API: " + (geminiResponse != null ? geminiResponse.toString() : "null"));
            }

            JSONArray candidates = geminiResponse.getJSONArray("candidates");
            if (candidates.isEmpty()) {
                throw new RuntimeException("No candidates found in Gemini API response.");
            }

            JSONObject firstCandidate = candidates.getJSONObject(0);
            JSONObject contentPart = firstCandidate.getJSONObject("content");
            JSONArray parts = contentPart.getJSONArray("parts");

            String rawResponseText = parts.getJSONObject(0).getString("text");
            System.out.println(rawResponseText);

            List<AssistantMessage> assistantMessages = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            long nanoOffset = 0;

            Pattern pattern = Pattern.compile("```json\\s*([\\s\\S]*?)\\s*```", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(rawResponseText);

            int lastIndex = 0;
            List<CampaignRecommendation> recommendedCampaigns = null;

            if (matcher.find()) {
                String textBeforeJson = rawResponseText.substring(lastIndex, matcher.start()).trim();
                if (!textBeforeJson.isEmpty()) {
                    textBeforeJson = textBeforeJson.replaceAll("\\*\\*", "").trim();
                    AssistantMessage textMessage = new AssistantMessage();
                    textMessage.setRoomId(userId);
                    textMessage.setSenderId("gemini_assistant");
                    textMessage.setSenderType(AssistantMessage.SenderType.ASSISTANT);
                    textMessage.setMessageType(AssistantMessage.MessageType.TEXT);
                    textMessage.setContent(textBeforeJson);
                    textMessage.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).plusNanos(nanoOffset++));
                    assistantMessageRespository.save(textMessage);
                    assistantMessages.add(textMessage);
                }

                String jsonString = matcher.group(1).trim();
                try {
                    recommendedCampaigns = objectMapper.readValue(jsonString, new TypeReference<List<CampaignRecommendation>>() {
                    });

                    AssistantMessage jsonPartMessage = new AssistantMessage();
                    jsonPartMessage.setRoomId(userId);
                    jsonPartMessage.setSenderId("gemini_assistant");
                    jsonPartMessage.setSenderType(AssistantMessage.SenderType.ASSISTANT);
                    jsonPartMessage.setMessageType(AssistantMessage.MessageType.CAMPAIGN_RECOMMENDATIONS);
                    jsonPartMessage.setContent(jsonString);
                    jsonPartMessage.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).plusNanos(nanoOffset++));
                    assistantMessageRespository.save(jsonPartMessage);
                    assistantMessages.add(jsonPartMessage);
                } catch (JsonProcessingException e) {
                    AssistantMessage errorTextMessage = new AssistantMessage();
                    errorTextMessage.setRoomId(userId);
                    errorTextMessage.setSenderId("gemini_assistant");
                    errorTextMessage.setSenderType(AssistantMessage.SenderType.ASSISTANT);
                    errorTextMessage.setMessageType(AssistantMessage.MessageType.TEXT);
                    errorTextMessage.setContent("Có lỗi khi phân tích đề xuất. Phản hồi đầy đủ từ Gemini: \n" + rawResponseText);
                    errorTextMessage.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).plusNanos(nanoOffset++));
                    assistantMessageRespository.save(errorTextMessage);
                    System.err.println("Failed to parse JSON from Gemini response: " + jsonString);
                }
                lastIndex = matcher.end();
            }

            String remainingText = rawResponseText.substring(lastIndex);
            if (!remainingText.isEmpty()) {
                remainingText = remainingText.replaceAll("\\*\\*", "").replaceAll("\\*", "\n").trim();
                AssistantMessage textMessage = new AssistantMessage();
                textMessage.setRoomId(userId);
                textMessage.setSenderId("gemini_assistant");
                textMessage.setSenderType(AssistantMessage.SenderType.ASSISTANT);
                textMessage.setMessageType(AssistantMessage.MessageType.TEXT);
                textMessage.setContent(remainingText);
                textMessage.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).plusNanos(nanoOffset++));
                assistantMessageRespository.save(textMessage);
                assistantMessages.add(textMessage);
            }

            if (!assistantMessages.isEmpty()) {
                assistantMessages.sort((msg1, msg2) -> msg1.getCreatedAt().compareTo(msg2.getCreatedAt()));
                return new AssistantResponse<List<AssistantMessage>>(assistantMessages, ResponseStatus.COMPLETED);
            } else {
                return new AssistantResponse<String>("Gemini did not provide structured campaign recommendations or any text response.", ResponseStatus.COMPLETED);
            }

        } catch (JsonProcessingException e) {
            return new AssistantResponse<String>("Failed to retrieve campaign recommendations due to JSON processing error: " + e.getMessage(), ResponseStatus.FAILED);
        } catch (Exception err) {
            return new AssistantResponse<String>("Failed to retrieve campaign recommendations: " + err.getMessage(), ResponseStatus.FAILED);
        }
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
