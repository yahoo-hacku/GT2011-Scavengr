package net.moosen.huntr.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import net.moosen.huntr.R;
import net.moosen.huntr.activities.account.AccountLoginActivity;
import net.moosen.huntr.activities.quests.QuestLogActivity;
import net.moosen.huntr.activities.quests.QuestSearchActivity;
import net.moosen.huntr.api.ApiHandler;
import net.moosen.huntr.api.ApiHandler.API_ACTION;
import net.moosen.huntr.exceptions.AuthenticationException;
import net.moosen.huntr.exceptions.StaleApiTokenException;

import static net.moosen.huntr.utils.Messages.ShowErrorDialog;

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
        Resources res = getResources();
        host.addTab(host.newTabSpec("one")
                .setIndicator("Quests", res.getDrawable(android.R.drawable.ic_menu_compass))
                .setContent(new Intent(this, QuestLogActivity.class)));
        host.addTab(host.newTabSpec("two")
                .setIndicator("Search", res.getDrawable(android.R.drawable.ic_menu_search))
                .setContent(new Intent(this, QuestSearchActivity.class)));

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
        catch (final AuthenticationException ex) { /* */ }
        catch (final StaleApiTokenException ex)
        {
            ShowErrorDialog(this, "Your credentials have expired somehow...");
            startActivity(new Intent(this, AccountLoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
