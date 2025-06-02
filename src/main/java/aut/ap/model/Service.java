package aut.ap.model;

import aut.ap.framework.SingletonSessionFactory;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Scanner;

public class Service {

    public static String submit (String name, String lastName,String password, int age, String email){



        SingletonSessionFactory.get().inTransaction(session -> session.persist(new User(email,age,lastName,name,password)));
        return "You have been singed up successfully.";
    }

    private static boolean emailCheck (String email){
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


    public static void login()throws NoResultException {
        Scanner scn = new Scanner(System.in);
        System.out.println("enter your username");
        String userName = scn.nextLine();
        System.out.println("enter your password");
        String password = scn.nextLine();

        User u = SingletonSessionFactory.get().fromTransaction(session -> session.createNativeQuery("SELECT * FROM user " + "WHERE email = :userName", User.class).setParameter("userName", userName).getSingleResult());

        if (u.password.equals(password)) {
            System.out.println("Welcome, " + u.name + " " + u.lastName + "!");
        }
        else throw new IllegalArgumentException("Wrong password");
    }
}
