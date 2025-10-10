package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class VaultBook {
    public static void main(String[] args) {
        run();
    }
    public static void run() {
        Scanner keyboard = new Scanner(System.in);

        System.out.println("""
==========================================
          WELCOME TO VAULT BOOK
==========================================
   Your trusted Accounting Ledger System
   Secure. Simple. Smart.
------------------------------------------
""");

        while (true)
        {
            homeScreen();
            menuSelector(keyboard);
        }
    }
    public static void homeScreen() {
        System.out.println("How may we assist you today?");
        System.out.println("(1) Add Deposit");
        System.out.println("(2) Make Withdrawal");
        System.out.println("(3) Ledger");
        System.out.println("(4) Exit");
    }
    public static void menuSelector(Scanner keyboard) {
        System.out.print("Please choose your option: ");
        int choice = keyboard.nextInt();
        keyboard.nextLine();

        switch (choice) {
            case 1:
                addDeposit(keyboard);
                break;
            case 2:
                makeWithdraw(keyboard);
                break;
            case 3:
                enterLedgerMenu(keyboard);
                break;
            case 4:
                exit(keyboard);
                break;
            default:
                System.out.println("Please enter a valid option");
        }
        System.out.print("Press ENTER to continue...");
        keyboard.nextLine();
    }
    public static void addDeposit(Scanner keyboard) {
        //Auto getting date and time so the Customer doesn't need to type it.
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        //Prompting fields

        double amount;

        //Asking for deposit first and making sure its Positive
        while(true){
        System.out.print("Enter deposit amount ");
        amount = keyboard.nextDouble();
        keyboard.nextLine();

        if (amount <= 0) {
            System.out.println("Invalid amount. Deposits must be positive. Try again.");
        }else {
            break;
        }
    }
        System.out.print("Enter brief description: ");
        String description = keyboard.nextLine();

        System.out.print("Enter vendor: ");
        String vendor = keyboard.nextLine();

        Transactions deposit = new Transactions(date,time,description,amount,vendor);



    }
    public static void makeWithdraw(Scanner keyboard) {
        System.out.println("Please enter the amount you want to withdraw");
    }
    public static void enterLedgerMenu(Scanner keyboard) {
        System.out.println("This is a test the LedgerMenu works");
    }
    public static void exit(Scanner keyboard) {
        keyboard.close();
        System.out.println("Have a nice day!");
        System.exit(0);
    }
}
