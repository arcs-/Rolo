package biz.stillhart.rolo.model;

import java.util.Date;

/**
 * Created by bzz on 22.06.2016.
 */
public class Flag extends Actor {

    protected Team team;
    protected int healthPoints;
    protected Date dateLock;

    public boolean damage() {
        return true;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public Date getDateLock() {
        return dateLock;
    }

    public void setDateLock(Date dateLock) {
        this.dateLock = dateLock;
    }
}
