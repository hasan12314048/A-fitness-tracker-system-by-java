package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HistoryPanel extends JPanel {
    private WorkoutService workoutService;
    private DashboardPanel dashboardPanel;
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> workoutList = new JList<>(listModel);

    public HistoryPanel(WorkoutService ws, DashboardPanel dp) {
        this.workoutService = ws;
        this.dashboardPanel = dp;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel header = new JLabel("📜 Workout History");
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.setForeground(new Color(40, 40, 70));
        add(header, BorderLayout.NORTH);

        workoutList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        workoutList.setBackground(new Color(230, 230, 240));
        workoutList.setSelectionBackground(new Color(0, 153, 102));
        workoutList.setSelectionForeground(Color.WHITE);
        add(new JScrollPane(workoutList), BorderLayout.CENTER);

        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        deleteBtn.setBackground(new Color(200, 50, 50));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        deleteBtn.addActionListener(this::onDelete);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(getBackground());
        buttonPanel.add(deleteBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        refresh();
    }

    public void refresh() {
        listModel.clear();
        for (Workout w : workoutService.getHistory()) {
            listModel.addElement(w.getDate() + " - " + w.getName() + " (" + w.getDurationMinutes() + " mins)");
        }
    }

    private void onDelete(ActionEvent e) {
        int idx = workoutList.getSelectedIndex();
        if (idx >= 0) {
            try {
                Workout selected = workoutService.getHistory().get(idx);
                workoutService.removeWorkout(selected);
                refresh();
                dashboardPanel.refresh();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Delete failed: " + ex.getMessage());
            }
        }
    }
}