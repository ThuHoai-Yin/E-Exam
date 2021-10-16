package model;

public class Authentication {
    private int id;
    private String username;
    private String role;

    public Authentication(int id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    
    
}
