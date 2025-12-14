package project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WorkoutService {
    private FileDB db;
    private String username;
    private ArrayList<Workout> cache;

    public WorkoutService(FileDB db, String username) throws DataAccessException {
        this.db = db;
        this.username = username;
        this.cache = db.loadWorkouts(username);
    }

    public void addWorkout(Workout w) throws ValidationException, DataAccessException {
        if (w.getName() == null || w.getName().trim().isEmpty())
            throw new ValidationException("Workout name required");
        if (w.getDurationMinutes() <= 0)
            throw new ValidationException("Duration must be positive");
        if (w.getDayOfWeek() == null || w.getDayOfWeek().trim().isEmpty())
            throw new ValidationException("Day of week required");
        cache.add(w);
        saveAll();
    }

    public void removeWorkout(Workout w) throws DataAccessException {
        cache.remove(w);
        saveAll();
    }

    public void saveAll() throws DataAccessException {
        db.saveWorkouts(username, cache);
    }

    public List<Workout> getHistory() {
        Collections.sort(cache, Comparator.comparing(Workout::getDate).reversed());
        return cache;
    }
}