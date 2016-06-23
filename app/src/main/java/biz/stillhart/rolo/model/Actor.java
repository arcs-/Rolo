package biz.stillhart.rolo.model;

import android.graphics.Bitmap;
import android.location.Location;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by bzz on 22.06.2016.
 */
public abstract class Actor {

    protected Bitmap image;
    protected Location position;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Location getPosition() {
        return position;
    }

    public void setPosition(Location position) {
        this.position = position;
    }
}
