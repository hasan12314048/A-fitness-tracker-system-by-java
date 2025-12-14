package project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WorkoutsPanel extends JPanel {
    private JComboBox<String> type = new JComboBox<>(new String[]{"Cardio", "Strength"});
    private JTextField name = new JTextField(12);
    private JTextField date = new JTextField(10);
    private JComboBox<String> day = new JComboBox<>(new String[]{
            "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY",
            "FRIDAY", "SATURDAY", "SUNDAY"
    });
    private JSpinner duration = new JSpinner(new SpinnerNumberModel(30, 1, 600, 1));

    private JSpinner distance = new JSpinner(new SpinnerNumberModel(2.0, 0.0, 200.0, 0.1));
    private JSpinner avgHr = new JSpinner(new SpinnerNumberModel(130.0, 60.0, 220.0, 1.0));

    private JSpinner sets = new JSpinner(new SpinnerNumberModel(3, 1, 50, 1));
    private JSpinner reps = new JSpinner(new SpinnerNumberModel(10, 1, 200, 1));
    private JSpinner weight = new JSpinner(new SpinnerNumberModel(20.0, 0.0, 500.0, 1.0));

    private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final Color BG = new Color(245, 245, 250);
    private static final Color CARD = new Color(230, 230, 240);
    private static final Color BORDER = new Color(200, 200, 210);
    private static final Color TEXT_PRIMARY = new Color(40, 40, 70);
    private static final Color TEXT_SECONDARY = new Color(60, 60, 90);
    private static final Color ACTION_GREEN = new Color(0, 153, 102);

    public WorkoutsPanel(WorkoutService ws, DashboardPanel dp, HistoryPanel hp) {
        setLayout(new BorderLayout(12, 12));
        setBackground(BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel header = new JLabel("🏋️ Workouts");
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.setForeground(TEXT_PRIMARY);
        add(header, BorderLayout.NORTH);

        JLabel lblType = new JLabel("Type:");
        JLabel lblName = new JLabel("Name:");
        JLabel lblDate = new JLabel("Date (yyyy-MM-dd):");
        JLabel lblDay = new JLabel("Day of Week:");
        JLabel lblDuration = new JLabel("Duration (min):");
        JLabel lblDistance = new JLabel("Distance (km):");
        JLabel lblAvgHr = new JLabel("Avg Heart Rate:");
        JLabel lblSets = new JLabel("Sets:");
        JLabel lblReps = new JLabel("Reps/set:");
        JLabel lblWeight = new JLabel("Weight (kg):");

        for (JLabel l : new JLabel[]{lblType, lblName, lblDate, lblDay, lblDuration, lblDistance, lblAvgHr, lblSets, lblReps, lblWeight}) {
            l.setFont(new Font("SansSerif", Font.BOLD, 14));
            l.setForeground(TEXT_SECONDARY);
        }

        styleTextField(name);
        styleTextField(date);
        styleCombo(type);
        styleCombo(day);
        styleSpinner(duration, "#0");
        styleSpinner(distance, "#0.0");
        styleSpinner(avgHr, "#0");
        styleSpinner(sets, "#0");
        styleSpinner(reps, "#0");
        styleSpinner(weight, "#0.0");

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setOpaque(false);

        form.add(lblType);     
        form.add(type);
        form.add(lblName);     
        form.add(name);
        form.add(lblDate);     
        form.add(date);
        form.add(lblDay);      
        form.add(day);
        form.add(lblDuration); 
        form.add(duration);
        form.add(lblDistance); 
        form.add(distance);
        form.add(lblAvgHr);    
        form.add(avgHr);
        form.add(lblSets);     
        form.add(sets);
        form.add(lblReps);     
        form.add(reps);
        form.add(lblWeight);   
        form.add(weight);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(16, 16, 16, 16)
        ));
        card.add(form, BorderLayout.CENTER);

        add(card, BorderLayout.CENTER);

        JButton addBtn = new JButton("Add Workout");
        stylePrimaryButton(addBtn);
        JPanel south = new JPanel();
        south.setBackground(BG);
        south.add(addBtn);
        add(south, BorderLayout.SOUTH);

        date.setText(LocalDate.now().format(ISO));
        updateDayFromDate();
        day.setEnabled(false);

        toggleFields();
        type.addActionListener(e -> toggleFields());
        date.getDocument().addDocumentListener(new SimpleDocListener(this::updateDayFromDate));

        addBtn.addActionListener(e -> {
            try {
                String t = (String) type.getSelectedItem();
                String n = name.getText().trim();
                LocalDate ld = LocalDate.parse(date.getText().trim(), ISO);
                String d = ld.format(ISO);
                String dow = ld.getDayOfWeek().name();
                int mins = ((Number) duration.getValue()).intValue();

                Workout w;
                if ("Cardio".equals(t)) {
                    double km = ((Number) distance.getValue()).doubleValue();
                    double hr = ((Number) avgHr.getValue()).doubleValue();
                    w = new CardioWorkout(n, d, dow, mins, km, hr);
                } else {
                    int s = ((Number) sets.getValue()).intValue();
                    int r = ((Number) reps.getValue()).intValue();
                    double kg = ((Number) weight.getValue()).doubleValue();
                    w = new StrengthWorkout(n, d, dow, mins, s, r, kg);
                }

                ws.addWorkout(w);
                JOptionPane.showMessageDialog(this, "Added: " + w.getType() + " - " + w.getName());
                dp.refresh();
                hp.refresh();

                name.setText("");
                duration.setValue(30);
                distance.setValue(2.0);
                avgHr.setValue(130.0);
                sets.setValue(3);
                reps.setValue(10);
                weight.setValue(20.0);

            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid", JOptionPane.WARNING_MESSAGE);
            } catch (DataAccessException ex) {
                JOptionPane.showMessageDialog(this, "Save failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Unexpected: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void toggleFields() {
        boolean isCardio = "Cardio".equals(type.getSelectedItem());
        distance.setEnabled(isCardio);
        avgHr.setEnabled(isCardio);
        sets.setEnabled(!isCardio);
        reps.setEnabled(!isCardio);
        weight.setEnabled(!isCardio);
    }

    private void updateDayFromDate() {
        try {
            LocalDate d = LocalDate.parse(date.getText().trim(), ISO);
            day.setSelectedItem(d.getDayOfWeek().name());
        } catch (Exception ignore) {
        }
    }

    private void styleTextField(JTextField tf) {
        tf.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tf.setBackground(CARD);
        tf.setForeground(TEXT_PRIMARY);
        tf.setCaretColor(TEXT_PRIMARY);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(6, 10, 6, 10)
        ));
    }

    private void styleCombo(JComboBox<?> cb) {
        cb.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cb.setBackground(CARD);
        cb.setForeground(TEXT_PRIMARY);
        cb.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(2, 6, 2, 6)
        ));
    }

    private void styleSpinner(JSpinner sp, String pattern) {
        sp.setFont(new Font("SansSerif", Font.PLAIN, 14));
        try {
            sp.setEditor(new JSpinner.NumberEditor(sp, pattern));
        } catch (Exception ignore) { }

        JComponent editor = sp.getEditor();
        if (editor instanceof JSpinner.DefaultEditor de) {
            JFormattedTextField tf = de.getTextField();
            tf.setFont(new Font("Monospaced", Font.PLAIN, 14));
            tf.setBackground(CARD);
            tf.setForeground(TEXT_PRIMARY);
            tf.setCaretColor(TEXT_PRIMARY);
            tf.setBorder(new EmptyBorder(6, 10, 6, 10));
        }
        sp.setBorder(BorderFactory.createLineBorder(BORDER));
    }

    private void stylePrimaryButton(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(ACTION_GREEN);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 24, 10, 24));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static class SimpleDocListener implements javax.swing.event.DocumentListener {
        private final Runnable onChange;
        SimpleDocListener(Runnable onChange) { 
            this.onChange = onChange; 
        }
        public void insertUpdate(javax.swing.event.DocumentEvent e) { 
            onChange.run(); 
        }
        public void removeUpdate(javax.swing.event.DocumentEvent e) { 
            onChange.run(); 
        }
        public void changedUpdate(javax.swing.event.DocumentEvent e) { 
            onChange.run(); 
        }
    }
}