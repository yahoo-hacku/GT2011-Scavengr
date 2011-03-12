package net.moosen.huntr.activities.quests.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * TODO: Enter class description.
 */
public class UserQuestDto implements Serializable
{
    private QuestDto quest;

    private ArrayList<UserQuestStepDto> steps;

    private Integer quest_id;

    public Integer getQuest_id() {
        return quest_id;
    }

    public void setQuest_id(Integer quest_id) {
        this.quest_id = quest_id;
    }

    private String created_at, start;

    public UserQuestDto() { }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public QuestDto getQuest() {
        return quest;
    }

    public void setQuest(QuestDto quest) {
        this.quest = quest;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public ArrayList<UserQuestStepDto> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<UserQuestStepDto> steps) {
        this.steps = steps;
    }
}
