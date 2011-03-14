package net.moosen.huntr.activities.quests;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import net.moosen.huntr.R;
import net.moosen.huntr.activities.account.AccountLoginActivity;
import net.moosen.huntr.api.ApiHandler;
import net.moosen.huntr.api.ApiHandler.API_ACTION;
import net.moosen.huntr.exceptions.AuthenticationException;
import net.moosen.huntr.exceptions.StaleApiTokenException;

import static net.moosen.huntr.utils.Messages.ShowErrorDialog;

/**
 * TODO: Enter class description.
 */
public class QuestSearchActivity extends Activity
{
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
