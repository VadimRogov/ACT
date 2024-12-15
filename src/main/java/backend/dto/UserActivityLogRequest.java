package backend.dto;

import lombok.Data;

@Data
public class UserActivityLogRequest {
    private String userIp;
    private String sessionId;
    private String pageUrl;
    private String eventType;
    private String eventDetails;
    private String referer;
}