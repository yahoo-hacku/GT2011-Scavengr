package net.moosen.huntr.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import net.moosen.huntr.activities.quests.dto.UserQuestDto;
import net.moosen.huntr.activities.quests.dto.UserQuestStepDto;
import net.moosen.huntr.exceptions.AuthenticationException;
import net.moosen.huntr.exceptions.StaleApiTokenException;

import static net.moosen.huntr.utils.Messages.Debug;
import static net.moosen.huntr.utils.Messages.Error;

/**
 * TODO: Enter class description.
 */
public class ApiHandler
{
    private static ApiHandler s_instance = new ApiHandler();

    public static ApiHandler GetInstance() { return s_instance; }

    public static final String WEB_HOST = "192.168.1.102";

    //public static final String WEB_HOST = "128.61.120.161";

    public static final Integer WEB_PORT = 3000;

    public static final String PREF_API_KEY = "user.api_key";

    public static final String PREF_USERNAME = "user.name";

    public static enum API_ACTION
    {
        PING("GET", "ping", "")
        {
            public <T> T handleResponse(final InputStream response) throws AuthenticationException
            {
                final String api_key = GetInstance().prefs.getString(PREF_API_KEY, "");
                Debug("###########API KEY " + api_key);
                //Log.d(getClass().getCanonicalName(), "###########API KEY " + api_key);
                final ApiToken token = new ApiToken().withToken(api_key);
                final String username = GetInstance().prefs.getString(PREF_USERNAME, "");
                Debug("pinging with: " + token.getToken() + " and: " + username);
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
        REGISTER("GET", "register", "")
        {
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
        LOGIN("GET", "login", "")
        {
            public <T> T handleResponse(final InputStream response) throws AuthenticationException
            {
                Reader response_reader = new InputStreamReader(response);
                ApiToken token = new Gson().fromJson(response_reader, ApiToken.class);
                Debug("HANDLING LOGIN: " + token.getToken());
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
        LOGOUT("GET", "logout", "")
        {
            public <T> T handleResponse(final InputStream response)
            {
                GetInstance().clearCredentials();
                return null;
            }
        },
        QUESTS("GET", "quests", "")
        {
            @SuppressWarnings({"unchecked"})
            public <T> T handleResponse(final InputStream response)
            {
                Reader response_reader = new InputStreamReader(response);
                Type collectionType = new TypeToken<Collection<QuestDto>>(){}.getType();
                Collection<QuestDto> quest_col = new Gson().fromJson(response_reader, collectionType);
                return (T) new ArrayList<QuestDto>(quest_col);
            }
        },
        USER_QUESTS("GET", "user_quests", "")
        {
            @SuppressWarnings({"unchecked"})
            public <T> T handleResponse(final InputStream response)
            {
                Reader response_reader = new InputStreamReader(response);
                Type collectionType = new TypeToken<Collection<UserQuestDto>>(){}.getType();
                Collection<UserQuestDto> quest_col = new Gson().fromJson(response_reader, collectionType);

                return (T) new ArrayList<UserQuestDto>(quest_col);
            }
        },
        STEPS("GET", "steps", "quests")
        {
            @SuppressWarnings({"unchecked"})
            public <T> T handleResponse(final InputStream response)
            {
                Reader response_reader = new InputStreamReader(response);
                Type collectionType = new TypeToken<Collection<QuestStepDto>>(){}.getType();
                Collection<QuestStepDto> quest_col = new Gson().fromJson(response_reader, collectionType);
                return (T) new ArrayList<QuestStepDto>(quest_col);
            }
        },
        USER_STEPS("GET", "user_steps", "user_quests")
        {
            @SuppressWarnings({"unchecked"})
            public <T> T handleResponse(final InputStream response)
            {
                Reader response_reader = new InputStreamReader(response);
                Type collectionType = new TypeToken<Collection<UserQuestStepDto>>(){}.getType();
                Collection<UserQuestStepDto> quest_col = new Gson().fromJson(response_reader, collectionType);

                return (T) new ArrayList<UserQuestStepDto>(quest_col);
            }

            @Override
            protected boolean hasParentAction(){ return true; }
        },
        USER_STEPS_COMPLETE("PUT", "user_steps", "user_quests")
        {
            @SuppressWarnings({"unchecked"})
            public <T> T handleResponse(final InputStream response)
            {
                Reader response_reader = new InputStreamReader(response);
                return (T) new Gson().fromJson(response_reader, UserQuestStepDto.class);
            }

            @Override
            protected boolean hasParentAction(){ return true; }

            @Override
            protected boolean isUpdateAction() { return true; }
        };

        private final String action_string, parent_action, http_verb;
        private API_ACTION(final String http_verb, final String action_string, final String parent_action)
        {
            this.http_verb = http_verb;
            this.action_string = action_string;
            this.parent_action = parent_action;

        }

        public String getActionString() { return this.action_string; }

        public String getParentAction() { return this.parent_action; }

        public String getHttpVerb() { return this.http_verb; }

        protected boolean hasParentAction() { return false; }

        protected boolean isEditAction() { return false; }

        protected boolean isUpdateAction() { return false; }

        protected abstract <T> T handleResponse(final InputStream response) throws AuthenticationException;
    }

    private ApiToken token = null;
    private String username = null;
    private SharedPreferences prefs = null;

    private ApiHandler() { this.token = new ApiToken(); }

    private void setToken(final ApiToken token) { Debug("###### SETTING TOKEN: " + token.getToken()); this.token = token; }

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
            Debug("###### WRITING CREDENTIALS TO PREFS: " + token.getToken() + " username: " + username);
            prefs.edit()
                    .putString(PREF_API_KEY, token.getToken())
                    .putString(PREF_USERNAME, username)
                    .commit();
        }
    }

    @SuppressWarnings({"unchecked"})
    public <T> T doAction(final API_ACTION action, final Pair<String, String> ... args)
            throws AuthenticationException, StaleApiTokenException
    {
        String url_string = String.format("http://%s:%d/api/", WEB_HOST, WEB_PORT);
        T return_value = null;

        try
        {
            String params = "";
            if (action.hasParentAction()) // means the first two parameters should be [parent_id, child_id, args] PERIOD.
            {
                url_string += String.format("%s/%d/%s",
                            action.getParentAction(),
                            Integer.parseInt(args[0].second), // parent id
                            action.getActionString());
                if (action.isUpdateAction()) url_string += String.format("/%d", Integer.parseInt(args[1].second));
            }
            else url_string += action.getActionString();

            for (int i = 0; i < args.length; i++)
            {
                String separator = (i == 0 ? "?" : "&");
                try // to see if this is a date being sent.
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf.parse(args[i].second);
                    params += String.format("%s%s='%s'", separator, args[i].first, args[i].second.replaceAll(" ", "%20"));
                }
                catch (final ParseException ex)
                {
                    params += String.format("%s%s=%s", separator, args[i].first, args[i].second);
                }
            }

            Debug("############## URL STRING: " + url_string);
            Debug("############## URL PARAMS: " + params);
            if (!action.isEditAction()) url_string += params;
            final URL url = new URL(url_string);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Connection", "close");
            connection.setRequestMethod(action.getHttpVerb());

            if (!action.equals(API_ACTION.LOGIN))
            {
                String credentials = String.format("%s:%s", username, token.getToken());
                connection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT).trim());
            }
            else this.username = args[0].second;

            if (action.isUpdateAction() || action.isEditAction())
            {
                connection.setDoOutput(true);
                final OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(params); out.flush(); out.close();
            }

            int response_code = connection.getResponseCode();
            if (response_code != HttpURLConnection.HTTP_UNAUTHORIZED)
            {

                try
                {
                    final InputStream response = connection.getInputStream();
                    return (T) action.handleResponse(response);
                }
                catch (final AuthenticationException ex)
                {
                    Error("ACTION RESULTED IN AN AUTH EXCEPTION. THROWING: " + ex.getMessage());
                    throw new AuthenticationException(ex.getMessage());
                }
                catch (final IOException ex) { Debug("Connection had no response."); }
                finally { connection.disconnect(); }
            }
            else
            {
                connection.disconnect();
                throw new StaleApiTokenException("Api Token has become stale.");
            }
        }
        catch (final MalformedURLException ex) { Error("Bad URL", ex); }
        catch (final IOException ex) { Error("IO Exception: ", ex); }
        return null;
    }

    private boolean authenticate(final ApiToken api_key, final String username)
    {
        final API_ACTION action = API_ACTION.PING;
        final String url_string = String.format("http://%s:%d/api/%s", WEB_HOST, WEB_PORT, action.getActionString());
        Debug("Ping string: " + url_string);
        try
        {
            final URL url = new URL(url_string);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            final String credentials = String.format("%s:%s", username, api_key.getToken());
            connection.setRequestMethod(action.getHttpVerb());
            connection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT).trim());
            connection.addRequestProperty("Accept", "application/json");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED)
            {
                // YOU DUN GOOFED
                Debug("That shit ain't right, punk!");
                return false;
            }
            Debug("PONG!");
            connection.disconnect();
        }
        catch (final MalformedURLException ex) { Error("Bad URL", ex); return false; }
        catch (final IOException ex) { Error("Bad URL", ex); return false; }
        return true;
    }
}
