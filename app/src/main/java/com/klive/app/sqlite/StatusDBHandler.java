package com.klive.app.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.klive.app.status_videos.model.VideoLinkModel;


import java.util.ArrayList;


public class StatusDBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "status_db";
    private static final int DB_VERSION = 1;

    private static final String VIDEOLINKS_TABLE = "video_links_table";

    private static final String KEY_ID = "key_userid";
    private static final String KEY_VIDEO_ROW_ID = "key_video_row_id";
    private static final String KEY_VIDEO_URL = "key_video_url";
    private static final String KEY_VIDEO_URISTRING = "key_video_uri_string";
    private static final String KEY_VIDEO_TIMESTAMP = "key_video_timestamp";


    private String TAG = "StatusDBHandler";


    Context context;


    public StatusDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_VIDEOLINKS_TABLE = "CREATE TABLE " + VIDEOLINKS_TABLE + "(" + KEY_VIDEO_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ID + " TEXT," + KEY_VIDEO_URL + " TEXT," + KEY_VIDEO_URISTRING + " TEXT," + KEY_VIDEO_TIMESTAMP + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_VIDEOLINKS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + VIDEOLINKS_TABLE);
        onCreate(sqLiteDatabase);

    }


    public void addVideos(String userId, String videoLink, String videoUriString, String timestamp) {

        Log.e(TAG, "addVideos userId==>" + userId +"===="+videoLink);

        Log.e(TAG, "addVideos: start");
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(TAG, "addVideos: current version " + db.getVersion());

//        addVideos userId==>461089605====https://ringlive2022.oss-ap-south-1.aliyuncs.com/ringliveVideos/1665395218.mp4
        ContentValues values = new ContentValues();
        values.put(KEY_ID, userId);
        values.put(KEY_VIDEO_URL, videoLink);
        values.put(KEY_VIDEO_URISTRING, videoUriString);
        values.put(KEY_VIDEO_TIMESTAMP, timestamp);
        db.insert(VIDEOLINKS_TABLE, null, values);

        Log.e(TAG, "addVideos: end");

    }


    public ArrayList<String> getVideoLinksList(String userId) {

        ArrayList<String> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + VIDEOLINKS_TABLE + " WHERE " + KEY_ID + "=" + userId + " order by key_video_timestamp DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(2));
                    Log.e(TAG, "getVideoLinksList: "+ cursor.getString(2));
                    //  Log.e("VideoStatusDB", "getVideoLinksList: "+cursor.getString(0)+" "+cursor.getString(1));

                } while (cursor.moveToNext());
            }
        }
        db.close();
        cursor.close();
        return list;
    }


    public ArrayList<VideoLinkModel> getVideosRowList(String userId) {

        ArrayList<VideoLinkModel> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + VIDEOLINKS_TABLE + " WHERE " + KEY_ID + "=" + userId + " order by key_video_timestamp DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    list.add(new VideoLinkModel(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
                    //  Log.e("VideoStatusDB", "getVideoLinksList: " + cursor.getString(2));
                    //  Log.e("VideoStatusDB", "getVideoLinksList: "+cursor.getString(0)+" "+cursor.getString(1));
                } while (cursor.moveToNext());
            }
        }
        db.close();
        cursor.close();
        return list;
    }

    public void removeVideoRow(String rowId) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(VIDEOLINKS_TABLE, KEY_VIDEO_ROW_ID + " = ?",
                new String[]{rowId});
        db.close();

    }

    public String getRowId(String userId, String video_url) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(VIDEOLINKS_TABLE, new String[]{KEY_VIDEO_ROW_ID, KEY_ID, KEY_VIDEO_URL, KEY_VIDEO_URISTRING, KEY_VIDEO_TIMESTAMP}, KEY_ID + "=?" + " AND " + KEY_VIDEO_URL + "=?", new String[]{userId, video_url}, null, null, null, null);

        if (cursor.moveToFirst()) {
            String rowId=cursor.getString(0);
            return rowId;
        }

        return null;
    }



}



