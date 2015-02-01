package cn.ljj.musicplayer.database;

import java.util.Arrays;
import java.util.List;

import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.files.BaiduMusicSearch;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MusicProvider extends ContentProvider {
	private MusicPlayerDatabase mDBHelper = null;
	ContentResolver mContentResolver = null;
	private static String TAG = "MusicProvider";
	public static final String AUTHORITY = "music-provider";
	public static final String URI_BASE = "content://" + AUTHORITY;
	public static final String URI_ALL_LIST = URI_BASE + "/list";
	public static final String URI_LIST = URI_BASE + "/list";
	public static final String URI_ALL_MUSIC = URI_BASE + "/music";
	public static final String URI_MUSIC = URI_BASE + "/music";
	public static final String URI_LIST_MUSIC = URI_BASE + "/music/list";
	public static final String URI_INTERNET = URI_BASE + "/internet";

	public static final int ALL_LIST = 0;
	public static final int LIST = 1;
	public static final int ALL_MUSIC = 2;
	public static final int MUSIC = 3;
	public static final int LIST_MUSIC = 4;
	public static final int INTERNET = 5;

	private static UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sMatcher.addURI(AUTHORITY, "list", ALL_LIST);
		sMatcher.addURI(AUTHORITY, "list/#", LIST);
		sMatcher.addURI(AUTHORITY, "music", ALL_MUSIC);
		sMatcher.addURI(AUTHORITY, "music/#", MUSIC);
		sMatcher.addURI(AUTHORITY, "music/list/#", LIST_MUSIC);
		sMatcher.addURI(AUTHORITY, "internet", INTERNET);
	}

	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Logger.d(TAG, "delete " + uri + "; selection=" + selection
				+ "; selectionArgs=" + Arrays.toString(selectionArgs));
		int ret = 0;
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		int match = sMatcher.match(uri);
		switch (match) {
		case ALL_LIST:
			ret = db.delete(MusicPlayerDatabase.TABLE_LIST, selection,
					selectionArgs);
			break;
		case LIST:
			ret = db.delete(MusicPlayerDatabase.TABLE_LIST, "_id = "
					+ uri.getPathSegments().get(1), null);
			if (ret > 0) {
				db.delete(MusicPlayerDatabase.TABLE_MUSICS,
						MusicPlayerDatabase.LIST_ID + " = "
								+ uri.getPathSegments().get(1), null);
			}
			break;
		case ALL_MUSIC:
			ret = db.delete(MusicPlayerDatabase.TABLE_MUSICS, selection,
					selectionArgs);
			break;
		case MUSIC:
			ret = db.delete(MusicPlayerDatabase.TABLE_MUSICS, "_id = "
					+ uri.getPathSegments().get(1), null);
			break;
		case LIST_MUSIC:
			ret = db.delete(MusicPlayerDatabase.TABLE_MUSICS,
					MusicPlayerDatabase.LIST_ID + " = "
							+ uri.getPathSegments().get(2), null);
			break;
		case INTERNET:
		default:
			Logger.e(TAG, "delete " + uri + " is not yet supported!");
		}
		if (ret > 0) {
			mContentResolver.notifyChange(uri, null);
		}
		return ret;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Logger.d(TAG, "insert " + uri + " values=" + values);
		long ret = 0;
		Uri retUri = null;
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		int match = sMatcher.match(uri);
		switch (match) {
		case LIST:
			ret = db.insert(MusicPlayerDatabase.TABLE_LIST, null, values);
			break;
		case MUSIC:
			ret = db.insert(MusicPlayerDatabase.TABLE_MUSICS, null, values);
			break;
		case ALL_MUSIC:
		case LIST_MUSIC:
		case ALL_LIST:
		case INTERNET:
		default:
			Logger.e(TAG, "insert " + uri + " is not yet supported!");
		}
		if (ret > 0) {
			mContentResolver.notifyChange(uri, null);
			retUri = uri.buildUpon().appendPath(String.valueOf(ret)).build();
		}
		return retUri;
	}

	@Override
	public boolean onCreate() {
		mDBHelper = MusicPlayerDatabase.getInstance(getContext());
		mContentResolver = getContext().getContentResolver();
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Logger.d(TAG,
				"query " + uri + "; projection=" + Arrays.toString(projection)
						+ "; selection=" + selection + "; selectionArgs="
						+ Arrays.toString(selectionArgs) + "; sortOrder="
						+ sortOrder);
		Cursor cursor = null;
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		int match = sMatcher.match(uri);
		switch (match) {
		case ALL_LIST:
			cursor = db.query(MusicPlayerDatabase.TABLE_LIST, projection,
					selection, selectionArgs, null, null, sortOrder);
			break;
		case LIST:
			cursor = db.query(MusicPlayerDatabase.TABLE_LIST, projection,
					"_id = " + uri.getPathSegments().get(1), null, null, null,
					null);
			break;
		case ALL_MUSIC:
			cursor = db.query(MusicPlayerDatabase.TABLE_MUSICS, projection,
					selection, selectionArgs, null, null, sortOrder);
			break;
		case MUSIC:
			cursor = db.query(MusicPlayerDatabase.TABLE_MUSICS, projection,
					"_id = " + uri.getPathSegments().get(1), null, null, null,
					null);
			break;
		case LIST_MUSIC:
			cursor = db.query(MusicPlayerDatabase.TABLE_MUSICS, projection,
					MusicPlayerDatabase.LIST_ID + " = "
							+ uri.getPathSegments().get(2), null, null, null,
					null);
			break;
		case INTERNET:
			String keys = uri.getQueryParameter(BaiduMusicSearch.KEY_QUERY);
			String pageSizeStr = uri
					.getQueryParameter(BaiduMusicSearch.KEY_PAGE_SIZE);
			String pageNoStr = uri
					.getQueryParameter(BaiduMusicSearch.KEY_PAGE_NO);
			Logger.d(TAG, "query searching music keys=" + keys + "; pageSize=" + pageSizeStr + "; pageNo="+pageNoStr);
			cursor = getFromInternet(keys, pageSizeStr, pageNoStr);
			break;
		default:
		}
		if (cursor != null) {
			cursor.setNotificationUri(mContentResolver, uri);
		}
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		Logger.d(
				TAG,
				"update " + uri + "; selection=" + selection + "; values="
						+ values + "; selectionArgs="
						+ Arrays.toString(selectionArgs));
		int match = sMatcher.match(uri);
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		int ret = 0;
		switch (match) {
		case ALL_LIST:
			ret = db.update(MusicPlayerDatabase.TABLE_LIST, values, selection,
					selectionArgs);
			break;
		case LIST:
			ret = db.update(MusicPlayerDatabase.TABLE_LIST, values, "_id = "
					+ uri.getPathSegments().get(1), null);
			break;
		case ALL_MUSIC:
			ret = db.update(MusicPlayerDatabase.TABLE_MUSICS, values,
					selection, selectionArgs);
			break;
		case MUSIC:
			ret = db.update(MusicPlayerDatabase.TABLE_MUSICS, values, "_id = "
					+ uri.getPathSegments().get(1), null);
			break;
		case LIST_MUSIC:
			ret = db.update(MusicPlayerDatabase.TABLE_MUSICS, values,
					MusicPlayerDatabase.LIST_ID + " = "
							+ uri.getPathSegments().get(2), null);
			break;
		case INTERNET:
		default:
			Logger.e(TAG, "update " + uri + " is not yet supported!");
		}
		if (ret > 0) {
			mContentResolver.notifyChange(uri, null);
		}
		return ret;
	}

	private Cursor getFromInternet(String keys, String pageSizeStr,
			String pageNoStr) {
		Logger.d(TAG, "getFromInternet searching music on thread " + Thread.currentThread());
		MatrixCursor cursor = new MatrixCursor(
				MusicPlayerDatabase.MUSIC_COLUMNS);
		int pageSize = 10;
		int pageNo = 0;
		if (keys == null) {
			keys = "";
		}
		try {
			if (pageSizeStr != null) {
				pageSize = Integer.parseInt(pageSizeStr);
			}
			if (pageNoStr != null) {
				pageNo = Integer.parseInt(pageNoStr);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		try {
			BaiduMusicSearch searcher = new BaiduMusicSearch();
			List<MusicInfo> result = searcher
					.searchSync(keys, pageSize, pageNo);
			int i = 0;
			if (result != null && result.size() > 0) {
				for (MusicInfo music : result) {
					Object[] columnValues = new Object[MusicPlayerDatabase.MUSIC_COLUMNS.length];
					columnValues[MusicPlayerDatabase.INDEX_ID] = i++;// music.getId();
					columnValues[MusicPlayerDatabase.INDEX_LIST_ID] = music.getListId();
					columnValues[MusicPlayerDatabase.INDEX_NAME] = music.getName();
					columnValues[MusicPlayerDatabase.INDEX_ARTIST] = music.getArtist();
					columnValues[MusicPlayerDatabase.INDEX_ALBUM] = music.getAlbum();
					columnValues[MusicPlayerDatabase.INDEX_MUSIC_PATH] = music.getMusicPath();
					columnValues[MusicPlayerDatabase.INDEX_PIC_PATH] = music.getPicPath();
					columnValues[MusicPlayerDatabase.INDEX_LRC_PATH] = music.getLrcPath();
					columnValues[MusicPlayerDatabase.INDEX_DURATION] = music.getDuration();
					columnValues[MusicPlayerDatabase.INDEX_SONG_ID] = music.getSongId();
					columnValues[MusicPlayerDatabase.INDEX_ALL_ARTIST_ID] = music.getAllArtistId();
					columnValues[MusicPlayerDatabase.INDEX_ALBUM_ID] = music.getAlbumId();
					columnValues[MusicPlayerDatabase.INDEX_LRC_LINK] = music.getLrclink();
					columnValues[MusicPlayerDatabase.INDEX_ALL_RATE] = music.getAllRate();
					columnValues[MusicPlayerDatabase.INDEX_CHARGE] = music.getCharge();
					columnValues[MusicPlayerDatabase.INDEX_RESOURCE_TYPE] = music.getResourceType();
					columnValues[MusicPlayerDatabase.INDEX_HAVE_HIGH] = music.getHavehigh();
					columnValues[MusicPlayerDatabase.INDEX_COPY_TYPE] = music.getCopyType();
					columnValues[MusicPlayerDatabase.INDEX_RELATE_STATUS] = music.getRelateStatus();
					columnValues[MusicPlayerDatabase.INDEX_HAS_MV] = music.getHasMv();
					columnValues[MusicPlayerDatabase.INDEX_SONG_PIC_SMALL] = music.getSongPicSmall();
					columnValues[MusicPlayerDatabase.INDEX_SONG_PIC_BIG] = music.getSongPicBig();
					columnValues[MusicPlayerDatabase.INDEX_SONG_PIC_RADIO] = music.getSongPicRadio();
					columnValues[MusicPlayerDatabase.INDEX_FORMAT] = music.getFormat();
					columnValues[MusicPlayerDatabase.INDEX_RATE] = music.getRate();
					columnValues[MusicPlayerDatabase.INDEX_SIZE] = music.getSize();
					columnValues[MusicPlayerDatabase.INDEX_APPENDIX] = music.getAppendix();
					columnValues[MusicPlayerDatabase.INDEX_CONTENT] = music.getContent();;
					cursor.addRow(columnValues);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cursor;
	}
}
