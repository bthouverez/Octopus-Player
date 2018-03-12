/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Playlist;
import Model.Song;
import java.io.IOException;

/**
 *
 * @author Babs
 */
public class OctopusPlayer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Song m1 = new Song("res\\Tasbum\\04 Testostu.mp3");
//        Song m3 = new Song("res\\Tasbum\\04 Blazbum.mp3");
//        System.out.println(m1);
//        m1.setTitle("ltitle");
//        m1.setArtist("lartist");
//        m1.setAlbum("lalbum");
//        m1.setYear("lann");
//        m1.setComment("lcommentaire");
//        m1.setTrack(12);
//        m1.setGenre(17);
//        m1.writeTags();
//        System.out.println(m1);

        Playlist pl = new Playlist();
        pl.loadDirectory("res/Tasbum");
        
    }
}
