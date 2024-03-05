package com.privatepe.host.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ChatDB extends SQLiteOpenHelper {
    public  static final int DATABASE_VERSION = 12;
    public  static final String DATABASE_NAME = "chatRtmDb";
    private static final String TABLE_CHAT = "chat";
    private static final String KEY_SNO = "sno";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TEXT_SENT = "text_sent";
    private static final String KEY_TEXT_GET = "text_get";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_CHAT_TYPE = "chat_type";
    private static final String KEY_TIME_SENT_TIME = "time_sent";
    private static final String KEY_TIME_GET_TIME = "time_get";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_IS_READ = "is_read";

    public ChatDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
        }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {


        // crash start from here, when login is done....
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CHAT + "("
                + KEY_ID + " TEXT ,"
                + KEY_NAME + " TEXT,"
                + KEY_TEXT_SENT + " TEXT,"
                + KEY_TEXT_GET + " TEXT, "
                + KEY_DATE + " TEXT,"
                + KEY_TIME_SENT_TIME + " TEXT,"
                + KEY_TIME_GET_TIME + " TEXT,"
                + KEY_IMAGE + " TEXT, "
                + KEY_SNO + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_IS_READ + " INTEGER, "
                + KEY_TIME + " TEXT,"
                + KEY_CHAT_TYPE + " TEXT " + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);
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
        values.put(KEY_IS_READ, chat.get_isRead());
        values.put(KEY_IMAGE, chat.get_image());
        values.put(KEY_TIME, chat.get_time());
        values.put(KEY_CHAT_TYPE, chat.get_chatType());

        Log.e("ChatDB", "addChat: " + chat.get_chatType());

        // Inserting Row
        db.insert(TABLE_CHAT, null, values);
        db.close(); // Closing database connection
    }

    // code to get the single contact
    public Chat getChat(String id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CHAT, new String[]{KEY_ID, KEY_NAME, KEY_TEXT_SENT, KEY_TEXT_GET, KEY_DATE, KEY_TIME_SENT_TIME, KEY_TIME_GET_TIME, KEY_IMAGE, KEY_CHAT_TYPE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        assert cursor != null;
        Chat chat = new Chat(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8), cursor.getString(9), cursor.getString(11));
        // return contact

        String[] columnNames = cursor.getColumnNames();

        Log.e("ChatDB", "getChat: total Columns  " + columnNames.length);

        for (int i = 0; i < columnNames.length; i++) {
            Log.e("ChatDB", "getColumnName: " + columnNames[i]);
        }
        Log.e("ChatDB", "getChat: " + cursor.getString(0) + "  " + cursor.getString(1) + "  " + cursor.getString(2) + "  " + cursor.getString(3) + "  " + cursor.getString(4) + "  " + cursor.getString(5) + "  " + cursor.getString(6) + "  " + cursor.getString(7) + "  " + cursor.getString(8) + "  " + cursor.getString(9) + "  " + cursor.getString(11) + "  ");

        cursor.close();

        return chat;
    }

    public List<Chat> getAllPeer() {
        List<Chat> chatList = new ArrayList<Chat>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT id FROM " + TABLE_CHAT + " ORDER BY sno DESC ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int i = 0;

        if (cursor.moveToFirst()) {
            do {
                Log.i("ColumnCount", "" + cursor.getColumnCount());
                Chat chat = new Chat();
                chat.set_id(cursor.getString(0));

                // chat.set_isRead(cursor.get(8));
               /* chat.set_name(cursor.getString(1));
                chat.set_text_sent(cursor.getString(2));
                chat.set_text_get(cursor.getString(3));
                chat.set_date(cursor.getString(4));
                chat.set_time_sent(cursor.getString(5));
                chat.set_time_get(cursor.getString(6));
                chat.set_image(cursor.getString(7));*/
                chatList.add(chat);

                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return chat list
        return chatList;
    }



    public List<Chat> getAllPeerByLimit(int startIndex,int endIndex)
    {
        List<Chat> chatList = new ArrayList<Chat>();
        String selectQuery = "SELECT DISTINCT id FROM " + TABLE_CHAT + " order by sno DESC Limit " + startIndex + "," + endIndex;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int i = 0;

        if (cursor.moveToFirst()) {
            do {
                Log.i("ColumnCount", "" + cursor.getColumnCount());
                Chat chat = new Chat();
                chat.set_id(cursor.getString(0));

                // chat.set_isRead(cursor.get(8));
                /* chat.set_name(cursor.getString(1));
                chat.set_text_sent(cursor.getString(2));
                chat.set_text_get(cursor.getString(3));
                chat.set_date(cursor.getString(4));
                chat.set_time_sent(cursor.getString(5));
                chat.set_time_get(cursor.getString(6));
                chat.set_image(cursor.getString(7));*/
                chatList.add(chat);

                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

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
                chat.set_isRead(cursor.getInt(8));
                chat.set_time(cursor.getString(9));
                chat.set_chatType(cursor.getString(11));


                String[] columnNames = cursor.getColumnNames();
                Log.e("ChatDB", "getChat: total Columns  " + columnNames.length);
                for (int i = 0; i < columnNames.length; i++) {
                    // Log.e("ChatDB", "getColumnName: "+columnNames[i]);
                }

                Log.e("ChatDBUnread", " All chat " + cursor.getString(0) + "  " + cursor.getString(1) + "  " + cursor.getString(2) + "  " + cursor.getString(3) + "  " + cursor.getString(4) + "  " + cursor.getString(5) + "  " + cursor.getString(6) + "  " + cursor.getString(7) + "  " + cursor.getString(8) + "  " + cursor.getString(9) + "  " + cursor.getString(11) + "  ");

                //    Log.e("ChatDB", "getAllChat: "+cursor.getString(0)+"  "+cursor.getString(1)+"  "+cursor.getString(2)+"  "+cursor.getString(3)+"  "+cursor.getString(4)+"  "+cursor.getString(5)+"  "+cursor.getString(6)+"  "+cursor.getString(7)+"  "+cursor.getString(8)+"  "+cursor.getString(9)+"  "+cursor.getString(11)+"  " );
                chatList.add(chat);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return chat list
        return chatList;
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
                chat.set_isRead(cursor.getInt(8));
                chat.set_time(cursor.getString(9));
                chat.set_chatType(cursor.getString(11));
                Log.e("ChatDB22", "getLastChat: " + cursor.getString(0) + "  " + cursor.getString(1) + "  " + cursor.getString(2) + "  " + cursor.getString(3) + "  " + cursor.getString(4) + "  " + cursor.getString(5) + "  " + cursor.getString(6) + "  " + cursor.getString(7) + "  " + cursor.getString(8) + "  " + cursor.getString(9) + "  " + cursor.getString(11) + "  ");

                chatList.add(chat);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return chat list
        return chatList;
    }

    public int getChatUnreadCount(String peerId) {
        List<Chat> chatList = new ArrayList<Chat>();
        // Select Last Query
        String selectQuery = "select * from " + TABLE_CHAT + " Where " + KEY_ID + " = '" + peerId + "' and " + KEY_IS_READ + " = " + 0;

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
                chat.set_isRead(cursor.getInt(8));
                chat.set_time(cursor.getString(9));
                chat.set_time(cursor.getString(10));
                chat.set_chatType(cursor.getString(11));
                Log.e("ChatDB", "getChatUnreadCount: " + cursor.getString(0) + "  " + cursor.getString(1) + "  " + cursor.getString(2) + "  " + cursor.getString(3) + "  " + cursor.getString(4) + "  " + cursor.getString(5) + "  " + cursor.getString(6) + "  " + cursor.getString(7) + "  " + cursor.getString(8) + "  " + cursor.getString(9) + "  " + cursor.getString(10) + "  " + cursor.getString(11) + "  ");

                chatList.add(chat);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Log.i("UmreadCount", "" + chatList.size());
        // return chat list
        return chatList.size();
    }

    public int getAllChatUnreadCount(String peerId) {
        List<Chat> chatList = new ArrayList<Chat>();
        // Select Last Query
        String selectQuery = "select * from " + TABLE_CHAT + " Where " + KEY_IS_READ + " = " + 0;

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
                chat.set_isRead(cursor.getInt(8));
                chat.set_time(cursor.getString(9));
                /*   chat.set_isRead(cursor.getInt(9));
                chat.set_time(cursor.getString(10));*/
                chat.set_chatType(cursor.getString(11));
                Log.e("ChatDBUnread", "getAllChatUnreadCount: " + cursor.getString(0) + "  " + cursor.getString(1) + "  " + cursor.getString(2) + "  " + cursor.getString(3) + "  " + cursor.getString(4) + "  " + cursor.getString(5) + "  " + cursor.getString(6) + "  " + cursor.getString(7) + "  " + cursor.getString(8) + "  " + cursor.getString(9) + "  " + cursor.getString(10) + "  " + cursor.getString(11));
                chatList.add(chat);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        // return chat list
        return chatList.size();
    }

    public void updateRead(String peerId) {
        // List<Chat> chatList = new ArrayList<Chat>();
        // Select Last Query
        String updateQuery = "UPDATE " + TABLE_CHAT + " SET " + KEY_IS_READ + " = " + 1 + " WHERE " + KEY_ID + " = '" + peerId + "'";
        //String selectQuery = "select * from " + TABLE_CHAT + " Where " + KEY_ID + " = '" +peerId+"' and " + KEY_IS_READ + " = " +0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(updateQuery);

        db.close();
        Log.i("UpdateRead", "read done");
    }

    public List<Chat> getChatList(String userId, int startIndex, int endIndex) {
        List<Chat> chatList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CHAT + " WHERE " + KEY_ID + "='" + userId +
                "' order by time DESC Limit " + startIndex + "," + endIndex;

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
                    chat.set_isRead(cursor.getInt(8));
                    chat.set_time(cursor.getString(9));
                    chat.set_chatType(cursor.getString(11));
                    Log.e("ChatDB", "getChatList: " + cursor.getString(0) + "  " + cursor.getString(1) + "  " + cursor.getString(2) + "  " + cursor.getString(3) + "  " + cursor.getString(4) + "  " + cursor.getString(5) + "  " + cursor.getString(6) + "  " + cursor.getString(7) + "  " + cursor.getString(8) + "  " + cursor.getString(9) + "  " + cursor.getString(11) + "  ");
                    chatList.add(chat);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        return chatList;
    }










    public List<Chat> getAllChatByLimit(String peerId,int fromIndex,int toIndex) {
        List<Chat> chatList = new ArrayList<Chat>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CHAT + " Where " + KEY_ID + " = '" + peerId + "' order by time DESC Limit " + fromIndex + "," + toIndex;

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
                chat.set_isRead(cursor.getInt(8));
                chat.set_time(cursor.getString(9));
                chat.set_chatType(cursor.getString(11));


                String[] columnNames = cursor.getColumnNames();
                Log.e("ChatDB", "getChat: total Columns  " + columnNames.length);
                for (int i = 0; i < columnNames.length; i++) {
                    // Log.e("ChatDB", "getColumnName: "+columnNames[i]);
                }

                Log.e("ChatDBUnread", " All chat " + cursor.getString(0) + "  " + cursor.getString(1) + "  " + cursor.getString(2) + "  " + cursor.getString(3) + "  " + cursor.getString(4) + "  " + cursor.getString(5) + "  " + cursor.getString(6) + "  " + cursor.getString(7) + "  " + cursor.getString(8) + "  " + cursor.getString(9) + "  " + cursor.getString(11) + "  ");

                //    Log.e("ChatDB", "getAllChat: "+cursor.getString(0)+"  "+cursor.getString(1)+"  "+cursor.getString(2)+"  "+cursor.getString(3)+"  "+cursor.getString(4)+"  "+cursor.getString(5)+"  "+cursor.getString(6)+"  "+cursor.getString(7)+"  "+cursor.getString(8)+"  "+cursor.getString(9)+"  "+cursor.getString(11)+"  " );
                chatList.add(chat);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return chat list
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

