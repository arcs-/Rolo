package biz.stillhart.rolo.model;

/**
 * Created by bzz on 22.06.2016.
 */
public class Player extends Actor {

    protected String id;
    protected String team;
    protected int points;

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
