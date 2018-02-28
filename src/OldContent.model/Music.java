package com.octopus.model;

import android.net.Uri;

public class Music implements Comparable<Music> {
	
	/* Attributs privés */
	private int id;
	private String title;
	private String artist;
	private String album;
	private Uri path;
	private String genre;
	private int duration;
	private String track;
	private String year;
	private String comment;
	
	/* Constructeurs */
	public Music(String i_title, String i_album, String i_artist, String i_path, int dur) {
		this.title = i_title;
		this.album = i_album;
		this.artist = i_artist;
		Uri.Builder uriBuilder = new Uri.Builder();
		uriBuilder.path(i_path);
		this.path = uriBuilder.build();
		this.duration = dur;
	}
	public Music(String i_title, String i_album, String i_artist, String i_path, int dur, String i_genre, String i_track, String i_year) {
		this.title = i_title;
		this.album = i_album;
		this.artist = i_artist;
		Uri.Builder uriBuilder = new Uri.Builder();
		uriBuilder.path(i_path);
		this.path = uriBuilder.build();
		this.duration = dur;
		this.genre = i_genre;
		this.track = i_track;
		this.year = i_year;
	}
	/* Constructeur dont les parametres sont dans le même ordre que les valeurs de MusicView */
	public Music(int i_id, String i_title, String i_artist, String i_album, String i_path, String i_genre, int i_duration, String i_year, String i_track, String i_comment) {
		this.id = i_id;
		this.title = i_title;
		this.artist = i_artist;
		this.album = i_album;
		Uri.Builder uriBuilder = new Uri.Builder();
		uriBuilder.path(i_path);
		this.path = uriBuilder.build();
		this.setGenre(i_genre);
		this.duration = i_duration;
		this.track = i_track;
		this.year = i_year;
		this.comment = i_comment;
	}
	
	/* Méthodes public */

	/* Methode set */
	public void setId(int id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public void setComment(String com) {
		this.comment = com;
	}
	public void setPath(Uri address) {
		this.path = address;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public void setTrack(String arg) {
		this.track = arg;
	}
	public void setYear(String arg) {
		this.year = arg;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	/* Methodes get */
	public int getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public String getAlbum() {
		return album;
	}
	public String getArtist() {
		return artist;
	}
	public Uri getPath() {
		return path;
	}
	public int getDuration() {
		return duration;
	}
	public String getTrack(){
		return track;
	}
	public String getYear(){
		return year;
	}
	public String getComment(){
		return comment;
	}
	public String getGenre() {
		return genre;
	}
	
	public String toString() {
		if( title.length()==0 ) {
			return path.getPath();
		}
		else {
			return title;
		}
	}
	public int compareTo(Music arg0) {
		if(this.getPath().equals(arg0.getPath())){
			return 0;
		} else {
			return this.getTitle().compareTo(arg0.getTitle());
		}
	}
}
