package aut.ap;
import aut.ap.model.Service;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scn = new Scanner(System.in);

        System.out.println("Send L or Login for login\nSend sing up or S for sing-up");
        String command = scn.nextLine();

        switch (command){
            case "L":
            case "Login":
                try {
                    Service.login();
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
                break;
            case "S":
            case"Sing up":
                try {
                    Service.singUp();
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }

                break;
            default:
                System.err.println("wrong entry");
        }
    }
}