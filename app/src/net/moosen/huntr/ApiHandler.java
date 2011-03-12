package net.moosen.huntr;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.moosen.huntr.exceptions.AuthenticationException;

/**
 * TODO: Enter class description.
 */
public class ApiHandler
{
    private static ApiHandler s_instance = new ApiHandler();

    public static ApiHandler GetInstance() { return s_instance; }

    public static final String WEB_HOST = "localhost";
    public static final Integer WEB_PORT = 3000;

    public static enum API_ACTION
    {
        PING("ping", "GET"),
        REGISTER("register", "GET"),
        LOGIN("login", "GET"),
        LOGOUT("logout", "GET"),
        QUESTS("quests", "GET");

        private final String action_string, http_verb;
        private API_ACTION(final String action_string, final String http_verb)
        {
            this.action_string = action_string;
            this.http_verb = http_verb;
        }

        public String getActionString()
        {
            return this.action_string;
        }

        public String getHttpVerb()
        {
            return this.http_verb;
        }
    }

    private String api_key = null, username = null, password = null;

    private ApiHandler() {}

    public void setCredentials(final String api_key, final String username)
            throws AuthenticationException
    {
        if (this.authenticate(api_key, username, password))
        {
            this.api_key = api_key;
            this.username = username;
            this.password = password;
        }
        else
            throw new AuthenticationException("Invalid credentials.");
    }

    private boolean authenticate(final String api_key, final String username, final String password)
    {
        final API_ACTION action = API_ACTION.PING;
        final String url_string = String.format("http://%s:%s@%s:%d/api/%s",
                username, api_key,
                WEB_HOST, WEB_PORT,
                action.getActionString());
        try
        {
            URL url = new URL(url_string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(action.getHttpVerb());
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED)
            {
                // YOU DUN GOOFED
                return false;
            }
        }
        catch (final MalformedURLException ex)
        {
            // bad url format
            return false;
        }
        catch (final IOException ex)
        {
            // connection could not be made.
            return false;
        }
        return true;
    }
}
