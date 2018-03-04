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
        // TODO code application logic here
        Song m = new Song("res\\Steal This Album!\\15 Roulette.mp3");
        System.out.println(m);
    }
    
}
