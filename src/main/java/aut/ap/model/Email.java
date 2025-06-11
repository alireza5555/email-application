package aut.ap.model;

import aut.ap.email_behavior.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Email {
    protected static HashMap<String,Email> box = new HashMap<>();
    private static String currentCode = "0";

    private ArrayList<String> recipients;
    private String code ;
    private String subject;
    private LocalDate date;
    private Status status;
    private String sender;
    private String body;

    public Email ( String subject, String sender, String body, ArrayList<String> recipients){
        this.body = body;
        this.sender = sender;
        this.subject = subject;
        this.recipients = recipients;

        codeGenerator();
    }

    private void codeGenerator(){
        int temp = Integer.parseInt(currentCode, 36);
        temp ++ ;
        currentCode = Integer.toString(temp, 36);
        currentCode = String.format("%6s", currentCode).replace(' ', '0');
    }

    public static HashMap<String, Email> getBox() {
        return box;
    }

    public static String getCurrentCode() {
        return currentCode;
    }

    public ArrayList<String> getRecipients() {
        return recipients;
    }

    public String getCode() {
        return code;
    }

    public String getSubject() {
        return subject;
    }

    public LocalDate getDate() {
        return date;
    }

    public Status getStatus() {
        return status;
    }

    public String getSender() {
        return sender;
    }

    public String getBody() {
        return body;
    }


    public static void setCurrentCode(String currentCode) {
        Email.currentCode = currentCode;
    }

    public void setRecipients(ArrayList<String> recipients) {
        this.recipients = recipients;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setBody(String body) {
        this.body = body;
    }
}


