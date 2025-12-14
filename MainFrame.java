package project;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(AuthService auth, String username) {
        super("FitTrack - " + username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        try {
            WorkoutService workoutService = new WorkoutService(auth.getDb(), username);
            ProgressService progressService = new ProgressService();
            ScheduleService scheduleService = new ScheduleService(auth.getDb(), username);

            DashboardPanel dashboardPanel = new DashboardPanel(workoutService, progressService);
            HistoryPanel historyPanel = new HistoryPanel(workoutService, dashboardPanel);

            WorkoutsPanel workoutsPanel = new WorkoutsPanel(workoutService, dashboardPanel, historyPanel);
            SchedulePanel schedulePanel = new SchedulePanel(scheduleService, workoutService);

            JTabbedPane tabs = new JTabbedPane();
            tabs.add("Dashboard", dashboardPanel);
            tabs.add("Workouts", workoutsPanel);
            tabs.add("History", historyPanel);
            tabs.add("Schedule", schedulePanel);

            add(tabs, BorderLayout.CENTER);

        } catch (DataAccessException e) {
            JOptionPane.showMessageDialog(this,
                    "Initialization error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Unexpected error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }
}