package net.moosen.huntr.activities.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import net.moosen.huntr.R;
import net.moosen.huntr.activities.HomeTabsActivity;
import net.moosen.huntr.api.ApiHandler;
import net.moosen.huntr.api.ApiHandler.API_ACTION;
import net.moosen.huntr.exceptions.AuthenticationException;

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



    protected void attachButtonListeners()
    {
        final Intent home_tabs = new Intent(this, HomeTabsActivity.class);
        findViewById(R.id.c_create_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                // TODO: account creation validation && rpc to server.
                try
                {
                    final String username = ((EditText) findViewById(R.id.c_username)).getText().toString();
                    final String password = ((EditText) findViewById(R.id.c_password)).getText().toString();
                    final String email = ((EditText) findViewById(R.id.c_email)).getText().toString();
                    ApiHandler.GetInstance().doAction(API_ACTION.REGISTER,
                            new Pair<String, String>("user.name", username),
                            new Pair<String, String>("user.password", password),
                            new Pair<String, String>("user.email", email));
                    startActivity(home_tabs);
                    finish();
                }
                catch (final AuthenticationException ex)
                {
                    // TODO: something here.
                }
                finish();
            }
        });
    }
}
