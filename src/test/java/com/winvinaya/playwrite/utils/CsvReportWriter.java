package com.winvinaya.playwrite.utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CsvReportWriter {

    private BufferedWriter writer;
    private String browser;
    private String environment;
    public static String Result = "FAILED"; // Default to FAILED

    private static boolean allPassed = true; // Track if all tests passed

    // Constructor opens file in overwrite mode to clear old data
    public CsvReportWriter(String filePath, String browser, String environment) throws IOException {
        this.browser = browser;
        this.environment = environment;

        writer = new BufferedWriter(new FileWriter(filePath, false)); // overwrite mode

        // Write header row freshly
        writer.write("Browser/App,Environment,TCID,TEST RESULT,TIME TAKEN (in secs.),TEST DESCRIPTION,TIME STAMP\n");
        writer.flush();
    }

    // Write a single test result row
    public void writeRow(String tcID, String description, String result, double durationInSeconds) throws IOException {
        // If any test fails, mark allPassed false
        if (!"PASSED".equalsIgnoreCase(result) && allPassed == true) {
            allPassed = false;
        }

        // Format timestamp
        String timestamp = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH).format(new Date());

        // Format duration
        String formattedDuration = (durationInSeconds <= 0)
                ? "Less than a min"
                : (durationInSeconds <= 9) ? String.format("0%.0f", durationInSeconds)
                : String.format("%.0f", durationInSeconds);

        String row = String.format("%s,%s,%s,%s,%s,%s,%s\n",
                browser,
                environment,
                tcID,
                result,
                formattedDuration,
                description,
                timestamp
        );

        writer.write(row);
        writer.flush();
    }

    // Call this after all tests are written to finalize Result
    public static void finalizeResult() {
        if (allPassed) {
            Result = "PASSED";
        } else {
            Result = "FAILED";
        }
    }

    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }
}
