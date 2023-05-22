/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dsgroup;

/**
 *
 * @author sharr
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

public class ReportGenerator {

    // Method to generate a report for a specific user in the specified format
    public static void generateReport(User user, String format) {
        try {
            // Create a new file with the user's ID as the file name
            File file = new File(user.getId() + "." + format);
            FileOutputStream fos = new FileOutputStream(file);
            PdfWriter writer = new PdfWriter(fos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            if (format.equals("txt")) {
                // Write the user's performance metrics to the file in plain text format
                document.add(new Paragraph("Account Balance: " + user.getAccountBalance()));
                document.add(new Paragraph("Total Profit/Loss: " + user.getTotalPnL()));
                document.add(new Paragraph("Number of Trades: " + user.getNumTrades()));
                document.add(new Paragraph("Winning Trades: " + user.getWinningTrades()));
                document.add(new Paragraph("Losing Trades: " + user.getLosingTrades()));
            } else if (format.equals("csv")) {
                // Write the user's performance metrics to the file in comma-separated value format
                document.add(new Paragraph("Account Balance,Total Profit/Loss,Number of Trades,Winning Trades,Losing Trades"));
                document.add(new Paragraph(user.getAccountBalance() + "," + user.getTotalPnL() + "," + user.getNumTrades() + ","
                        + user.getWinningTrades() + "," + user.getLosingTrades()));
            } else if (format.equals("pdf")) {
                // Write the user's performance metrics to the file in PDF format using iText library
                document.add(new Paragraph("User Performance Metrics").setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("Account Balance: " + user.getAccountBalance()));
                document.add(new Paragraph("Total Profit/Loss: " + user.getTotalPnL()));
                document.add(new Paragraph("Number of Trades: " + user.getNumTrades()));
                document.add(new Paragraph("Winning Trades: " + user.getWinningTrades()));
                document.add(new Paragraph("Losing Trades: " + user.getLosingTrades()));
            } else {
                // Handle unsupported formats
                throw new IllegalArgumentException("Unsupported format: " + format);
            }

            // Close the document
            document.close();

            // Print a success message
            System.out.println("Report generated for user " + user.getId() + " in " + format + " format");

        } catch (IOException e) {
            // Handle any exceptions
            e.printStackTrace();
        }
    }
}
