
package cn.ljj.musicplayer.ui.listadapter;

import cn.ljj.musicplayer.R;
import cn.ljj.musicplayer.database.MusicPlayerDatabase;
import android.content.Context;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

public class SavedListAdapter extends AbstractListAdapter {
    public static final  String TAG = "SavedListAdapter2";

    public SavedListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int COLUMN_INDEX_NAME = cursor.getColumnIndexOrThrow(MusicPlayerDatabase.LIST_NAME);
        int COLUMN_INDEX_SIZE = cursor.getColumnIndexOrThrow(MusicPlayerDatabase.LIST_SIZE);
        int COLUMN_INDEX_ID = cursor.getColumnIndexOrThrow(MusicPlayerDatabase._ID);
        TextView name = (TextView) view.findViewById(R.id.list_name);
        String ListName = cursor.getString(COLUMN_INDEX_NAME);
        int listSize = cursor.getInt(COLUMN_INDEX_SIZE);
        name.setText(ListName + " (" + listSize + ")");
        TextView isPalying = (TextView) view.findViewById(R.id.text_isplaying);
        isPalying.setText("");
    }

    public int getAdapterType() {
        return TYPE_SAVED_LIST;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = null;
        view = mInflater.inflate(R.layout.list_item_playlist, parent, false);
        return view;
    }

    @Override
    public void proessOnItemClick(AdapterView<?> adapterView, View view, int index, long arg3) {
        
    }

    @Override
    public void processCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        
    }

}
