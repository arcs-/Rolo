package biz.stillhart.rolo.model;

import android.graphics.Bitmap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by bzz on 22.06.2016.
 */
public abstract class Actor {

    protected Bitmap image;
    protected LatLng position;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }
}
