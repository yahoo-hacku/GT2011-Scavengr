package net.moosen.huntr;

import static net.moosen.huntr.api.ApiHandler.PREF_API_KEY;
import static net.moosen.huntr.api.ApiHandler.PREF_USERNAME;
import net.moosen.huntr.activities.HomeTabsActivity;
import net.moosen.huntr.activities.account.AccountLoginActivity;
import net.moosen.huntr.api.ApiHandler;
import net.moosen.huntr.api.ApiHandler.API_ACTION;
import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * TODO: Enter class description.
 */
public class HuntrMainActivity extends Activity {

	private static final int PROGRESS = 0x1;

	private ProgressBar progress;

	private final int progress_status = 0;

	private final Handler handler = new Handler();

	private final boolean ready = false;

	private void handlePingResult(final boolean login_valid) {
		runOnUiThread(new Runnable() {

			public void run() {
				final Intent next = new Intent();
				if (login_valid) {
					next.setClass(HuntrMainActivity.this, HomeTabsActivity.class);
				} else {
					next.setClass(HuntrMainActivity.this, AccountLoginActivity.class);
				}
				startActivity(next);
				finish();
			}
		});
	}

	private void check_login() {
		new Thread(new Runnable()
		{

			public void run()
			{
				ApiHandler.GetInstance().setPreferences(getPreferences(Context.MODE_PRIVATE));
				SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
				final String api_key = preferences.getString(PREF_API_KEY, "");
				final String username = preferences.getString(PREF_USERNAME, "");

				Log.d(getClass().getCanonicalName(), "Key: " + api_key);
				try {
					if (api_key.isEmpty()) {
						// no api key, we need to send them to the login page.
						Log.d(getClass().getCanonicalName(), "###### API KEY WAS EMPTY");
						handlePingResult(false);
						return;
					} else {
						// ping the server to check the api_key
						if (!username.isEmpty()) {
							// ping server
							try {
								Log.d(getClass().getCanonicalName(), "###### PINGING");
								ApiHandler.GetInstance().doAction(API_ACTION.PING);
								handlePingResult(true);
								return;

							} catch (final Exception ex) {
								Log.d(getClass().getCanonicalName(), "Caught exception. Redirecting to login.");
								handlePingResult(false);
								return;
							}
						} else {
							// send them to the login page.
							Log.d(getClass().getCanonicalName(), "###### NO USERNAME");
							handlePingResult(false);
							return;
						}
					}
				} catch (final Exception ex) {
					Toast.makeText(HuntrMainActivity.this, "WTF LOGIN!?!?!?", Toast.LENGTH_SHORT);
				}

			}
		}).start();
	}

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		setContentView(R.layout.main);
		progress = (ProgressBar) findViewById(R.id.progress_main);
		progress.setProgress(50);

	}
}
