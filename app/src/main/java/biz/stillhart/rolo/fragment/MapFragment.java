package biz.stillhart.rolo.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import biz.stillhart.map.R;
import biz.stillhart.rolo.model.AudioController;
import biz.stillhart.rolo.model.Player;
import biz.stillhart.rolo.model.Renderer;
import biz.stillhart.rolo.utils.CoordinateUtils;
import biz.stillhart.rolo.utils.JsonTools;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;

/**
 * Created by bzz on 22.06.2016.
 */
public class MapFragment extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    static final int PERMISSION = 233;

    protected GoogleMap mMap;
    protected View mapView;
    protected ProgressBar progressBar;
    protected View pick;
    protected TextView finding;
    protected String team = "";
    protected Player currentPlayer;
    protected Array players;
    protected Array flags;
    protected Renderer renderer;
    protected AudioController audioController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        finding = (TextView) findViewById(R.id.finding);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION);
        } else setupApp();


        mapView = findViewById(R.id.map);
        mapView.setAlpha(0.3f);

        pick = findViewById(R.id.pick);
        final View redTeam = findViewById(R.id.redteam);
        final View blueTeam = findViewById(R.id.blueteam);
        redTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                team = "red";
                if(currentPlayer != null) currentPlayer.setTeam(team);
                if(finding.getVisibility() != View.VISIBLE) mapView.setAlpha(1);
                redTeam.setVisibility(View.GONE);
                blueTeam.setVisibility(View.GONE);
                pick.setVisibility(View.GONE);


            }
        });


        blueTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                team = "blue";
                if(currentPlayer != null) currentPlayer.setTeam(team);
                if(finding.getVisibility() != View.VISIBLE) mapView.setAlpha(1);
                redTeam.setVisibility(View.GONE);
                blueTeam.setVisibility(View.GONE);
                pick.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupApp();

                }
            }

        }
    }

    private void setupApp() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 100, 0, this);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, this);
            System.out.println("worked");
        } else System.out.println("not really");

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) mMap.setMyLocationEnabled(true);

        final Activity activity = this;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                boolean update = true;
                while (update) {

                    try {


                        JSONArray players = JsonTools.readArrayFromUrl(new URL("http://172.16.171.121:2016/players/list"));
                        JSONArray flags = JsonTools.readArrayFromUrl(new URL("http://172.16.171.121:2016/flags/list"));

                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                mMap.clear();
                            }
                        });

                        for (int i = 0; i < players.length(); i++) {
                            final JSONObject player = players.getJSONObject(i);

                            BitmapDescriptor icon;

                            if(currentPlayer != null && player.getString("id").equals(currentPlayer.getId())) icon = BitmapDescriptorFactory.fromResource(R.drawable.me);
                            else icon = BitmapDescriptorFactory.fromResource(R.drawable.user);

                            setPoint(activity, Double.parseDouble(player.getString("lat")), Double.parseDouble(player.getString("lng")), player.getString("team") + " #"+player.getString("id"), icon);
                        }

                        boolean taking = false;
                        boolean nearFlag = false;
                        for (int i = 0; i < flags.length(); i++) {
                            final JSONObject flag = flags.getJSONObject(i);
                            BitmapDescriptor icon = null;
                            if (flag.getString("capture").equals("0"))
                                icon = BitmapDescriptorFactory.fromResource(R.drawable.flag_gray);
                            else if (flag.getString("team").equals("blue"))
                                icon = BitmapDescriptorFactory.fromResource(R.drawable.flag_blue);
                            else if (flag.getString("team").equals("red"))
                                icon = BitmapDescriptorFactory.fromResource(R.drawable.flag_red);

                            double lat = Double.parseDouble(flag.getString("lat"));
                            double lng = Double.parseDouble(flag.getString("lng"));

                            setPoint(activity, lat, lng, flag.getString("capture"), icon);


                            if (!taking && currentPlayer != null && CoordinateUtils.distanceInKm(lat, lng, currentPlayer.getPosition().getLatitude(), currentPlayer.getPosition().getLongitude()) < 0.030) {
                                System.out.println("close to " + flag.getString("id") + " >> " + CoordinateUtils.distanceInKm(lat, lng, currentPlayer.getPosition().getLatitude(), currentPlayer.getPosition().getLongitude()));

                                nearFlag = true;
                                final int status = JsonTools.readObjectFromUrl(new URL("http://172.16.171.121:2016/flags/capture?id=" + flag.getString("id") + "&team=" + currentPlayer.getTeam())).getInt("capture");

                                // show bar
                                activity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        progressBar.setProgress(status);
                                    }
                                });

                                if(status < 100 ) taking = true;

                            }
                        }

                        final boolean isNearFlag = nearFlag;
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                if(isNearFlag) progressBar.setVisibility(View.VISIBLE);
                                else progressBar.setVisibility(View.INVISIBLE);
                            }
                        });


                        Thread.sleep(1000);


                    } catch (IOException | JSONException | InterruptedException e) {
                        e.printStackTrace();
                    }

                }


            }
        });

        thread.start();


    }

    public void setPoint(Activity activity, final double lat, final double lng, final String desc, final BitmapDescriptor icon) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                LatLng playerPos = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(playerPos).title(desc).icon(icon));
            }
        });
    }


    boolean firstLocationUpdate = true;

    @Override
    public void onLocationChanged(final Location location) {

        final String lat = "lat=" + location.getLatitude();
        final String lng = "&lng=" + location.getLongitude();

        final Activity activity = this;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (currentPlayer == null) {
                        JSONObject newPlayer = JsonTools.readObjectFromUrl(new URL("http://172.16.171.121:2016/players/update?" + lat + lng + "&team="+team));
                        currentPlayer = new Player();
                        currentPlayer.setId(newPlayer.getString("id"));
                        currentPlayer.setTeam(team);
                        currentPlayer.setPosition(location);
                    } else {
                        JsonTools.readObjectFromUrl(new URL("http://172.16.171.121:2016/players/update?id=" + currentPlayer.getId() + "&" + lat + lng + "&team=" + currentPlayer.getTeam()));
                        currentPlayer.setPosition(location);
                    }


                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LatLng currentPos = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 18f));
                            if (firstLocationUpdate) {
                                finding.setVisibility(View.INVISIBLE);
                                if(pick.getVisibility() != View.VISIBLE) mapView.setAlpha(1);
                                 firstLocationUpdate = false;
                            }
                            System.out.println("=>" + currentPlayer.getId() + " " + location.getLatitude() + " " + location.getLongitude());
                        }
                    });


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    public void showMenu(boolean show) {

    }

    public void showScore(boolean show) {

    }

    @Override
    protected void onStop() {
        super.onStop();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (currentPlayer != null) JsonTools.readObjectFromUrl(new URL("http://172.16.171.121:2016/players/delete?id=" + currentPlayer.getId()));

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();

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
