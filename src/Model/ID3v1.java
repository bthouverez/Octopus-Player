/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Babs
 */
public class ID3v1 {

    // ID3v1 - mp3 tags
    // last 128 bytes (chars)
    // http://id3.org
    //
    // startByte-endByte (size) -> content
    //
    // 0-2 (3) -> id "TAG"
    // 3-32 (30) -> title 
    private String title;
    // 33-62 (30) -> artist
    private String artist;
    // 63-92 (30) -> album
    private String album;
    // 93-96 (4) -> year
    private String year;
    // 97-126 (30) -> comment
    private String comment;
    /* ID3v1.1 
    @Wikipedia The track number is stored in the last two bytes of the 
     comment field. If the comment is 29 or 30 characters long, no track number 
     can be stored. 
    
     zero-byte[4]        1 	If a track number is stored, this byte contains a binary 0.
     track[4]            1 	The number of the track on the album, or 0. Invalid, if previous byte is not a binary 0.
     */
    private int track;
    // 126-127 (1) -> genre, Index in a list of genres, or 255
    private String genre;

    private static final String[] GENRES = {"Blues", "Classic rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-hop", "Jazz", "Metal", "New age", "Oldies", "Autre", "Pop", "RnB", "Rap", "Reggae", "Rock", "Techno", "Musique industrielle (industrial)", "Rock alternatif (alternative)", "Ska", "Death metal", "Pranks", "Musique de film (soundtrack)", "Euro techno", "Ambient", "Trip hop", "Musique vocale (vocal)", "Jazz-funk", "Fusion", "Trance", "Musique classique (classical)", "Instrumental", "Acid", "House", "Musique de jeu vidéo", "Extrait sonore (sound clip ou sample)", "Gospel", "Musique bruitiste (noise)", "Rock alternatif", "Bass", "Soul", "Punk", "Space", "Musique de relaxation et de méditation (meditative)", "Pop instrumental", "Rock instrumental", "Musique ethnique", "Gothique", "Dark wave", "Techno-industrial", "Musique électronique", "Pop folk", "Eurodance", "Dream", "Rock sudiste (southern rock)", "Comédie", "Morceau culte (cult)", "Gangsta", "Hit-parade (top 40)", "Rap chrétien (christian rap)", "Pop/Funk", "Jungle", "Musique amérindienne2", "Cabaret", "New wave", "Psychédélique", "Rave", "Comédie musicale (showtunes)", "Bande-annonce", "Lo-fi", "Musique tribale", "Acid punk", "Acid jazz", "Polka", "Rétro", "Théâtre", "Rock 'n' Roll", "Hard rock"};

    public ID3v1() {
        this.title = "Unknown title";
        this.artist = "Unknown artist";
        this.album = "Unknown album";
        this.year = "Unknown year";
        this.comment = "Unknown comment";
        this.track = 0;
        this.genre = "Unknown genre";
    }

    public ID3v1(String uri) throws FileNotFoundException, IOException {

        File file = new File(uri);
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        byte[] array = new byte[1000];
        int nbBytes = 128;
        raf.seek(file.length() - nbBytes);
        raf.read(array, 0, nbBytes);

        String tags = "";
        for (int ii = 0; ii < nbBytes; ii++) {
            tags += (char) array[ii];
        }
        //System.out.println(tags);
        this.title = tags.substring(3, 33).trim();
        //this.artist = tags.substring(33, 63).replace(" ", "*");
        this.artist = tags.substring(33, 63).trim();
        this.album = tags.substring(63, 93).trim();
        this.year = tags.substring(93, 97);
        this.comment = tags.substring(97, 125).trim();
        /// TODO get full comment if no track (2 last chars)
        char isTrackSet = tags.charAt(125);
        this.track = tags.charAt(126);
        int idGenre = tags.charAt(127);
        this.genre = GENRES[idGenre];
    }

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }

    public String getAlbum() {
        return this.album;
    }

    public String getYear() {
        return this.year;
    }

    public String getComment() {
        return this.comment;
    }

    public int getTrack() {
        return this.track;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setTitle(String ti) {
        this.title = ti;
    }

    public void setArtist(String ar) {
        this.artist = ar;
    }

    public void setAlbum(String al) {
        this.album = al;
    }

    public void setYear(String ye) {
        this.year = ye;
    }

    public void setComment(String co) {
        this.comment = co;
    }

    public void setTrack(int tr) {
        this.track = tr;
    }

    public void setGenre(String ge) {
        this.genre = ge;
    }

    @Override
    public String toString() {
        return "Tags{\n"
                + "   title    = " + title + "\n"
                + "   artist   = " + artist + "\n"
                + "   album    = " + album + "\n"
                + "   year     = " + year + "\n"
                + "   comment  = " + comment + "\n"
                + "   track    = " + track + "\n"
                + "   genre    = " + genre + "\n }";
    }

}
