package com.api.service;


import com.api.dto.response.BrandStatisticsResponse;
import com.api.dto.response.InfluencerStatisticsResponse;
import com.api.model.Application;
import com.api.model.Invitation;
import com.api.repository.ApplicationRepository;
import com.api.repository.InvitationRepository;
// import com.api.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private InvitationRepository invitationRepository;
    // @Autowired
    // private CampaignRepository campaignRepository;

    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    @Override
    public BrandStatisticsResponse getBrandStatistics(Long brandId) {
        String brandIdStr = String.valueOf(brandId);
        List<Application> applications = applicationRepository.findAllByBrandId(brandIdStr);
        List<Invitation> invitations = invitationRepository.findAll()
            .stream().filter(i -> brandIdStr.equals(i.getBrandId())).collect(Collectors.toList());

        // Group by month
        Map<String, List<Invitation>> invitationByMonth = invitations.stream()
            .collect(Collectors.groupingBy(i -> i.getCreatedAt().format(MONTH_FORMATTER)));
        Map<String, List<Application>> applicationByMonth = applications.stream()
            .collect(Collectors.groupingBy(a -> a.getCreatedAt().format(MONTH_FORMATTER)));

        List<BrandStatisticsResponse.Invitation> invitationStats = new ArrayList<>();
        for (String month : invitationByMonth.keySet()) {
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
        for (String month : applicationByMonth.keySet()) {
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
        response.setInvitations(invitationStats);
        response.setApplications(applicationStats);
        response.setTotalInvitations(totalInvitations);
        response.setAcceptanceRate(acceptanceRate);
        response.setTotalApplications(totalApplications);
        // Các trường cost sẽ xử lý sau
        return response;
    }

    @Override
    public InfluencerStatisticsResponse getInfluencerStatistics(Long influencerId) {
        String influencerIdStr = String.valueOf(influencerId);
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
        for (String month : applicationByMonth.keySet()) {
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
        int totalApplications = applications.size();
        int totalApproved = (int) applications.stream().filter(a -> "APPROVED".equalsIgnoreCase(a.getStatus())).count();
        int totalPending = (int) applications.stream().filter(a -> "PENDING".equalsIgnoreCase(a.getStatus())).count();
        int totalRejected = (int) applications.stream().filter(a -> "REJECTED".equalsIgnoreCase(a.getStatus())).count();
        double approvalRate = totalApplications == 0 ? 0 : (double) totalApproved / totalApplications;
        int totalInvitations = invitations.size();
        int totalAccepted = (int) invitations.stream().filter(i -> "ACCEPTED".equalsIgnoreCase(i.getStatus())).count();
        int totalRejectedInvitations = (int) invitations.stream().filter(i -> "REJECTED".equalsIgnoreCase(i.getStatus())).count();
        double invitationAcceptanceRate = totalInvitations == 0 ? 0 : (double) totalAccepted / totalInvitations;

        InfluencerStatisticsResponse response = new InfluencerStatisticsResponse();
        response.setApplications(applicationStats);
        response.setInvitations(invitationStats);
        response.setTotalApplications(totalApplications);
        response.setTotalApproved(totalApproved);
        response.setTotalPending(totalPending);
        response.setTotalRejected(totalRejected);
        response.setApprovalRate(approvalRate);
        response.setTotalInvitations(totalInvitations);
        response.setTotalAccepted(totalAccepted);
        response.setTotalRejectedInvitations(totalRejectedInvitations);
        response.setInvitationAcceptanceRate(invitationAcceptanceRate);
        return response;
    }
}
