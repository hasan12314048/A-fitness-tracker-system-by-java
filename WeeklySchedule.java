package project;

import java.util.HashMap;

public class WeeklySchedule {
    private HashMap<String, String> byDay;

    public WeeklySchedule() {
        byDay = new HashMap<String, String>();
        byDay.put("MONDAY", "");
        byDay.put("TUESDAY", "");
        byDay.put("WEDNESDAY", "");
        byDay.put("THURSDAY", "");
        byDay.put("FRIDAY", "");
        byDay.put("SATURDAY", "");
        byDay.put("SUNDAY", "");
    }

    public void setDayPlan(String day, String textBlock) {
        byDay.put(day, textBlock);
    }

    public String getDayPlan(String day) {
        String v = byDay.get(day);
        if (v == null) return "";
        return v;
    }

    public HashMap<String, String> getAll() {
        return byDay;
    }
}