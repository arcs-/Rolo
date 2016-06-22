package biz.stillhart.biz.stillhart.rolo.model;

import java.util.Date;

/**
 * Created by bzz on 22.06.2016.
 */
public class Flag {

    protected Team team;
    protected int healthPoints;
    protected Date dateLock;

    public boolean damage() {
        return true;
    }

}
