package project;

public class User {
    private String username;
    private String password;

    public User(String u, String p) {
        this.username = u;
        this.password = p;
    }

    public String getUsername() { 
        return username; 
    }
    public boolean checkPassword(String p) { 
        return password.equals(p); 
    }
}