package net.moosen.huntr.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import net.moosen.huntr.R;
import net.moosen.huntr.activities.account.AccountLoginActivity;
import net.moosen.huntr.api.ApiHandler;
import net.moosen.huntr.api.ApiHandler.API_ACTION;
import net.moosen.huntr.exceptions.AuthenticationException;

/**
 * TODO: Enter class description.
 */
public class HomeTabsActivity extends TabActivity
{
    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.home_tabs);

        TabHost host = getTabHost();

        /*host.addTab(host.newTabSpec("one")
                .setIndicator("CW")
                .setContent(new Intent(this, CWBrowser.class)));

        host.addTab(host.newTabSpec("two")
                .setIndicator("Android")
                .setContent(new Intent(this, AndroidBrowser.class)));*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            switch (item.getItemId())
            {
                case R.id.menu_logout:
                    ApiHandler.GetInstance().doAction(API_ACTION.LOGOUT);
                    startActivity(new Intent(this, AccountLoginActivity.class));
                    finish();
                    break;
            }
        }
        catch (final AuthenticationException ex)
        {

        }

        return super.onOptionsItemSelected(item);
    }
}
