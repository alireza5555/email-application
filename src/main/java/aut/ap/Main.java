package aut.ap;
import aut.ap.model.Service;
import aut.ap.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        User user = null ;
        Scanner scn = new Scanner(System.in);
        while (true) {
        System.out.println("Send L for login\nSend S for sing-up");
        String command = scn.nextLine();

            switch (command) {
                case "L":
                case "Login":
                    try {
                        user = Service.login();

                            while (true){
                                System.out.println("Send email: S\nView emails: V\nReply to an email: R\nForward an email: F");
                                command = scn.nextLine();

                                switch(command){
                                    case "S":
                                    Service.sendEmail(user);
                                    break;

                                    case "V":

                                    case "R":

                                    case "F":

                                    default:
                                }



                            }

                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case "S":
                case "Sing up":
                    try {
                        Service.singUp();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    break;
                default:
                    System.err.println("wrong entry");
            }
        }
    }
}