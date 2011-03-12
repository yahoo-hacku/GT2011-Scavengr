package net.moosen.huntr.activities.quests.dto;

import java.io.Serializable;

/**
 * TODO: Enter class description.
 */
public class UserQuestStepDto implements Serializable
{
    private QuestStepDto step;
    private String datecompleted, created_at, updated_at;
    private Integer id, user_quest_id, step_id;

    private boolean completed = false, current = false;

    public UserQuestStepDto() { }

    public void makeCurrent()
    {
        this.current = true;
    }
    public boolean isCurrent()
    {
        return this.current;
    }
    public void setCurrent(boolean current)
    {
        this.current = current;
    }
    public void complete()
    {
        this.completed = true;
    }

    public boolean isComplete()
    {
        return this.completed;
    }
    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDatecompleted() {
        return datecompleted;
    }

    public void setDatecompleted(String datecompleted) {
        this.datecompleted = datecompleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public QuestStepDto getStep() {
        return step;
    }

    public void setStep(QuestStepDto step) {
        this.step = step;
    }

    public Integer getStep_id() {
        return step_id;
    }

    public void setStep_id(Integer step_id) {
        this.step_id = step_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getUser_quest_id() {
        return user_quest_id;
    }

    public void setUser_quest_id(Integer user_quest_id) {
        this.user_quest_id = user_quest_id;
    }
}
