package com.api.service;


import com.api.dto.response.BrandStatisticsResponse;
import com.api.dto.response.InfluencerStatisticsResponse;
import com.api.model.Application;
import com.api.model.Invitation;
import com.api.repository.ApplicationRepository;
import com.api.repository.InvitationRepository;
import com.api.repository.ContentPostingRepository;
// import com.api.repository.CommentRepository;
// import com.api.repository.LikesRepository;
// import com.api.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private ContentPostingRepository contentPostingRepository;
    // @Autowired
    // private CommentRepository commentRepository;
    // @Autowired
    // private LikesRepository likesRepository;

    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    public BrandStatisticsResponse getBrandStatistics(String brandId) {
        List<Application> applications = applicationRepository.findAllByBrandId(brandId);
        List<Invitation> invitations = invitationRepository.findAllByBrandId(brandId);

        // Group by month
        Map<String, List<Invitation>> invitationByMonth = invitations.stream()
            .collect(Collectors.groupingBy(i -> i.getCreatedAt().format(MONTH_FORMATTER)));
        Map<String, List<Application>> applicationByMonth = applications.stream()
            .collect(Collectors.groupingBy(a -> a.getCreatedAt().format(MONTH_FORMATTER)));

        List<BrandStatisticsResponse.Invitation> invitationStats = new ArrayList<>();
        List<String> months = new ArrayList<>(invitationByMonth.keySet());
        months.sort(Comparator.naturalOrder());
        for (String month : months) {
            List<Invitation> monthInvites = invitationByMonth.get(month);
            int sent = monthInvites.size();
            int accepted = (int) monthInvites.stream().filter(i -> "ACCEPTED".equalsIgnoreCase(i.getStatus())).count();
            int rejected = (int) monthInvites.stream().filter(i -> "REJECTED".equalsIgnoreCase(i.getStatus())).count();
            BrandStatisticsResponse.Invitation stat = new BrandStatisticsResponse.Invitation();
            stat.setMonth(month);
            stat.setSent(sent);
            stat.setAccepted(accepted);
            stat.setRejected(rejected);
            invitationStats.add(stat);
        }

        List<BrandStatisticsResponse.Application> applicationStats = new ArrayList<>();
        List<String> applicationMonths = new ArrayList<>(applicationByMonth.keySet());
        applicationMonths.sort(Comparator.naturalOrder());
        for (String month : applicationMonths) {
            List<com.api.model.Application> monthApps = applicationByMonth.get(month);
            int total = monthApps.size();
            int approved = (int) monthApps.stream().filter(a -> "APPROVED".equalsIgnoreCase(a.getStatus())).count();
            int pending = (int) monthApps.stream().filter(a -> "PENDING".equalsIgnoreCase(a.getStatus())).count();
            int rejected = (int) monthApps.stream().filter(a -> "REJECTED".equalsIgnoreCase(a.getStatus())).count();
            BrandStatisticsResponse.Application stat = new BrandStatisticsResponse.Application();
            stat.setMonth(month);
            stat.setTotal(total);
            stat.setApproved(approved);
            stat.setPending(pending);
            stat.setRejected(rejected);
            applicationStats.add(stat);
        }
        int totalInvitations = invitations.size();
        int totalAccepted = (int) invitations.stream().filter(i -> "ACCEPTED".equalsIgnoreCase(i.getStatus())).count();
        double acceptanceRate = totalInvitations == 0 ? 0 : (double) totalAccepted / totalInvitations;
        int totalApplications = applications.size();

        BrandStatisticsResponse response = new BrandStatisticsResponse();
        response.setInvitations(invitationStats != null ? invitationStats : new ArrayList<>());
        response.setApplications(applicationStats != null ? applicationStats : new ArrayList<>());
        // Đảm bảo costs luôn là mảng rỗng nếu chưa có logic
        response.setCosts(new ArrayList<>());
        response.setTotalInvitations(totalInvitations);
        response.setAcceptanceRate(acceptanceRate);
        response.setTotalApplications(totalApplications);
        response.setCurrentMonthCost(0);
        response.setTotalPaid(0);
        response.setTotalPending(0);
        response.setTotalCost(0);
        response.setAvgCost(0);
        return response;
    }

    public InfluencerStatisticsResponse getInfluencerStatistics(String influencerId) {
        String influencerIdStr = influencerId;
        List<Application> applications = applicationRepository.findAllByInfluencerId(influencerIdStr);
        List<Invitation> invitations = invitationRepository.findAll()
            .stream().filter(i -> influencerIdStr.equals(i.getInfluencerId())).collect(Collectors.toList());

        // Group by month
        Map<String, List<Invitation>> invitationByMonth = invitations.stream()
            .collect(Collectors.groupingBy(i -> i.getCreatedAt().format(MONTH_FORMATTER)));
        Map<String, List<Application>> applicationByMonth = applications.stream()
            .collect(Collectors.groupingBy(a -> a.getCreatedAt().format(MONTH_FORMATTER)));

        List<InfluencerStatisticsResponse.Invitation> invitationStats = new ArrayList<>();
        for (String month : invitationByMonth.keySet()) {
            List<Invitation> monthInvites = invitationByMonth.get(month);
            int sent = monthInvites.size();
            int accepted = (int) monthInvites.stream().filter(i -> "ACCEPTED".equalsIgnoreCase(i.getStatus())).count();
            int rejected = (int) monthInvites.stream().filter(i -> "REJECTED".equalsIgnoreCase(i.getStatus())).count();
            InfluencerStatisticsResponse.Invitation stat = new InfluencerStatisticsResponse.Invitation();
            stat.setMonth(month);
            stat.setSent(sent);
            stat.setAccepted(accepted);
            stat.setRejected(rejected);
            invitationStats.add(stat);
        }

        List<InfluencerStatisticsResponse.Application> applicationStats = new ArrayList<>();
        List<String> sortedApplicationMonths = new ArrayList<>(applicationByMonth.keySet());
        Collections.sort(sortedApplicationMonths);
        for (String month : sortedApplicationMonths) {
            List<com.api.model.Application> monthApps = applicationByMonth.get(month);
            int total = monthApps.size();
            int approved = (int) monthApps.stream().filter(a -> "APPROVED".equalsIgnoreCase(a.getStatus())).count();
            int pending = (int) monthApps.stream().filter(a -> "PENDING".equalsIgnoreCase(a.getStatus())).count();
            int rejected = (int) monthApps.stream().filter(a -> "REJECTED".equalsIgnoreCase(a.getStatus())).count();
            InfluencerStatisticsResponse.Application stat = new InfluencerStatisticsResponse.Application();
            stat.setMonth(month);
            stat.setTotal(total);
            stat.setApproved(approved);
            stat.setPending(pending);
            stat.setRejected(rejected);
            applicationStats.add(stat);
        }

        // Income: group các application APPROVED theo tháng, income = 0 (chưa có payment), campaigns = số application APPROVED
        Map<String, List<Application>> approvedAppsByMonth = applications.stream()
            .filter(a -> "APPROVED".equalsIgnoreCase(a.getStatus()))
            .collect(Collectors.groupingBy(a -> a.getCreatedAt().format(MONTH_FORMATTER)));
        List<InfluencerStatisticsResponse.Income> incomeStats = new ArrayList<>();
        for (String month : approvedAppsByMonth.keySet()) {
            List<Application> monthApps = approvedAppsByMonth.get(month);
            InfluencerStatisticsResponse.Income stat = new InfluencerStatisticsResponse.Income();
            stat.setMonth(month);
            stat.setIncome(0); // Chưa có payment
            stat.setCampaigns(monthApps.size());
            incomeStats.add(stat);
        }

        // Forum: group các contentPosting theo tháng, tính posts, likes, comments, shares/views = 0
        List<com.api.model.ContentPosting> posts = contentPostingRepository.findAll().stream()
            .filter(p -> influencerIdStr.equals(p.getUserId())).collect(Collectors.toList());
        Map<String, List<com.api.model.ContentPosting>> postsByMonth = posts.stream()
            .collect(Collectors.groupingBy(p -> p.getCreatedDate().format(MONTH_FORMATTER)));
        List<InfluencerStatisticsResponse.Forum> forumStats = new ArrayList<>();
        for (String month : postsByMonth.keySet()) {
            List<com.api.model.ContentPosting> monthPosts = postsByMonth.get(month);
            int postCount = monthPosts.size();
            int likeCount = monthPosts.stream().mapToInt(com.api.model.ContentPosting::getLikeCount).sum();
            int commentCount = monthPosts.stream().mapToInt(com.api.model.ContentPosting::getCommentCount).sum();
            InfluencerStatisticsResponse.Forum stat = new InfluencerStatisticsResponse.Forum();
            stat.setMonth(month);
            stat.setPosts(postCount);
            stat.setLikes(likeCount);
            stat.setComments(commentCount);
            stat.setShares(0);
            stat.setViews(0);
            forumStats.add(stat);
        }

        // Tổng hợp
        int totalApplications = applications.size();
        int totalApproved = (int) applications.stream().filter(a -> "APPROVED".equalsIgnoreCase(a.getStatus())).count();
        int totalPending = (int) applications.stream().filter(a -> "PENDING".equalsIgnoreCase(a.getStatus())).count();
        int totalRejected = (int) applications.stream().filter(a -> "REJECTED".equalsIgnoreCase(a.getStatus())).count();
        double approvalRate = totalApplications == 0 ? 0 : (double) totalApproved / totalApplications;
        int totalInvitations = invitations.size();
        int totalAccepted = (int) invitations.stream().filter(i -> "ACCEPTED".equalsIgnoreCase(i.getStatus())).count();
        int totalRejectedInvitations = (int) invitations.stream().filter(i -> "REJECTED".equalsIgnoreCase(i.getStatus())).count();
        double invitationAcceptanceRate = totalInvitations == 0 ? 0 : (double) totalAccepted / totalInvitations;

        // currentMonthIncome: lấy tháng hiện tại
        String currentMonth = java.time.ZonedDateTime.now().format(MONTH_FORMATTER);
        int currentMonthIncome = incomeStats.stream().filter(i -> currentMonth.equals(i.getMonth())).mapToInt(InfluencerStatisticsResponse.Income::getIncome).sum();
        int totalForumPosts = forumStats.stream().mapToInt(InfluencerStatisticsResponse.Forum::getPosts).sum();
        int totalIncome = incomeStats.stream().mapToInt(InfluencerStatisticsResponse.Income::getIncome).sum();
        int totalCampaigns = incomeStats.stream().mapToInt(InfluencerStatisticsResponse.Income::getCampaigns).sum();
        double avgIncomePerCampaign = totalCampaigns == 0 ? 0 : (double) totalIncome / totalCampaigns;
        double avgIncome = incomeStats.size() == 0 ? 0 : (double) totalIncome / incomeStats.size();

        InfluencerStatisticsResponse response = new InfluencerStatisticsResponse();
        response.setApplications(applicationStats);
        response.setInvitations(invitationStats);
        response.setIncome(incomeStats);
        response.setForum(forumStats);
        response.setTotalApplications(totalApplications);
        response.setTotalApproved(totalApproved);
        response.setTotalPending(totalPending);
        response.setTotalRejected(totalRejected);
        response.setApprovalRate(approvalRate);
        response.setTotalInvitations(totalInvitations);
        response.setTotalAccepted(totalAccepted);
        response.setTotalRejectedInvitations(totalRejectedInvitations);
        response.setInvitationAcceptanceRate(invitationAcceptanceRate);
        response.setCurrentMonthIncome(currentMonthIncome);
        response.setTotalForumPosts(totalForumPosts);
        response.setTotalIncome(totalIncome);
        response.setTotalCampaigns(totalCampaigns);
        response.setAvgIncomePerCampaign(avgIncomePerCampaign);
        response.setAvgIncome(avgIncome);
        return response;
    }

}
