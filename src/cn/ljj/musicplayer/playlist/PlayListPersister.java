package cn.ljj.musicplayer.playlist;

import java.util.ArrayList;
import java.util.List;

import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.database.MusicPlayerDatabase;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.util.Log;

public class PlayListPersister {
	private Context mContext = null;
	private SQLiteDatabase db = null;
	private String TAG = "PlayListPersister";

	public PlayListPersister(Context context) {
		mContext = context;
		db = MusicPlayerDatabase.getInstance(mContext).getWritableDatabase();
	}

	public List<MusicInfo> load(String listName) {
		Log.d(TAG, "load listName=" + listName);
		List<MusicInfo> list = new ArrayList<MusicInfo>();
		String sql = "select _id from " + MusicPlayerDatabase.TABLE_LIST
				+ " where " + MusicPlayerDatabase.LIST_NAME + " = " + listName;
		Cursor cursor = null;
		try {
			long listId = -1;
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				listId = cursor.getLong(0);
			}
			cursor.close();

			sql = "select * from " + MusicPlayerDatabase.TABLE_MUSICS
					+ " where " + MusicPlayerDatabase.LIST_ID + " = " + listId;
			cursor = db.rawQuery(sql, null);
			list = getMusics(cursor);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}

	private List<MusicInfo> getMusics(Cursor cursor) {
		List<MusicInfo> list = new ArrayList<MusicInfo>();
		int idIndex = cursor.getColumnIndex("_id");
		int nameIndex = cursor.getColumnIndex(MusicPlayerDatabase.NAME);
		int artistIndex = cursor.getColumnIndex(MusicPlayerDatabase.ARTIST);
		int albumIndex = cursor.getColumnIndex(MusicPlayerDatabase.ALBUM);
		int durationIndex = cursor.getColumnIndex(MusicPlayerDatabase.DURATION);
		int musicPathIndex = cursor
				.getColumnIndex(MusicPlayerDatabase.MUSIC_PATH);
		int lrcPathIndex = cursor.getColumnIndex(MusicPlayerDatabase.LRC_PATH);
		int picPathIndex = cursor.getColumnIndex(MusicPlayerDatabase.PIC_PATH);

		while (cursor.moveToNext()) {
			long _id = cursor.getInt(idIndex);
			String name = cursor.getString(nameIndex);
			String artist = cursor.getString(artistIndex);
			String album = cursor.getString(albumIndex);
			int duration = cursor.getInt(durationIndex);
			String musicPath = cursor.getString(musicPathIndex);
			String lrcPath = cursor.getString(lrcPathIndex);
			String picPath = cursor.getString(picPathIndex);

			MusicInfo music = new MusicInfo(name, musicPath);
			music.setId(_id);
			music.setAlbum(album);
			music.setArtist(artist);
			music.setDuration(duration);
			music.setLrcPath(lrcPath);
			music.setPicPath(picPath);

			list.add(music);
		}
		return list;
	}

	public int persist(List<MusicInfo> list, String listName, boolean cover) {
		Log.d(TAG, "persist list=" + list + "; listName=" + listName
				+ "; cover=" + cover);
		String sql = "select _id from " + MusicPlayerDatabase.TABLE_LIST
				+ " where " + MusicPlayerDatabase.LIST_NAME + " = " + listName;
		Cursor cursor = null;
		try {

			long listId = -1;
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				listId = cursor.getLong(0);
			}
			cursor.close();
			if (listId != -1) {
				if (cover) {
					// clear musics under this list
					db.delete(MusicPlayerDatabase.TABLE_MUSICS,
							MusicPlayerDatabase.LIST_ID + " = " + listId, null);
				}
			} else {
				// create new list
				ContentValues values = new ContentValues();
				values.put(MusicPlayerDatabase.LIST_NAME, listName);
				listId = db.insert(MusicPlayerDatabase.TABLE_LIST,
						MusicPlayerDatabase.LIST_NAME, values);
			}
			if (listId == -1) {
				return -1;
			}
			// insert musics table
			db.beginTransaction();
			for (MusicInfo music : list) {
				ContentValues values = new ContentValues();
				values.put(MusicPlayerDatabase.LIST_ID, listId);
				values.put(MusicPlayerDatabase.NAME, music.getName());
				values.put(MusicPlayerDatabase.ALBUM, music.getAlbum());
				values.put(MusicPlayerDatabase.ARTIST, music.getArtist());
				values.put(MusicPlayerDatabase.DURATION, music.getDuration());
				values.put(MusicPlayerDatabase.MUSIC_PATH, music.getMusicPath());
				values.put(MusicPlayerDatabase.LRC_PATH, music.getLrcPath());
				values.put(MusicPlayerDatabase.PIC_PATH, music.getPicPath());
				db.insert(MusicPlayerDatabase.TABLE_MUSICS,
						MusicPlayerDatabase.NAME, values);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			// updata list count
			sql = "select * from " + MusicPlayerDatabase.TABLE_MUSICS
					+ "where " + MusicPlayerDatabase.LIST_ID + " = " + listId;
			cursor = db.rawQuery(sql, null);
			ContentValues values = new ContentValues();
			values.put(MusicPlayerDatabase.LIST_SIZE, cursor.getCount());
			db.update(MusicPlayerDatabase.TABLE_LIST, values,
					MusicPlayerDatabase.LIST_ID + " = " + listId, null);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return list.size();
	}

	public List<MusicInfo> loadFromMediaStore() {
		Log.d(TAG, "loadFromMediaStore");
		List<MusicInfo> list = new ArrayList<MusicInfo>();
		ContentResolver cr = mContext.getContentResolver();
		Cursor playlistCursor = null;
		try {
			playlistCursor = cr.query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
					null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
			while (playlistCursor.moveToNext()) {
				int isMusic = playlistCursor
						.getInt(playlistCursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC));
				if (isMusic == 1) {
					String name = playlistCursor
							.getString(playlistCursor
									.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
					String path = playlistCursor
							.getString(playlistCursor
									.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
					String duration = playlistCursor
							.getString(playlistCursor
									.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
					String artist = playlistCursor
							.getString(playlistCursor
									.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
					String album = playlistCursor
							.getString(playlistCursor
									.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
					int durationInt = Integer.parseInt(duration);
					MusicInfo music = new MusicInfo(name, path);
					music.setDuration(durationInt);
					music.setAlbum(album);
					music.setArtist(artist);
					list.add(music);
				}
			}
			playlistCursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (playlistCursor != null) {
				playlistCursor.close();
			}
		}
		return list;
	}

	public Long persist(MusicInfo music, String listName) {
		Log.d(TAG, "persist music=" + music + "; listName=" + listName);
		String sql = "select _id from " + MusicPlayerDatabase.TABLE_LIST
				+ " where " + MusicPlayerDatabase.LIST_NAME + " = " + listName;
		long musicId = -1;
		Cursor cursor = null;
		try {
			long listId = -1;
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				listId = cursor.getLong(0);
			}
			cursor.close();
			cursor = null;
			if (listId != -1) {
				ContentValues values = new ContentValues();
				values.put(MusicPlayerDatabase.LIST_ID, listId);
				values.put(MusicPlayerDatabase.NAME, music.getName());
				values.put(MusicPlayerDatabase.ALBUM, music.getAlbum());
				values.put(MusicPlayerDatabase.ARTIST, music.getArtist());
				values.put(MusicPlayerDatabase.DURATION, music.getDuration());
				values.put(MusicPlayerDatabase.MUSIC_PATH, music.getMusicPath());
				values.put(MusicPlayerDatabase.LRC_PATH, music.getLrcPath());
				values.put(MusicPlayerDatabase.PIC_PATH, music.getPicPath());
				musicId = db.insert(MusicPlayerDatabase.TABLE_MUSICS,
						MusicPlayerDatabase.NAME, values);
			}
		} catch (Exception e) {
			e.printStackTrace();
			musicId = -1;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return musicId;
	}

	public int removeMusic(long id) {
		Log.d(TAG, "removeMusic id=" + id);
		if (id == -1) {
			return -1;
		}
		return db.delete(MusicPlayerDatabase.TABLE_MUSICS, "_id = " + id, null);
	}

	public int deletePlayList(String listName) {
		Log.d(TAG, "deletePlayList listName=" + listName);
		String sql = "select _id from " + MusicPlayerDatabase.TABLE_LIST
				+ " where " + MusicPlayerDatabase.LIST_NAME + " = " + listName;
		int ret = -1;
		Cursor cursor = null;
		try {

			long listId = -1;
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				listId = cursor.getLong(0);
			}
			cursor.close();
			cursor = null;
			if (listId != -1) {
				ret = db.delete(MusicPlayerDatabase.TABLE_MUSICS,
						MusicPlayerDatabase.LIST_ID + " = " + listId, null);
				db.delete(MusicPlayerDatabase.TABLE_LIST, " _id = " + listId,
						null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return ret;
	}

	public List<SavedList> getAllSavedPlayList() {
		List<SavedList> list = new ArrayList<SavedList>();
		String sql = "select * from " + MusicPlayerDatabase.TABLE_LIST;
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			int idIndex = cursor.getColumnIndex("_id");
			int countIndex = cursor
					.getColumnIndex(MusicPlayerDatabase.LIST_SIZE);
			int nameIndex = cursor
					.getColumnIndex(MusicPlayerDatabase.LIST_NAME);
			while (cursor.moveToNext()) {
				SavedList l = new SavedList();
				l.setCount(cursor.getInt(countIndex));
				l.setListId(cursor.getInt(idIndex));
				l.setListName(cursor.getString(nameIndex));
				list.add(l);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		Log.d(TAG, "getAllSavedPlayList list=" + list);
		return list;
	}
}
