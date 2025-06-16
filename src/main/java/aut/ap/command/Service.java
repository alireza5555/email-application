package aut.ap.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void singUp()throws IllegalArgumentException{
        logger.info("** start singUp method **");

        Scanner scn = new Scanner(System.in);
        System.out.println("enter your  first name");
        String name = scn.nextLine();

        System.out.println("enter your last name");
        String lastName = scn.nextLine();

        System.out.println("enter your password");
        String password = scn.nextLine();

        if(password.length() < 8) {
            logger.warn("user entered weak password: {}", password);
            throw new IllegalArgumentException("Weak password");
        }

        System.out.println("enter your age");
        int age = scn.nextInt();
        scn.nextLine();

        System.out.println("enter your email");
        String email = scn.nextLine();

        Boolean result = true;
        try {
            logger.info("check input email");
             emailCheck(email);
        }
        catch (Exception e){
            result = false;
        }

        if(result){
            logger.warn("the email is a duplicate");
            throw new IllegalArgumentException("An account with this email already exists");
        }

        logger.info("email was ok. now adding information to database");
        submit(name, lastName,password,age,normalizeEmail(email));

        logger.info("** end singUp method **");
    }



    public static User login()throws IllegalArgumentException {
        logger.info("** Start login method **");

        Scanner scn = new Scanner(System.in);
        System.out.println("enter your email");
        String userName = scn.nextLine();

        userName = normalizeEmail(userName);
        System.out.println("enter your password");
        String password = scn.nextLine();

        String finalUserName = userName;
        User user;

        logger.info("execute finding user profile query, with email: {}",userName);
        try {
           user = SingletonSessionFactory.get().fromTransaction(session -> session.createNativeQuery
                    ("SELECT * FROM user " + "WHERE email = :userName", User.class).setParameter("userName", finalUserName).getSingleResult());
        }
        catch (NoResultException e){
            logger.warn("user entered wrong email");
            throw new IllegalArgumentException("this email does not exist.");
        }

        logger.info("check if password: {} match",password);
        if (user.getPassword().equals(password)) {
            System.out.println("Welcome, " + user.getName() + " " + user.getLastName() + "!\n");

            logger.info("login was successful. showing unread emails for user with email: {}.",user.getEmail());
            List<Object[]> emailList = null;
            try {
               emailList = SingletonSessionFactory.get().fromTransaction(
                        session -> session.createNativeQuery("SELECT e.code,e.subject,e.sender FROM recipients r "
                                + " JOIN email e ON r.code = e.code "
                                + " WHERE r.email = :email AND status = 'UNREAD'"
                                + " ORDER BY e.date DESC")
                                .setParameter("email", user.getEmail())
                                .getResultList());
            }
            catch (Exception e){
                logger.error(e);
                System.err.println(e.getMessage());
            }

           for(Object[] temp:emailList){
               System.out.println("+ " + temp[2] + " - " + temp[1] + " - (" + temp[0] + ")");
           }

            logger.info("** end login method **");
            return user;
        }
        else{
            logger.warn("user entered wrong password");
            throw new IllegalArgumentException("Wrong password");
        }
    }



    public static void sendEmail(User user){
        logger.info("** start sendEmail method **");

        Scanner scn = new Scanner(System.in);

        ArrayList<String> recipients = recordRecipients();

        String subject;
        String body;

        System.out.println("Enter your subject:");
        subject = scn.nextLine();

        System.out.println("Enter your Email body (press enter at the end of the text)");
        body = scn.nextLine();

        Email temp2 = addEmailToDB(user, subject, body, recipients);
        logger.info("email {} sent",temp2);

        System.out.println("Successfully sent your email.\n" + "Code: " + temp2.getCode());
        logger.info("** end sendEmail method **");
    }



    public static void showAllEmails(User user , String userInput){
        logger.info("** start showAllEmails method **");
        AtomicReference<String> command = new AtomicReference<>("");

        switch (userInput){
            case "all":
                command.set("WHERE r.email = '" + user.getEmail() + "'");
                break;
            case "unread":
                command.set(" WHERE r.email = '" + user.getEmail() + "' AND status = 'UNREAD' ");
                break;
            case "read":
                command.set("WHERE r.email = '" + user.getEmail() + "' AND status = 'READ'");
                break;
            case "sent":
                command.set("WHERE e.sender = " + "'" + user.getEmail() + "'");
                break;

        }

        logger.info("user want to see {} emails\n executing query.",userInput);

        List<Object[]> emailList =  SingletonSessionFactory.get().fromTransaction(
                session -> session.createNativeQuery("SELECT e.code,e.subject,r.email FROM recipients r "
                        +"JOIN email e ON r.code = e.code "
                        + command.get()
                        +" ORDER BY e.date DESC" )
                        .getResultList());

        for(Object[] temp:emailList){
            System.out.println("+" + temp[2] + " - " + temp[1] + " - (" + temp[0] + ")");
        }

        logger.info("** end showAllEmails **");
    }


    public static void showWithCode (User user, String code){
        logger.info("(** start showWithCode method **");
        logger.info("getting recipients");
        List<String> temp = getRecipients(code);

        logger.info("finding email");
        Email email = getEmail(code);

        temp.add(email.getSender());

        logger.info("check if user: {} can see email with code: {}",user.getEmail(),email.getCode());
        for(String customer : temp){
                 if(customer.equals(user.getEmail())){
                 email.showInf(temp);
                 SingletonSessionFactory.get().inTransaction(session -> session.createMutationQuery("UPDATE Recipients SET status = 'READ' " +
                 " WHERE email = :email AND code = :code")
                         .setParameter("code",code)
                         .setParameter("email", user.getEmail())
                         .executeUpdate());

                 logger.info("everything was ok.\n** end showWithCode method**");
        return;
    }
    }
        logger.warn("user {} cannot access email with code: {}",user.getEmail(),email.getCode());
       System.out.println("You cannot read this email.");
    }


    public static void reply(User user)throws NoResultException{
        logger.info("** start reply method **");

        Scanner scn = new Scanner(System.in);

        System.out.println("Enter the code of email that you want to respond: ");
        String code = scn.nextLine();

        System.out.println("Enter your respond text: ");
        String body = scn.nextLine();

        logger.info("check if user: {} have access to the email: {}.",user.getEmail(),code);
        if(checkUser(code, user.getEmail())){
            logger.warn("user {} cannot access to email with code: {} ",user.getEmail(),code);
            System.err.println("You cannot reply this email.");

            logger.info("** end reply method **");
            return;
        }

        Email oldEmail = getEmail(code);
        logger.info("creating and adding reply email");
        Email newEmail = addEmailToDB(user, "[RE] " + oldEmail.getSubject(), body,getRecipients(code) );

        System.out.println("Successfully sent your reply to email " + oldEmail.getCode() + "\nCode: " + newEmail.getCode());
        logger.info("** end reply method **");
    }


    public static void forwardEmail(User user){
        logger.info("** start forwardEmail **");

        Scanner scn = new Scanner(System.in);
        System.out.println("Enter the code of your email: ");
        String code = scn.nextLine();

        logger.info("check if user have access to email: {}",code);
        List<String> userList = SingletonSessionFactory.get().fromTransaction(session -> session.createNativeQuery(
                "SELECT r.email FROM recipients r " + "WHERE r.code = :code",String.class)
                .setParameter("code", code)
                .getResultList());
        Boolean flag = true;
        for (String temp : userList){
            if(temp.equals(user.getEmail())){
                flag = false;
                break;
            }
        }

        if(flag){
            logger.warn("user cannot access email with code: {}",code);
            System.err.println("You cannot forward this email");

            logger.info("** end forwardEmail method **");
            return;
        }

        logger.info("creating new email and adding it to database.");
        Email oldEmail = getEmail(code);
        ArrayList<String> recipients = recordRecipients();
        Email newEmail = addEmailToDB(user, "[FW] " + oldEmail.getSubject(), oldEmail.getBody(), recipients);

        System.out.println("Successfully forwarded your email.\n" + "Code: " + newEmail.getCode());
        logger.info("** end forwardEmail method **");
    }

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

private static Email getEmail(String code)throws NoResultException {
        logger.info("* start getEmail method *");

            Email temp = SingletonSessionFactory.get().fromTransaction(session -> session.createNativeQuery("SELECT * FROM email" +
                    " WHERE code = :code ", Email.class).setParameter("code", code).getSingleResult());
            logger.info("returning email\n" +
                    "* end getEmail method *");
            return temp;

}


    private static String normalizeEmail(String email){
        logger.info("* start normalizeEmail method *");

        email = email.trim();
        if(email.contains("@")) {
            if(email.endsWith("@milou.com")){
                logger.info("* end normalizeEmail method *");
                return email;
            }
            logger.warn("user entered foreign domain");
            throw new IllegalArgumentException("Your domain must be @milou.com");
           }

        else {
            logger.info("* end normalizeEmail method *");
            return email + "@milou.com";
        }
    }



    private static String submit (String name, String lastName,String password, int age, String email){
        logger.info("* start submit method *");

        logger.info("adding user with email: {} to database",email);
        SingletonSessionFactory.get().inTransaction(session -> session.persist(new User(email,age,lastName,name,password)));

        logger.info("* end submit method *");
        return "Your new account is created.\nGo ahead and login!";
    }



    private static boolean emailCheck (String email){
        logger.info("* start emailCheck method *");

        logger.info("execute the query to check if email: {} exist.",email);
                try {
                    SingletonSessionFactory.get().inTransaction(session -> session.createNativeQuery("SELECT email FROM user " +
                            " WHERE email = :email", String.class).setParameter("email", normalizeEmail(email)).getSingleResult());
                }
                catch (NoResultException e){
                    logger.warn("email: {} does not exist.",email);
                    throw new NoResultException();
                }
                logger.info("email: {} exist in database\n* end emailCheck method *",email);
    return true;
    }


    private static Email addEmailToDB(User user, String subject, String body, List<String> recipients) {
        logger.info("* start addEmailToDB method *");

        Email temp2 = new Email(subject, user.getEmail(), body);
        logger.info("trying to add email with code: {} to database ",temp2.getCode());
        try {
            SingletonSessionFactory.get().inTransaction(session -> {
                session.persist(temp2);
                recipients.forEach(recipient ->session.persist(new Recipients(temp2.getCode() ,recipient)));
            });
        }
        catch (Exception e){
            logger.error(e);
            System.err.println(e.getMessage());
        }

        logger.info("email: {} has been successfully aded to database\n" +
                "* end addEmailToDB method *",temp2.getCode());
        return temp2;
    }

    private static List<String> getRecipients(String code) {
        logger.info("* start getRecipients method *");

        List<String> temp ;
        logger.info("extracting recipients of email with code: {} from database",code);
        temp = SingletonSessionFactory.get().fromTransaction(
                session -> session.createNativeQuery("SELECT r.email FROM recipients r "
                        + " JOIN email e ON r.code = e.code "
                        + " WHERE r.code = :code ",String.class)
                        .setParameter("code", code)
                        .getResultList());

        logger.info("* end getRecipients method *");
        return temp;
    }


    private static ArrayList<String> recordRecipients() {
        logger.info("* start recordRecipients method *");

        Scanner scn = new Scanner(System.in);
        ArrayList<String> recipients = new ArrayList<>();

        System.out.println("enter the email of recipient");
        boolean flag = true;
        String temp = scn.nextLine();
        while(flag) {
            try {
                logger.info("check if email: {} exist in database",temp);
                emailCheck(temp);
                recipients.add(normalizeEmail(temp));
            } catch (NoResultException e) {
                logger.warn("email was not found in database");
                System.err.println("Wrong email, try again.");
            }
            catch (Exception e){
                logger.error(e);
                System.err.println(e.getMessage());
            }

            System.out.println("Do you want to add another recipient?\nSend their email\n otherwise send: 0");
            temp = scn.nextLine();
            if(temp.equals("0"))flag = false;

        }

        logger.info("returning recipients...\n* end recordRecipients method *");
        return recipients;
    }


    private static boolean checkUser(String code, String email) {
        logger.info("* start checkUser method *");

        logger.info("execute the query to see if user with email: {} exist in database",email);
        try {
            SingletonSessionFactory.get().fromTransaction(session ->
                    session.createNativeQuery(
                                    "SELECT email FROM recipients WHERE code = :code AND email = :email",
                                    String.class)
                            .setParameter("code", code)
                            .setParameter("email", email)
                            .getSingleResult()
            );

        } catch (NoResultException e) {
            logger.info("user does not exist\n* end checkUser method *");
            return false;

        }

        logger.info("user exist\n* end checkUser method *");
        return true;
    }




}
