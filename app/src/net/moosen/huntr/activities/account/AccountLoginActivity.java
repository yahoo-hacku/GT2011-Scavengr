package net.moosen.huntr.activities.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import net.moosen.huntr.exceptions.AuthenticationException;

import static net.moosen.huntr.results.Results.ACCOUNT_CREATE_RESULT;

public class AccountLoginActivity extends Activity
{

    private String m_username, m_password;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        attachButtonListeners();
        attachCredentialsListeners();

        //SharedPreferences p = getPreferences(Context.MODE_PRIVATE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(getClass().getCanonicalName(), "You tried to login.");
                try
                {
                    ApiHandler.GetInstance().doAction(API_ACTION.LOGIN,
                            new Pair<String, String>("username", m_username),
                            new Pair<String, String>("password", m_password));
                }
                catch (final AuthenticationException ex)
                {

                }
/*
                {
                    ApiHandler.GetInstance().writeCredentials();
                }
*/

                startActivity(home_tabs);
                finish();
            }
        });

        create.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                launchCreateAccount();
            }
        });
    }

    protected void attachCredentialsListeners()
    {
        final EditText user_text = (EditText) findViewById(R.id.l_username);
        final EditText pass_text = (EditText) findViewById(R.id.l_password);
        user_text.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable)
            {
                //Log.d(getClass().getName(), "username="+editable.toString());
                m_username = editable.toString();
            }
        });
        pass_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                m_password = editable.toString();
            }
        });
        m_username = user_text.getText().toString();
        m_password = pass_text.getText().toString();
    }
}
