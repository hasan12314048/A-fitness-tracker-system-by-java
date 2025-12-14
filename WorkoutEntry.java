package project;

import java.time.LocalDate;

public class WorkoutEntry {
    private LocalDate date;
    private String workout;
    private int durationMinutes;
    private int calories;

    public WorkoutEntry(LocalDate date, String workout, int durationMinutes, int calories) {
        this.date = date;
        this.workout = workout;
        this.durationMinutes = durationMinutes;
        this.calories = calories;
    }

    public LocalDate getDate() { 
        return date; 
    }
    public String getWorkout() { 
        return workout; 
    }
    public int getDurationMinutes() { 
        return durationMinutes; 
    }
    public int getCalories() { 
        return calories; 
    }

    public void setDate(LocalDate date) { 
        this.date = date; 
    }
    public void setWorkout(String workout) { 
        this.workout = workout; 
    }
    public void setDurationMinutes(int durationMinutes) { 
        this.durationMinutes = durationMinutes; 
    }
    public void setCalories(int calories) { 
        this.calories = calories; 
    }
}