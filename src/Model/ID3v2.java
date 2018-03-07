/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * ID3v2 Struct
 *
 * HEADER 10 bytes 0->2 : TAG ID3 3 : versionMajor 4 : versionMinor 5 : flags
 * 6->9 : size
 */
public class ID3v2 {

    public ID3v2() {

    }

    public ID3v2(String uri) throws FileNotFoundException, IOException {

        BufferedReader br = new BufferedReader(new FileReader(uri));
        String strLine;
        //Read File Line By Line

        // HEADER //////////////////////////////////////////////////////////////
        String tags = br.readLine();
        /* while ((strLine = br.readLine()) != null) {
            // Print the content on the console
            System.out.println(strLine);
        }
         */
        System.out.println(tags);
        System.out.println(tags.length());
        System.out.println("");
        String tag = tags.substring(0, 3);
        //System.out.println("tag : "+tag);

        int v_major = tags.charAt(3);
        int v_minor = tags.charAt(4);
        String version = v_major + "." + v_minor;
        //System.out.println(version);
        System.out.println(tag + "v2." + version);

        String flags = Integer.toBinaryString((int) tags.charAt(5));
        System.out.println("flags : " + flags);
        if (tags.charAt(5) != 0) {
            System.out.println("TODO SOMETHING");
        } else {
            //System.out.println("NO FLAGS NADA TO DO");
        }

        String s_size = tags.substring(6, 10);
        int i_size = 0;
        i_size += tags.charAt(6);
        i_size += tags.charAt(7);
        i_size += tags.charAt(8);
        i_size += tags.charAt(9);
        System.out.println("s_size : " + s_size);
        System.out.println("i_size : " + i_size);
        // END HEADER //////////////////////////////////////////////////////////
    }

}
