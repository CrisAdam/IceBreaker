package ice_breaker_server.adam4.com;

public class DatabaseConnectionInfo
{
    public String url, user, password;
    public int connections;


    public DatabaseConnectionInfo(String string, int con)
    {
        String[] params = string.split(";");
        url = params[0];
        user = params[1];
        password = params[2];
        connections = con;
    }
}
