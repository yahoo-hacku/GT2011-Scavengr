package net.moosen.huntr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import net.moosen.huntr.activities.HomeTabsActivity;
import net.moosen.huntr.activities.account.AccountLoginActivity;
import net.moosen.huntr.exceptions.AuthenticationException;

/**
 * TODO: Enter class description.
 */
public class HuntrMainActivity extends Activity
{
    private static final int PROGRESS = 0x1;

    private ProgressBar progress;

    private int progress_status = 0;

    private Handler handler = new Handler();

    private boolean ready = false;

    private static enum PROGRESS_STATE
    {
        LOOP,
        LOGIN,
        HOME_TABS
    }

    private PROGRESS_STATE progress_state = PROGRESS_STATE.LOOP;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.main);
        progress = (ProgressBar) findViewById(R.id.progress_main);
        final Intent login = new Intent(), home_tabs = new Intent();
        login.setClass(this, AccountLoginActivity.class);
        home_tabs.setClass(this, HomeTabsActivity.class);

        // Start lengthy operation in a background thread
        new Thread(new Runnable()
        {
            public void run()
            {
                boolean die = false;
                while (progress_status < 100 && !die)
                {
                    try { Thread.sleep(10); }
                    catch (final InterruptedException ex) {}

                    switch (progress_state)
                    {
                        case LOOP:
                            progress_status++;
                            break;
                        case LOGIN:
                            startActivity(login);
                            finish();
                            die = true;
                            break;
                        case HOME_TABS:
                            startActivity(home_tabs);
                            finish();
                            die = true;
                            break;
                        default:
                            Log.d(getClass().getCanonicalName(), "Received an unexpected progress state code...");
                            progress_state = PROGRESS_STATE.LOOP;
                            break;
                    }

                    // Update the progress bar
                    handler.post(new Runnable()
                    {
                        public void run()
                        {
                            progress.setProgress(progress_status);
                        }
                    });
                }
            }
        }).start();

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        final String api_key = preferences.getString("user.api_key", "");
        final String username = preferences.getString("user.name", "");
        try
        {
            if (api_key.isEmpty())
            {
                // no api key, we need to send them to the login page.
                progress_state = PROGRESS_STATE.LOGIN;
            }
            else
            {
                // ping the server to check the api_key
                if (!username.isEmpty())
                {
                    // ping server
                    try
                    {
                        ApiHandler.GetInstance().setCredentials(api_key, username);
                        progress_state = PROGRESS_STATE.HOME_TABS;

                    }
                    catch (final AuthenticationException ex)
                    {
                        progress_state = PROGRESS_STATE.LOGIN;
                    }
                }
                else
                {
                    // send them to the login page.
                    progress_state = PROGRESS_STATE.LOGIN;
                }
            }
        }
        catch (final Exception ex)
        {
            progress_state = PROGRESS_STATE.LOOP;
        }
    }
}
