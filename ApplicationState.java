import java.sql.Connection;
import java.sql.DriverManager;

public class ApplicationState {
    private static ApplicationState instance;
    private static Connection connection;
    private static User loggedInUser;

    private ApplicationState(User loggedInUser) throws Exception {
        String url = "jdbc:mysql://localhost:3306/inventorydatabase";
        String username = "root";
        String password = "shani9944k";
        ApplicationState.connection = DriverManager.getConnection(url, username, password);
        ApplicationState.loggedInUser = loggedInUser;
    }

    public static ApplicationState getInstance(User loggedInUser) throws Exception {
        if (ApplicationState.loggedInUser == null)
            instance = new ApplicationState(loggedInUser);
        instance = new ApplicationState(ApplicationState.loggedInUser);

        return instance;
    }

    public static Connection getConnection() throws Exception{
        ApplicationState.getInstance(null);
        return ApplicationState.connection;
    }

    public static User getLoggedInUSer() {
        return ApplicationState.loggedInUser;
    }

    public static void setLoggedInUSer(User user) {
        ApplicationState.loggedInUser = user;
    }

}
