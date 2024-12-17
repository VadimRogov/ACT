package backend.dto.analytics;

import lombok.Data;

public class TrafficSourceStats {
    private String source; // Источник трафика
    private int visits; // Количество визитов

    public TrafficSourceStats() {

    }

    public TrafficSourceStats(String source, int visits) {
        this.source = source;
        this.visits = visits;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }
}