package backend.service;

import backend.dto.analytics.InteractiveElementStats;
import backend.dto.analytics.PageStatsAnalytics;
import backend.dto.analytics.TimeOnSiteStatsAnalytics;
import backend.dto.analytics.TrafficSourceStats;
import backend.model.UserActivity;
import backend.repository.UserActivityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    private final UserActivityRepository userActivityRepository;

    public AnalyticsService(UserActivityRepository userActivityRepository) {
        this.userActivityRepository = userActivityRepository;
    }

    public Long getUniqueVisitorsCount() {
        return userActivityRepository.findAll().stream()
                .map(UserActivity::getUserIp)
                .distinct()
                .count();
    }

    public List<TrafficSourceStats> getTrafficSources() {
        return userActivityRepository.findAll().stream()
                .filter(activity -> activity.getReferer() != null)
                .collect(Collectors.groupingBy(UserActivity::getReferer))
                .entrySet().stream()
                .map(entry -> {
                    TrafficSourceStats stats = new TrafficSourceStats();
                    stats.setSource(entry.getKey()); // Используем setSource
                    stats.setVisits(entry.getValue().size());
                    return stats;
                })
                .collect(Collectors.toList());
    }

    public List<TimeOnSiteStatsAnalytics> getTimeOnSite() {
        return userActivityRepository.findAll().stream()
                .collect(Collectors.groupingBy(UserActivity::getSessionId))
                .entrySet().stream()
                .map(entry -> {
                    List<UserActivity> activities = entry.getValue();
                    LocalDateTime firstEvent = activities.stream()
                            .map(UserActivity::getTimestamp)
                            .min(LocalDateTime::compareTo)
                            .orElse(null);
                    LocalDateTime lastEvent = activities.stream()
                            .map(UserActivity::getTimestamp)
                            .max(LocalDateTime::compareTo)
                            .orElse(null);

                    long timeOnSite = firstEvent != null && lastEvent != null
                            ? java.time.Duration.between(firstEvent, lastEvent).toSeconds()
                            : 0;

                    TimeOnSiteStatsAnalytics stats = new TimeOnSiteStatsAnalytics();
                    stats.setSessionId(entry.getKey());
                    stats.setTimeOnSite(timeOnSite);
                    return stats;
                })
                .collect(Collectors.toList());
    }

    public List<PageStatsAnalytics> getPopularPages() {
        return userActivityRepository.findAll().stream()
                .filter(activity -> "view".equals(activity.getEventType()))
                .collect(Collectors.groupingBy(UserActivity::getPageUrl))
                .entrySet().stream()
                .map(entry -> {
                    PageStatsAnalytics stats = new PageStatsAnalytics();
                    stats.setPageUrl(entry.getKey());
                    stats.setViews(entry.getValue().size());
                    return stats;
                })
                .collect(Collectors.toList());
    }

    public List<InteractiveElementStats> getInteractiveElementInteractions() {
        return List.of(
                new InteractiveElementStats("button1", 100),
                new InteractiveElementStats("link1", 50)
        );
    }
}