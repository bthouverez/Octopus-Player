/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

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
        Song m2 = new Song("res\\Tasbum\\05 Tastusto.mp3");
        Song m3 = new Song("res\\Tasbum\\04 Blazbum.mp3");
        System.out.println(m1);
        System.out.println(m2);
        System.out.println(m3);
    //m.writeTags();
    }
    
}
