package net.moosen.huntr.activities.quests;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import net.moosen.huntr.R;
import net.moosen.huntr.activities.quests.steps.QuestStep;

/**
 * TODO: Enter class description.
 */
public class Quest extends Activity
{
    private Integer id;

    private String description, name;

    private List<QuestStep> steps;

    @Override
    public void onCreate(final Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.quest);
    }

    public void setId(final Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return this.id;
    }

    public void setStepList(final List<QuestStep> steps)
    {
        this.steps = steps;
    }

    public List<QuestStep> getSteps()
    {
        return this.steps;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return this.description;
    }



}
