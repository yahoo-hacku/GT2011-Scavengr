package net.moosen.huntr.activities.quests;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TwoLineListItem;
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
 * TODO: Enter class description.
 */
public class QuestLogActivity extends Activity
{

    @Override
    public void onCreate(final Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.quest_log);
        final ListView list_view = (ListView) findViewById(R.id.quest_log);

        try
        {
            final ArrayList<UserQuestDto> quests = ApiHandler.GetInstance().doAction(API_ACTION.USER_QUESTS);
            final UserQuestAdapter adapter = new UserQuestAdapter(quests);
            for (UserQuestDto quest : quests)
            {
                quest.setSteps(ApiHandler.GetInstance().<ArrayList<UserQuestStepDto>>doAction(
                        API_ACTION.USER_STEPS,
                        new Pair<String, String>("step.step_id", quest.getQuest_id().toString())));
            }

            list_view.setAdapter(adapter);
            list_view.setOnItemClickListener(adapter);
        }
        catch (final AuthenticationException ex) { /* */ }
        catch (final StaleApiTokenException ex)
        {
            ShowErrorDialog(this, "Your credentials have expired somehow...");
            startActivity(new Intent(this, AccountLoginActivity.class));
            finish();
        }
    }

    private void launchUserQuest(UserQuestDto quest)
    {
        Intent next = new Intent();
        next.setClass(this, Quest.class);
        next.putExtra("quest", quest);
        next.putExtra("current_sequence", 0);
        startActivity(next);
    }

    class UserQuestAdapter extends BaseAdapter implements AdapterView.OnItemClickListener
    {

        private final List<UserQuestDto> quests;
        private final LayoutInflater inflater;

        public UserQuestAdapter(final List<UserQuestDto> quests)
        {
            this.quests = quests;
            this.inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() { return quests.size(); }

        public Object getItem(int position) { return position; }

        public long getItemId(int position) { return position; }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            TwoLineListItem view = (convertView != null) ? (TwoLineListItem) convertView :
                    createView(parent);
            bindView(view, quests.get(position));
            return view;
        }

        private TwoLineListItem createView(ViewGroup parent)
        {
            TwoLineListItem item = (TwoLineListItem) inflater.inflate(
                    android.R.layout.simple_list_item_2, parent, false);
            item.getText2().setSingleLine();
            item.getText2().setEllipsize(TextUtils.TruncateAt.END);
            return item;
        }

        private void bindView(TwoLineListItem view, UserQuestDto quest)
        {
            view.getText1().setText(quest.getQuest().getName());
            view.getText2().setText(quest.getQuest().getDescription());
        }

        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            launchUserQuest(quests.get(position));
        }
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
