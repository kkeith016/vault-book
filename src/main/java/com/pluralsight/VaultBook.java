package com.pluralsight;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;


public class VaultBook {
    public static void main(String[] args) {
        run();
    }

    public static void run() { Scanner keyboard = new Scanner(System.in);

        // <-- ArrayList lives in main()
        ArrayList<Transactions> transactions = new ArrayList<>();

        // Load existing transactions from CSV
        loadTransactions(transactions);

        System.out.println("""
                ==========================================
                          WELCOME TO VAULT BOOK
                ==========================================
                   Your trusted Accounting Ledger System
                   Secure. Simple. Smart.
                ------------------------------------------
                """);

        while (true) {
            homeScreen();
            menuSelector(keyboard, transactions); // pass the ArrayList
        }
    }

    public static void homeScreen() {
        System.out.println("How may we assist you today?");
        System.out.println("(1) Add Deposit");
        System.out.println("(2) Make Withdrawal");
        System.out.println("(3) Show Balance");
        System.out.println("(4) Exit");
    }

    public static void menuSelector(Scanner keyboard, ArrayList<Transactions> transactions) {
        System.out.print("Please choose your option: ");
        int choice = keyboard.nextInt();
        keyboard.nextLine();

        switch (choice) {
            case 1:
                addDeposit(keyboard, transactions);
                break;
            case 2:
                makeWithdraw(keyboard, transactions);
                break;
            case 3:
                showBalance(transactions);
                break;
            case 4:
                exit(keyboard);
                break;
            default:
                System.out.println("Please enter a valid option");
                break;
        }
        System.out.println();
    }

    public static void addDeposit(Scanner keyboard, ArrayList<Transactions> transactions) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        double amount;

        while (true) {
            System.out.print("Enter deposit amount: ");
            amount = keyboard.nextDouble();
            keyboard.nextLine();

            if (amount <= 0) {
                System.out.println("Invalid amount. Deposits must be positive. Try again.");
            } else {
                break;
            }
        }

        System.out.print("Enter brief description: ");
        String description = keyboard.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = keyboard.nextLine();

        Transactions deposit = new Transactions(date, time, description, amount, vendor);

        transactions.add(deposit);       // Add to ArrayList
        appendTransaction(deposit);      // Write to CSV

        System.out.println("Deposit added successfully.");
    }

    public static void makeWithdraw(Scanner keyboard, ArrayList<Transactions> transactions) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        double amount;

        double balance = getTotalBalance(transactions);

        while (true) {
            System.out.print("Enter withdrawal amount: ");
            amount = keyboard.nextDouble();
            keyboard.nextLine();

            if (amount <= 0) {
                System.out.println("Invalid amount. Must be positive.");
            } else if (amount > balance) {
                System.out.printf("Insufficient funds! Current balance: $%.2f%n", balance);
            } else {
                break;
            }
        }

        amount = -amount; // store as negative
        System.out.print("Enter description: ");
        String description = keyboard.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = keyboard.nextLine();

        Transactions withdrawal = new Transactions(date, time, description, amount, vendor);

        transactions.add(withdrawal);
        appendTransaction(withdrawal);

        System.out.println("Withdrawal added successfully.");
    }

    public static void showBalance(ArrayList<Transactions> transactions) {
        double balance = getTotalBalance(transactions);
        System.out.printf("Your current total balance is: $%.2f%n", balance);
    }

    public static void exit(Scanner keyboard) {
        keyboard.close();
        System.out.println("Exiting Vault Book. Have a nice day!");
        System.exit(0);
    }

    // ------------------- CSV Handling -------------------
    public static void loadTransactions(ArrayList<Transactions> transactions) {
        String filePath = "src/main/resources/transactions.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // <-- skip header
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    String[] parts = line.split("\\|");
                    LocalDate date = LocalDate.parse(parts[0]);
                    LocalTime time = LocalTime.parse(parts[1]);
                    String description = parts[2];
                    String vendor = parts[3];
                    double amount = Double.parseDouble(parts[4]);

                    transactions.add(new Transactions(date, time, description, amount, vendor));
                }
            }
        } catch (IOException e) {
            System.out.println("CSV not found. Starting with empty ledger.");
        }
    }

    public static void appendTransaction(Transactions t) {
        String filePath = "src/main/resources/transactions.csv";
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(t.toString() + "\n");
        } catch (IOException e) {
            System.out.println("Error saving transaction!");
            e.printStackTrace();
        }
    }

    public static double getTotalBalance(ArrayList<Transactions> transactions) {
        double total = 0;
        for (Transactions t : transactions) {
            total += t.getAmount();
        }
        return total;
    }
    }

