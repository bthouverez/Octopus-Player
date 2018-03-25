package com.octopus.database;

import java.util.List;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.octopus.model.Music;

public class MusicRepository extends Repository {
	
	// Attributs privés //
	private static final String TAG = "MusicRepository";
	private Context context;
	
	// Constructeurs //
	public MusicRepository(Context context) {
		super(context);
		this.context = context;
	}
	
	// Methodes publiques //
	public List<Music> getAllMusic() {
		openDataBase();
		Cursor cursor = dataBase.query("MusicView", new String[] { "musicId",
				"musicTitle", "artistName", "albumName", "musicPath",
				"genreName", "musicDuration", "musicTrack", "musicYear",
				"musicComment" }, null, null, null, null, "musicTitle");
		List<Music> result = convertCursorToListObject(cursor);
		cursor.close();
		closeDataBase();
		return result;
	}

	public List<Music> getMusicFromArtist(String artistName) {
		openDataBase();
		Cursor cursor = dataBase.query("MusicView", new String[] { "musicId",
				"musicTitle", "artistName", "albumName", "musicPath",
				"genreName", "musicDuration", "musicTrack", "musicYear",
				"musicComment" }, "artistName=?", new String[] {artistName},
				null, null, "musicTitle");
		List<Music> result = convertCursorToListObject(cursor);
		cursor.close();
		closeDataBase();
		return result;
	}

	public List<Music> getMusicFromAlbum(String albumName) {
		openDataBase();
		Cursor cursor = dataBase.query("MusicView", new String[] { "musicId",
				"musicTitle", "artistName", "albumName", "musicPath",
				"genreName", "musicDuration", "musicTrack", "musicYear",
				"musicComment" }, "albumName=?", new String[] {albumName}, null,
				null, "musicTitle");
		List<Music> result = convertCursorToListObject(cursor);
		cursor.close();
		closeDataBase();
		return result;
	}
	public List<Music> getMusicFromArtistAndAlbum(String artistName, String albumName) {
		openDataBase();
		Cursor cursor = dataBase.query("MusicView", new String[] { "musicId",
				"musicTitle", "artistName", "albumName", "musicPath",
				"genreName", "musicDuration", "musicTrack", "musicYear",
				"musicComment" }, "albumName=? AND artistName=?", new String[] {albumName, artistName}, null,
				null, "musicTitle");
		List<Music> result = convertCursorToListObject(cursor);
		cursor.close();
		closeDataBase();
		return result;
	}
	public List<Music> getMusicFromPlaylist(int playlistId) {
		Log.i(TAG, "Chargement des musiques de la playlist id="+playlistId);
		openDataBase();
		Cursor cursor = dataBase.rawQuery("select MusicView.musicId,musicTitle,artistName,albumName,musicPath,genreName,musicDuration,musicTrack,musicYear,musicComment from MusicView,PlaylistAssoc where MusicView.musicId=PlaylistAssoc.musicId and PlaylistAssoc.playlistId=?;", new String[] {String.valueOf(playlistId)});
		Log.i(TAG, "Conversion en list...");
		List<Music> result = convertCursorToListObject(cursor);
		cursor.close();
		closeDataBase();
		return result;
	}
	public Music get(int id) {
		openDataBase();
		Cursor cursor = dataBase.query("MusicView", new String[] { "musicId",
				"musicTitle", "artistName", "albumName", "musicPath",
				"genreName", "musicDuration", "musicTrack", "musicYear",
				"musicComment" }, "musicId=" + id, null, null, null, null, "1");
		
		if (cursor.moveToFirst()) {
			Music result = convertCursorToObject(cursor);
			cursor.close();
			closeDataBase();
			return result;
		}
		else {
			cursor.close();
			return null;
		}
	}
	
	public int add(Music element) {
		Log.d(TAG, "Tentative ajout musique \""+element.toString()+"\"...");
		
		//TODO : gérer les cas ou un artiste (idem album, genre) ne pointe plus vers aucune musique
		int id_artist, id_album, id_genre;
		ArtistRepository artistRepo = new ArtistRepository(context);
		id_artist = artistRepo.add(element.getArtist());
		if (id_artist == -1) {
			// Erreur d'insertion de l'artiste, on n'insère pas la musique
			return -1;
		} else {
			AlbumRepository albumRepo = new AlbumRepository(context);
			id_album = albumRepo.add(element.getAlbum());
			if (id_album == -1) {
				// Erreur d'insertion d'un album, on n'insère pas la musique
				return -1;
			} else {
				GenreRepository genreRepo = new GenreRepository(context);
				id_genre = genreRepo.add(element.getGenre());
				if (id_genre == -1) {
					// Erreur d'insertion d'un genre, on n'insère pas la musique
					return -1;
				} else {
					// Artiste, album et genre existant, insertion de la musique
					ContentValues data = new ContentValues();
					data.put("artistId", id_artist);
					data.put("albumId", id_album);
					data.put("musicTitle", element.getTitle());
					data.put("musicPath", element.getPath().getPath());
					data.put("musicYear", element.getYear());
					data.put("musicTrack", element.getTrack());
					data.put("musicComment", element.getComment());
					data.put("musicDuration", element.getDuration());
					data.put("genreId", id_genre);
					data.put("stillExist", true);
					openDataBase();
					int retour = (int) dataBase.insert("Music", null, data);
					//closeDataBase();
					Log.v(TAG, "Tentative ajout musique " + element.toString()
							+ " enregistré avec l'id " + retour);
					return retour;
				}
			}
		}
	}

	public int update(Music element) {
		// TODO : tester + commenter
		int artisteId, albumId, genreId;
		artisteId = dataBase.query("Artist", new String[] { "artistId" },
				"artistName=?", new String[] {element.getArtist()}, null, null,
				null).getInt(0);
		albumId = dataBase.query("Album", new String[] { "albumId" },
				"albumName=?", new String[] {element.getAlbum()}, null, null,
				null).getInt(0);
		genreId = dataBase.query("Genre", new String[] { "genreId" },
				"genreName=?", new String[] {element.getGenre()}, null, null,
				null).getInt(0);
		ContentValues data = new ContentValues();
		data.put("artistId", artisteId);
		data.put("albumId", albumId);
		data.put("musicTitle", element.getTitle());
		data.put("musicPath", element.getPath().getPath());
		data.put("musicYear", element.getYear());
		data.put("musicTrack", element.getTrack());
		data.put("musicComment", element.getComment());
		data.put("musicDuration", element.getDuration());
		data.put("genreId", genreId);
		long result;
		try {
			openDataBase();
			result = dataBase.update("Music", data,
					"musicId=?" , new String[] {String.valueOf(element.getId())});
		} catch (SQLiteConstraintException e) {
			result = -1;
		}
		closeDataBase();
		return (int) result;
	}

	public boolean delete(int id) {
		openDataBase();
		Cursor cursor = dataBase.query("MusicView", new String[] {
				"artistName", "albumName" }, "musicId=?", new String[] {String.valueOf(id)}, null, null,
				null);
		if(cursor.moveToFirst()){
			String artistName = cursor.getString(0);
			String albumName = cursor.getString(1);
			int result = dataBase.delete("Music", "musicId=?",
					new String[] { String.valueOf(id) });
			cursor = dataBase.query("MusicView", new String[] { "musicId" },
					"artistName='" + artistName + "'", null, null, null, null);
			if (cursor.getCount() == 0) {
				dataBase.delete("Artist", "artistName=?", new String[] {artistName});
			}
			cursor = dataBase.query("MusicView", new String[] { "musicId" },
					"albumName=?", new String[] {albumName}, null, null, null);
			if (cursor.getCount() == 0) {
				dataBase.delete("Album", "albumName=?", new String[] {albumName});
			}
			cursor.close();
			return (result != 0);
		} else {
			cursor.close();
			return false;
		}
	}
	
	public Vector<Music> updateFilesList(Vector<Music> initialVect) {
		openDataBase();
		
		ContentValues data1 = new ContentValues();
		data1.put("stillExist", 0);
		int rowsAffected=dataBase.update("Music", data1, null, null);
		Log.v(TAG, String.valueOf(rowsAffected)+" rows set to 'not exists'");
		ContentValues data2 = new ContentValues();
		data2.put("stillExist", 1);
		
		Cursor cursor;
		Vector<Music> newFiles = new Vector<Music>();
		for(Music actuMusic : initialVect) {
			String actuPath = actuMusic.getPath().getPath();
			Log.v(TAG, "Recherche dans la base de : "+actuPath);
			cursor = dataBase.query("Music", new String[] { "musicId" }, "musicPath=?", new String[] {actuPath}, null, null, null, "1");
			if(cursor.getCount() == 0) {
				Log.v(TAG, "Aucune donnée trouvée, ajout à la liste d'insertion");
				add(actuMusic);
				newFiles.add(actuMusic);
			} else {
				cursor.moveToFirst();
				dataBase.update("Music", data2, "musicId=?", new String[] {String.valueOf(cursor.getInt(0))});
				Log.v(TAG, "Donnée trouvée");
			}
			cursor.close();
		}
		
		cursor = dataBase.query("Music", new String[] { "musicId" }, "stillExist=?", new String[] {"0"}, null, null, null, null);
		while(cursor.moveToNext()){
			Log.v(TAG, String.valueOf(cursor.getInt(0))+" doesn't exist anymore. We delete it from database.");
			delete(cursor.getInt(0));
		}
		cursor.close();
		closeDataBase();
		return newFiles;
	}
}
