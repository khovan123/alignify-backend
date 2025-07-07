package com.api.service.statistic.impl;

import com.api.dto.statistic.*;
import com.api.service.statistic.InfluencerStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class InfluencerStatsServiceImpl implements InfluencerStatsService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public InfluencerStatsServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public InfluencerStatsResponse getInfluencerStats(String influencerId, int months) {
        List<InvitationData> invData = new ArrayList<>();
        List<ApplicationData> appData = new ArrayList<>();
        List<IncomeData> incData = new ArrayList<>();
        List<ForumData> forumData = new ArrayList<>();

        LocalDate now = LocalDate.now();
        for (int i = months - 1; i >= 0; i--) {
            YearMonth ym = YearMonth.from(now).minusMonths(i);
            String label = "T" + ym.getMonthValue();

            LocalDate start = ym.atDay(1);
            LocalDate end = ym.atEndOfMonth();

            Criteria baseCrit = Criteria.where("influencerId").is(influencerId)
                    .and("createdDate").gte(start.atStartOfDay(ZoneId.systemDefault()))
                    .lte(end.atTime(23, 59, 59).atZone(ZoneId.systemDefault()));

            // Invitations
            MatchOperation matchInv = Aggregation.match(baseCrit);
            GroupOperation groupInv = Aggregation.group()
                    .sum(ConditionalOperators.when(Criteria.where("status").is("RECEIVED")).then(1).otherwise(0))
                    .as("received")
                    .sum(ConditionalOperators.when(Criteria.where("status").is("ACCEPTED")).then(1).otherwise(0))
                    .as("accepted")
                    .sum(ConditionalOperators.when(Criteria.where("status").is("REJECTED")).then(1).otherwise(0))
                    .as("rejected");
            ProjectionOperation projInv = Aggregation.project()
                    .andExpression("'" + label + "'").as("month")
                    .and("received").as("received")
                    .and("accepted").as("accepted")
                    .and("rejected").as("rejected");
            AggregationResults<InvitationData> resInv = mongoTemplate.aggregate(
                    Aggregation.newAggregation(matchInv, groupInv, projInv),
                    "invitations", InvitationData.class);
            InvitationData inv = resInv.getUniqueMappedResult();
            // Nếu null, tạo object mặc định
            if (inv == null) {
                inv = new InvitationData(label, 0, 0, 0);
            }
            invData.add(inv);

            // Applications
            MatchOperation matchApp = Aggregation.match(baseCrit);
            GroupOperation groupApp = Aggregation.group()
                    .sum(ConditionalOperators.when(Criteria.where("type").is("SENT")).then(1).otherwise(0)).as("sent")
                    .sum(ConditionalOperators.when(Criteria.where("type").is("ACCEPTED")).then(1).otherwise(0))
                    .as("accepted")
                    .sum(ConditionalOperators.when(Criteria.where("type").is("REJECTED")).then(1).otherwise(0))
                    .as("rejected");
            ProjectionOperation projApp = Aggregation.project()
                    .andExpression("'" + label + "'").as("month")
                    .and("sent").as("sent")
                    .and("accepted").as("accepted")
                    .and("rejected").as("rejected");
            AggregationResults<ApplicationData> resApp = mongoTemplate.aggregate(
                    Aggregation.newAggregation(matchApp, groupApp, projApp),
                    "applications", ApplicationData.class);
            ApplicationData app = resApp.getUniqueMappedResult();
            if (app == null) {
                app = new ApplicationData(label, 0, 0, 0);
            }
            appData.add(app);

            // Income (transactions)
            MatchOperation matchInc = Aggregation.match(baseCrit);
            GroupOperation groupInc = Aggregation.group()
                    .sum("amount").as("income")
                    .count().as("campaigns");
            ProjectionOperation projInc = Aggregation.project()
                    .andExpression("'" + label + "'").as("month")
                    .and("income").as("income")
                    .and("campaigns").as("campaigns");
            AggregationResults<IncomeData> resInc = mongoTemplate.aggregate(
                    Aggregation.newAggregation(matchInc, groupInc, projInc),
                    "transactions", IncomeData.class);
            IncomeData inc = resInc.getUniqueMappedResult();
            if (inc == null) {
                inc = new IncomeData(label, 0L, 0);
            }
            incData.add(inc);

            // Forum (posts)
            MatchOperation matchForum = Aggregation.match(baseCrit);
            GroupOperation groupForum = Aggregation.group()
                    .count().as("posts")
                    .sum("likes").as("likes")
                    .sum("comments").as("comments")
                    .sum("shares").as("shares")
                    .sum("views").as("views");
            ProjectionOperation projForum = Aggregation.project()
                    .andExpression("'" + label + "'").as("month")
                    .and("posts").as("posts")
                    .and("likes").as("likes")
                    .and("comments").as("comments")
                    .and("shares").as("shares")
                    .and("views").as("views");
            AggregationResults<ForumData> resForum = mongoTemplate.aggregate(
                    Aggregation.newAggregation(matchForum, groupForum, projForum),
                    "posts", ForumData.class);
            ForumData forum = resForum.getUniqueMappedResult();
            if (forum == null) {
                forum = new ForumData(label, 0, 0, 0, 0, 0);
            }
            forumData.add(forum);
        }

        return new InfluencerStatsResponse(invData, appData, incData, forumData);
    }
}
