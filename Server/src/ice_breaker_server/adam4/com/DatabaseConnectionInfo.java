package ice_breaker_server.adam4.com;

public class DatabaseConnectionInfo
{
    public String url, user, password;
    public int connections;

    public DatabaseConnectionInfo(String url, String user, String password, int connections)
    {
        this.url = url;
        this.user = user;
        this.password = password;
        this.connections = connections;
    }

    public DatabaseConnectionInfo(String string)
    {
        String[] params = string.split(";");
        url = params[0];
        user = params[1];
        password = params[2];
    }
}
