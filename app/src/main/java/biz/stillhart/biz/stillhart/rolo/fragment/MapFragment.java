package biz.stillhart.biz.stillhart.rolo.fragment;

import com.google.android.gms.maps.GoogleMap;

import java.lang.reflect.Array;

import biz.stillhart.biz.stillhart.rolo.model.AudioController;
import biz.stillhart.biz.stillhart.rolo.model.Player;
import biz.stillhart.biz.stillhart.rolo.model.Renderer;

/**
 * Created by bzz on 22.06.2016.
 */
public class MapFragment {

    protected GoogleMap map;
    protected Player currentPlayer;
    protected Array players;
    protected Array flags;
    protected Renderer renderer;
    protected AudioController audioController;

    public void updatePositions() {

    }

    public void draw() {

    }

    public void showMenu(boolean show) {

    }

    public void showScore(boolean show) {

    }

    public void onResume() {

    }

    public void onPause() {

    }

    public GoogleMap getMap() {
        return map;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
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
