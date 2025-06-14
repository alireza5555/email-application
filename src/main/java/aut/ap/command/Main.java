package aut.ap.command;
import aut.ap.model.User;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        User user = null ;
        Scanner scn = new Scanner(System.in);
        Boolean flag = true;

        while (true) {
        System.out.println("Send L for login\nSend S for sing-up\nSend E for Exit");
        String command = scn.nextLine();

            switch (command) {
                case "L":
                case "Login":
                    try {
                        user = Service.login();

                            while (flag){
                                System.out.println("Send email: S\nView emails: V\nReply to an email: R\nForward an email: F");
                                command = scn.nextLine();

                                switch(command){
                                    case "S":
                                        Service.sendEmail(user);
                                    break;

                                    case "V":
                                        while(flag){
                                            System.out.println("Choose which one you want to see:\n[A]ll emails\n[U]nread\n[S]ent emails\n[R]ead emails\nRead by [C]ode");

                                            switch (command){
                                                case "A":
                                                    Service.showAllEmails(user, "all");
                                                    break;
                                                case"U":
                                                    Service.showAllEmails(user, "unread");
                                                    break;
                                                case"S":
                                                    Service.showAllEmails(user, "sent");
                                                    break;
                                                case"R":
                                                    Service.showAllEmails(user, "read");
                                                    break;
                                                case"C":
                                                    System.out.println("Enter the code of your email: ");
                                                    command = scn.nextLine();
                                                    Service.showWithCode(user,command);
                                                    break;
                                                case "E":
                                                    flag = false;
                                                    break;
                                            }
                                        }
                                        flag = true;
                                        break;

                                    case "R":
                                        Service.reply(user);
                                    case "F":
                                        Service.forwardEmail(user);
                                    default:
                                        System.err.println("Wrong entry");
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
                case "E":
                    return;
                default:
                    System.err.println("wrong entry");
            }
        }
    }
}