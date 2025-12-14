package project;

import java.util.ArrayList;

public class ProgressService {
    public Progress compute(ArrayList<Workout> list) {
        return Progress.from(list);
    }
}