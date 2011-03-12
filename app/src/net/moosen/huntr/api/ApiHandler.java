package net.moosen.huntr.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.moosen.huntr.activities.quests.dto.QuestDto;
import net.moosen.huntr.activities.quests.dto.QuestStepDto;
import net.moosen.huntr.exceptions.AuthenticationException;

/**
 * TODO: Enter class description.
 */
public class ApiHandler
{
    private static ApiHandler s_instance = new ApiHandler();

    public static ApiHandler GetInstance() { return s_instance; }

    public static final String WEB_HOST = "143.215.118.119";

    public static final Integer WEB_PORT = 3001;

    public static final String PREF_API_KEY = "user.api_key";

    public static final String PREF_USERNAME = "user.name";

    public static enum API_ACTION
    {
        PING("ping", "GET")
        {
            public <T> T handleResponse(final InputStream response) throws AuthenticationException
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

                return null;
            }
        },
        REGISTER("register", "GET"){
            public <T> T handleResponse(final InputStream response) throws AuthenticationException
            {
                Reader response_reader = new InputStreamReader(response);
                ApiToken token = new Gson().fromJson(response_reader, ApiToken.class);
                if (token.getToken() == null)
                {
                    throw new AuthenticationException("Username or Password was incorrect!");
                }
                else
                {
                    GetInstance().setToken(token);
                    GetInstance().writeCredentials();
                }
                return null;
            }
        },
        LOGIN("login", "GET")
        {
            public <T> T handleResponse(final InputStream response) throws AuthenticationException
            {
                Reader response_reader = new InputStreamReader(response);
                ApiToken token = new Gson().fromJson(response_reader, ApiToken.class);
                Log.d(getClass().getCanonicalName(), "HANDLING LOGIN: " + token.getToken());
                if (token.getToken() == null)
                {
                    Log.d(getClass().getCanonicalName(), "Login failed.");
                    throw new AuthenticationException("Username or Password was incorrect!");
                }
                else
                {
                    GetInstance().setToken(token);
                    GetInstance().writeCredentials();
                }
                return null;
            }
        },
        LOGOUT("logout", "GET")
        {
            public <T> T handleResponse(final InputStream response)
            {
                GetInstance().clearCredentials();
                return null;
            }
        },
        QUESTS("quests", "GET")
        {
            @SuppressWarnings({"unchecked"})
            public <T> T handleResponse(final InputStream response)
            {
                Reader response_reader = new InputStreamReader(response);
                Gson gson = new Gson();
                Type collectionType = new TypeToken<Collection<QuestDto>>(){}.getType();
                Collection<QuestDto> quest_col = gson.fromJson(response_reader, collectionType);
                return (T) new ArrayList<QuestDto>(quest_col);
            }
        },
        STEPS("steps", "GET")
        {
            @SuppressWarnings({"unchecked"})
            public <T> T handleResponse(final InputStream response)
            {
                Reader response_reader = new InputStreamReader(response);
                Gson gson = new Gson();
                Type collectionType = new TypeToken<Collection<QuestStepDto>>(){}.getType();
                Collection<QuestStepDto> quest_col = gson.fromJson(response_reader, collectionType);
                return (T) new ArrayList<QuestStepDto>(quest_col);
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

        protected abstract <T> T handleResponse(final InputStream response) throws AuthenticationException;
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
        Log.d(getClass().getCanonicalName(), "###### SETTING TOKEN: " + token.getToken());
        this.token = token;
    }

    public void setPreferences(final SharedPreferences prefs)
    {
        this.prefs = prefs;
        this.token = new ApiToken().withToken(prefs.getString(PREF_API_KEY, ""));
        this.username = prefs.getString(PREF_USERNAME, "");
    }

    protected void clearCredentials()
    {
        if (prefs != null)
        {
            prefs.edit()
                    .putString(PREF_API_KEY, null)
                    .putString(PREF_USERNAME, null)
                    .commit();
        }
    }

    protected void writeCredentials()
    {
        if (prefs != null)
        {
            Log.d(getClass().getCanonicalName(), "###### WRITING CREDENTIALS TO PREFS: " + token.getToken() + " username: " + username);
            prefs.edit()
                    .putString(PREF_API_KEY, token.getToken())
                    .putString(PREF_USERNAME, username)
                    .commit();
        }
    }

    public <T> T doAction(final API_ACTION action, final Pair<String, String> ... args) throws AuthenticationException
    {
        String url_string = String.format("http://%s:%d/api/",
                WEB_HOST, WEB_PORT);
        T return_value = null;

        try
        {
            String action_str;
            if (action.equals(API_ACTION.STEPS))
            {
                action_str = String.format("quests/%d/steps", Integer.parseInt(args[0].second));
            }
            else
            {
                action_str = action.getActionString();
            }
            if (action.getHttpVerb().equals("GET"))
            {
                url_string += action_str;
                for (int i = 0; i < args.length; i++)
                {
                    String separator = (i == 0 ? "?" : "&");
                    url_string += String.format("%s%s=%s", separator, args[i].first, args[i].second);
                }
            }
            else
            {
                url_string += action_str;
            }



            Log.d(getClass().getCanonicalName(), "##############URL STRING: " + url_string);

            URL url = new URL(url_string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Connection", "close");
            connection.setRequestMethod(action.getHttpVerb());

            if (!action.equals(API_ACTION.LOGIN))
            {
                String credentials = String.format("%s:%s", username, token.getToken());
                connection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT).trim());
            }
            else
            {
                this.username = args[0].second;
            }

            connection.connect();
            int response_code = connection.getResponseCode();
            if (response_code != HttpURLConnection.HTTP_UNAUTHORIZED)
            {
                AuthenticationException exception = null;
                InputStream response = connection.getInputStream();
                try
                {

                    return_value = (T) action.handleResponse(response);

                }
                catch (final AuthenticationException ex)
                {
                    Log.d(getClass().getCanonicalName(), "ACTION RESULTED IN AN AUTH EXCEPTION. THROWING: " + ex.getMessage());
                    exception = new AuthenticationException(ex.getMessage());
                }
                /*finally
                {
                    response.close();
                }*/
                if (exception != null)
                {
                    throw exception;
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
        return return_value;
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
