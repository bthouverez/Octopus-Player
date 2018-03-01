package Model;

//import android.net.Uri;

public class Music /*implements Comparable<Music>*/ {
    
    private int id;
    
    // ID3v1 - mp3 tags
    // 128 bytes (chars)
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
    /* @Wikipedia The track number is stored in the last two bytes of the 
    comment field. If the comment is 29 or 30 characters long, no track number 
    can be stored. 
    
        zero-byte[4]        1 	If a track number is stored, this byte contains a binary 0.
        track[4]            1 	The number of the track on the album, or 0. Invalid, if previous byte is not a binary 0.
    */
    private String track;
    // 126-127 (1) -> genre, Index in a list of genres, or 255
    private String genre;
    
    private int duration;
    
//    private Uri path;

    /* Constructeurs */
    public Music() {
        this.id = -1;
        this.title = "Unknown title";
        this.artist = "Unknown artist";
        this.album = "Unknown album";
        this.year = "Unknown year";
        this.comment = "Unknown comment";
        this.track = "Unknown track";
        this.genre = "Unknown genre";
        this.duration = 0;
    //    this. Uri path;
        
    }
    public Music(String i_title, String i_album, String i_artist, String i_path, int dur) {
        this.title = i_title;
        this.album = i_album;
        this.artist = i_artist;
//        Uri.Builder uriBuilder = new Uri.Builder();
//        uriBuilder.path(i_path);
//        this.path = uriBuilder.build();
        this.duration = dur;
    }

    public int getId() { return this.id; }
    public String getTitle() { return this.title; }
    public String getArtist() { return this.artist; }
    public String getAlbum() { return this.album; }
    public String getYear() { return this.year; }
    public String getComment() { return this.comment; }
    public String getTrack() { return this.track; }
    public String getGenre() { return this.genre; }
    public int getDuration() { return this.duration; }
    
    public void setId(int id) { this.id = id; }
    public void setTitle(String ti) { this.title = ti; }
    public void setArtist(String ar) { this.artist = ar; }
    public void setAlbum(String al) { this.album = al; }
    public void setYear(String ye) { this.year = ye; }
    public void setComment(String co) { this.comment = co; }
    public void setTrack(String tr) { this.track = tr; }
    public void setGenre(String ge) { this.genre = ge; }
    public void setDuration(int du) { this.duration = du; }


//    public Uri getPath() {
//        return path;
//    }
//    public void setPath(Uri address) {
//        this.path = address;
//    }


    @Override
    public String toString() {
        return "Music{\nid=" + id + "\n"
                + " title=" + title + "\n"
                + " artist=" + artist + "\n"
                + " album=" + album + "\n"
                + " year=" + year + "\n"
                + " comment=" + comment + "\n"
                + " track=" + track + "\n"
                + " genre=" + genre + "\n"
                + " duration=" + duration + "\n}";
    }
/*
    public int compareTo(Music arg0) {
        
        if (this.getPath().equals(arg0.getPath())) {
            return 0;
        } else {
            return this.getTitle().compareTo(arg0.getTitle());
        }
    }
*/
}
