package net.moosen.huntr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static net.moosen.huntr.results.Account.ACCOUNT_CREATE_SUCCESS;

/**
 * TODO: Enter class description.
 */
public class CreateAccountActivity extends Activity {
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
        ((Button) findViewById(R.id.c_create_button)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                // TODO: account creation validation && rpc to server.
                Intent data = new Intent();
                data.putExtra("asdfsda", "adsfsd");
                setResult(ACCOUNT_CREATE_SUCCESS);//, data);
                finish();
            }
        });
    }
}
