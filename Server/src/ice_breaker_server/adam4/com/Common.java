package ice_breaker_server.adam4.com;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common
{
    public static final String SEPARATOR = (char) 31 + "";
    public static final String ENCODING = "UTF-8";
    public final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public static Timestamp getTime()
    {
        return new Timestamp(new Date().getTime());
    }

    public static String prefixColon(String s)
    {
        if (s.startsWith(":"))
        {
            return s;
        }
        return ':' + s;
    }

    public static String removeColons(String s)
    {
        return s.replaceAll(":", "");
    }

    public static String removeNewLine(String s)
    {
        return s.replaceAll("(\\r|\\n)", "");
    }

    public static String getHostName()
    {
        try
        {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e)
        {
            
        }
        return "unknown host";

    }

    public static String readResourceFile(String fileName)
    {
        BufferedReader br;
        String line = null;
        StringBuilder output = new StringBuilder();
        try
        {

            br = new BufferedReader(new FileReader(System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() + "resources" + FileSystems.getDefault().getSeparator() + fileName));
            while ((line = br.readLine()) != null)
            {
                output.append(line);
            }
            br.close();
        }
        catch (IOException e)
        {
            return (fileName + " file not found");
        }
        return output.toString();
    }

    public static String replaceNewLines(String str)
    {
        return str.replaceAll("[\\t\\n\\r]", " ");
    }

    public static String hashPassword(String password, String salt)
    {
        String output = "hash failure";
        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance("SHA-512");

            for (int i = 0; i < 100000; i++) // note: 100000 is hard-coded such
                                             // that it does not change!
            { // any change would render all existing passwords useless!
                digest.update(password.getBytes());
                digest.update(salt.getBytes());
            }
            byte[] byteOutput = digest.digest(password.getBytes());
            BigInteger bigInt = new BigInteger(1, byteOutput);
            output = bigInt.toString(16);
            while (output.length() < 32)
            {
                output += '!';
            }
        }
        catch (NoSuchAlgorithmException e)
        {

        }
        return output;
    }

    public static Boolean isGoodUserName(String input)
    {
        if (2 > input.length() || input.length() > 20)
        {
            return false;
        }
        return !input.matches("^.*[^a-zA-Z0-9].*$");
    }

    static String getSystem()
    {
        return System.getProperty("os.name");
    }
    
    public static Boolean isValidEmail(String email)
    {
        //Set the email pattern string
        Pattern p = Pattern.compile(" (?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"
                +"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")"
                       + "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\]");

        //Match the given string with the pattern
        Matcher m = p.matcher(email);

        //check whether match is found 
        return m.matches();

    }

}