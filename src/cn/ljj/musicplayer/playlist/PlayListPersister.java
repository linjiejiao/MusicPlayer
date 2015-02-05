
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
import android.net.Uri;
import android.provider.MediaStore;

public class PlayListPersister {
    private Context mContext = null;
    private ContentResolver mContentResolver = null;
    private String TAG = "PlayListPersister";

    public PlayListPersister(Context context) {
        mContext = context;
        mContentResolver = mContext.getContentResolver();
    }

    protected List<MusicInfo> getMusics(Cursor cursor) {
        List<MusicInfo> list = new ArrayList<MusicInfo>();
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            MusicInfo music = new MusicInfo(cursor);
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
                music.setListId(listId);
                cvs[i] = getContentValues(music);;
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

    public Long persistMusic(MusicInfo music, long listId) {
        Logger.d(TAG, "persist music=" + music + "; listId=" + listId);
        if (listId == -1) {
            return listId;
        }
        try {
            ContentValues values = getContentValues(music);
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

    public long deleteMusic(long musicId) {
        Logger.i(TAG, "removeMusic MusicId=" + musicId);
        if (musicId == -1) {
            return -1;
        }
        Uri url = MusicProvider.URI_MUSIC.buildUpon().appendPath(String.valueOf(musicId)).build();
        int ret = mContentResolver.delete(url, null, null);
        return ret;
    }

    public int deletePlayList(long listId) {
        Logger.d(TAG, "deletePlayList listId=" + listId);
        Uri url = MusicProvider.URI_LIST.buildUpon().appendPath(String.valueOf(listId)).build();
        int ret = mContentResolver.delete(url, null, null);
        return ret;
    }

    public long updateMusic(MusicInfo music) {
        int ret = -1;
        long musicId = music.getId();
        if (musicId != -1) {
            ContentValues values = getContentValues(music);
            Uri url = MusicProvider.URI_MUSIC.buildUpon().appendPath(String.valueOf(musicId)).build();
            ret = mContentResolver.update(url, values, null, null);
        }
        return ret;
    }

    private ContentValues getContentValues(MusicInfo music){
        ContentValues values = new ContentValues();
        values.put(MusicPlayerDatabase.LIST_ID, music.getListId());
        values.put(MusicPlayerDatabase.NAME, music.getName());
        values.put(MusicPlayerDatabase.ALBUM, music.getAlbum());
        values.put(MusicPlayerDatabase.ARTIST, music.getArtist());
        values.put(MusicPlayerDatabase.DURATION, music.getDuration());
        values.put(MusicPlayerDatabase.MUSIC_PATH, music.getMusicPath());
        values.put(MusicPlayerDatabase.LRC_PATH, music.getLrcPath());
        values.put(MusicPlayerDatabase.PIC_PATH, music.getPicPath());

        values.put(MusicPlayerDatabase.SONG_ID, music.getSongId());
        values.put(MusicPlayerDatabase.ALL_ARTIST_ID, music.getAllArtistId());
        values.put(MusicPlayerDatabase.ALBUM_ID, music.getAlbumId());
        values.put(MusicPlayerDatabase.LRC_LINK, music.getLrclink());
        values.put(MusicPlayerDatabase.ALL_RATE, music.getRate());
        values.put(MusicPlayerDatabase.CHARGE, music.getCharge());
        values.put(MusicPlayerDatabase.RESOURCE_TYPE, music.getResourceType());
        values.put(MusicPlayerDatabase.HAVE_HIGH, music.getHavehigh());
        values.put(MusicPlayerDatabase.COPY_TYPE, music.getCopyType());
        values.put(MusicPlayerDatabase.RELATE_STATUS, music.getRelateStatus());
        values.put(MusicPlayerDatabase.HAS_MV, music.getHasMv());
        values.put(MusicPlayerDatabase.SONG_PIC_SMALL, music.getSongPicSmall());
        values.put(MusicPlayerDatabase.SONG_PIC_BIG, music.getSongPicBig());
        values.put(MusicPlayerDatabase.SONG_PIC_RADIO, music.getSongPicRadio());
        values.put(MusicPlayerDatabase.FORMAT, music.getFormat());
        values.put(MusicPlayerDatabase.RATE, music.getRate());
        values.put(MusicPlayerDatabase.SIZE, music.getSize());
        values.put(MusicPlayerDatabase.APPENDIX, music.getAppendix());
        values.put(MusicPlayerDatabase.CONTENT, music.getContent());
        return values;
    }
}
