package net.moosen.huntr.activities.quests;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import net.moosen.huntr.R;
import net.moosen.huntr.activities.quests.dto.UserQuestDto;
import net.moosen.huntr.activities.quests.dto.UserQuestStepDto;

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
        current_sequence = getIntent().getIntExtra("seq", 0);

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
                    back.putExtra("current_sequence", current_sequence);
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
}
