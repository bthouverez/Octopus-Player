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
 * @author Babs ID3v1 metadata management Attributes are array of chars of
 * specific size, always filled with spaces to fit the 128 bytes
 */
public class ID3v1 {

    // ID3v1 - mp3 tags
    // last 128 bytes (chars)
    // http://id3.org/ID3v1
    //
    //
    // startByte-endByte (size) -> content
    //
    // 0-2 (3) -> id "TAG"
    // 3-32 (30) -> title 
    private char[] title;
    // 33-62 (30) -> artist
    private char[] artist;
    // 63-92 (30) -> album
    private char[] album;
    // 93-96 (4) -> year
    private char[] year;
    // 97-126 (28) -> comment
    private char[] comment;
    /* ID3v1.1 
     @Wikipedia The track number is stored in the last two bytes of the 
     comment field. If the comment is 29 or 30 characters long, no track number 
     can be stored. 
    
     zero-byte[4]        1 	If a track number is stored, this byte contains a binary 0.
     track[4]            1 	The number of the track on the album, or 0. Invalid, if previous byte is not a binary 0.
     */
    private int track;
    // 126-127 (1) -> genre, Index in a list of genres, or 255
    private int genre;

    // genre is an id linking to following array (80)
    private static final String[] GENRES = {"Blues", "Classic rock", "Country", 
        "Dance", "Disco", "Funk", "Grunge", "Hip-hop", "Jazz", "Metal", "New age", 
        "Oldies", "Autre", "Pop", "RnB", "Rap", "Reggae", "Rock", "Techno", "Industrial",
        "Alternative", "Ska", "Death metal", "Pranks", "Soundtrack", "Euro techno", 
        "Ambient", "Trip hop", "Vocal", "Jazz-funk", "Fusion", "Trance", "Classical", 
        "Instrumental", "Acid", "House", "Video game", "Sample", "Gospel", "Noise", 
        "Rock alternative", "Bass", "Soul", "Punk", "Space", "Meditative", "Pop instrumental", 
        "Rock instrumental", "Ethnic", "Gothic", "Dark wave", "Techno-industrial", 
        "Electronic", "Pop folk", "Eurodance", "Dream", "Southern rock", "Comedy", 
        "Cult", "Gangsta", "Hit-parade (top 40)", "Christian rap", "Pop/Funk", "Jungle", 
        "Amerindian2", "Cabaret", "New wave", "Psychedelic", "Rave", "Showtunes", 
        "Teaser", "Lo-fi", "Tribal", "Acid punk", "Acid jazz", "Polka", "Vintage", "Theater", 
        "Rock 'n' Roll", "Hard rock"};

    public ID3v1() {
        this.title = new char[30];
        this.title = "Unknown title".toCharArray();
        this.artist = new char[30];
        this.artist = "Unknown artist".toCharArray();
        this.album = new char[30];
        this.album = "Unknown album".toCharArray();
        this.year = new char[4];
        this.year = "Year".toCharArray();
        this.comment = new char[28];
        this.comment = "Unknown comment".toCharArray();
        this.track = 0;
        this.genre = 0;
    }

    /**
     * Open and extract given mp3 file tags
     * @param uri path to mp3 file
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public ID3v1(String uri) throws FileNotFoundException, IOException {

        File file = new File(uri);
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        byte[] array = new byte[128];
        int nbBytes = 128;
        // getting to end of file
        raf.seek(file.length() - nbBytes);
        // reading last 128 bytes
        raf.read(array, 0, nbBytes);

        String tags = "";
        for (int ii = 0; ii < nbBytes; ii++) {
            tags += (char) array[ii];
        }

        String str = tags.substring(3, 33).trim();
        this.title = str.toCharArray();
        str = tags.substring(33, 63).trim();
        this.artist = str.toCharArray();
        str = tags.substring(63, 93).trim();
        this.album = str.toCharArray();
        str = tags.substring(93, 97);
        this.year = str.toCharArray();

        // ID3v1   : comment = 30 bytes
        // ID3v1.1 : if comment <= 28, byte 28 is 0 binary, byte 29 is track number
        int isTrackSet = tags.charAt(125);
        if (isTrackSet != 0) {
            str = tags.substring(97, 127).trim();
            this.comment = str.toCharArray();
            this.track = 0;
        } else {
            str = tags.substring(97, 125).trim();
            this.comment = str.toCharArray();
            this.track = tags.charAt(126);
        }
        this.genre = tags.charAt(127);
    }

    public String getTitle() {
        return new String(this.title);
    }

    public String getArtist() {
        return new String(this.artist);
    }

    public String getAlbum() {
        return new String(this.album);
    }

    public String getYear() {
        return new String(this.year);
    }

    public String getComment() {
        return new String(this.comment);
    }

    public int getTrack() {
        return this.track;
    }

    public int getGenre() {
        return this.genre;
    }

    public String getGenreStr() {
        return GENRES[this.genre];
    }

    public void setTitle(String ti) {
        if (ti.length() > 30) {
            this.title = ti.substring(0, 30).toCharArray();
        } else {
            this.title = ti.toCharArray();
        }
    }

    public void setArtist(String ar) {
        if (ar.length() > 30) {
            this.artist = ar.substring(0, 30).toCharArray();
        } else {
            this.artist = ar.toCharArray();
        }
    }

    public void setAlbum(String al) {
        if (al.length() > 30) {
            this.album = al.substring(0, 30).toCharArray();
        } else {
            this.album = al.toCharArray();
        }
    }

    public void setYear(String ye) {
        if (ye.length() > 4) {
            this.year = ye.substring(0, 4).toCharArray();
        } else {
            this.year = ye.toCharArray();
        }
    }

    public void setComment(String co) {
        if (co.length() > 30) {
            this.comment = co.substring(0, 28).toCharArray();
        } else {
            this.comment = co.toCharArray();
        }
    }

    public void setTrack(int tr) {
        this.track = tr;
    }

    public void setGenre(int ge) {
        this.genre = ge;
    }

    // to check, may be not really powerful
    public boolean write(String uri) throws FileNotFoundException, IOException {
        String nTags = "TAG";

        String cTag = new String(this.title);
        cTag = fillWithSpaces(cTag, 30);
        nTags += cTag;

        cTag = new String(this.artist);
        cTag = fillWithSpaces(cTag, 30);
        nTags += cTag;

        cTag = new String(this.album);
        cTag = fillWithSpaces(cTag, 30);
        nTags += cTag;

        cTag = new String(this.year);
        cTag = fillWithSpaces(cTag, 4);
        nTags += cTag;

        cTag = new String(this.comment);
        cTag = fillWithSpaces(cTag, 28);
        nTags += cTag;

        nTags += (char) 0;

        cTag = ((char) this.track) + "";
        nTags += cTag;

        cTag = ((char) this.genre) + "";
        nTags += cTag;

        System.out.println(nTags);
        File file = new File(uri);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        byte[] array = new byte[128];
        int nbBytes = 128;
        raf.seek(file.length() - nbBytes);
        raf.writeBytes(nTags);
        return true;
    }

    private String fillWithSpaces(String s, int count) {
        for (int ii = s.length(); ii < count; ii++) {
            s += ' ';
        }
        return s;
    }

    @Override
    public String toString() {
        String ti = new String(title);
        ti = fillWithSpaces(ti, 30);
        String ar = new String(artist);
        ar = fillWithSpaces(ar, 30);
        String al = new String(album);
        al = fillWithSpaces(al, 30);
        String ye = new String(year);
        ye = fillWithSpaces(ye, 4);
        String co = new String(comment);
        co = fillWithSpaces(co, 28);
        return "Tags{\n"
                + "   title    = " + ti + " (" + ti.length() + ")\n"
                + "   artist   = " + ar + " (" + ar.length() + ")\n"
                + "   album    = " + al + " (" + al.length() + ")\n"
                + "   year     = " + ye + " (" + ye.length() + ")\n"
                + "   comment  = " + co + " (" + co.length() + ")\n"
                + "   track    = " + track + "\n"
                + "   genre    = " + GENRES[genre] + "\n }";
    }

}
