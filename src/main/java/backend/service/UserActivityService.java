package backend.service;

import backend.dto.UserActivityLogRequest;
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

    // Получение статистики по источникам трафика
    public List<Map<String, Object>> getTrafficSources() {
        return userActivityRepository.findAll().stream()
                .filter(activity -> activity.getReferer() != null)
                .collect(Collectors.groupingBy(UserActivity::getReferer))
                .entrySet().stream()
                .map(entry -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("referer", entry.getKey());
                    result.put("visits", entry.getValue().size());
                    return result;
                })
                .collect(Collectors.toList());
    }

    // Получение статистики по времени, проведенному на сайте
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
}