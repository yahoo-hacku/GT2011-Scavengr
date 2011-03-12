package net.moosen.huntr.activities.quests;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import net.moosen.huntr.R;

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

    @Override
    public void onCreate(final Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.quest_step);
        final String clue = getIntent().getStringExtra("clue");
        final Integer seq = getIntent().getIntExtra("seq", 0);
        ((TextView) findViewById(R.id.step_num)).setText(seq.toString());
        ((TextView) findViewById(R.id.step_clue)).setText(clue);

        // etc
    }
}
