package com.banking;

import com.banking.exception.BankingException;
import com.banking.model.Account;
import com.banking.model.AccountType;
import com.banking.service.AccountService;
import com.banking.util.FileReporter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class BankingSystem {
    private final AccountService accountService;

    private BankingSystem() {
        this.accountService = new AccountService();
    }

    private static class SingletonHolder {
        private static final BankingSystem INSTANCE = new BankingSystem();
    }

    public static BankingSystem getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Account createAccount(AccountType type, String accountId, BigDecimal initialBalance) throws BankingException {
        return accountService.createAccount(type, accountId, initialBalance);
    }

    public void deposit(String accountId, BigDecimal amount) throws BankingException {
        accountService.deposit(accountId, amount);
    }

    public void withdraw(String accountId, BigDecimal amount) throws BankingException {
        accountService.withdraw(accountId, amount);
    }

    public void transfer(String fromAccountId, String toAccountId, BigDecimal amount) throws BankingException {
        accountService.transfer(fromAccountId, toAccountId, amount);
    }

    public BigDecimal getBalance(String accountId) throws BankingException {
        return accountService.getBalance(accountId);
    }

    public List<String> getTransactionHistory(String accountId) {
        return accountService.getTransactionHistory(accountId);
    }

    public List<String> getAllTransactions() {
        return accountService.getAllTransactions();
    }

    // Add this method for Account Summary Report
    public String getAccountSummaryReport() throws BankingException, IOException {
        Map<String, Object> data = accountService.getAccountSummary();

        String report=String.format(
                "ACCOUNT SUMMARY REPORT\n" +
                        "Generated: %s\n" +
                        "-------------------------\n" +
                        "Total Accounts: %d\n" +
                        "Total Balance: $%.2f\n",
                LocalDateTime.now(),
                data.get("totalAccounts"),
                data.get("totalBalance")
        );

        // Save to file
        FileReporter.saveReport(report);

        return report;
    }

    // Add this method for Daily Transactions Report
    public String getDailyTransactionReport() throws BankingException, IOException {
        Map<String, Object> data = accountService.getDailyTransactions();

        String report= String.format(
                "TODAY'S TRANSACTIONS\n" +
                        "Date: %s\n" +
                        "-------------------------\n" +
                        "Money Deposited: $%.2f\n" +
                        "Money Withdrawn: $%.2f\n" +
                        "Total Change: $%.2f\n",
                LocalDate.now(),
                data.get("totalDeposits"),
                data.get("totalWithdrawals"),
                ((BigDecimal)data.get("totalDeposits"))
                        .add((BigDecimal)data.get("totalWithdrawals"))
        );

        // Save to file
        FileReporter.saveReport(report);

        return report;
    }

    // Add this method for Account Activity Report
    public String getAccountActivityReport() throws BankingException, IOException {
        Map<String, Object> data = accountService.getAccountActivity();

        String report= String.format(
                "TOP ACCOUNTS REPORT\n" +
                        "Generated: %s\n" +
                        "-------------------------\n" +
                        "Most Active Account: %s\n" +
                        "→ Number of Transactions: %d\n\n" +
                        "Highest Balance Account: %s\n" +
                        "→ Current Balance: $%.2f\n",
                LocalDateTime.now(),
                data.get("mostActiveAccount"),
                data.get("transactionCount"),
                data.get("highestBalanceAccount"),
                data.get("highestBalance")
        );

        // Save to file
        FileReporter.saveReport(report);

        return report;
    }
}
