package aut.ap.command;

import aut.ap.framework.SingletonSessionFactory;
import aut.ap.model.Email;
import aut.ap.model.Recipients;
import aut.ap.model.User;
import jakarta.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class Service {
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
        User user = SingletonSessionFactory.get().fromTransaction(session -> session.createNativeQuery
                ("SELECT * FROM user " + "WHERE email = :userName", User.class).setParameter("userName", finalUserName).getSingleResult());

        if (user.getPassword().equals(password)) {
            System.out.println("Welcome, " + user.getName() + " " + user.getLastName() + "!\n");
           List<Object[]> emailList =  SingletonSessionFactory.get().fromTransaction(
                   session -> session.createNativeQuery("SELECT e.code,e.subject,r.email FROM recipients r"
                           + "JOIN email e ON r.code = e.code"
                           +"WHERE status = UNREAD"
                   +"ORDER BY e.date DESC" )).getResultList();
           for(Object[] temp:emailList){
               System.out.println("+" + temp[2] + " - " + temp[1] + " - (" + temp[0] + ")");
           }

            return user;
        }
        else throw new IllegalArgumentException("Wrong password");
    }



    public static void sendEmail(User user){
        Scanner scn = new Scanner(System.in);

        ArrayList<String> recipients = recordRecipients();

        String subject;
        String body;

        System.out.println("Enter your subject:");
        subject = scn.nextLine();

        System.out.println("Enter your Email body (press enter at the end of the text)");
        body = scn.nextLine();

        Email temp2 = addEmailToDB(user, subject, body, recipients);

        System.out.println("Successfully sent your email.\n" + "Code: " + temp2.getCode());
    }



    public static void showAllEmails(User user , String userInput){
        AtomicReference<String> command = new AtomicReference<>("");

        switch (userInput){
            case "all":
                command.set("WHERE r.email = :email");
                break;
            case "unread":
                command.set("WHERE r.email = :email AND status = UNREAD");
                break;
            case "read":
                command.set("WHERE r.email = :email AND status = READ");
                break;
            case "sent":
                command.set("WHERE e.sender = :sender");
                break;

        }

        List<Object[]> emailList =  SingletonSessionFactory.get().fromTransaction(
                session -> session.createNativeQuery("SELECT e.code,e.subject,r.email FROM recipients r"
                        +"JOIN email e ON r.code = e.code "
                        + command.get()
                        +" ORDER BY e.date DESC" )).setParameter("sender", user.getEmail()).setParameter("email",user.getEmail()).getResultList();

        for(Object[] temp:emailList){
            System.out.println("+" + temp[2] + " - " + temp[1] + " - (" + temp[0] + ")");
        }
    }


    public static void showWithCode (User user, String code){

        List<String> temp = getRecipients(code);
        Email email = getEmail(code);

        temp.add(email.getSender());

        for(String customer : temp){
                 if(customer.equals(user.getEmail())){
                 email.showInf(temp);
                 SingletonSessionFactory.get().inTransaction(session -> session.createMutationQuery("UPDATE recipients SET status = 'READ'" +
                 " WHERE email = :email AND code = :code").setParameter("code",code).setParameter("email", user.getEmail()).executeUpdate());
        return;
    }
    }
       System.out.println("You cannot read this email.");

    }


    public static void reply(User user){
        Scanner scn = new Scanner(System.in);

        System.out.println("Enter the code of email that you want to respond: ");
        String code = scn.nextLine();

        System.out.println("Enter your respond text: ");
        String body = scn.nextLine();

        Email oldEmail = getEmail(code);
        Email newEmail = addEmailToDB(user, "[RE] " + oldEmail.getSubject(), body,getRecipients(code) );

        System.out.println("Successfully sent your reply to email " + oldEmail.getCode() + "\nCode: " + newEmail.getCode());
    }


    public static void forwardEmail(User user){
        Scanner scn = new Scanner(System.in);
        System.out.println("Enter the code of your email: ");
        String code = scn.nextLine();

        List<String> userList = SingletonSessionFactory.get().fromTransaction(session -> session.createNativeQuery(
                "SELECT r.email FROM recipients r " + "WHERE r.code = :code",String.class).setParameter("code", code).getResultList());

        Boolean flag = true;
        for (String temp : userList){
            if(temp.equals(user.getEmail())){
                flag = false;
                break;
            }
        }

        if(flag){
            System.err.println("You cannot forward this email");
            return;
        }

        Email oldEmail = getEmail(code);
        ArrayList<String> recipients = recordRecipients();
        Email newEmail = addEmailToDB(user, "[FW] " + oldEmail.getSubject(), oldEmail.getBody(), recipients);
        System.out.println("Successfully forwarded your email.\n" + "Code: " + newEmail.getCode());
    }

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

private static Email getEmail(String code) {
    return SingletonSessionFactory.get().fromTransaction(session -> session.createNativeQuery("SELECT * FROM email" +
            " WHERE code = :code ", Email.class).setParameter("code", code).getSingleResult());

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



    private static String submit (String name, String lastName,String password, int age, String email){
        SingletonSessionFactory.get().inTransaction(session -> session.persist(new User(email,age,lastName,name,password)));
        return "Your new account is created.\nGo ahead and login!";
    }



    private static boolean emailCheck (String email){
        email = normalizeEmail(email);
        List<String> allUsers =
                SingletonSessionFactory.get().fromTransaction(session -> session.createNativeQuery("SELECT email FROM user").getResultList());

        for (String temp : allUsers){
            if(temp.equals(email)) return true;
        }
        return false;

    }

    private static Email addEmailToDB(User user, String subject, String body, List<String> recipients) {
        Email temp2 = new Email(subject, user.getEmail(), body);
        try {
            SingletonSessionFactory.get().inTransaction(session -> {
                session.persist(temp2);
                recipients.forEach(recipient ->session.persist(new Recipients(temp2.getCode() ,recipient)));
            });
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
        return temp2;
    }

    private static List<String> getRecipients(String code) {
        List<String> temp ;
        temp = SingletonSessionFactory.get().fromTransaction(
                session -> session.createNativeQuery("SELECT r.email FROM recipients r "
                        + " JOIN email e ON r.code = e.code "
                        + " WHERE r.code = :code ",String.class).setParameter("code", code)).getResultList();
        return temp;
    }


    private static ArrayList<String> recordRecipients() {
        Scanner scn = new Scanner(System.in);
        ArrayList<String> recipients = new ArrayList<>();


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
        return recipients;
    }



}
