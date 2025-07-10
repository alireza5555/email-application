package aut.ap.framework;

public class UserSession {
    private static String currentUserEmail;

    public static void set(String email) {
        currentUserEmail = email;
    }

    public static String get() {
        return currentUserEmail;
    }
}