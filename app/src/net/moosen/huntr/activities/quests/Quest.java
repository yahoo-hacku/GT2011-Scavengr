package net.moosen.huntr.activities.quests;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TwoLineListItem;
import net.moosen.huntr.R;
import net.moosen.huntr.activities.quests.dto.QuestStepDto;

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
    @Override
    @SuppressWarnings({"unchecked"})
    public void onCreate(final Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.quest);
        ArrayList<QuestStepDto> step_dtos = (ArrayList<QuestStepDto>) getIntent().getSerializableExtra("steps");
        QuestStepAdapter adapter = new QuestStepAdapter(step_dtos);
        ListView steps = (ListView) findViewById(R.id.quest_steps);
        steps.setAdapter(adapter);
        steps.setOnItemClickListener(adapter);
        final String desc = getIntent().getStringExtra("description");
        final String name = getIntent().getStringExtra("name");
        ((TextView) findViewById(R.id.quest_name)).setText(name);
        ((TextView) findViewById(R.id.quest_description)).setText(desc);
        // TODO: fill stuff from intent
    }

    private void launchQuestStep(QuestStepDto step) {
        Intent next = new Intent();
        next.setClass(this, QuestStep.class);
        next.putExtra("seq", step.getSeq());
        next.putExtra("clue", step.getClue());
        // set the rest of the intent data.
        startActivity(next);
    }

    class QuestStepAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        private final List<QuestStepDto> steps;
        private final LayoutInflater inflater;

        public QuestStepAdapter(List<QuestStepDto> steps) {
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

        private void bindView(TwoLineListItem view, QuestStepDto quest) {
            view.getText1().setText(quest.getSeq());
            view.getText2().setText(quest.getClue());
        }

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            launchQuestStep(steps.get(position));
        }
    }
}
