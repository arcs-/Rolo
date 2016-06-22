package biz.stillhart.rolo;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import biz.stillhart.map.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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
                    JSONArray players = JsonTools.readArrayFromUrl(new URL("http://172.16.171.121:2016/players"));

                    for(int i = 0; i < players.length(); i++) {
                        final JSONObject player = players.getJSONObject(i);

                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    LatLng playerPos = new LatLng(Double.parseDouble(player.getString("lat")), Double.parseDouble(player.getString("lng")));
                                    mMap.addMarker(new MarkerOptions().position(playerPos).title(player.getString("team")));
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                }catch (IOException | JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        thread.start();


       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
