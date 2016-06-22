package biz.stillhart.rolo.server;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import biz.stillhart.map.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final Activity activity = this;

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {



                try {
                    boolean update = true;
                    while(update) {

                        mMap.clear();

                        JSONArray players = JsonTools.readArrayFromUrl(new URL("http://172.16.171.121:2016/players"));
                        for (int i = 0; i < players.length(); i++) {
                            final JSONObject player = players.getJSONObject(i);
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.user);
                            setPoint(activity, Double.parseDouble(player.getString("lat")), Double.parseDouble(player.getString("lng")), player.getString("team"), icon);
                        }

                        JSONArray flags = JsonTools.readArrayFromUrl(new URL("http://172.16.171.121:2016/flags"));
                        for (int i = 0; i < flags.length(); i++) {
                            final JSONObject flag = flags.getJSONObject(i);
                            BitmapDescriptor icon = null;
                            if (flag.getString("team").equals("blue"))
                                icon = BitmapDescriptorFactory.fromResource(R.drawable.flag_blue);
                            else if (flag.getString("team").equals("red"))
                                icon = BitmapDescriptorFactory.fromResource(R.drawable.flag_red);

                            setPoint(activity, Double.parseDouble(flag.getString("lat")), Double.parseDouble(flag.getString("lng")), flag.getString("team"), icon);
                        }

                        Thread.sleep(1000);
                    }

                }catch (IOException | JSONException | InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });

        thread.start();


       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void setPoint(Activity activity, final double lat, final double lng, final String desc, final BitmapDescriptor icon) {
        activity.runOnUiThread(new Runnable() {
            public void run() {

                    LatLng playerPos = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions().position(playerPos).title(desc).icon(icon));
            }
        });
    }
}
