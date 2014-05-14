package cn.ljj.musicplayer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据表的SQLiteOpenHelper
 * @author jiajian.liang
 *
 */
public class DBOpenHelper extends SQLiteOpenHelper{

	private static final String DATABASENAME = "playlist.db";
	private static final int DATABASEVERSION = 1;
	private String TABLE_LIST = "list";
	private String TABLE_MUSICS = "musics";
	public static final String MUSIC_ID = "_id";
	public static final String NAME = "name";
	public static final String ARTIST = "artist";
	public static final String ALBUM = "album";
	public static final String MUSIC_PATH = "music_path";
	public static final String PIC_PATH = "pic_path";
	public static final String LRC_PATH = "lrc_path";
	public static final String DURATION = "duration";
	
	public DBOpenHelper(Context context) {
		super(context, DATABASENAME, null, DATABASEVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String cmd = "CREATE TABLE " + TABLE_MUSICS
				+ " (musicId integer primary key autoincrement,"
				+ " name varchar(20),"
				+ " path varchar(50),"
				+ " duration integer)";
		db.execSQL(cmd);
		cmd = "CREATE TABLE " + TABLE_LIST
				+ " (_id integer primary key autoincrement,"
				+ " playlist_name varchar(20),"
				+ " table_name varchar(50),"
				+ " count integer)";
		db.execSQL(cmd);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSICS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);
		onCreate(db);
	}
	
}
