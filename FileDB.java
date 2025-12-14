package project;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileDB {
    private File dataDir;

    public FileDB(String dir) {
        dataDir = new File(dir);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    public synchronized void saveUser(String username, String password) throws DataAccessException {
        File f = new File(dataDir, "users.txt");
        try {
            FileWriter fw = new FileWriter(f, true);
            fw.write(username + "," + password + "\n");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            throw new DataAccessException("Failed to save user", e);
        }
    }

    public synchronized boolean userExists(String username) throws DataAccessException {
        File f = new File(dataDir, "users.txt");
        if (!f.exists()) return false;
        Scanner sc = null;
        try {
            sc = new Scanner(new FileReader(f));
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.length() == 0) continue;
                String[] parts = line.split(",", -1);
                if (parts.length >= 2 && username.equals(parts[0])) {
                    sc.close();
                    return true;
                }
            }
            sc.close();
            return false;
        } catch (IOException e) {
            if (sc != null) sc.close();
            throw new DataAccessException("Failed to read users", e);
        }
    }

    public synchronized boolean checkLogin(String username, String password) throws DataAccessException {
        File f = new File(dataDir, "users.txt");
        if (!f.exists()) return false;
        Scanner sc = null;
        try {
            sc = new Scanner(new FileReader(f));
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.length() == 0) continue;
                String[] parts = line.split(",", -1);
                if (parts.length >= 2) {
                    if (username.equals(parts[0]) && password.equals(parts[1])) {
                        sc.close();
                        return true;
                    }
                }
            }
            sc.close();
            return false;
        } catch (IOException e) {
            if (sc != null) sc.close();
            throw new DataAccessException("Failed to read users", e);
        }
    }

    public synchronized void saveWorkouts(String username, ArrayList<Workout> list) throws DataAccessException {
        File f = new File(dataDir, "workouts_" + username + ".txt");
        FileWriter fw = null;
        try {
            fw = new FileWriter(f, false);
            for (int i = 0; i < list.size(); i++) {
                fw.write(list.get(i).toRow() + "\n");
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            if (fw != null) try { fw.close(); } catch (IOException ex) {}
            throw new DataAccessException("Failed to save workouts", e);
        }
    }

    public synchronized ArrayList<Workout> loadWorkouts(String username) throws DataAccessException {
        ArrayList<Workout> out = new ArrayList<Workout>();
        File f = new File(dataDir, "workouts_" + username + ".txt");
        if (!f.exists()) return out;
        Scanner sc = null;
        try {
            sc = new Scanner(new FileReader(f));
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.length() == 0) continue;
                String[] p = line.split(",", -1);
                if (p.length >= 5) {
                    String type = p[0];
                    if ("Cardio".equals(type) && p.length >= 7) {
                        Workout w = new CardioWorkout(p[1], p[2], p[3], parseInt(p[4]), parseDouble(p[5]), parseDouble(p[6]));
                        out.add(w);
                    } else if ("Strength".equals(type) && p.length >= 8) {
                        Workout w = new StrengthWorkout(p[1], p[2], p[3], parseInt(p[4]), parseInt(p[5]), parseInt(p[6]), parseDouble(p[7]));
                        out.add(w);
                    }
                }
            }
            sc.close();
            return out;
        } catch (IOException e) {
            if (sc != null) sc.close();
            throw new DataAccessException("Failed to load workouts", e);
        }
    }

    public synchronized void saveSchedule(String username, WeeklySchedule sched) throws DataAccessException {
        File f = new File(dataDir, "schedule_" + username + ".txt");
        FileWriter fw = null;
        try {
            fw = new FileWriter(f, false);
            fw.write("MONDAY:" + escape(sched.getDayPlan("MONDAY")) + "\n");
            fw.write("TUESDAY:" + escape(sched.getDayPlan("TUESDAY")) + "\n");
            fw.write("WEDNESDAY:" + escape(sched.getDayPlan("WEDNESDAY")) + "\n");
            fw.write("THURSDAY:" + escape(sched.getDayPlan("THURSDAY")) + "\n");
            fw.write("FRIDAY:" + escape(sched.getDayPlan("FRIDAY")) + "\n");
            fw.write("SATURDAY:" + escape(sched.getDayPlan("SATURDAY")) + "\n");
            fw.write("SUNDAY:" + escape(sched.getDayPlan("SUNDAY")) + "\n");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            if (fw != null) try { fw.close(); } catch (IOException ex) {}
            throw new DataAccessException("Failed to save schedule", e);
        }
    }

    public synchronized WeeklySchedule loadSchedule(String username) throws DataAccessException {
        WeeklySchedule s = new WeeklySchedule();
        File f = new File(dataDir, "schedule_" + username + ".txt");
        if (!f.exists()) return s;
        Scanner sc = null;
        try {
            sc = new Scanner(new FileReader(f));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                int idx = line.indexOf(':');
                if (idx > 0) {
                    String day = line.substring(0, idx);
                    String data = line.substring(idx + 1);
                    s.setDayPlan(day, unescape(data));
                }
            }
            sc.close();
            return s;
        } catch (IOException e) {
            if (sc != null) sc.close();
            throw new DataAccessException("Failed to load schedule", e);
        }
    }

    private int parseInt(String s) {
        try { return Integer.parseInt(s.trim()); } 
        catch (Exception e) { return 0; }
    }
    private double parseDouble(String s) {
        try { return Double.parseDouble(s.trim()); } 
        catch (Exception e) { return 0.0; }
    }
    private String escape(String s) { 
        if (s == null) return ""; return s.replace("\n", "\\n"); 
    }
    private String unescape(String s) { 
        if (s == null) return ""; return s.replace("\\n", "\n"); 
    }
}