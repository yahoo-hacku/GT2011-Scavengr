package net.moosen.huntr.activities.quests;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import net.moosen.huntr.R;
import net.moosen.huntr.activities.account.AccountLoginActivity;
import net.moosen.huntr.activities.quests.dto.UserQuestDto;
import net.moosen.huntr.activities.quests.dto.UserQuestStepDto;
import net.moosen.huntr.api.ApiHandler;
import net.moosen.huntr.api.ApiHandler.API_ACTION;
import net.moosen.huntr.exceptions.AuthenticationException;

/**
 * {"clue":null,
 * "created_at":"2011-03-12T07:39:19Z",
 * "error_radius":"4.0",
 * "id":1,
 * "lat":"43.1121",
 * "lon":"21.2223",
 * "quest_id":1,
 * "seq":0,
 * "updated_at":"2011-03-12T07:39:19Z"}
 */
public class QuestStep extends Activity {

    public static final String CURRENT_STEP_CHANGED = "net.moosen.huntr.events.CHANGE_CURRENT_QUEST_STEP_EVENT";

    private UserQuestDto quest;

    private UserQuestStepDto step;

    private Integer current_sequence;
    @Override
    public void onCreate(final Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.quest_step);
        quest = (UserQuestDto) getIntent().getSerializableExtra("quest");
        step = (UserQuestStepDto) getIntent().getSerializableExtra("step");
        current_sequence = getIntent().getIntExtra("current_sequence", 0);

        ((TextView) findViewById(R.id.step_clue)).setText("\"" + step.getStep().getClue() + "\"");
        ((TextView) findViewById(R.id.step_qname)).setText("Quest: " + quest.getQuest().getName());
        ((TextView) findViewById(R.id.step_num)).setText("Clue #: " + step.getStep().getSeq().toString());


        final boolean complete = step.isComplete();

        final Intent back = new Intent(this, Quest.class);
        findViewById(R.id.check_step_btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (!complete)
                {
                    back.putExtra("quest", quest);
                    back.putExtra("step", step);
                    current_sequence++;
                    Log.d(getClass().getCanonicalName(), "------------------------Posting sequence: " + current_sequence);
                    back.putExtra("current_sequence", current_sequence);
                    //showErrorDialog("Yes! You are!");
                    startActivity(back);
                    finish();
                }
                else
                {
                    showErrorDialog("You have already completed this step!");
                }

            }
        });
        // etc
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
            //
        }

        return super.onOptionsItemSelected(item);
    }
}
