package net.moosen.huntr.activities.quests.steps;

import java.io.Serializable;

/**
 * TODO: Enter class description.
 */
public class QuestStepDto implements Serializable {
    public static final long serialVersionUID = 1;
    private Integer seq;
    private Integer id, quest_id;
    private String lat, lon, error_radius;
    private String clue, created_at, updated_at;

    public QuestStepDto() { }
    public String getClue() {
        return clue;
    }

    public void setClue(String clue) {
        this.clue = clue;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public Integer getQuest_id() {
        return quest_id;
    }

    public void setQuest_id(Integer quest_id) {
        this.quest_id = quest_id;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getError_radius() {
        return error_radius;
    }

    public void setError_radius(String error_radius) {
        this.error_radius = error_radius;
    }
}
