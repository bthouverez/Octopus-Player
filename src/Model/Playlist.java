/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import View.TabbedPlaylistWindow;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author thouverez
 */
public class Playlist extends ArrayList<Song> {

    private String name;
    private int currentSong;
    private boolean random;

    public Playlist() {
        super();
        this.currentSong = 0;
        this.random = false;
        this.name = "Current playlist";
    }

    /**
     * Switch to next song in playlist
     */
    public void next() {
        if (this.random) {
            Random rnd = new Random();
            int r = rnd.nextInt(this.size());
            // if random is current song, going on next()
            if(r != this.currentSong)
                this.currentSong = r;
            else
                next();
        } else {
            this.currentSong = (this.currentSong + 1) % this.size();
        }
    }

    /**
     * Switch to previous song in playlist
     */
    public void previous() {
        // TODO previous should be disabled if random
        if (this.random) {
            Random rnd = new Random();
            int r = rnd.nextInt(this.size());
            // if random is current song, going on previous()
            if(r != this.currentSong)
                this.currentSong = r;
            else
                previous();
        } else {
            this.currentSong = this.currentSong == 0
                    ? this.size() - 1
                    : this.currentSong - 1;
        }
    }
    
    public Song getCurrentSong() {
        return this.get(this.currentSong);
    }
    
    public boolean isRandom() {
        return this.random;
    }

    public void switchRandom() {
        this.random = !this.random;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String n) {
        this.name = n;
    }

    
    /**
     * Load recursively a directory and add all .mp3 files to current playlist
     * @param path path to directory to load
     * @throws IOException 
     */
    // TODO check some more errors
    public void loadDirectory(String path) throws IOException {
        File directory = new File(path);
        File[] contents = directory.listFiles();
        for (File f : contents) {
            String absPath = f.getAbsolutePath();
            if (f.isDirectory()) {
                loadDirectory(absPath);
            } else {
                if (absPath.substring(absPath.lastIndexOf('.') + 1).contentEquals("mp3")) {
                    Song s = new Song(absPath);
                    this.add(s);
                }
            }
        }
        TabbedPlaylistWindow w = new TabbedPlaylistWindow();
        w.setVisible(true);
        w.setPlaylistName(this.name);
        w.fill(this);
    }

    @Override
    public String toString() {
        String res = this.size() + " songs on playlist \n\n";
        for (int ii = 0; ii < this.size(); ii++) {
            res += this.get(ii).toString() + "\n";
        }
        return res;
    }

}
