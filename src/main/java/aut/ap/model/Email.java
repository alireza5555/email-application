package aut.ap.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "email")
public class Email {

    private static String currentCode = "0";

    @Id
    private String code ;
    private String subject;
    private LocalDate date;
    private String sender;
    private String body;

    public Email ( String subject, String sender, String body){
        this.body = body;
        this.sender = sender;
        this.subject = subject;
        this.date = LocalDate.now();

        codeGenerator();
    }

    public Email (){};

    private void codeGenerator(){
        int temp = Integer.parseInt(currentCode, 36);
        temp ++ ;
        currentCode = Integer.toString(temp, 36);
        currentCode = String.format("%6s", currentCode).replace(' ', '0');
        this.code = currentCode;
    }

    @Override
    public String toString(){
       return sender + " - " + subject + " - (" + code + ")";
    }

    public void showInf(List<String> recipients){
        System.out.println("=================================\n");
        System.out.println("Code: (" + getCode() + ")\nSender: " + getSender() + "\nRecipients: ");
        for (String temp : recipients){
            System.out.print(temp + ", ");
        }
        System.out.println("\nSubject: " + getSubject() + "\nDate: " + getDate());
        System.out.println("\n" + getBody() + "\n");
        System.out.println("=================================");
    }

    public static String getCurrentCode() {
        return currentCode;
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


    public String getSender() {
        return sender;
    }

    public String getBody() {
        return body;
    }


    public static void setCurrentCode(String currentCode) {
        Email.currentCode = currentCode;
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


    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setBody(String body) {
        this.body = body;
    }
}


