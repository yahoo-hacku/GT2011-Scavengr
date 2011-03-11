package net.moosen.huntr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static net.moosen.huntr.results.Results.ACCOUNT_CREATE_RESULT;

public class LoginActivity extends Activity
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

    protected void checkPreferences()
    {
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        String check_username, check_password;
        check_username = prefs.getString("user.name", "");
        check_password = prefs.getString("user.password", "");

        if (check_username == null || check_username.isEmpty())
        {

        }

        if (check_password == null || check_password.isEmpty())
        {

        }

    }

    protected void launchCreateAccount()
    {
        Intent intent = new Intent();
        intent.setClass(this, CreateAccountActivity.class);
        intent.putExtra("username", m_username);
        startActivityForResult(intent, ACCOUNT_CREATE_RESULT);
    }

    protected void attachButtonListeners()
    {
        Button login = (Button) findViewById(R.id.l_login_button), create = (Button) findViewById(R.id.l_create_button);
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Log.d(getClass().getCanonicalName(), "You tried to login.");
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
        ((EditText) findViewById(R.id.l_username)).addTextChangedListener(new TextWatcher()
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
        ((EditText) findViewById(R.id.l_password)).addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable)
            {
                m_username = editable.toString();
            }
        });
    }
}
