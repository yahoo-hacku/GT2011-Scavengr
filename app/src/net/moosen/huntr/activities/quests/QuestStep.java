package net.moosen.huntr.activities.quests;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
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
import net.moosen.huntr.exceptions.StaleApiTokenException;

import static net.moosen.huntr.utils.Messages.ShowErrorDialog;

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

    private UserQuestDto quest;

    private UserQuestStepDto step;

    private Integer current_sequence;
    private Context getContext() { return this; }
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

        findViewById(R.id.check_step_btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!complete)
                {
                    try
                    {
                        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        ApiHandler.GetInstance().doAction(API_ACTION.USER_STEPS_COMPLETE,
                            new Pair<String, String>("quest.quest_id", quest.getQuest_id().toString()),
                            new Pair<String, String>("step.step_id", step.getStep_id().toString()),
                            new Pair<String, String>("step.datecompleted", fmt.format(new Date())));
                        Intent result = new Intent();
                        result.putExtra("quest", quest);
                        result.putExtra("step", step);
                        result.putExtra("current_sequence", current_sequence);
                        setResult(RESULT_OK, result);
                        //finishActivity(STEP_RETURNED);
                        finish();
                    }
                    catch (final AuthenticationException ex) { /* */ }
                    catch (final StaleApiTokenException ex)
                    {
                        ShowErrorDialog(getContext(), "Your credentials have expired somehow...");
                        startActivity(new Intent(getContext(), AccountLoginActivity.class));
                        finish();
                    }

                }
                else { ShowErrorDialog(getContext(), "You have already completed this step!");  }
            }
        });
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
        catch (final StaleApiTokenException ex)
        {
            ShowErrorDialog(getContext(), "Your credentials have expired somehow...");
            startActivity(new Intent(getContext(), AccountLoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
