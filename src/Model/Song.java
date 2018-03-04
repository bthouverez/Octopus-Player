package Model;

//import android.net.Uri;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Song /*implements Comparable<Music>*/ {

    private static int CPT = 0;
    private int id;
    private String location;

    private ID3v1 tags;


    
    public Song() {
        this.id = CPT++;
        this.location = "Unknown location";
        this.tags = new ID3v1();
    }
    
    
    public Song(String file) throws IOException {
        this.id = CPT++;
        this.location = file;
        this.tags = new ID3v1(file);
    }

    public int getId() {
        return this.id;
    }

    public String getLocation() {
        return this.location;
    }



    public void setId(int id) {
        this.id = id;
    }

    public void setLocation(String lo) {
        this.location = lo;
    }


    @Override
    public String toString() {
        return "Music{\n id=" + id + "\n"
                + " location = " + location + "\n"
                +"  "+ tags + "\n}";
    }
}
