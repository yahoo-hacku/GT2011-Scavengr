package net.moosen.huntr.activities.quests.dto;

import java.io.Serializable;
import java.util.ArrayList;

import net.moosen.huntr.activities.quests.dto.QuestStepDto;

/**
 * TODO: Enter class description.
 */
public class QuestDto implements Serializable
{
    public static final long serialVersionUID = 1;

    private Boolean active;

    private Integer id, time_limit;

    private String description, name, created_at, updated_at;

    private ArrayList<QuestStepDto> steps;

    public QuestDto() { }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<QuestStepDto> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<QuestStepDto> steps) {
        this.steps = steps;
    }

    public Integer getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(Integer time_limit) {
        this.time_limit = time_limit;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
