package aut.ap;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scn = new Scanner(System.in);

        System.out.println("Send L or Login for login\nSent sing up or S for sing-up");
        String command = scn.nextLine();

        switch (command){
            case "L":
            case "Login":

                break;
            case "S":
            case"Sing up":

                break;

            default:
                System.err.println("wrong entry");
        }
    }
}