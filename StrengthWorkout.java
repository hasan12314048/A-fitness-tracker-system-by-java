package project;

public class StrengthWorkout extends Workout {
    private int sets;
    private int repsPerSet;
    private double weightKg;

    public StrengthWorkout(String name, String date, String dayOfWeek, int durationMinutes, int sets, int repsPerSet, double weightKg) {
        super("Strength", name, date, dayOfWeek, durationMinutes);
        this.sets = sets;
        this.repsPerSet = repsPerSet;
        this.weightKg = weightKg;
    }

    public int getSets() { 
        return sets; 
    }
    public int getRepsPerSet() { 
        return repsPerSet; 
    }
    public double getWeightKg() { 
        return weightKg; 
    }

    public double calculateCalories() {
        double base = 4.5 * durationMinutes;
        double volume = sets * repsPerSet * weightKg * 0.015;
        return base + volume;
    }

    @Override
    public String toRow() {
        return super.toRow() + "," + sets + "," + repsPerSet + "," + weightKg;
    }
}