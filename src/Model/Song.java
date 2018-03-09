package Model;

import java.io.IOException;

public class Song  {

    private static int CPT = 0;
    private int id;
    private String location;

    private ID3v1 tags_v1;
    //private ID3v2 tags_v2;

    public Song() {
        this.id = CPT++;
        this.location = "Unknown location";
        this.tags_v1 = new ID3v1();
    }

    public Song(String file) throws IOException {
        this.id = CPT++;
        this.location = file;
        this.tags_v1 = new ID3v1(file);
    }

    public int getId() {
        return this.id;
    }

    public String getLocation() {
        return this.location;
    }

    public ID3v1 getTags1() {
        return this.tags_v1;
    }
    

    public void setId(int id) {
        this.id = id;
    }

    public void setLocation(String lo) {
        this.location = lo;
    }

    public void setTags1(ID3v1 t) {
        this.tags_v1 = t;
    }
  
    
    public String getTitle() {
        return this.tags_v1.getTitle();
    }

    public String getArtist() {
        return this.tags_v1.getArtist();
    }

    public String getAlbum() {
        return this.tags_v1.getAlbum();
    }

    public String getYear() {
        return this.tags_v1.getYear();
    }

    public String getComment() {
        return this.tags_v1.getComment();
    }

    public int getTrack() {
        return this.tags_v1.getTrack();
    }

    public int getGenre() {
        return this.tags_v1.getGenre();
    }
    
    public String getGenreStr() {
        return this.tags_v1.getGenreStr();
    }

    public void setTitle(String ti) {
        this.tags_v1.setTitle(ti);
    }

    public void setArtist(String ar) {
        this.tags_v1.setArtist(ar);
    }

    public void setAlbum(String al) {
        this.tags_v1.setAlbum(al);
    }

    public void setYear(String ye) {
        this.tags_v1.setYear(ye);
    }

    public void setComment(String co) {
        this.tags_v1.setComment(co);
    }

    public void setTrack(int tr) {
        this.tags_v1.setTrack(tr);
    }

    public void setGenre(int ge) {
        this.tags_v1.setGenre(ge);
    }
    
    public boolean writeTags() throws IOException {
        return this.tags_v1.write(this.location);
    }
    
    @Override
    public String toString() {
        return "Music{\n id=" + id + "\n"
                + " location = " + location /*+ "\n"
                + "  " + tags_v1*/ + "\n}";
    }
}
