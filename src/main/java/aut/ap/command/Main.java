package aut.ap.command;
import aut.ap.model.User;
import jakarta.persistence.NoResultException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("===== NEW RUN =====" );

        User user = null ;
        Scanner scn = new Scanner(System.in);
        Boolean flag = true;

        while (true) {
            logger.info("-- Entering login/sing up menu --");

        System.out.println("Send L for login\nSend S for sing-up\nSend E for Exit");
        String command = scn.nextLine();

            switch (command) {
                case "L":
                case "Login":
                        try {
                            user = Service.login();
                        }
                        catch (IllegalArgumentException e){
                            System.err.println(e.getMessage());
                            continue;
                        }

                            while (flag){
                                logger.info("-- Entering Email menu --");

                                System.out.println("Send email: S\nView emails: V\nReply to an email: R\nForward an email: F\nLogout: E");
                                command = scn.nextLine();

                                switch(command){
                                    case "S":
                                        Service.sendEmail(user);
                                    break;

                                    case "V":
                                        while(flag){
                                            logger.info("-- Entering view menu --");

                                            System.out.println("Choose which one you want to see:\n[A]ll emails\n[U]nread\n[S]ent emails\n[R]ead emails\nRead by [C]ode\nExit: E\n");
                                            command = scn.nextLine();

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
                                                    logger.info("-- quiting view menu --");
                                                    break;
                                            }
                                        }
                                        flag = true;
                                        break;

                                    case "R":
                                        try {
                                            Service.reply(user);
                                        }
                                        catch (NoResultException e){
                                            System.err.println("Wrong code, try again.");
                                        }
                                        break;
                                    case "F":
                                        Service.forwardEmail(user);
                                        break;
                                    case "E":
                                        flag = false;
                                        logger.info("-- quiting Email menu --");
                                        break;
                                    default:
                                        System.err.println("Wrong entry");
                                }



                            }
                    flag = true;

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
                    logger.info("Bye Bye!");
                    return;
                default:
                    System.err.println("wrong entry");
            }
        }
    }
}