package biz.stillhart.rolo.model;

/**
 * Created by bzz on 22.06.2016.
 */
public class Player {

    protected String name;
    protected Team team;
    protected int points;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
