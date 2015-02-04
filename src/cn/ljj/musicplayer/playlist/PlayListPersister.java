
package cn.ljj.musicplayer.playlist;

import java.util.ArrayList;
import java.util.List;

import cn.ljj.musicplayer.data.MusicInfo;
import cn.ljj.musicplayer.database.Logger;
import cn.ljj.musicplayer.database.MusicPlayerDatabase;
import cn.ljj.musicplayer.database.MusicProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;

public class PlayListPersister {
    private Context mContext = null;
    private ContentResolver mContentResolver = null;
    private SQLiteDatabase db = null;
    private String TAG = "PlayListPersister";

    public PlayListPersister(Context context) {
        mContext = context;
        mContentResolver = mContext.getContentResolver();
        db = MusicPlayerDatabase.getInstance(mContext).getWritableDatabase();
    }

    public List<MusicInfo> load(long listId) {
        Logger.d(TAG, "load listId=" + listId);
        List<MusicInfo> list = new ArrayList<MusicInfo>();
        Cursor cursor = null;
        try {
            Uri uri = MusicProvider.URI_LIST.buildUpon().appendPath(String.valueOf(listId)).build();
            cursor = mContentResolver.query(uri, null, null, null, null);
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

    protected List<MusicInfo> getMusics(Cursor cursor) {
        List<MusicInfo> list = new ArrayList<MusicInfo>();
        int idIndex = cursor.getColumnIndex("_id");
        int nameIndex = cursor.getColumnIndex(MusicPlayerDatabase.NAME);
        int artistIndex = cursor.getColumnIndex(MusicPlayerDatabase.ARTIST);
        int albumIndex = cursor.getColumnIndex(MusicPlayerDatabase.ALBUM);
        int durationIndex = cursor.getColumnIndex(MusicPlayerDatabase.DURATION);
        int musicPathIndex = cursor.getColumnIndex(MusicPlayerDatabase.MUSIC_PATH);
        int lrcPathIndex = cursor.getColumnIndex(MusicPlayerDatabase.LRC_PATH);
        int picPathIndex = cursor.getColumnIndex(MusicPlayerDatabase.PIC_PATH);
        int listIdIndex = cursor.getColumnIndex(MusicPlayerDatabase.LIST_ID);
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            long _id = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            String artist = cursor.getString(artistIndex);
            String album = cursor.getString(albumIndex);
            int duration = cursor.getInt(durationIndex);
            String musicPath = cursor.getString(musicPathIndex);
            String lrcPath = cursor.getString(lrcPathIndex);
            String picPath = cursor.getString(picPathIndex);
            long listId = cursor.getLong(listIdIndex);

            MusicInfo music = new MusicInfo(name, musicPath);
            music.setId(_id);
            music.setAlbum(album);
            music.setArtist(artist);
            music.setDuration(duration);
            music.setLrcPath(lrcPath);
            music.setPicPath(picPath);
            music.setListId(listId);
            list.add(music);
        }
        return list;
    }

    public long persist(List<MusicInfo> list, String listName, boolean cover) {
         Logger.d(TAG, "persist list=" + list + "; listName=" + listName
                 + "; cover=" + cover);
        Cursor cursor = null;
        long listId = -1;
        try {
            cursor = mContentResolver.query(MusicProvider.URI_LIST, null,
                    MusicPlayerDatabase.LIST_NAME + " = '" + listName + "'", null, null);
            if (cursor.moveToFirst()) {
                listId = cursor.getLong(0);
            }
            cursor.close();
            if (listId != -1 && cover) {
                // clear musics under this list
                Uri uri = MusicProvider.URI_LIST.buildUpon().appendPath(String.valueOf(listId))
                        .build();
                mContentResolver.delete(uri, null, null);
            }
            // create new list
            ContentValues listValues = new ContentValues();
            listValues.put(MusicPlayerDatabase.LIST_NAME, listName);
            listValues.put(MusicPlayerDatabase.LIST_SIZE, list.size());
            Uri returnUri = mContentResolver.insert(MusicProvider.URI_LIST, listValues);
            Logger.e(TAG, "persist bulkInsert returnUri:" + returnUri);
            try {
                listId = Integer.parseInt(returnUri.getPathSegments().get(1));
            } catch (Exception e) {
                listId = -1;
                e.printStackTrace();
            }
            if (listId == -1) {
                return -1;
            }
            // insert musics table
            ContentValues[] cvs = new ContentValues[list.size()];
            for (int i = 0; i < list.size(); i++) {
                MusicInfo music = list.get(i);
                ContentValues values = new ContentValues();
                values.put(MusicPlayerDatabase.LIST_ID, listId);
                values.put(MusicPlayerDatabase.NAME, music.getName());
                values.put(MusicPlayerDatabase.ALBUM, music.getAlbum());
                values.put(MusicPlayerDatabase.ARTIST, music.getArtist());
                values.put(MusicPlayerDatabase.DURATION, music.getDuration());
                values.put(MusicPlayerDatabase.MUSIC_PATH, music.getMusicPath());
                values.put(MusicPlayerDatabase.LRC_PATH, music.getLrcPath());
                values.put(MusicPlayerDatabase.PIC_PATH, music.getPicPath());
                cvs[i] = values;
            }
            mContentResolver.bulkInsert(MusicProvider.URI_MUSIC, cvs);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listId;
    }

    public List<MusicInfo> loadFromMediaStore() {
        Logger.d(TAG, "loadFromMediaStore");
        List<MusicInfo> list = new ArrayList<MusicInfo>();
        ContentResolver cr = mContext.getContentResolver();
        Cursor playlistCursor = null;
        try {
            playlistCursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            while (playlistCursor.moveToNext()) {
                int isMusic = playlistCursor.getInt(playlistCursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC));
                if (isMusic == 1) {
                    String name = playlistCursor.getString(playlistCursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                    String path = playlistCursor.getString(playlistCursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    String duration = playlistCursor.getString(playlistCursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                    String artist = playlistCursor.getString(playlistCursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    String album = playlistCursor.getString(playlistCursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                    int durationInt = Integer.parseInt(duration);
                    String format = "";
                    if (name.contains(".")) {
                        String temp = name;
                        name = temp.substring(0, temp.lastIndexOf("."));
                        format = temp.substring(temp.lastIndexOf(".") + 1);
                    }
                    MusicInfo music = new MusicInfo(name, path);
                    music.setDuration(durationInt);
                    music.setAlbum(album);
                    music.setArtist(artist);
                    music.setFormat(format);
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

    public Long persist(MusicInfo music, long listId) {
        Logger.d(TAG, "persist music=" + music + "; listId=" + listId);
        if (listId == -1) {
            return listId;
        }
        try {
            ContentValues values = new ContentValues();
            values.put(MusicPlayerDatabase.LIST_ID, listId);
            values.put(MusicPlayerDatabase.NAME, music.getName());
            values.put(MusicPlayerDatabase.ALBUM, music.getAlbum());
            values.put(MusicPlayerDatabase.ARTIST, music.getArtist());
            values.put(MusicPlayerDatabase.DURATION, music.getDuration());
            values.put(MusicPlayerDatabase.MUSIC_PATH, music.getMusicPath());
            values.put(MusicPlayerDatabase.LRC_PATH, music.getLrcPath());
            values.put(MusicPlayerDatabase.PIC_PATH, music.getPicPath());
            Uri musicUri = mContentResolver.insert(MusicProvider.URI_MUSIC, values);
            if (musicUri != null) {
                music.setListId(listId);
                long musicId = Long.parseLong(musicUri.getPathSegments().get(1));
                music.setId(musicId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            listId = -1;
        }
        return listId;
    }

    public long removeMusic(long musicId, long listId) {
        Logger.i(TAG, "removeMusic MusicId=" + musicId);
        if (musicId == -1 || listId == -1) {
            return -1;
        }
        db.delete(MusicPlayerDatabase.TABLE_MUSICS, "_id = " + musicId, null);
        return listId;
    }

    public int deletePlayList(long listId) {
        Logger.d(TAG, "deletePlayList listId=" + listId);
        Uri url = MusicProvider.URI_LIST.buildUpon().appendPath(String.valueOf(listId)).build();
        int ret = mContentResolver.delete(url, null, null);
        return ret;
    }

    public long update(MusicInfo music) {
        long musicId = music.getId();
        if (musicId != -1) {
            ContentValues values = new ContentValues();
            values.put(MusicPlayerDatabase.NAME, music.getName());
            values.put(MusicPlayerDatabase.ALBUM, music.getAlbum());
            values.put(MusicPlayerDatabase.ARTIST, music.getArtist());
            values.put(MusicPlayerDatabase.DURATION, music.getDuration());
            values.put(MusicPlayerDatabase.MUSIC_PATH, music.getMusicPath());
            values.put(MusicPlayerDatabase.LRC_PATH, music.getLrcPath());
            values.put(MusicPlayerDatabase.PIC_PATH, music.getPicPath());
            musicId = db.update(MusicPlayerDatabase.TABLE_MUSICS, values, "_id=" + musicId, null);
        }
        return musicId;
    }

}
