package backend.service;

import backend.dto.UserActivityLogRequest;
import backend.dto.userActivy.PageStats;
import backend.dto.userActivy.TrafficSourceStats;
import backend.dto.userActivy.TimeOnSiteStats;
import backend.model.UserActivity;
import backend.repository.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserActivityService {
    private final UserActivityRepository userActivityRepository;

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
    public List<PageStats> getPopularPages() {
        return userActivityRepository.findAll().stream()
                .filter(activity -> "view".equals(activity.getEventType()))
                .collect(Collectors.groupingBy(UserActivity::getPageUrl))
                .entrySet().stream()
                .map(entry -> {
                    PageStats stats = new PageStats();
                    stats.setPageUrl(entry.getKey());
                    stats.setViews(entry.getValue().size());
                    return stats;
                })
                .collect(Collectors.toList());
    }

    // Получение статистики по источникам трафика
    public List<TrafficSourceStats> getTrafficSources() {
        return userActivityRepository.findAll().stream()
                .filter(activity -> activity.getReferer() != null)
                .collect(Collectors.groupingBy(UserActivity::getReferer))
                .entrySet().stream()
                .map(entry -> {
                    TrafficSourceStats stats = new TrafficSourceStats();
                    stats.setReferer(entry.getKey());
                    stats.setVisits(entry.getValue().size());
                    return stats;
                })
                .collect(Collectors.toList());
    }

    // Получение статистики по времени, проведенному на сайте
    public List<TimeOnSiteStats> getTimeOnSite() {
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

                    TimeOnSiteStats stats = new TimeOnSiteStats();
                    stats.setSessionId(entry.getKey());
                    stats.setTimeOnSite(timeOnSite);
                    return stats;
                })
                .collect(Collectors.toList());
    }
}