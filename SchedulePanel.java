package project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SchedulePanel extends JPanel {
    private JTextArea mon = area();
    private JTextArea tue = area();
    private JTextArea wed = area();
    private JTextArea thu = area();
    private JTextArea fri = area();
    private JTextArea sat = area();
    private JTextArea sun = area();
    private JLabel adherence = new JLabel("Adherence: —");

    private static final Color BG = new Color(245, 245, 250);
    private static final Color CARD = new Color(230, 230, 240);
    private static final Color BORDER = new Color(200, 200, 210);
    private static final Color TEXT_PRIMARY = new Color(40, 40, 70);
    private static final Color TEXT_SECONDARY = new Color(60, 60, 90);
    private static final Color ACTION_GREEN = new Color(0, 153, 102);

    public SchedulePanel(final ScheduleService ss, final WorkoutService ws) {
        setLayout(new BorderLayout(12, 12));
        setBackground(BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel header = new JLabel("📅 Weekly Schedule");
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.setForeground(TEXT_PRIMARY);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(BG);
        top.add(header, BorderLayout.WEST);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setBackground(BG);
        JButton save = new JButton("Save Schedule");
        stylePrimaryButton(save);
        JButton eval = new JButton("Evaluate Adherence");
        stylePrimaryButton(eval);
        buttons.add(save);
        buttons.add(eval);
        top.add(buttons, BorderLayout.EAST);

        JPanel grid = new JPanel(new GridLayout(0, 2, 10, 10));
        grid.setOpaque(false);

        grid.add(dayCard("MONDAY", mon));
        grid.add(dayCard("TUESDAY", tue));
        grid.add(dayCard("WEDNESDAY", wed));
        grid.add(dayCard("THURSDAY", thu));
        grid.add(dayCard("FRIDAY", fri));
        grid.add(dayCard("SATURDAY", sat));
        grid.add(dayCard("SUNDAY", sun));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(16, 16, 16, 16)
        ));
        card.add(grid, BorderLayout.CENTER);

        adherence.setFont(new Font("SansSerif", Font.BOLD, 14));
        adherence.setForeground(TEXT_SECONDARY);
        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(BG);
        south.add(adherence, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(card, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        try {
            WeeklySchedule s = ss.load();
            mon.setText(s.getDayPlan("MONDAY"));
            tue.setText(s.getDayPlan("TUESDAY"));
            wed.setText(s.getDayPlan("WEDNESDAY"));
            thu.setText(s.getDayPlan("THURSDAY"));
            fri.setText(s.getDayPlan("FRIDAY"));
            sat.setText(s.getDayPlan("SATURDAY"));
            sun.setText(s.getDayPlan("SUNDAY"));
        } catch (DataAccessException e) {
            JOptionPane.showMessageDialog(this, "Load schedule failed: " + e.getMessage());
        }

        save.addActionListener((ActionEvent e) -> {
            WeeklySchedule s = new WeeklySchedule();
            s.setDayPlan("MONDAY", mon.getText());
            s.setDayPlan("TUESDAY", tue.getText());
            s.setDayPlan("WEDNESDAY", wed.getText());
            s.setDayPlan("THURSDAY", thu.getText());
            s.setDayPlan("FRIDAY", fri.getText());
            s.setDayPlan("SATURDAY", sat.getText());
            s.setDayPlan("SUNDAY", sun.getText());
            try {
                ss.save(s);
                JOptionPane.showMessageDialog(SchedulePanel.this, "Schedule saved.");
            } catch (DataAccessException ex) {
                JOptionPane.showMessageDialog(SchedulePanel.this, "Save failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        eval.addActionListener((ActionEvent e) -> {
            try {
                WeeklySchedule s = new WeeklySchedule();
                s.setDayPlan("MONDAY", mon.getText());
                s.setDayPlan("TUESDAY", tue.getText());
                s.setDayPlan("WEDNESDAY", wed.getText());
                s.setDayPlan("THURSDAY", thu.getText());
                s.setDayPlan("FRIDAY", fri.getText());
                s.setDayPlan("SATURDAY", sat.getText());
                s.setDayPlan("SUNDAY", sun.getText());
                double pct = ss.adherencePercent(s, ws.getHistory());
                adherence.setText(String.format("Adherence: %.1f%%", pct));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(SchedulePanel.this, "Error: " + ex.getMessage());
            }
        });
    }

    private JPanel dayCard(String title, JTextArea ta) {
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setForeground(TEXT_SECONDARY);
        lbl.setBorder(new EmptyBorder(0, 2, 4, 0));

        JScrollPane sp = new JScrollPane(ta);
        sp.setBorder(BorderFactory.createLineBorder(BORDER));
        sp.setBackground(Color.WHITE);
        sp.getViewport().setBackground(Color.WHITE);
        sp.getHorizontalScrollBar().setBackground(CARD);
        sp.getVerticalScrollBar().setBackground(CARD);


        ta.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.setOpaque(false);
        p.add(lbl, BorderLayout.NORTH);
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    private static JTextArea area() {
        JTextArea ta = new JTextArea(4, 20);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setFont(new Font("SansSerif", Font.PLAIN, 14));
        ta.setForeground(TEXT_SECONDARY);
        ta.setBackground(Color.WHITE);
        ta.setBorder(new EmptyBorder(8, 8, 8, 8));
        return ta;
    }

    private void stylePrimaryButton(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(ACTION_GREEN);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 20, 8, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}