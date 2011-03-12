package net.moosen.huntr.activities.quests;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TwoLineListItem;
import net.moosen.huntr.R;
import net.moosen.huntr.activities.account.AccountLoginActivity;
import net.moosen.huntr.activities.quests.dto.UserQuestDto;
import net.moosen.huntr.activities.quests.dto.UserQuestStepDto;
import net.moosen.huntr.api.ApiHandler;
import net.moosen.huntr.api.ApiHandler.API_ACTION;
import net.moosen.huntr.exceptions.AuthenticationException;

/**
 * {"active":true,
"created_at":"2011-03-12T06:41:53Z",
"created_by_id":1,
"description":"The Greated Quest",
"id":1,
"name":null,
"seq":null,
"time_limit":null,
"updated_at":"2011-03-12T06:41:53Z"
 */
public class Quest extends Activity
{
    private Integer current_sequence = 0;
    private UserQuestDto quest;

    @Override
    @SuppressWarnings({"unchecked"})
    public void onCreate(final Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.quest);
        this.quest = (UserQuestDto) getIntent().getSerializableExtra("quest");
        current_sequence = getIntent().getIntExtra("current_sequence", -1);

        QuestStepAdapter adapter = new QuestStepAdapter(quest.getSteps());
        ListView steps = (ListView) findViewById(R.id.quest_steps);
        steps.setAdapter(adapter);
        steps.setOnItemClickListener(adapter);
        ((TextView) findViewById(R.id.quest_name)).setText("Quest Name: " + quest.getQuest().getName());
        ((TextView) findViewById(R.id.quest_description)).setText("Quest Description: " + quest.getQuest().getDescription());
        Log.d(getClass().getCanonicalName(), "Current Sequence: " + current_sequence);
        if (current_sequence > 0 && quest.getSteps().size() - 1 >= current_sequence)
        {
            quest.getSteps().get(current_sequence).makeCurrent();
            quest.getSteps().get(current_sequence - 1).setCurrent(false);
            quest.getSteps().get(current_sequence - 1).complete();
        }
        else if (current_sequence == 0 && quest.getSteps().size() > 0)
        {
            quest.getSteps().get(current_sequence).makeCurrent();
        }
    }

    private void launchQuestStep(UserQuestStepDto step) {
        if (step.isCurrent() || step.isComplete())
        {
            Intent next = new Intent();
            next.setClass(this, QuestStep.class);
            next.putExtra("step", step);
            next.putExtra("quest", quest);
            Log.d(getClass().getCanonicalName(), "----------Posting step with sequence; " + current_sequence);
            next.putExtra("current_sequence", current_sequence);
            // set the rest of the intent data.
            startActivity(next);
        }
        else
        {
            // show error
            showErrorDialog("You must complete earlier quest steps before attempting this one!");
        }


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

    class QuestStepAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        private final List<UserQuestStepDto> steps;
        private final LayoutInflater inflater;

        public QuestStepAdapter(List<UserQuestStepDto> steps) {
            this.steps = steps;
            this.inflater = (LayoutInflater) getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return steps.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TwoLineListItem view = (convertView != null) ? (TwoLineListItem) convertView :
                    createView(parent);
            bindView(view, steps.get(position));
            return view;
        }

        private TwoLineListItem createView(ViewGroup parent) {
            TwoLineListItem item = (TwoLineListItem) inflater.inflate(
                    android.R.layout.simple_list_item_2, parent, false);
            item.getText2().setSingleLine();
            item.getText2().setEllipsize(TextUtils.TruncateAt.END);
            return item;
        }

        private void bindView(TwoLineListItem view, UserQuestStepDto quest) {
            view.getText1().setText(quest.getStep().getSeq().toString());
            view.getText2().setText(quest.isComplete()  ? quest.getStep().getClue() : "---");
        }

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            launchQuestStep(steps.get(position));
        }
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
