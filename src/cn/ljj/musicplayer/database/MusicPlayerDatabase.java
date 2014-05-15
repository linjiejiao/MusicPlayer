package cn.ljj.musicplayer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据表的SQLiteOpenHelper
 * @author jiajian.liang
 *
 */
public class MusicPlayerDatabase extends SQLiteOpenHelper{

	private static final String DATABASENAME = "playlist.db";
	private static final int DATABASEVERSION = 1;

	public static final String TABLE_LIST = "list";
	public static final String TABLE_MUSICS = "musics";
	public static final String LIST_ID = "list_id";
	public static final String NAME = "name";
	public static final String ARTIST = "artist";
	public static final String ALBUM = "album";
	public static final String MUSIC_PATH = "music_path";
	public static final String PIC_PATH = "pic_path";
	public static final String LRC_PATH = "lrc_path";
	public static final String DURATION = "duration";

	public static final String LIST_NAME = "list_name";
	public static final String LIST_SIZE = "list_size";
	
	
	private MusicPlayerDatabase(Context context) {
		super(context, DATABASENAME, null, DATABASEVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String cmd = "CREATE TABLE " + TABLE_MUSICS 
				+ " ( _id integer primary key autoincrement,"
				+ LIST_ID + " integer, "
				+ NAME + " varchar(20), "
				+ ARTIST + " varchar(50), "
				+ ALBUM + " varchar(50), "
				+ MUSIC_PATH + " varchar(50), "
				+ PIC_PATH + " varchar(50), "
				+ LRC_PATH + " varchar(50), "
				+ DURATION + " integer)";
		db.execSQL(cmd);
		cmd = "CREATE TABLE " + TABLE_LIST
				+ " ( _id integer primary key autoincrement, "
				+ LIST_NAME + " varchar(20), "
				+ LIST_SIZE + " integer)";
		db.execSQL(cmd);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSICS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);
		onCreate(db);
	}

	static MusicPlayerDatabase sInstance = null;
	public static MusicPlayerDatabase getInstance(Context context){
		if(sInstance == null){
			sInstance = new MusicPlayerDatabase(context);
		}
		return sInstance;
	}
}
