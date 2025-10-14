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

    public static void run() {
        Scanner keyboard = new Scanner(System.in);
        ArrayList<Transactions> transactions = new ArrayList<>();
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
            menuSelector(keyboard, transactions);
        }
    }

    public static void homeScreen() {
        System.out.println("""
        ====================================
                     HOME MENU              
        ====================================
        |  1  | Add Deposit                |
        |  2  | Make Payment               |
        |  3  | Show Balance               |
        |  4  | Ledger                     |
        |  5  | Exit                       |
        ====================================
        """);
    }

    public static void menuSelector(Scanner keyboard, ArrayList<Transactions> transactions) {
        System.out.println();
        System.out.print("Please choose your option: ");
        int choice = keyboard.nextInt();
        keyboard.nextLine();

        switch (choice) {
            case 1:
                addDeposit(keyboard, transactions);
                break;
            case 2:
                makePayment(keyboard, transactions);
                break;
            case 3:
                showBalance(transactions);
                break;
            case 4:
                ledger(keyboard, transactions);
                break;
            case 5:
                exit(keyboard);
                break;
            default:
                System.out.println("Please enter a valid option");
                break;
        }
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

        transactions.add(deposit);
        appendTransaction(deposit);

        System.out.println("Deposit added successfully.");
    }

    public static void makePayment(Scanner keyboard, ArrayList<Transactions> transactions) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        double amount;

        double balance = getTotalBalance(transactions);

        while (true) {
            System.out.print("Enter Payment amount: ");
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

        Transactions payment = new Transactions(date, time, description, amount, vendor);

        transactions.add(payment);
        appendTransaction(payment);

        System.out.println("Payment added successfully.");
    }

    public static void showBalance(ArrayList<Transactions> transactions) {
        double balance = getTotalBalance(transactions);
        System.out.printf("Your current total balance is: $%.2f%n", balance);
    }

    //------------------------------------ LEDGER

    public static void ledger(Scanner keyboard,ArrayList<Transactions> transactions) {

        while (true) {
            ledgerMenu();
            System.out.print("Please enter your option: ");
            int option = keyboard.nextInt();
            keyboard.nextLine();

            switch (option) {
                case 1:
                    viewAllTransactions(transactions);
                    break;
                case 2:
                    allDeposits(transactions);
                    break;
                case 3:
                    allPayments(transactions);
                    break;
                case 4:
                    reports(keyboard, transactions);
                    break;
                case 5:
                    System.out.println("Returning to Home Screen...");
                    while (true) {
                        homeScreen();
                        menuSelector(keyboard, transactions);
                    }
                default:
                    System.out.println("Invalid option. Try again.");
            }
            }
        }

    private static void ledgerMenu() {
        System.out.println("""
        ====================================
                  LEDGER SYSTEM            
        ====================================
        |  1  | Display All Transactions  |
        |  2  | View All Deposits         |
        |  3  | View All Payments         |
        |  4  | Reports                   |
        |  5  | Return to Home Screen     |
        ====================================
        """);
    }

    public static void viewAllTransactions(ArrayList<Transactions> transactions) {
        sortNewest(transactions);
        System.out.println();
        System.out.println("==================================== ALL TRANSACTIONS ====================================");
        printTableHeader();

        if(transactions.isEmpty()) {
            System.out.println("There are no transactions in the system.");
        } else {
            for (Transactions t : transactions) {
                printTransactions(t);
            }
        }
        printTableFooter();
    }
    public static void allDeposits(ArrayList<Transactions> transactions) {
        System.out.println();
        printTableHeader();

        for(Transactions t : transactions){
            if(t.getAmount() > 0){
                printTransactions(t);
            }
        }
        printTableFooter();
    }
    public static void allPayments(ArrayList<Transactions> transactions) {
        System.out.println();
        System.out.println("==================================== ALL PAYMENTS =======================================");
        printTableHeader();

        for(Transactions t : transactions){
            if(t.getAmount() < 0){
                printTransactions(t);
            }
        }
        printTableFooter();
    }

    // ------------------- REPORTS

    public static void reports(Scanner keyboard, ArrayList<Transactions> transactions) {

        while(true){
            reportsMenu();
            System.out.print("Please enter your option: ");
            int choice = keyboard.nextInt();
            keyboard.nextLine();

            switch (choice){
                case 1:
                     monthToDate(keyboard,transactions);
                     break;
                case 2:
                     previousMonth(keyboard,transactions);
                     break;
                case 3:
                     yearToDate(keyboard,transactions);
                     break;
                case 4:
                     previousYear(keyboard,transactions);
                     break;
                case 5:
                     searchByVendor(keyboard,transactions);
                     break;
                case 6:
                    System.out.println("Returning to Ledger...");
                    return;
                case 7:
                    System.out.println("Returning to Home Screen...");
                    while (true) {
                        homeScreen();
                        menuSelector(keyboard, transactions);
                    }

                    default:
                    System.out.println("Invalid option. Try again.");
                    break;

            }
            }
        }

    public static void reportsMenu(){
        System.out.print("""
            ====================================
                          REPORTS               
            ====================================
            |  1  | Month to Date              |
            |  2  | Previous Month             |
            |  3  | Year to Date               |
            |  4  | Previous Year              |
            |  5  | Search by Vendor           |
            |  6  | Back to Ledger             |
            |  7  | Back to Home               |
            ====================================
            """);
    }

    public static void monthToDate(Scanner keyboard,ArrayList<Transactions> transactions){
        LocalDate date = LocalDate.now();
        int currentMonth = date.getMonthValue();
        int currentYear = date.getYear();

        System.out.println();
        System.out.println("==================================== Month to Date =======================================");
        printTableHeader();

        ArrayList<Transactions> reportTransactions = new ArrayList<>();

        for(Transactions t : transactions){
            date = t.getDate();
            if (date.getMonthValue() == currentMonth && date.getYear() == currentYear){
                printTransactions(t);
                reportTransactions.add(t);
            }
        }
        printTableFooter();
        saveToFile(keyboard, reportTransactions,"Month to Date");
        naviAfterReports(keyboard,transactions);
    }

    public static void previousMonth(Scanner keyboard,ArrayList<Transactions> transactions){
        LocalDate date = LocalDate.now();
        LocalDate lastMonth = date.minusMonths(1);
        int previousMonth = lastMonth.getMonthValue();
        int previousYear = lastMonth.getYear();

        System.out.println();
        System.out.println("==================================== Previous Month =======================================");
        printTableHeader();
        ArrayList<Transactions> reportTransactions = new ArrayList<>();

        for (Transactions t : transactions){
            date = t.getDate();
            if (date.getMonthValue() == previousMonth && date.getYear() == previousYear){
                printTransactions(t);
            }
        }
        printTableFooter();
        saveToFile(keyboard, reportTransactions, "Previous Month");
        naviAfterReports(keyboard, transactions);
    }

    public static void yearToDate(Scanner keyboard,ArrayList<Transactions> transactions){
        LocalDate date = LocalDate.now();
        int currentYear = date.getYear();

        System.out.println();
        System.out.println("==================================== Year to Date =======================================");
        printTableHeader();
        ArrayList<Transactions> reportTransactions = new ArrayList<>();

        for (Transactions t : transactions){
            date = t.getDate();
            if (date.getYear() == currentYear){
                printTransactions(t);
            }
        }
        printTableFooter();
        saveToFile(keyboard, reportTransactions,"Year to Date");
        naviAfterReports(keyboard, transactions);
    }

    public static void previousYear(Scanner keyboard, ArrayList<Transactions> transactions){
        LocalDate date = LocalDate.now();
        int currentYear = date.getYear();
        int previousYear = currentYear - 1;
        System.out.println();
        System.out.println("==================================== Previous Year =======================================");
        printTableHeader();
        ArrayList<Transactions> reportTransactions = new ArrayList<>();


        boolean found = false;
        for (Transactions t : transactions){
            LocalDate transactionDate = t.getDate();
            if (transactionDate.getYear() == previousYear){
                printTransactions(t);
                found = true;
            }
        }
        if (!found){
            System.out.println("There is no transaction with that year.");
        }
        printTableFooter();
        saveToFile(keyboard, reportTransactions,"Previous Year");
        naviAfterReports(keyboard, transactions);
    }

    public static void searchByVendor(Scanner keyboard, ArrayList<Transactions> transactions) {
        System.out.print("Enter vendor name to search: ");
        String vendorName = keyboard.nextLine().trim().toLowerCase();

        System.out.println();
        System.out.println("==================================== SEARCH RESULTS =======================================");
        printTableHeader();
        ArrayList<Transactions> reportTransactions = new ArrayList<>();

        boolean found = false;
        for (Transactions t : transactions) {
            if (t.getVendor().toLowerCase().contains(vendorName)) {
                printTransactions(t);
                found = true;
            }
        }

        if (!found) {
            System.out.println("There is no transaction with that vendor.");
        }

        printTableFooter();
        saveToFile(keyboard, reportTransactions, "Vendor Report");
        naviAfterReports(keyboard, transactions);

    }



    //------------------------------------Helper Methods

    public static void waitForEnter(Scanner keyboard){
        System.out.println("\nPress ENTER to continue...");
        keyboard.nextLine();
    }

    public static void printTableHeader(){
        System.out.printf("%-12s | %-8s | %-25s | %-20s | %10s%n",
                "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    public static void printTableFooter(){
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    public static void naviAfterReports(Scanner keyboard, ArrayList<Transactions> transactions) {
        while (true) {
            System.out.println();
            System.out.println("--------------------------------------------");
            System.out.print("Press [H] to return Home or [R] to return to Reports: ");
            String choice = keyboard.nextLine().trim().toUpperCase();

            if (choice.equals("H")) {
                // Go back to Home Menu properly
                while (true) {
                    homeScreen();
                    menuSelector(keyboard, transactions);
                }
            } else if (choice.equals("R")) {
                // Go directly back to reports menu
                reports(keyboard, transactions);
                return; // after finishing reports, return to previous menu
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    public static void sortNewest(ArrayList<Transactions> transactions){
        for (int i = 0; i < transactions.size(); i++) {
            for (int j = i + 1; j < transactions.size(); j++) {
                Transactions t1 = transactions.get(i);
                Transactions t2 = transactions.get(j);

                if (t1.getDate().isBefore(t2.getDate())) {
                    transactions.set(i, t2);
                    transactions.set(j, t1);
                }

                else if (t1.getDate().isEqual(t2.getDate())) {
                    if (t1.getTime().isBefore(t2.getTime())) {
                        transactions.set(i, t2);
                        transactions.set(j, t1);
                    }
                }

            }
        }
    }
    public static void saveToFile(Scanner keyboard, ArrayList<Transactions> transactionsToSave,String reportTitle){
        System.out.print("Do you want to save this report? (Yes/Y or No/N): ");
        String save = keyboard.nextLine().trim().toUpperCase();

        if (save.equals("YES") || save.equals("Y")) {
            System.out.print("Enter the file name to save as: ");
            String fileName = keyboard.nextLine().trim();

            try (FileWriter fileSaver = new FileWriter("src/main/resources/" + fileName + ".txt")) {
                fileSaver.write("==================================== " + reportTitle + " =======================================\n");

                fileSaver.write(String.format("%-12s | %-8s | %-25s | %-20s | %10s%n",
                        "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT"));
                fileSaver.write("-----------------------------------------------------------------------------------------\n");

                for (Transactions t : transactionsToSave) {
                    fileSaver.write(String.format("%-12s | %-8s | %-25s | %-20s | %10.2f%n",
                            t.getDate(),
                            t.getTime().toString().substring(0,5),
                            t.getDescription(),
                            t.getVendor(),
                            t.getAmount()));
                }
                fileSaver.write("-----------------------------------------------------------------------------------------\n");

                System.out.println("File saved successfully as " + fileName + ".txt");

            } catch (IOException e) {
                System.out.println("Error saving file!");
                e.printStackTrace();
            }
        } else {
            System.out.println("File not saved.");
        }
    }


    public static void printTransactions(Transactions t){
        System.out.printf("%-12s | %-8s | %-25s | %-20s | %10.2f%n",
                t.getDate(),
                t.getTime().toString().substring(0, 5),
                t.getDescription(),
                t.getVendor(),
                t.getAmount());
    }


    public static void exit(Scanner keyboard) {
        keyboard.close();
        System.out.println("Exiting Vault Book. Have a nice day!");
        System.exit(0);
    }


    public static void loadTransactions(ArrayList<Transactions> transactions) {
        String filePath = "src/main/resources/transactions.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
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

