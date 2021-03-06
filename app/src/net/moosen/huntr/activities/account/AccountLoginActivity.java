package net.moosen.huntr.activities.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import net.moosen.huntr.R;
import net.moosen.huntr.activities.HomeTabsActivity;
import net.moosen.huntr.api.ApiHandler;
import net.moosen.huntr.api.ApiHandler.API_ACTION;
import net.moosen.huntr.api.NetworkCaller;

import static net.moosen.huntr.results.Results.ACCOUNT_CREATE_RESULT;
import static net.moosen.huntr.utils.Messages.ShowErrorDialog;

public class AccountLoginActivity extends Activity implements NetworkCaller
{

    private String m_username, m_password;
    private Context getContext() { return this; }
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        attachButtonListeners();
        attachCredentialsListeners();
    }

    @Override
    public void receivedNetworkResponse(Message m)
    {

    }

    protected void launchCreateAccount()
    {
        Intent intent = new Intent();
        intent.setClass(this, AccountCreateActivity.class);
        intent.putExtra("username", m_username);
        startActivityForResult(intent, ACCOUNT_CREATE_RESULT);
    }

    protected void attachButtonListeners()
    {
        Button login = (Button) findViewById(R.id.l_login_button), create = (Button) findViewById(R.id.l_create_button);
        final Intent home_tabs = new Intent();
        home_tabs.setClass(this, HomeTabsActivity.class);
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String error = null;
                try
                {
                    Log.d(getClass().getCanonicalName(), "You tried to login.");

                    if (m_username.equals("") || m_password.equals(""))
                    {
                        error = "Username and Password fields must not be empty!";
                    }
                    else
                    {
                        ApiHandler.GetInstance().doAction(getCaller(), API_ACTION.LOGIN,
                            new Pair<String, String>("user.name", m_username),
                            new Pair<String, String>("user.password", m_password));
                    }
                }
                catch (final Exception ex)
                {
                    Log.d(getClass().getCanonicalName(), "CAUGHT EXCEPTION, DISPLAYING ALERT.");
                    error = ex.getMessage();
                }
                finally
                {
                    if (error == null)
                    {
                        startActivity(home_tabs);
                        finish();
                    }
                    else
                        ShowErrorDialog(getContext(), error);
                }
            }
        });

        create.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view) { launchCreateAccount(); }
        });
    }

    protected NetworkCaller getCaller() { return this; }

    protected void attachCredentialsListeners()
    {
        final EditText user_text = (EditText) findViewById(R.id.l_username);
        final EditText pass_text = (EditText) findViewById(R.id.l_password);
        user_text.addTextChangedListener(new TextWatcher()
        {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override public void afterTextChanged(Editable editable) { m_username = editable.toString(); }
        });

        pass_text.addTextChangedListener(new TextWatcher()
        {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override public void afterTextChanged(Editable editable) { m_password = editable.toString(); }
        });
        m_username = user_text.getText().toString();
        m_password = pass_text.getText().toString();
    }
}
