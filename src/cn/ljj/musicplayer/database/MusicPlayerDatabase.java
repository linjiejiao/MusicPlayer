package cn.ljj.musicplayer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ��ݱ��SQLiteOpenHelper
 * @author jiajian.liang
 *
 */
public class MusicPlayerDatabase extends SQLiteOpenHelper{

	private static final String DATABASENAME = "playlist.db";
	private static final int DATABASEVERSION = 1;

	public static final String TABLE_LIST = "list";
	public static final String TABLE_MUSICS = "musics";
	//columns for table musics
    public static final String _ID = "_id";
	public static final String LIST_ID = "list_id";
	public static final String NAME = "name";
	public static final String ARTIST = "artist";
	public static final String ALBUM = "album";
	public static final String MUSIC_PATH = "music_path";
	public static final String PIC_PATH = "pic_path";
	public static final String LRC_PATH = "lrc_path";
	public static final String DURATION = "duration";
	//online
	public static final String SONG_ID = "song_id";
	public static final String ALL_ARTIST_ID = "all_artist_id";
	public static final String ALBUM_ID = "album_id";
	public static final String LRC_LINK = "lrc_link";
	public static final String ALL_RATE = "all_rate";
	public static final String CHARGE = "charge";
	public static final String RESOURCE_TYPE = "resource_type";
	public static final String HAVE_HIGH = "have_high";
	public static final String COPY_TYPE = "copy_type";
	public static final String RELATE_STATUS = "relate_status";
	public static final String HAS_MV = "has_mv";
	public static final String SONG_PIC_SMALL = "song_pic_small";
	public static final String SONG_PIC_BIG = "song_pic_big";
	public static final String SONG_PIC_RADIO  = "song_pic_radio";
	public static final String FORMAT  = "format";
	public static final String RATE  = "rate";
	public static final String SIZE  = "size";
	public static final String APPENDIX  = "appendix";
	public static final String CONTENT  = "content";
	//columns for table list
	public static final String LIST_NAME = "list_name";
	public static final String LIST_SIZE = "list_size";
	 
	public static final String[] MUSIC_COLUMNS = new String[] { _ID, LIST_ID,
			NAME, ARTIST, ALBUM, MUSIC_PATH, PIC_PATH, LRC_PATH, DURATION,
			//online
			SONG_ID, ALL_ARTIST_ID, ALBUM_ID, LRC_LINK, ALL_RATE, CHARGE,
			RESOURCE_TYPE, HAVE_HIGH, COPY_TYPE, RELATE_STATUS, HAS_MV,
			SONG_PIC_SMALL, SONG_PIC_BIG, SONG_PIC_RADIO, FORMAT, RATE, SIZE,
			APPENDIX, CONTENT };

	 public static final int INDEX_ID = 0;
	 public static final int INDEX_LIST_ID=1;
	 public static final int INDEX_NAME = 2;
	 public static final int INDEX_ARTIST = 3;
	 public static final int INDEX_ALBUM = 4;
	 public static final int INDEX_MUSIC_PATH = 5;
	 public static final int INDEX_PIC_PATH = 6;
	 public static final int INDEX_LRC_PATH = 7;
	 public static final int INDEX_DURATION = 8;
		//online
	 public static final int INDEX_SONG_ID = 9;
	 public static final int INDEX_ALL_ARTIST_ID = 10;
	 public static final int INDEX_ALBUM_ID = 11;
	 public static final int INDEX_LRC_LINK = 12;
	 public static final int INDEX_ALL_RATE = 13;
	 public static final int INDEX_CHARGE = 14;
	 public static final int INDEX_RESOURCE_TYPE = 15;
	 public static final int INDEX_HAVE_HIGH =16;
	 public static final int INDEX_COPY_TYPE = 17;
	 public static final int INDEX_RELATE_STATUS = 18;
	 public static final int INDEX_HAS_MV = 19;
	 public static final int INDEX_SONG_PIC_SMALL = 20;
	 public static final int INDEX_SONG_PIC_BIG = 21;
	 public static final int INDEX_SONG_PIC_RADIO = 22;
	 public static final int INDEX_FORMAT = 23;
	 public static final int INDEX_RATE = 24;
	 public static final int INDEX_SIZE = 25;
	 public static final int INDEX_APPENDIX = 26;
	 public static final int INDEX_CONTENT = 27; 

	 public static final int INDEX_LIST_NAME = 1;
	 public static final int INDEX_LIST_SIZE=2;
	 
	private MusicPlayerDatabase(Context context) {
		super(context, DATABASENAME, null, DATABASEVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String cmd = "CREATE TABLE " + TABLE_MUSICS 
				+ " (  "
				+ _ID + " INTEGER primary key autoincrement,"
				+ LIST_ID + " INTEGER, "
				+ NAME + " TEXT, "
				+ ARTIST + " TEXT, "
				+ ALBUM + " TEXT, "
				+ MUSIC_PATH + " TEXT, "
				+ PIC_PATH + " TEXT, "
				+ LRC_PATH + " TEXT, "
				+ DURATION + " TEXT,"
				//online
				+ SONG_ID + " TEXT, "
				+ ALL_ARTIST_ID + " TEXT, "
				+ ALBUM_ID + " TEXT, "
				+ LRC_LINK + " TEXT, " 
				+ ALL_RATE + " TEXT, "
				+ CHARGE + " TEXT, "
				+ RESOURCE_TYPE + " TEXT, "
				+ HAVE_HIGH + " TEXT, "
				+ COPY_TYPE + " TEXT, "
				+ RELATE_STATUS + " TEXT, "
				+ HAS_MV + " TEXT, "
				+ SONG_PIC_SMALL + " TEXT, "
				+ SONG_PIC_BIG + " TEXT, "
				+ SONG_PIC_RADIO + " TEXT, "
				+ FORMAT + " TEXT, "
				+ RATE + " INTEGER, "
				+ SIZE + " INTEGER, "
				+ APPENDIX + " TEXT, "
				+ CONTENT + " TEXT)";
		db.execSQL(cmd);
		cmd = "CREATE TABLE " + TABLE_LIST
				+ " (  "
				+ _ID + " INTEGER primary key autoincrement,"
				+ LIST_NAME + " TEXT, "
				+ LIST_SIZE + " INTEGER)";
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
