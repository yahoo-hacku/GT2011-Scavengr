package net.moosen.huntr.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import com.google.gson.Gson;
import net.moosen.huntr.exceptions.AuthenticationException;

/**
 * TODO: Enter class description.
 */
public class ApiHandler
{
    private static ApiHandler s_instance = new ApiHandler();

    public static ApiHandler GetInstance() { return s_instance; }

    public static final String WEB_HOST = "143.215.118.119";

    public static final Integer WEB_PORT = 3000;

    public static final String PREF_API_KEY = "user.api_key";

    public static final String PREF_USERNAME = "user.name";

    public static enum API_ACTION
    {
        PING("ping", "GET")
        {
            public void handleResponse(final InputStream response) throws AuthenticationException
            {
                final String api_key = GetInstance().prefs.getString(PREF_API_KEY, "");
                Log.d(getClass().getCanonicalName(), " ###########API KEY " + api_key);
                final ApiToken token = new ApiToken().withToken(api_key);
                final String username = GetInstance().prefs.getString(PREF_USERNAME, "");
                Log.d(getClass().getCanonicalName(), "pinging with: " + token.getToken() + " and: " + username);
                if (GetInstance().authenticate(token, username))
                {
                    GetInstance().token = token;
                    GetInstance().username = username;
                }
                else
                    throw new AuthenticationException("Invalid credentials.");
            }
        },
        REGISTER("register", "GET"){
            public void handleResponse(final InputStream response)
            {

            }
        },
        LOGIN("login", "GET")
        {
            public void handleResponse(final InputStream response)
            {
                Gson gson = new Gson();
                Reader response_reader = new InputStreamReader(response);
                ApiToken token = gson.fromJson(response_reader, ApiToken.class);
                GetInstance().setToken(token);
                GetInstance().writeCredentials();
            }
        },
        LOGOUT("logout", "GET")
        {
            public void handleResponse(final InputStream response)
            {

            }
        },
        QUESTS("quests", "GET")
        {
            public void handleResponse(final InputStream response)
            {

            }
        };

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



        protected abstract void handleResponse(final InputStream response) throws AuthenticationException;
    }



    private ApiToken token = null;
    private String username = null;
    private SharedPreferences prefs = null;

    private ApiHandler()
    {
        this.token = new ApiToken();
    }

    private void setToken(final ApiToken token)
    {
        this.token = token;
    }

    public void setPreferences(final SharedPreferences prefs)
    {
        this.prefs = prefs;
    }

    protected void writeCredentials()
    {
        if (prefs != null)
        {
            prefs.edit()
                    .putString(PREF_API_KEY, token.getToken())
                    .putString(PREF_USERNAME, username)
                    .commit();
        }
    }

    public void doAction(final API_ACTION action, final Pair<String, String> ... args) throws AuthenticationException
    {
        String url_string = String.format("http://%s:%d/api/%s",
                WEB_HOST, WEB_PORT,
                action.getActionString());
        try
        {
            if (action.getHttpVerb().equals("GET"))
            {
                for (int i = 0; i < args.length; i++)
                {
                    String separator = (i == 0 ? "?" : "&");
                    url_string += String.format("%s%s=%s", separator, args[i].first, args[i].second);
                }
            }
            Log.d(getClass().getCanonicalName(), "##############URL STRING: " + url_string);

            URL url = new URL(url_string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Accept", "application/json");
            connection.setRequestMethod(action.getHttpVerb());

            if (!action.equals(API_ACTION.LOGIN))
            {
                String credentials = String.format("%s:%s", username, token.getToken());
                connection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT).trim());
            }
            connection.connect();
            int response_code = connection.getResponseCode();
            if (response_code != HttpURLConnection.HTTP_UNAUTHORIZED)
            {
                // YOU DUN GOOFED
                try
                {
                    action.handleResponse(connection.getInputStream());
                }
                catch (final AuthenticationException ex)
                {
                    throw new AuthenticationException(ex.getMessage());
                }

            }
            connection.disconnect();
        }
        catch (final MalformedURLException ex)
        {
            // bad url format
            Log.d(getClass().getCanonicalName(), "Bad URL");
        }
        catch (final IOException ex)
        {
            // connection could not be made.
            Log.d(getClass().getCanonicalName(), "IO Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
        catch (final Exception ex)
        {

        }
    }

    private boolean authenticate(final ApiToken api_key, final String username)
    {
        final API_ACTION action = API_ACTION.PING;
        final String url_string = String.format("http://%s:%d/api/%s",
                WEB_HOST, WEB_PORT,
                action.getActionString());
        Log.d(getClass().getCanonicalName(), "Ping string: " + url_string);
        try
        {
            URL url = new URL(url_string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(action.getHttpVerb());
            String credentials = String.format("%s:%s", username, api_key.getToken());
            connection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT).trim());
            connection.addRequestProperty("Accept", "application/json");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED)
            {
                // YOU DUN GOOFED
                Log.d(getClass().getCanonicalName(), "SHIIIIIIT!");
                return false;
            }
            Log.d(getClass().getCanonicalName(), "PONG!");
            connection.disconnect();
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
