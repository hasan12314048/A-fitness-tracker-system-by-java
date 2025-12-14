package project;

import java.io.Serializable;

public abstract class Workout implements Serializable, CalorieComputable {
    protected String type;    
    protected String name;      
    protected String date;
    protected String dayOfWeek;
    protected int durationMinutes;

    public Workout(String type, String name, String date, String dayOfWeek, int durationMinutes) {
        this.type = type;
        this.name = name;
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.durationMinutes = durationMinutes;
    }

    public String getType() { 
        return type; 
    }
    public String getName() { 
        return name; 
    }
    public String getDate() { 
        return date; 
    }
    public String getDayOfWeek() { 
        return dayOfWeek; 
    }
    public int getDurationMinutes() { 
        return durationMinutes; 
    }

    public abstract double calculateCalories();

    public String toRow() {
        return type + "," + name + "," + date + "," + dayOfWeek + "," + durationMinutes;
    }
}