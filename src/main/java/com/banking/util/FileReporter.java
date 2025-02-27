package com.banking.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FileReporter {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String REPORTS_DIR = "reports";
    private static final String LOGS_DIR = "logs";

    static {
        // Create directories if they don't exist
        new File(REPORTS_DIR).mkdirs();
        new File(LOGS_DIR).mkdirs();
    }

    /**
     * Saves a report to a file with the current date in the filename
     *
     * @param reportContent The content of the report
     * @throws IOException If there's an error writing to the file
     */
    public static void saveReport(String reportContent) throws IOException {
        String filename = REPORTS_DIR + File.separator + "reports-" + LocalDate.now().format(DATE_FORMATTER) + ".txt";
        writeToFile(filename, reportContent, true);
    }

    /**
     * Logs a transaction activity to a file with the current date in the filename
     *
     * @param activity The activity to log
     * @throws IOException If there's an error writing to the file
     */
    public static void logActivity(String activity) throws IOException {
        String filename = LOGS_DIR + File.separator + "transactions-" + LocalDate.now().format(DATE_FORMATTER) + ".txt";
        writeToFile(filename, activity, true);
    }

    private static void writeToFile(String filename, String content, boolean append) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, append))) {
            writer.write(content);
            writer.newLine();
            writer.flush();
        }
    }
}
