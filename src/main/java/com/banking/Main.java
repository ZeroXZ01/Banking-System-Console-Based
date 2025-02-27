package com.banking;

import com.banking.model.Account;
import com.banking.model.AccountType;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        try {
            // Start H2 web server  (http://localhost:8082)
            org.h2.tools.Server.createWebServer("-web").start();

            BankingSystem bank = BankingSystem.getInstance();

//            // Create accounts
//            System.out.println("Creating accounts...");
//            Account savings = bank.createAccount(
//                AccountType.SAVINGS,
//                "SAV001",
//                new BigDecimal("1000.00")
//            );
//            System.out.println("Created savings account: " + savings);
//
//            Account checking = bank.createAccount(
//                AccountType.CHECKING,
//                "CHK001",
//                new BigDecimal("500.00")
//            );
//            System.out.println("Created checking account: " + checking);
//
//            // Perform transactions
//            System.out.println("\nPerforming transactions...");
//
//            // Deposit to savings
//            bank.deposit("SAV001", new BigDecimal("500.00"));
//            System.out.println("Deposited $500 to savings");
//            System.out.println("Savings balance: $" + bank.getBalance("SAV001"));
//
//            // Transfer from savings to checking
//            bank.transfer("SAV001", "CHK001", new BigDecimal("300.00"));
//            System.out.println("Transferred $300 from savings to checking");
//            System.out.println("Savings balance: $" + bank.getBalance("SAV001"));
//            System.out.println("Checking balance: $" + bank.getBalance("CHK001"));
//
//            // View transaction history
//            System.out.println("\nTransaction History for SAV001:");
//            for (String transaction : bank.getTransactionHistory("SAV001")) {
//                System.out.println(transaction);
//            }
//
//            System.out.println("\nTransaction History for CHK001:");
//            for (String transaction : bank.getTransactionHistory("CHK001")) {
//                System.out.println(transaction);
//            }
//
//            // View all transactions
//            System.out.println("\nAll Transactions:");
//            for (String transaction : bank.getAllTransactions()) {
//                System.out.println(transaction);
//            }

            // 1. Create test accounts
            System.out.println("Creating test accounts...");
            bank.createAccount(AccountType.SAVINGS, "SAV001",
                    new BigDecimal("5000.00"));  // Rich account
            bank.createAccount(AccountType.CHECKING, "CHK001",
                    new BigDecimal("1000.00"));  // Active account
            bank.createAccount(AccountType.SAVINGS, "SAV002",
                    new BigDecimal("500.00"));   // Small account

            // 2. Do test transactions
            System.out.println("\nPerforming test transactions...");
            bank.deposit("CHK001", new BigDecimal("2000.00"));
            bank.withdraw("SAV002", new BigDecimal("100.00"));
            bank.transfer("SAV001", "CHK001", new BigDecimal("1000.00"));

            // 3. Show reports
            System.out.println("\n=== Account Summary ===");
            System.out.println(bank.getAccountSummaryReport());

            System.out.println("\n=== Daily Transactions ===");
            System.out.println(bank.getDailyTransactionReport());

            System.out.println("\n=== Account Activity ===");
            System.out.println(bank.getAccountActivityReport());

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Stack trace:");
            System.err.println(e.toString());
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("\tat " + element);
            }
        }
    }
}
