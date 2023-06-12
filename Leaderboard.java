import java.util.*;
public class Leaderboard {
    private List<User> users;

    public Leaderboard(List<User> userlist) {
        Database db = new Database();
        users = db.retriveUserList();
    }

    public void displayLeaderboard() {
        // Sort the users based on their points in descending order
        Collections.sort(users, new PointsComparator().reversed());
        System.out.println("Leaderboard:");
        int rank = 1;
        for (int i = 0; i < Math.min(users.size(), 10); i++) {
            User user = users.get(i);
            System.out.println(rank + ". " + user.getName() + " - Points: " + user.points);
            rank++;
        }
    }

    private static class PointsComparator implements Comparator<User> {
        @Override
        public int compare(User user2, User user1) {
            return Double.compare(user2.points, user1.points);
        }
    }

    

}

