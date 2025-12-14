package project;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HistoryService {

    private final File file;

    public HistoryService(File file) {
        this.file = file;
    }

    public List<WorkoutEntry> load() throws IOException {
        List<WorkoutEntry> list = new ArrayList<WorkoutEntry>();
        if (!file.exists()) return list;

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                WorkoutEntry e = fromCsv(line);
                if (e != null) list.add(e);
            }
        } finally {
            if (br != null) br.close();
        }
        return list;
    }

    public void save(List<WorkoutEntry> entries) throws IOException {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(file, false));
            for (int i = 0; i < entries.size(); i++) {
                out.println(toCsv(entries.get(i)));
            }
        } finally {
            if (out != null) out.close();
        }
    }

    private String toCsv(WorkoutEntry e) {
        return e.getDate().toString() + "," +
               escape(e.getWorkout()) + "," +
               e.getDurationMinutes() + "," +
               e.getCalories();
    }

    private WorkoutEntry fromCsv(String line) {
        try {
            String[] p = line.split(",", -1);
            if (p.length < 4) return null;
            LocalDate date = LocalDate.parse(p[0].trim());
            String workout = p[1].trim();
            int duration = Integer.parseInt(p[2].trim());
            int calories = Integer.parseInt(p[3].trim());
            return new WorkoutEntry(date, workout, duration, calories);
        } catch (Exception ex) {
            return null;
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return s;
    }
}