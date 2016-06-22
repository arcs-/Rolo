package biz.stillhart.rolo.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import biz.stillhart.map.R;
import biz.stillhart.server.JsonTools;
import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;

import biz.stillhart.rolo.model.AudioController;
import biz.stillhart.rolo.model.Player;
import biz.stillhart.rolo.model.Renderer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bzz on 22.06.2016.
 */
public class MapFragment extends FragmentActivity implements OnMapReadyCallback {

    protected GoogleMap mMap;
    protected Player currentPlayer;
    protected Array players;
    protected Array flags;
    protected Renderer renderer;
    protected AudioController audioController;

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

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    boolean update = true;
                    while (update) {

                        JSONArray players = JsonTools.readArrayFromUrl(new URL("http://172.16.171.121:2016/players"));
                        JSONArray flags = JsonTools.readArrayFromUrl(new URL("http://172.16.171.121:2016/flags"));

                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                            mMap.clear();
                            }
                        });

                        for (int i = 0; i < players.length(); i++) {
                            final JSONObject player = players.getJSONObject(i);
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.user);
                            setPoint(activity, Double.parseDouble(player.getString("lat")), Double.parseDouble(player.getString("lng")), player.getString("team"), icon);
                        }

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

                } catch (IOException | JSONException | InterruptedException e) {
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



    public void updatePositions() {

    }

    public void draw() {

    }

    public void showMenu(boolean show) {

    }

    public void showScore(boolean show) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public GoogleMap getMap() {
        return mMap;
    }

    public void setMap(GoogleMap map) {
        this.mMap = map;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Array getPlayers() {
        return players;
    }

    public void setPlayers(Array players) {
        this.players = players;
    }

    public Array getFlags() {
        return flags;
    }

    public void setFlags(Array flags) {
        this.flags = flags;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public AudioController getAudioController() {
        return audioController;
    }

    public void setAudioController(AudioController audioController) {
        this.audioController = audioController;
    }
}
