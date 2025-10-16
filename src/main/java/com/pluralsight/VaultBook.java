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
                               VAULT BOOK
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
    // ===============================
    //  Home Menu and Menu Selector
    // ===============================

    public static void homeScreen() {
        System.out.println("""
        ====================================
                     HOME MENU              
        ====================================
        |  D  | Add Deposit                |
        |  P  | Make Payment               |
        |  B  | Show Balance               |
        |  L  | Ledger                     |
        |  X  | Exit                       |
        ====================================
        """);
    }

    public static void menuSelector(Scanner keyboard, ArrayList<Transactions> transactions) {
        System.out.print("Please choose your option: ");
        String choice = keyboard.nextLine().trim().toUpperCase();

        switch (choice) {
            case "D":
                addDeposit(keyboard, transactions);
                break;
            case "P":
                makePayment(keyboard, transactions);
                break;
            case "B":
                showBalance(transactions);
                break;
            case "L":
                ledger(keyboard, transactions);
                break;
            case "X":
                exit(keyboard);
                break;
            default:
                System.out.println("Please enter a valid option");
                break;
        }
    }

    // ============================================================
    //                    Home Screen Methods
    // ------------------------------------------------------------
    // Manages main menu options and user input, including:
    // - Adding deposits and payments
    // - Accessing the Ledger screen
    // - Exiting the application
    // ============================================================

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

        System.out.println("Transaction saved successfully!");
        System.out.println();
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

        amount = -amount;
        System.out.print("Enter description: ");
        String description = keyboard.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = keyboard.nextLine();

        Transactions payment = new Transactions(date, time, description, amount, vendor);

        transactions.add(payment);
        appendTransaction(payment);

        System.out.println("Payment made successfully!");
        System.out.println();
    }

    public static void showBalance(ArrayList<Transactions> transactions) {
        double balance = getTotalBalance(transactions);
        System.out.printf("Your current total balance is: $%.2f%n", balance);
        System.out.println();
    }

    // ===============================
    //         Ledger & Menu
    // ===============================

    public static void ledger(Scanner keyboard,ArrayList<Transactions> transactions) {

        while (true) {
            ledgerMenu();
            System.out.print("Please enter your option: ");
            String option = keyboard.nextLine().trim().toUpperCase();

            switch (option) {
                case "A":
                    viewAllTransactions(transactions,keyboard);
                    break;
                case "D":
                    allDeposits(transactions,keyboard);
                    break;
                case "P":
                    allPayments(transactions,keyboard);
                    break;
                case "R":
                    reports(keyboard, transactions);
                    break;
                case "H":
                    System.out.println("Returning to Home Screen...");
                    return;

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
        |  A  | Display All Transactions  |
        |  D  | View All Deposits         |
        |  P  | View All Payments         |
        |  R  | Reports                   |
        |  H  | Return to Home Screen     |
        ====================================
        """);
    }
// ============================================================
//                      Ledger Methods
// ------------------------------------------------------------
// Responsible for ledger display and management, including:
// - Viewing all transactions (newest first)
// - Filtering deposits and payments
// - Navigating to the Reports screen
// - Returning to the Home screen
// ============================================================

    public static void viewAllTransactions(ArrayList<Transactions> transactions,Scanner keyboard) {
        sortNewest(transactions);

        System.out.println();
        System.out.println("==================================== ALL TRANSACTIONS ====================================");
        printTableHeader();
        ArrayList<Transactions> reportTransactions = new ArrayList<>();

        if(transactions.isEmpty()) {
            System.out.println("There are no transactions in the system.");
        } else {
            for (Transactions t : transactions) {
                printTransactions(t);
                reportTransactions.add(t);
            }
        }
        printTableFooter();
        saveToFile(keyboard, reportTransactions,"ALL TRANSACTIONS");
    }
    public static void allDeposits(ArrayList<Transactions> transactions,Scanner keyboard) {
        System.out.println();
        System.out.println("==================================== ALL DEPOSITS ====================================");
        printTableHeader();
        ArrayList<Transactions> reportTransactions = new ArrayList<>();

        for(Transactions t : transactions){
            if(t.getAmount() > 0){
                printTransactions(t);
                reportTransactions.add(t);
            }
        }
        printTableFooter();
        saveToFile(keyboard, reportTransactions,"All DEPOSITS");
    }
    public static void allPayments(ArrayList<Transactions> transactions,Scanner keyboard) {
        System.out.println();
        System.out.println("==================================== ALL PAYMENTS =======================================");
        printTableHeader();
        ArrayList<Transactions> reportTransactions = new ArrayList<>();

        for(Transactions t : transactions){
            if(t.getAmount() < 0){
                printTransactions(t);
                reportTransactions.add(t);
            }
        }
        printTableFooter();
        saveToFile(keyboard, reportTransactions,"All PAYMENTS");
    }

    // ===============================
    //         Reports & Menu
    // ===============================

    public static void reports(Scanner keyboard, ArrayList<Transactions> transactions) {

        while(true){
            reportsMenu();
            System.out.println();
            System.out.print("Please enter your option: ");
            String choice = keyboard.nextLine().trim().toUpperCase();

            switch (choice){
                case "1":
                     monthToDate(keyboard,transactions);
                     break;
                case "2":
                     previousMonth(keyboard,transactions);
                     break;
                case "3":
                     yearToDate(keyboard,transactions);
                     break;
                case "4":
                     previousYear(keyboard,transactions);
                     break;
                case "5":
                     searchByVendor(keyboard,transactions);
                     break;
                case "0":
                    System.out.println("Returning to Ledger...");
                    System.out.println();
                    return;
                case "H":
                    System.out.println("Returning to Home Screen...");
                    System.out.println();
                    return;

                    default:
                    System.out.println("Invalid option. Try again.");
                        System.out.println();
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
            |  0  | Back to Ledger             |
            |  H  | Back to Home               |
            ====================================
            """);
    }
    // ============================================================
    //                      Reports Methods
    // ------------------------------------------------------------
    // Handles all reporting features, including:
    // - Month-to-Date, Previous Month, and Year-to-Date reports
    // - Previous Year report
    // - Vendor search functionality
    // - (Optional) Custom search filters for date, vendor, and amount
    // ============================================================

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
                reportTransactions.add(t);
            }
        }
        printTableFooter();
        saveToFile(keyboard, reportTransactions, "Previous Month");
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
                reportTransactions.add(t);
            }
        }
        printTableFooter();
        saveToFile(keyboard, reportTransactions,"Year to Date");
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
                reportTransactions.add(t);
                found = true;
            }
        }
        if (!found){
            System.out.println("There is no transaction with that year.");
        }
        printTableFooter();
        saveToFile(keyboard, reportTransactions,"Previous Year");
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
                reportTransactions.add(t);
                found = true;
            }
        }

        if (!found) {
            System.out.println("There is no transaction with that vendor.");
        }

        printTableFooter();
        saveToFile(keyboard, reportTransactions, "Vendor Report");
    }

// ============================================================
//                     Helper Methods
// ------------------------------------------------------------
// Includes:
// - Header and footer display
// - Navigation and sorting functions
// - Transaction printing and saving
// - File loading and appending
// - Balance calculation
// - Exit handling
// ============================================================


    public static void printTableHeader(){
        System.out.printf("%-12s | %-8s | %-25s | %-20s | %10s%n",
                "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    public static void printTableFooter(){
        System.out.println("-----------------------------------------------------------------------------------------");
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
        System.out.println();
        System.out.print("Do you want to save this report? (Y/N): ");
        String save = keyboard.nextLine().trim().toUpperCase();

        if (save.equals("YES") || save.equals("Y")) {
            System.out.println();
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
        System.out.println("Thank you for using Vault Book!");
        System.exit(0);
    }


    public static void loadTransactions(ArrayList<Transactions> transactions) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/transactions.csv"))) {
            String line = reader.readLine(); // Skip header line if it exists

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] parts = line.split("\\|");
                if (parts.length != 5) {
                    System.out.println("Skipping line: " + line);
                    continue;
                }

                try {
                    LocalDate date = LocalDate.parse(parts[0].trim());
                    LocalTime time = LocalTime.parse(parts[1].trim());
                    String description = parts[2].trim();
                    String vendor = parts[3].trim();
                    double amount = Double.parseDouble(parts[4].trim());

                    transactions.add(new Transactions(date, time, description, amount, vendor));
                } catch (Exception e) {
                    System.out.println("Skipping invalid line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("File not found. Starting with empty ledger.");
        }
    }

    public static void appendTransaction(Transactions t) {
        try (FileWriter writer = new FileWriter(("src/main/resources/transactions.csv"), true)) {
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