package edu.westga.cs3211.pirate_ship_inventory_manager.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Authenticator {
	private final Map<String, User> user = new HashMap<>();

    public Authenticator() {
        this.user.put("crewmate1", new User("crewmate1", "pass1", Roles.CREWMATE));
        this.user.put("quarter1", new User("quarter1", "pass2", Roles.QUARTERMASTER));
        this.user.put("cook1", new User("cook1", "pass3", Roles.COOK));
    }

    public boolean verifyCredentials(String username, String password) {
        if (!this.user.containsKey(username)) {
            return false;
        }
        User foundUser = this.user.get(username);
        return foundUser.getPassword().equals(password);
    }

    public User getUser(String username) {
        return this.user.get(username);
    }
  
}
