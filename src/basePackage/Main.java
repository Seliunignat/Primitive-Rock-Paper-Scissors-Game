package basePackage;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
	// write your code here
        if(args.length == 0) //if NO ARGS
        {
            System.out.println("There is no arguments!");
            return;
        }
        String input;
        int computerMoveInd = 0 + (int) (Math.random()* (args.length - 1));
        input = args[computerMoveInd];

        SecureRandom random = SecureRandom.getInstanceStrong();
        byte[] key = new byte[16]; // 128 bit
        random.nextBytes(key);

        StringBuilder sb = new StringBuilder(); //creating string from SECRET KEY(in hex)
        for (byte b : key) {
            sb.append(String.format("%02x", b));
        }


        StringBuilder sb1 = new StringBuilder(); //creating string from key
        for (byte b : HMAC.calcHmacSha256(key, input.getBytes())) //HMAC hex string
        {
            sb1.append(String.format("%02x", b));
        }

        boolean wrong = false;
        boolean repeats = false;
        ArrayList<String> moves = new ArrayList();
        if(args.length >= 3 && args.length % 2 != 0)
        {
            for (String arg: args)
            {
                for (int i = 0; i < moves.size(); i++) {
                    if((moves.get(i)).equals(arg)) {wrong = true; repeats = true; break;}
                }
                if(wrong) break;
                else moves.add(arg.toLowerCase());
            }
        }
        else
        {
            if(args.length < 3) System.out.println("It's not enough arguments!");
            else if(args.length % 2 == 0) System.out.println("You ENTERED an even number of values!");
            wrong = true;
        }

        if(wrong)
            {
                if(repeats) System.out.println("There are repeats in arguments!");
                return;
            }
        else //If it's NOTHING WRONG <----
        {

            Scanner sc = new Scanner(System.in);
            int playerMoveInd = 0;
            do
            {
                System.out.println("HMAC: " + sb1); //OUR HMAC in hex
                System.out.println("Available moves:");
                for (int i = 0; i < moves.size(); i++) {
                    System.out.println(i+1 + " - " + moves.get(i));
                }
                System.out.println("0 - exit");
                System.out.print("Enter your move: ");
                playerMoveInd = sc.nextInt();
                if(playerMoveInd < 0 || playerMoveInd > args.length) System.out.println("ENTER the correct value!");
            } while(playerMoveInd < 0 || playerMoveInd > args.length);
            playerMoveInd--;
            if(playerMoveInd != -1)
                System.out.println("Your move: " + moves.get(playerMoveInd));
            else return;
            System.out.println("Computer move: " + input);

            int half = args.length/2; //half of args
            if(playerMoveInd < computerMoveInd) //Definition of WIN! or LOOSE! or DRAW!
                if(playerMoveInd < computerMoveInd-half)
                    System.out.println("You WIN!");
                else System.out.println("You LOOSE!");
            else if(playerMoveInd > computerMoveInd)
                if(playerMoveInd > computerMoveInd + half)
                    System.out.println("You LOOSE!");
                else System.out.println("You WIN!");
            else if(playerMoveInd == computerMoveInd)
                System.out.println("It's a DRAW!");

            System.out.println("HMAC key: " + sb.toString()); //OUR SECRET KEY in hex
        }
    }
}

