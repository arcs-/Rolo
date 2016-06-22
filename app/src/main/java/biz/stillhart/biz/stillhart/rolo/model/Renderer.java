package biz.stillhart.biz.stillhart.rolo.model;

import com.google.android.gms.maps.GoogleMap;
import java.lang.reflect.Array;

/**
 * Created by bzz on 22.06.2016.
 */
public class Renderer {

    protected GoogleMap map;

    public Renderer(GoogleMap map) {

    }

    public void draw(Array list) {

    }

    public GoogleMap getMap() {
        return map;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }
}
