package com.octopus.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/* Classe d'aide à la création et à la mise à jour d'une base de données */

public class DataBaseOpenHelper extends SQLiteOpenHelper {
	private static String TAG = "DATABASE";

	private final static int BASE_VERSION = 5;
	private final static String BASE_NAME = "octopus.db";

	public static final String MUSIC_INDEX_PATH = "check";

		
	/* ------------------------------ */
	/* Requetes de création de tables */
	/* ------------------------------ */
	/* Table Album */
	private static final String QUERY_CREATE_ALBUM = "create table Album (albumId  integer primary key autoincrement, albumName varchar(254));";
	/* Table Artist */
	private static final String QUERY_CREATE_ARTIST = "create table Artist(artistId integer primary key autoincrement, artistName varchar(254));";
	/* Table Genre */
	private static final String QUERY_CREATE_GENRE = "create table Genre(genreId integer primary key autoincrement, genreName varchar(128));";
	/* Table Music */
	private static final String QUERY_CREATE_MUSIC = "create table Music(albumId integer constraint fk_album references Album(albumId),artistId integer constraint fk_artist references Artist(artistId),musicId integer primary key autoincrement,genreId integer constraint fk_genre references Genre(genreId),musicTitle varchar(254),musicComment varchar(508),musicTrack varchar(10),musicPath varchar(508),musicYear varchar(4), musicDuration integer, stillExist bool);";
	/* Table Playlist */
	private static final String QUERY_CREATE_PLAYLIST = "create table Playlist(playlistId integer primary key autoincrement,playlistName varchar(128));";
	/* Table playlistAssoc */
	private static final String QUERY_CREATE_PLAYLISTASSOC = "create table PlaylistAssoc(playlistId integer constraint agr_playlist references Playlist(playlistId),musicId integer constraint agr_music references Music(musicId), PRIMARY KEY(musicId, playlistId));";
	
	/* ---------------------------- */
	/* Requetes de création de vues */
	/* ---------------------------- */
	/* Vue MusicView */
	private static final String QUERY_CREATE_MUSICVIEW = "create view MusicView as select musicId,musicTitle,artistName,albumName,musicPath,genreName,musicDuration,musicYear,musicTrack,musicComment from `Music` join `Artist` on `Music`.`artistId` = `Artist`.`artistId` join `Album` on `Music`.`albumId` = `Album`.`albumId` join 'Genre' on `Music`.`genreId` = `Genre`.`genreId` ORDER BY `Music`.`musicId`;";

	
	
	
	public DataBaseOpenHelper(Context context, CursorFactory cursorfactory) {
		super(context, BASE_NAME, cursorfactory, BASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "Création de la base...");
		/* Création des tables */
		db.execSQL(QUERY_CREATE_ALBUM);
		db.execSQL(QUERY_CREATE_ARTIST);
		db.execSQL(QUERY_CREATE_GENRE);
		db.execSQL(QUERY_CREATE_MUSIC);
		db.execSQL(QUERY_CREATE_PLAYLIST);
		db.execSQL(QUERY_CREATE_PLAYLISTASSOC);
		
		/* Création des vues */
		db.execSQL(QUERY_CREATE_MUSICVIEW);
	}

	@Override
	public void onUpgrade(SQLiteDatabase base, int oldVersion, int newVersion) {
		if (BASE_VERSION < newVersion) {
			/* Destruction des tables */
			base.execSQL("drop table Album;");
			base.execSQL("drop table Artist;");
			base.execSQL("drop table Genre;");
			base.execSQL("drop table Music;");
			base.execSQL("drop table Playlist;");
			base.execSQL("drop table PlaylistAssoc;");
			/* Destruction des index */
			base.execSQL("drop index index_albumName on Album;");
			base.execSQL("drop index index_artistName on Artist;");
			base.execSQL("drop index index_genreName on Genre;");
			base.execSQL("drop index index_musicPath on Music;");
			base.execSQL("drop index index_playlistName on Playlist;");
			/* Destruction des vues */
			base.execSQL("drop view MusicView;");
			
			/* Nouvelle création de base */
			onCreate(base);
		}
	}
	/*
	 * public int addMusique(String chemin, int id_artiste, int id_album, String
	 * titre, int annee, int piste, String commentaire) { ContentValues data =
	 * new ContentValues(); data.put("id_artiste", id_artiste);
	 * data.put("id_album", id_album); data.put("titre", titre);
	 * data.put("chemin", chemin); data.put("annee", annee); data.put("piste",
	 * piste); data.put("commentaire", commentaire); return (int)
	 * sqldb.insert("musique", null, data);
	 * 
	 * }
	 * 
	 * public int addMusique(String chemin, String nom_artiste, String
	 * nom_album, String titre, int annee, int piste, String commentaire) { //
	 * TODO : créer l'artiste si il n'existe pas, créer l'album si il //
	 * n'existe pas avec l'id artiste, et appeler le premier addMusique avec //
	 * les deux ids obtenus int id_artiste, id_album; id_artiste =
	 * addArtiste(nom_artiste); if (id_artiste == -1) { return -1; } else {
	 * id_album = addAlbum(id_artiste, nom_album); if (id_album == -1) { return
	 * -1; } else { return addMusique(chemin, id_artiste, id_album, titre,
	 * annee, piste, commentaire); } } }
	 * 
	 * public boolean deleteMusique(int id) { return sqldb.delete("musique",
	 * "id_musique=" + id, null) != 0; }
	 * 
	 * public boolean deleteMusique(String chemin) { return
	 * sqldb.delete("musique", "chemin='" + chemin + "'", null) != 0; // à faire
	 * : vérifier que ce n'était pas la dernière musique d'un album // qui se
	 * retrouverai vide }
	 * 
	 * public int addAlbum(int id_artiste, String nom) { ContentValues data =
	 * new ContentValues(); data.put("nom", nom); data.put("id_artiste",
	 * id_artiste); long result = sqldb.insert("album", null, data); if (result
	 * == -1) { Cursor c = sqldb.query("album", new String[] { "id_album" },
	 * "nom=" + nom, null, null, null, null); c.moveToFirst(); return
	 * c.getInt(0); } else { return (int) result; } }
	 * 
	 * public int addArtiste(String nom) { ContentValues data = new
	 * ContentValues(); data.put("nom", nom); long result =
	 * sqldb.insert("artiste", null, data); if (result == -1) { Cursor c =
	 * sqldb.query("artiste", new String[] { "id_artiste" }, "nom=" + nom, null,
	 * null, null, null); c.moveToFirst(); return c.getInt(0); } else { return
	 * (int) result; } }
	 * 
	 * public void synchronize(MusicBank MB) { ArrayList<MusicBankItem> liste =
	 * MB.getBank(); Iterator<MusicBankItem> it = liste.iterator();
	 * ContentValues data = new ContentValues(); data.put("check", 0);
	 * sqldb.update("musique", data, null, null); ContentValues data2 = new
	 * ContentValues(); data.put("check", 1); while (it.hasNext()) {
	 * MusicBankItem musique = it.next(); int id_musique =
	 * this.addMusique(musique.getAddress().getPath(), musique.getArtist(),
	 * musique.getAlbum(), musique.getTitle(), musique.getAnnee(),
	 * musique.getPiste(), musique.getCom()); sqldb.update("musique", data2,
	 * "id_musique=" + id_musique, null); } sqldb.delete("musique", "check=0",
	 * null); }
	 */
}
