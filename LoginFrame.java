package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    private JTextField username = new JTextField(15);
    private JPasswordField password = new JPasswordField(15);
    private JButton loginBtn = new JButton("Login");
    private JButton registerBtn = new JButton("Register");
    private AuthService auth;

    public LoginFrame() {
        super("FitTrack - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 200);
        setLocationRelativeTo(null);

        try {
            auth = new AuthService();
        } catch (DataAccessException e) {
            JOptionPane.showMessageDialog(this, "Storage error: " + e.getMessage());
            System.exit(1);
        }

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        form.add(new JLabel("Username:"));
        form.add(username);
        form.add(new JLabel("Password:"));
        form.add(password);
        form.add(loginBtn);
        form.add(registerBtn);
        add(form);

        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        username.setFont(fieldFont);
        password.setFont(fieldFont);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        getRootPane().setDefaultButton(loginBtn);

        loginBtn.addActionListener(e -> onLogin());
        registerBtn.addActionListener(e -> onRegister());
    }

    private void onLogin() {
        String u = username.getText().trim();
        String p = new String(password.getPassword());

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            auth.login(u, p);
            new MainFrame(auth, u).setVisible(true);
            dispose();
        } catch (AuthenticationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Login Failed", JOptionPane.WARNING_MESSAGE);
        } catch (DataAccessException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void onRegister() {
        String u = username.getText().trim();
        String p = new String(password.getPassword());

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            auth.register(u, p);
            JOptionPane.showMessageDialog(this,
                    "Registered successfully. You can log in now.");
        } catch (AuthenticationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Registration Failed", JOptionPane.WARNING_MESSAGE);
        } catch (DataAccessException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}