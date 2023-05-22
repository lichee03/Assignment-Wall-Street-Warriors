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

    private List<Notification> notifications = new ArrayList<Notification>();
    private List<String> enabledEmails = new ArrayList<String>();
    private Map<String, Double> pAndLThresholds = new HashMap<String, Double>();
    private Timer timer;

    public void configurePAndLThreshold(String userEmail, double threshold) {
        pAndLThresholds.put(userEmail, threshold);
    }

    public void enableEmailNotification(String userEmail) {
        enabledEmails.add(userEmail);
    }

    public void disableEmailNotification(String userEmail) {
        enabledEmails.remove(userEmail);
    }

    public void scheduleNotificationCheck() {
        timer = new Timer();
        timer.schedule(new NotificationTask(), 0, 60000); // check for notifications every minute
    }

    public void sendNotification(Notification notification) {
        notifications.add(notification);
        String userEmail = notification.getUserEmail();
        if (enabledEmails.contains(userEmail)) {
            try {
                // set up email properties
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                // create email session with sender's credentials
                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("sender@gmail.com", "password");
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
            } catch (MessagingException e) {
                System.out.println("Failed to send notification to " + userEmail);
                e.printStackTrace();
            }
        }
    }

    class NotificationTask extends TimerTask {

        public void run() {
            for (Map.Entry<String, Double> entry : pAndLThresholds.entrySet()) {
                String userEmail = entry.getKey();
                double threshold = entry.getValue();
                double pAndL = getUserPAndL(userEmail); // get user's current P&L
                if (pAndL >= threshold) {
                    Notification notification = new Notification(userEmail, "Your P&L has crossed the threshold of RM" + threshold);
                    sendNotification(notification);
                }
            }
        }
    }

    private double getUserPAndL(String userEmail) {
        // implement logic to get the user's current P&L
        return 0.0;
    }
}
