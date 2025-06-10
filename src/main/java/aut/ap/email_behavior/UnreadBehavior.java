package aut.ap.email_behavior;

public class UnreadBehavior implements Status{
    @Override
    public String getStatus() {
        return "unread";
    }
}
