package com.privatepe.host.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SystemDB extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "chatSystemDb";
    private static final String TABLE_CHAT = "systemChat";
    private static final String KEY_SNO = "sno";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TEXT_SENT = "text_sent";
    private static final String KEY_TEXT_GET = "text_get";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME_SENT_TIME = "time_sent";
    private static final String KEY_TIME_GET_TIME = "time_get";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_TIME = "time";
    private static final String KEY_IS_READ = "is_read";
    private static final String TOTAL_UNREAD_COUNT = "total_unread_count";
    private static final String TABLE_UNREAD_COUNT = "table_unread_count";
    private static final String UNREAD_KEY_SNO = "UNREAD";


    //  private static final String


    public SystemDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CHAT + "(" + KEY_ID + " TEXT ," + KEY_NAME + " TEXT," + KEY_TEXT_SENT + " TEXT," + KEY_TEXT_GET + " TEXT, " + KEY_DATE + " TEXT," + KEY_TIME_SENT_TIME + " TEXT," + KEY_TIME_GET_TIME + " TEXT," + KEY_IMAGE + " TEXT, " + KEY_SNO + " INTEGER PRIMARY KEY AUTOINCREMENT " + ")";
        String CREATE_UNREAD_COUNT_TABLE = "CREATE TABLE " + TABLE_UNREAD_COUNT + "(" + KEY_ID + " TEXT, " + TOTAL_UNREAD_COUNT + " INTEGER, " + UNREAD_KEY_SNO + " INTEGER PRIMARY KEY AUTOINCREMENT " + ")";

        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_UNREAD_COUNT_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNREAD_COUNT);

        // Create tables again
        onCreate(db);
    }

    public void addChat(Chat chat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, chat.get_id());
        values.put(KEY_NAME, chat.get_name());
        values.put(KEY_TEXT_SENT, chat.get_text_sent());
        values.put(KEY_TEXT_GET, chat.get_text_get());
        values.put(KEY_DATE, chat.get_date());
        values.put(KEY_TIME_SENT_TIME, chat.get_time_sent());
        values.put(KEY_TIME_GET_TIME, chat.get_time_get());
        values.put(KEY_IMAGE, chat.get_image());

        // Inserting Row
        db.insert(TABLE_CHAT, null, values);
        db.close(); // Closing database connection
    }

    // code to get the single contact
    public Chat getChat(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CHAT, new String[]{KEY_ID, KEY_NAME, KEY_TEXT_SENT, KEY_TEXT_GET, KEY_DATE, KEY_TIME_SENT_TIME, KEY_TIME_GET_TIME, KEY_IMAGE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        assert cursor != null;
        Chat chat = new Chat(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8), cursor.getString(9), cursor.getString(10));
        // return contact
        return chat;
    }

    public List<Chat> getAllPeer() {
        List<Chat> chatList = new ArrayList<Chat>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT id FROM " + TABLE_CHAT + " ORDER BY sno DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Chat chat = new Chat();
                chat.set_id(cursor.getString(0));
               /* chat.set_name(cursor.getString(1));
                chat.set_text_sent(cursor.getString(2));
                chat.set_text_get(cursor.getString(3));
                chat.set_date(cursor.getString(4));
                chat.set_time_sent(cursor.getString(5));
                chat.set_time_get(cursor.getString(6));
                chat.set_image(cursor.getString(7));*/
                chatList.add(chat);
            } while (cursor.moveToNext());
        }

        // return chat list
        return chatList;
    }


    public List<Chat> getAllChat(String peerId) {
        List<Chat> chatList = new ArrayList<Chat>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CHAT + " Where " + KEY_ID + " = '" + peerId + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Chat chat = new Chat();
                chat.set_id(cursor.getString(0));
                chat.set_name(cursor.getString(1));
                chat.set_text_sent(cursor.getString(2));
                chat.set_text_get(cursor.getString(3));
                chat.set_date(cursor.getString(4));
                chat.set_time_sent(cursor.getString(5));
                chat.set_time_get(cursor.getString(6));
                chat.set_image(cursor.getString(7));
                chatList.add(chat);
            } while (cursor.moveToNext());
        }

        // return chat list
        return chatList;

    }


    public void setTotalSystemUnreadCount(int unreadCount) {
        Log.e("NewUnreadCountTable", "setTotalSystemUnreadCount: unreadCount "+unreadCount );

        if (getUnreadCountTableListSize() > 0) {
            Log.e("NewUnreadCountTable", "setTotalSystemUnreadCount: update-> " );
            String updateQuery = "UPDATE " + TABLE_UNREAD_COUNT + " SET " + TOTAL_UNREAD_COUNT + " = " + unreadCount + " WHERE " + KEY_ID + " = '" + "System" + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(updateQuery);
            db.close();
        } else {
            Log.e("NewUnreadCountTable", "setTotalSystemUnreadCount: store-> " );
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ID, "System");
            values.put(TOTAL_UNREAD_COUNT, unreadCount);
            // Inserting Row
            db.insert(TABLE_UNREAD_COUNT, null, values);
            db.close(); // Closing database connection
        }


    }


    public int getTotalSystemUnreadCount() {

        String selectQuery = "SELECT  * FROM " + TABLE_UNREAD_COUNT + " Where " + KEY_ID + " = '" + "System" + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int TotalUnreadCount = 0;

        if (cursor.moveToFirst()) {
            do {
                TotalUnreadCount = cursor.getInt(1);
            } while (cursor.moveToNext());
        }
        Log.e("NewUnreadCountTable", "getTotalSystemUnreadCount: TotalUnreadCount "+TotalUnreadCount );
        return TotalUnreadCount;

    }


    private int getUnreadCountTableListSize() {
        String selectQuery = "SELECT  * FROM " + TABLE_UNREAD_COUNT + " Where " + KEY_ID + " = '" + "System" + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int TableSize = 0;

        if (cursor.moveToFirst()) {
            do {
                TableSize++;
            } while (cursor.moveToNext());
        }
        Log.e("NewUnreadCountTable", "getUnreadCountTableListSize: TableSize "+TableSize );

        return TableSize;
    }


    public List<Chat> getLastChat(String peerId) {
        List<Chat> chatList = new ArrayList<Chat>();
        // Select Last Query
        String selectQuery = "select * from " + TABLE_CHAT + " Where " + KEY_ID + " = '" + peerId + "'  ORDER BY sno DESC LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Chat chat = new Chat();
                chat.set_id(cursor.getString(0));
                chat.set_name(cursor.getString(1));
                chat.set_text_sent(cursor.getString(2));
                chat.set_text_get(cursor.getString(3));
                chat.set_date(cursor.getString(4));
                chat.set_time_sent(cursor.getString(5));
                chat.set_time_get(cursor.getString(6));
                chat.set_image(cursor.getString(7));
                chatList.add(chat);
            } while (cursor.moveToNext());
        }

        // return chat list
        return chatList;
    }


    public List<Chat> getChatList(String userId, int startIndex, int endIndex) {
        List<Chat> chatList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CHAT + " WHERE " + KEY_ID + "=" + userId +
                " order by sno DESC Limit " + startIndex + "," + endIndex;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    Chat chat = new Chat();
                    chat.set_id(cursor.getString(0));
                    chat.set_name(cursor.getString(1));
                    chat.set_text_sent(cursor.getString(2));
                    chat.set_text_get(cursor.getString(3));
                    chat.set_date(cursor.getString(4));
                    chat.set_time_sent(cursor.getString(5));
                    chat.set_time_get(cursor.getString(6));
                    chat.set_image(cursor.getString(7));
                    chatList.add(chat);
                } while (cursor.moveToNext());
            }
        }
        return chatList;
    }


    // code to get chat according to user
   /* public List<ChatBean> getChatList(String userId, int startIndex, int endIndex) {
        List<ChatBean> chatList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CHAT + " WHERE " + KEY_ID + "=" + userId +
                " order by time DESC Limit " + startIndex + "," + endIndex;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    boolean isSelf = (cursor.getInt(3) == 1);

                    ChatBean chatModel = new ChatBean();
                    Chat messages = new Chat();
                    chatModel.setAccount(cursor.getString(1));
                    messages.setMessage(cursor.getString(2));
                    messages.setFromImage(cursor.getString(5));
                    messages.setType(cursor.getString(7));
                    chatModel.setMessage(messages);
                    chatModel.setBeSelf(isSelf);
                    chatModel.setTimestamp(cursor.getString(4));
                    chatList.add(chatModel);
                } while (cursor.moveToNext());
            }
        }
        return chatList;
    }*/

  /*  public int updateContact(Chat contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }*/

}


