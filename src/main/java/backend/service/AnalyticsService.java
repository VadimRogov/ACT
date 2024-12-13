package backend.service;

import backend.model.UserActivity;
import backend.repository.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final UserActivityRepository userActivityRepository;

    // Получение количества уникальных посетителей
    public long getUniqueVisitorsCount() {
        return userActivityRepository.findAll().stream()
                .map(UserActivity::getUserIp)
                .distinct()
                .count();
    }

    // Получение источников трафика
    public List<Map<String, Object>> getTrafficSources() {
        return userActivityRepository.findAll().stream()
                .collect(Collectors.groupingBy(UserActivity::getUserIp))
                .entrySet().stream()
                .map(entry -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("userIp", entry.getKey());
                    result.put("visits", entry.getValue().size());
                    return result;
                })
                .collect(Collectors.toList());
    }

    // Получение времени, проведенного на сайте
    public List<Map<String, Object>> getTimeOnSite() {
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

                    Map<String, Object> result = new HashMap<>();
                    result.put("sessionId", entry.getKey());
                    result.put("timeOnSite", timeOnSite);
                    return result;
                })
                .collect(Collectors.toList());
    }

    // Получение популярных страниц
    public List<Map<String, Object>> getPopularPages() {
        return userActivityRepository.findAll().stream()
                .filter(activity -> "view".equals(activity.getEventType()))
                .collect(Collectors.groupingBy(UserActivity::getPageUrl))
                .entrySet().stream()
                .map(entry -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("pageUrl", entry.getKey());
                    result.put("views", entry.getValue().size());
                    return result;
                })
                .collect(Collectors.toList());
    }

    // Получение взаимодействий с интерактивными элементами
    public List<Map<String, Object>> getInteractiveElementInteractions() {
        return userActivityRepository.findAll().stream()
                .filter(activity -> "click".equals(activity.getEventType()))
                .collect(Collectors.groupingBy(UserActivity::getEventDetails))
                .entrySet().stream()
                .map(entry -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("element", entry.getKey());
                    result.put("clicks", entry.getValue().size());
                    return result;
                })
                .collect(Collectors.toList());
    }
}