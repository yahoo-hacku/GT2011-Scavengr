package net.moosen.huntr.activities.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import net.moosen.huntr.R;
import net.moosen.huntr.activities.HomeTabsActivity;
import net.moosen.huntr.api.ApiHandler;
import net.moosen.huntr.api.ApiHandler.API_ACTION;

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

    protected void showErrorDialog(final String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
               .setCancelable(false)
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) { }
               });
        builder.create().show();
    }


    protected void attachButtonListeners()
    {
        final Intent home_tabs = new Intent(this, HomeTabsActivity.class);
        findViewById(R.id.c_create_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String error = null;
                // TODO: account creation validation && rpc to server.
                try {
                    final String username = ((EditText) findViewById(R.id.c_username)).getText().toString();
                    final String password = ((EditText) findViewById(R.id.c_password)).getText().toString();
                    final String email = ((EditText) findViewById(R.id.c_email)).getText().toString();
                    ApiHandler.GetInstance().doAction(API_ACTION.REGISTER,
                            new Pair<String, String>("user.name", username),
                            new Pair<String, String>("user.password", password),
                            new Pair<String, String>("user.email", email));
                } catch (final Exception ex) {
                    Log.d(getClass().getCanonicalName(), "CAUGHT EXCEPTION, DISPLAYING ALERT.");
                    error = ex.getMessage();
                } finally {
                    if (error == null) {
                        startActivity(home_tabs);
                        finish();
                    } else
                        showErrorDialog(error);
                }
            }
        });
    }
}
