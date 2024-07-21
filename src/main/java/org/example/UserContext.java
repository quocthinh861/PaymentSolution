package org.example;

public class UserContext {
    private static UserContext instance;
    private int userId;

    private UserContext() {}

    public static UserContext getInstance() {
        if (instance == null) {
            instance = new UserContext();
        }
        return instance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static void switchUser(int newUserId) {
        getInstance().setUserId(newUserId);
    }
}
