package project;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

public class ScheduleService {
    private FileDB db;
    private String username;

    public ScheduleService(FileDB db, String username) {
        this.db = db;
        this.username = username;
    }

    public WeeklySchedule load() throws DataAccessException {
        return db.loadSchedule(username);
    }

    public void save(WeeklySchedule s) throws DataAccessException {
        db.saveSchedule(username, s);
    }

    public double adherencePercent(WeeklySchedule s, List<Workout> history) {
        int planned = 0;
        planned += countLines(s.getDayPlan("MONDAY"));
        planned += countLines(s.getDayPlan("TUESDAY"));
        planned += countLines(s.getDayPlan("WEDNESDAY"));
        planned += countLines(s.getDayPlan("THURSDAY"));
        planned += countLines(s.getDayPlan("FRIDAY"));
        planned += countLines(s.getDayPlan("SATURDAY"));
        planned += countLines(s.getDayPlan("SUNDAY"));

        int actual = (history == null) ? 0 : history.size();
        if (planned <= 0) return 100.0;
        if (actual > planned) actual = planned;
        return (100.0 * actual) / planned;
    }

    private int countLines(String block) {
        if (block == null || block.trim().length() == 0) return 0;
        String[] lines = block.split("\\r?\\n");
        int c = 0;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().length() > 0) c++;
        }
        return c;
    }
}