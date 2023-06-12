/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dsgroup;

/**
 *
 * @author sharr
 */
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class NotificationService {

    private List<Notification> notifications = new ArrayList<>();
    private List<String> enabledEmails = new ArrayList<>();
    private Map<String, Double> pAndLThresholds = new HashMap<>();

    public void configurePAndLThreshold(String userEmail, double threshold) {
        pAndLThresholds.put(userEmail, threshold);
    }

    public void enableEmailNotification(String userEmail) {
        enabledEmails.add(userEmail);
    }

    public void disableEmailNotification(String userEmail) {
        enabledEmails.remove(userEmail);
    }

    public void checkNotificationsAndSendEmails() {
        boolean notificationsFound = false;

        for (Map.Entry<String, Double> entry : pAndLThresholds.entrySet()) {
            String userEmail = entry.getKey();
            double threshold = entry.getValue();
            double pAndL = getUserPAndL(userEmail); // get user's current P&L
            if (pAndL >= threshold) {
                Notification notification = new Notification(userEmail, "Your P&L has crossed the threshold of RM" + threshold);
                sendNotification(notification);
                notificationsFound = true;
            }
        }

        // If no notifications were found, send a "good luck" email
        if (!notificationsFound) {
            if (!enabledEmails.isEmpty()) {
                String userEmail = enabledEmails.get(0); // Assuming there is only one enabled email
                Notification goodLuckNotification = new Notification(userEmail, "Good luck with your trading!");
                sendNotification(goodLuckNotification);
            }
        }
    }

    public boolean sendNotification(Notification notification) {
        notifications.add(notification);
        String userEmail = notification.getUserEmail();
        if (enabledEmails.contains(userEmail)) {
            try {
                // set up email properties
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");

                // create email session
                Session session = Session.getInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("sender@gmail.com", "<access_token>");
                    }
                });

                // compose message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("sender@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
                message.setSubject("Stock Trading Notification");
                message.setText(notification.getMessage());

                // send message
                Transport.send(message);

                System.out.println("Notification sent successfully to " + userEmail);
                return true;
            } catch (MessagingException e) {
                System.out.println("Failed to send notification to " + userEmail);
                e.printStackTrace();
            }
        }
        return false;
    }

    private double getUserPAndL(String userEmail) {
        // implement logic to get the user's current P&L
        return 0.0;
    }

    public static void main(String[] args) {
        // Create NotificationService instance
        NotificationService notificationService = new NotificationService();

        // Prompt user for email account
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your email account: ");
        String userEmail = scanner.nextLine();

        // Configure P&L threshold for the user
        notificationService.configurePAndLThreshold(userEmail, 1000.0);

        // Enable email notification for the user
        notificationService.enableEmailNotification(userEmail);

        // Logging statement indicating program execution
        System.out.println("Notification service started. Checking for notifications...");

        // Check notifications and send emails
        notificationService.checkNotificationsAndSendEmails();
    }
}

class Notification {
    private String userEmail;
    private String message;

    public Notification(String userEmail, String message) {
        this.userEmail = userEmail;
        this.message = message;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getMessage() {
        return message;
    }
}

