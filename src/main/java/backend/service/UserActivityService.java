package backend.service;

import backend.dto.UserActivityLogRequest;
import backend.dto.userActivy.PageStatsUserActivity;
import backend.dto.userActivy.TimeOnSiteStatsUserActivity;
import backend.dto.userActivy.TrafficSourceStatsUserActivity;
import backend.model.UserActivity;
import backend.repository.UserActivityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserActivityService {
    private final UserActivityRepository userActivityRepository;

    public UserActivityService(UserActivityRepository userActivityRepository) {
        this.userActivityRepository = userActivityRepository;
    }

    // Логирование активности пользователя
    public void logActivity(UserActivityLogRequest request) {
        UserActivity activity = UserActivity.builder()
                .userIp(request.getUserIp())
                .sessionId(request.getSessionId())
                .pageUrl(request.getPageUrl())
                .eventType(request.getEventType())
                .eventDetails(request.getEventDetails())
                .referer(request.getReferer())
                .timestamp(LocalDateTime.now())
                .build();

        userActivityRepository.save(activity);
    }

    // Получение статистики по популярным страницам
    public List<PageStatsUserActivity> getPopularPages() {
        return userActivityRepository.findAll().stream()
                .filter(activity -> "view".equals(activity.getEventType()))
                .collect(Collectors.groupingBy(UserActivity::getPageUrl))
                .entrySet().stream()
                .map(entry -> {
                    PageStatsUserActivity stats = new PageStatsUserActivity();
                    stats.setPageUrl(entry.getKey());
                    stats.setViews(entry.getValue().size());
                    return stats;
                })
                .collect(Collectors.toList());
    }

   // Получение статистики по источникам трафика
    public List<TrafficSourceStatsUserActivity> getTrafficSources() {
        return userActivityRepository.findAll().stream()
                .filter(activity -> activity.getReferer() != null)
                .collect(Collectors.groupingBy(UserActivity::getReferer))
                .entrySet().stream()
                .map(entry -> {
                    TrafficSourceStatsUserActivity stats = new TrafficSourceStatsUserActivity();
                    stats.setReferer(entry.getKey());
                    stats.setVisits(entry.getValue().size());
                    return stats;
                })
                .collect(Collectors.toList());
    }

    public List<TimeOnSiteStatsUserActivity> getTimeOnSite() {
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

                    TimeOnSiteStatsUserActivity stats = new TimeOnSiteStatsUserActivity();
                    stats.setSessionId(entry.getKey());
                    stats.setTimeOnSite(timeOnSite);
                    return stats;
                })
                .collect(Collectors.toList());
    }
}