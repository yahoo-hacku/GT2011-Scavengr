package net.moosen.huntr.activities.quests;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TwoLineListItem;
import net.moosen.huntr.R;
import net.moosen.huntr.activities.quests.steps.QuestStep;
import net.moosen.huntr.api.ApiHandler;
import net.moosen.huntr.api.ApiHandler.API_ACTION;
import net.moosen.huntr.exceptions.AuthenticationException;

/**
 * TODO: Enter class description.
 */
public class QuestLogActivity extends Activity
{
    private ListView list_view;
    @Override
    public void onCreate(final Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.quest_log);

        list_view = (ListView) findViewById(R.id.quest_log);

        try
        {
            List<Quest> quests = ApiHandler.GetInstance().doAction(API_ACTION.QUESTS);
            for (Quest quest : quests)
            {
                quest.setStepList(ApiHandler.GetInstance().<List<QuestStep>>doAction(API_ACTION.STEPS,
                        new Pair<String, String>("id", quest.getId().toString())));
            }
            QuestAdapter adapter = new QuestAdapter(quests);
            list_view.setAdapter(adapter);
            list_view.setOnItemClickListener(adapter);

        }
        catch (final AuthenticationException ex)
        {
            // big proglems
        }
    }

    private void launchQuest(Quest quest) {
        Intent next = new Intent();
        next.setClass(this, Quest.class);
        next.putExtra("name", quest.getName());
        next.putExtra("description", quest.getDescription());
        startActivity(next);
    }

    class QuestAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        private final List<Quest> quests;
        private final LayoutInflater inflater;

        public QuestAdapter(List<Quest> quests) {
            this.quests = quests;
            this.inflater = (LayoutInflater) getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return quests.size();
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
            bindView(view, quests.get(position));
            return view;
        }

        private TwoLineListItem createView(ViewGroup parent) {
            TwoLineListItem item = (TwoLineListItem) inflater.inflate(
                    android.R.layout.simple_list_item_2, parent, false);
            item.getText2().setSingleLine();
            item.getText2().setEllipsize(TextUtils.TruncateAt.END);
            return item;
        }

        private void bindView(TwoLineListItem view, Quest quest) {
            view.getText1().setText(quest.getName());
            view.getText2().setText(quest.getDescription());
        }

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            launchQuest(quests.get(position));
        }


    }
}
