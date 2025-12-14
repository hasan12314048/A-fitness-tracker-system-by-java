package project;

import java.util.ArrayList;

public class Progress {
    private double totalCalories;
    private int totalMinutes;
    private int workoutsCount;

    public double getTotalCalories() { 
        return totalCalories; 
    }
    public int getTotalMinutes() { 
        return totalMinutes; 
    }
    public int getWorkoutsCount() { 
        return workoutsCount; 
    }

    public static Progress from(ArrayList<Workout> list) {
        Progress p = new Progress();
        p.totalCalories = 0.0;
        p.totalMinutes = 0;
        p.workoutsCount = list.size();
        for (int i = 0; i < list.size(); i++) {
            Workout w = list.get(i);
            p.totalCalories += w.calculateCalories();
            p.totalMinutes += w.getDurationMinutes();
        }
        return p;
    }
}