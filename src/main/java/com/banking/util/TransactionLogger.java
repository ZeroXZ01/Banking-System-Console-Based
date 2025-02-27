package com.banking.util;

import com.banking.db.DatabaseConnection;
import com.banking.util.FileReporter;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransactionLogger {
    private final DatabaseConnection dbConnection;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // added DateTimeFormatter for log to file

    public TransactionLogger() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public void logTransaction(String accountId, BigDecimal amount) {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO transactions (account_id, amount) VALUES (?, ?)"
             )) {
            stmt.setString(1, accountId);
            stmt.setBigDecimal(2, amount);
            stmt.executeUpdate();

            // Also log to file*
            String activityType = amount.compareTo(BigDecimal.ZERO) > 0 ? "DEPOSIT" : "WITHDRAWAL";
            String logEntry = String.format("[%s] %s: Account %s - $%.2f",
                    LocalDateTime.now().format(FORMATTER),
                    activityType,
                    accountId,
                    amount.abs());

            try {
                FileReporter.logActivity(logEntry);
            } catch (IOException e) {
                System.err.println("Failed to log activity to file: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Failed to log transaction: " + e.getMessage());
        }
    }

    public void logTransfer(String fromAccountId, String toAccountId, BigDecimal amount) {
        // Log transfer activity to file*
        String logEntry = String.format("[%s] TRANSFER: From %s to %s - $%.2f",
                LocalDateTime.now().format(FORMATTER),
                fromAccountId,
                toAccountId,
                amount);

        try {
            FileReporter.logActivity(logEntry);
        } catch (IOException e) {
            System.err.println("Failed to log transfer activity to file: " + e.getMessage());
        }
    }

    public List<String> readTransactionHistory() {
        List<String> history = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT transaction_date, account_id, amount FROM transactions ORDER BY transaction_date DESC"
             );
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                history.add(String.format("%s,%s,%.2f",
                    rs.getTimestamp("transaction_date"),
                    rs.getString("account_id"),
                    rs.getBigDecimal("amount")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Failed to read transaction history: " + e.getMessage());
        }
        return history;
    }

    public List<String> readTransactionHistory(String accountId) {
        List<String> history = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT transaction_date, amount FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC"
             )) {
            stmt.setString(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    history.add(String.format("%s,%s,%.2f",
                        rs.getTimestamp("transaction_date"),
                        accountId,
                        rs.getBigDecimal("amount")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to read transaction history: " + e.getMessage());
        }
        return history;
    }

    public void clearTransactions() {
        try (Connection conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM transactions"
            )) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to clear transactions: " + e.getMessage());
        }
    }
}
