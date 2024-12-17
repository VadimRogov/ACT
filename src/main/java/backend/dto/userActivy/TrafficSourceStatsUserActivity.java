package backend.dto.userActivy;


public class TrafficSourceStatsUserActivity {
    private String referer; // Источник трафика
    private int visits; // Количество визитов

    public TrafficSourceStatsUserActivity() {

    }

    public TrafficSourceStatsUserActivity(String referer, int visits) {
        this.referer = referer;
        this.visits = visits;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }
}
