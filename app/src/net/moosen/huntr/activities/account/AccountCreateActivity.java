package net.moosen.huntr.activities.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import net.moosen.huntr.R;
import net.moosen.huntr.activities.HomeTabsActivity;
import net.moosen.huntr.api.ApiHandler;
import net.moosen.huntr.api.ApiHandler.API_ACTION;

import static net.moosen.huntr.utils.Messages.Error;
import static net.moosen.huntr.utils.Messages.ShowErrorDialog;

/**
 * TODO: Enter class description.
 */
public class AccountCreateActivity extends Activity {
     /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createaccount);
        attachButtonListeners();
        Intent data = getIntent();
        ((EditText) findViewById(R.id.c_username)).setText(data.getStringExtra("username"));
    }

    private Context getContext() { return this; }

    protected void attachButtonListeners()
    {
        final Intent home_tabs = new Intent(this, HomeTabsActivity.class);
        findViewById(R.id.c_create_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String error = null;
                try
                {
                    final String username = ((EditText) findViewById(R.id.c_username)).getText().toString();
                    final String password = ((EditText) findViewById(R.id.c_password)).getText().toString();
                    final String email = ((EditText) findViewById(R.id.c_email)).getText().toString();
                    ApiHandler.GetInstance().doAction(API_ACTION.REGISTER,
                            new Pair<String, String>("user.name", username),
                            new Pair<String, String>("user.password", password),
                            new Pair<String, String>("user.email", email));
                }
                catch (final Exception ex)
                {
                    Error("CAUGHT EXCEPTION, DISPLAYING ALERT.");
                    error = ex.getMessage();
                }
                finally
                {
                    if (error == null)
                    {
                        startActivity(home_tabs);
                        finish();
                    }
                    else ShowErrorDialog(getContext(), error);
                }
            }
        });
    }
}
