package project;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private WorkoutService workoutService;
    private ProgressService progressService;
    private JLabel caloriesLabel = new JLabel();
    private JLabel minutesLabel = new JLabel();
    private JLabel countLabel = new JLabel();

    public DashboardPanel(WorkoutService ws, ProgressService ps) {
        this.workoutService = ws;
        this.progressService = ps;

        setLayout(new GridLayout(3, 1, 10, 10));
        setBackground(new Color(245, 245, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel header = new JLabel("📊 Dashboard");
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.setForeground(new Color(40, 40, 70));
        add(header);

        styleLabel(caloriesLabel);
        styleLabel(minutesLabel);
        styleLabel(countLabel);

        add(caloriesLabel);
        add(minutesLabel);
        add(countLabel);

        refresh();
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setForeground(new Color(60, 60, 90));
        label.setOpaque(true);
        label.setBackground(new Color(230, 230, 240));
        label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
    }

    public void refresh() {
        Progress p = progressService.compute(new java.util.ArrayList<>(workoutService.getHistory()));
        caloriesLabel.setText("Total Calories Burned: " + p.getTotalCalories());
        minutesLabel.setText("Total Duration (minutes): " + p.getTotalMinutes());
        countLabel.setText("Number of Workouts: " + p.getWorkoutsCount());
    }
}