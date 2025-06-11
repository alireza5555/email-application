package aut.ap.model;

import aut.ap.framework.SingletonSessionFactory;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.persistence.Id;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Service {

    public static String submit (String name, String lastName,String password, int age, String email){

        SingletonSessionFactory.get().inTransaction(session -> session.persist(new User(email,age,lastName,name,password)));
        return "Your new account is created.\nGo ahead and login!";
    }

    private static boolean emailCheck (String email){
        email = normalizeEmail(email);
            List<String> allUsers = SingletonSessionFactory.get().fromTransaction(session -> session.createNativeQuery("SELECT email FROM user").getResultList());
        for (String temp : allUsers){
            if(temp.equals(email)) return true;
        }
        return false;

    }

    public static void singUp(){
        Scanner scn = new Scanner(System.in);
        System.out.println("enter your  first name");
        String name = scn.nextLine();

        System.out.println("enter your last name");
        String lastName = scn.nextLine();

        System.out.println("enter your password");
        String password = scn.nextLine();
        if(password.length() < 8) throw new IllegalArgumentException("Weak password");

        System.out.println("enter your age");
        int age = scn.nextInt();
        scn.nextLine();

        System.out.println("enter your email");
        String email = scn.nextLine();
        if(emailCheck(email)) throw new IllegalArgumentException("An account with this email already exists");

       submit(name, lastName,password,age,email);
    }


    public static User login()throws NoResultException {
        Scanner scn = new Scanner(System.in);
        System.out.println("enter your email");
        String userName = scn.nextLine();
        userName = normalizeEmail(userName);
        System.out.println("enter your password");
        String password = scn.nextLine();

        String finalUserName = userName;
        User user = SingletonSessionFactory.get().fromTransaction(session -> session.createNativeQuery("SELECT * FROM user " + "WHERE email = :userName", User.class).setParameter("userName", finalUserName).getSingleResult());

        if (user.getPassword().equals(password)) {
            System.out.println("Welcome, " + user.getName() + " " + user.getLastName() + "!");
            return user;
        }
        else throw new IllegalArgumentException("Wrong password");
    }

    public static String normalizeEmail(String email){
        email = email.trim();
        if(email.contains("@")) {
            if(email.endsWith("@milou.com"))  return email;
            throw new IllegalArgumentException("Your domain must be @milou.com");
           }

        else {
            return email + "@milou.com";
        }
    }

    public static void sendEmail(User user){
        Scanner scn = new Scanner(System.in);

        ArrayList<String> recipients = new ArrayList<>();
        String subject;
        String body;


        System.out.println("enter the email of recipient");
        boolean flag = true;
        String temp = scn.nextLine();
        while(flag) {
            try {
                recipients.add(normalizeEmail(temp));
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
            System.out.println("Do you want to add another recipient?\nSend their email\n otherwise send: 0");
            temp = scn.nextLine();
            if(temp.equals("0"))flag = false;

        }
        System.out.println("Enter your subject:");
        subject = scn.nextLine();

        System.out.println("Enter your Email body (press enter at the end of the text)");
        body = scn.nextLine();

        Email email = new Email(subject, user.getName(), body, recipients);
        String code = email.getCode();
        Email.box.put(code, email);

        System.out.println("Successfully sent your email.\n" + "Code: " + code);
    }
}
