/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author thouverez
 */
public class Playlist extends ArrayList<Song> {

    private int currentSong;

    public Playlist() {
        super();
        this.currentSong = 0;
    }

    public Song nextSong() {
        this.currentSong = (this.currentSong + 1) % this.size();
        return this.get(this.currentSong);
    }

    public Song previousSong() {
        this.currentSong = this.currentSong - 1 < 0 ? this.size() - 1 : this.currentSong - 1;
        return this.get(this.currentSong);
    }

    public void shuffle() {
        Collections.shuffle(this);
    }

    public String toString() {
        String res = this.size() + " songs on playlist \n\n";
        for (int ii = 0; ii < this.size(); ii++) {
            res += this.get(ii).toString();
        }
        return res;
    }
}
