package com.privatepe.app.Inbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "UserManager";
    private static final String TABLE_USER = "users";
    private static final String TABLE_CONTACT = "contacts";
    private static final String TABLE_CHAT = "chats";
    private static final String KEY_ROW_ID = "id";
    private static final String KEY_ID = "user_id";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_TIME = "time";
    private static final String KEY_USERPIC = "user_photo";
    private static final String KEY_MSG_COUNT = "unread_msg_count";
    private static final String KEY_CHAT_ID = "chat_id";
    private static final String KEY_IS_READ = "is_read";
    private static final String KEY_BE_SELF = "be_self";
    private static final String KEY_PROFILE_ID = "profile_id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_GIFT_COUNT = "gift_count";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACT + "("
                + KEY_ROW_ID + " INTEGER PRIMARY KEY   AUTOINCREMENT," + KEY_ID + " INTEGER," + KEY_NAME + " TEXT,"
                + KEY_MESSAGE + " TEXT," + KEY_TIME + " TEXT," + KEY_USERPIC + " TEXT," + KEY_MSG_COUNT + " TEXT," + KEY_PROFILE_ID + " INTEGER," + KEY_TYPE + " TEXT," + KEY_GIFT_COUNT + " INTEGER DEFAULT 0" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USER + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_CHATS_TABLE = "CREATE TABLE " + TABLE_CHAT + "("
                + KEY_CHAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ID + " INTEGER,"
                + KEY_MESSAGE + " TEXT," + KEY_BE_SELF + " INTEGER DEFAULT 0," + KEY_TIME + " TEXT," + KEY_USERPIC + " TEXT," + KEY_IS_READ + " INTEGER," + KEY_TYPE + " TEXT" + ")";
        db.execSQL(CREATE_CHATS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);
        // Create tables again
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(oldVersion);
    }

    // code to add the new contact
    public String addContact(UserInfo userInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, userInfo.getUser_id());
        values.put(KEY_NAME, userInfo.getUser_name());
        values.put(KEY_MESSAGE, userInfo.getMessage());
        values.put(KEY_TIME, userInfo.getTime());
        values.put(KEY_USERPIC, userInfo.getUser_photo());
        values.put(KEY_MSG_COUNT, userInfo.getUnread_msg_count());
        values.put(KEY_PROFILE_ID, userInfo.getProfile_id());
        values.put(KEY_TYPE, userInfo.getMsg_type());
        values.put(KEY_GIFT_COUNT, userInfo.getGift_count());
        // Inserting Row
        long contactId = db.insert(TABLE_CONTACT, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
        return String.valueOf(contactId);
    }

    // code to get the single contact
    public UserInfo getContactInfo(String userId, String profileId) {
        try {
            if (userId == null || profileId == null) return null;
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(TABLE_CONTACT, new String[]{KEY_ROW_ID, KEY_ID,
                            KEY_NAME, KEY_MESSAGE, KEY_TIME, KEY_USERPIC, KEY_MSG_COUNT, KEY_PROFILE_ID, KEY_TYPE, KEY_GIFT_COUNT}, KEY_ID + "=?" + " AND " + KEY_PROFILE_ID + "=?",
                    new String[]{userId, profileId}, null, null, null, null);
            Log.e("getUserInfo", "cursor=" + cursor);
            if (cursor != null) {
                boolean isDataAvail = cursor.moveToFirst();
                Log.e("getUserInfo", "isDataAvail=" + isDataAvail);
                if (isDataAvail) {
                    UserInfo userInfo = new UserInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                            cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
                    return userInfo;
                }
            }
            db.close();
        } catch (Exception e) {
        }
        return null;
    }

    public String getContactGiftCount(String userId, String profileId) {
        try {
            if (userId == null || profileId == null) return null;
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(TABLE_CONTACT, new String[]{KEY_GIFT_COUNT}, KEY_ID + "=?" + " AND " + KEY_PROFILE_ID + "=?",
                    new String[]{userId, profileId}, null, null, null, null);
            Log.e("getUserInfo", "cursor=" + cursor);
            if (cursor != null) {
                boolean isDataAvail = cursor.moveToFirst();
                Log.e("getUserInfo", "isDataAvail=" + isDataAvail);
                if (isDataAvail) {
                    return cursor.getString(0);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    // code to get contacts according to user id
    public List<UserInfo> getAllContacts(String profileId, int startIndex, int endIndex) {
        List<UserInfo> contactList = new ArrayList<UserInfo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACT + " WHERE " + KEY_PROFILE_ID + "=" + profileId +
                " order by time DESC Limit " + startIndex + "," + endIndex;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserInfo userInfo = new UserInfo();
                userInfo.setId(cursor.getString(0));
                userInfo.setUser_id(cursor.getString(1));
                userInfo.setUser_name(cursor.getString(2));
                userInfo.setMessage(cursor.getString(3));
                userInfo.setTime(cursor.getString(4));
                userInfo.setUser_photo(cursor.getString(5));
                userInfo.setUnread_msg_count(cursor.getString(6));
                userInfo.setProfile_id(cursor.getString(7));
                userInfo.setMsg_type(cursor.getString(8));
                userInfo.setGift_count(cursor.getString(9));
                // Adding userInfo to list
                contactList.add(userInfo);
            } while (cursor.moveToNext());
        }
        db.close();

        // return userInfo list
        return contactList;
    }

    // code to update the single contact
    public int updateContact(UserInfo userInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, userInfo.getUser_name());
        values.put(KEY_MESSAGE, userInfo.getMessage());
        values.put(KEY_TIME, userInfo.getTime());
        values.put(KEY_USERPIC, userInfo.getUser_photo());
        values.put(KEY_MSG_COUNT, userInfo.getUnread_msg_count());
        values.put(KEY_TYPE, userInfo.getMsg_type());
        values.put(KEY_GIFT_COUNT, userInfo.getGift_count());
        // updating row
        return db.update(TABLE_CONTACT, values, KEY_ROW_ID + " = ?",
                new String[]{String.valueOf(userInfo.getId())});
    }

    // Deleting single userInfo
    public void deleteContact(UserInfo userInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACT, KEY_ROW_ID + " = ?",
                new String[]{userInfo.getId()});
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    // Getting total unreadmsg Count
    public int getTotalUnreadMsgCount(String profileId) {
        String countQuery = "SELECT  SUM(unread_msg_count) FROM " + TABLE_CONTACT + " WHERE " + KEY_PROFILE_ID + "=" + profileId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int totalUnreadCount = cursor.getInt(0);
        cursor.close();
        // return count
        return totalUnreadCount;
    }

    // code to add the new user
    public void addUser(UserInfo userInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, userInfo.getUser_id());
        values.put(KEY_NAME, userInfo.getUser_name());
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single contact
    public UserInfo getUsertInfo(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[]{KEY_ID,
                        KEY_NAME}, KEY_ID + "=?",
                new String[]{id}, null, null, null, null);
        Log.e("getUserInfo", "cursor=" + cursor);
        if (cursor != null) {
            boolean isDataAvail = cursor.moveToFirst();
            Log.e("getUserInfo", "isDataAvail=" + isDataAvail);
            if (isDataAvail) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUser_id(cursor.getString(0));
                userInfo.setUser_name(cursor.getString(1));
                return userInfo;
            }
        }
        return null;
    }

    // code to get all users in a list view
    public List<UserInfo> getAllUsers() {
        List<UserInfo> userList = new ArrayList<UserInfo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserInfo userInfo = new UserInfo();
                userInfo.setUser_id(cursor.getString(0));
                userInfo.setUser_name(cursor.getString(1));
                // Adding userInfo to list
                userList.add(userInfo);
            } while (cursor.moveToNext());
        }

        // return userInfo list
        return userList;
    }

    // code to update the single contact
    public int updateUser(UserInfo userInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, userInfo.getUser_name());

        // updating row
        return db.update(TABLE_USER, values, KEY_ID + " = ?",
                new String[]{userInfo.getUser_id()});
    }

    // Deleting single userInfo
    public void deleteUser(UserInfo userInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, KEY_ID + " = ?",
                new String[]{userInfo.getUser_id()});
        db.close();
    }

    // code to add the new contact
    public void addChat(MessageBean messageBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean isBeSelf = messageBean.isBeSelf();
        int isSelf = isBeSelf ? 1 : 0;
        ContentValues values = new ContentValues();
        values.put(KEY_ID, messageBean.getAccount());
        values.put(KEY_MESSAGE, messageBean.getMessage().getMessage());
        Log.e("sdgadaf",new Gson().toJson(messageBean));
        values.put(KEY_BE_SELF, isSelf);
        values.put(KEY_TIME, messageBean.getTimestamp());
        values.put(KEY_USERPIC, messageBean.getMessage().getFromImage());
        values.put(KEY_IS_READ, 0);
        values.put(KEY_TYPE, messageBean.getMessage().getType());
        // Inserting Row
        db.insert(TABLE_CHAT, null, values);
        //2nd argument is String containing nullColumnHack
        db.close();// Closing database connection
        //Log.e("LocalDb", db.close());
        //Log.e("localdb", String.valueOf(db.insert(TABLE_CHAT, null, values)));
    }

    // code to get chat according to user
    public List<MessageBean> getChatList(String userId, int startIndex, int endIndex) {
        List<MessageBean> chatList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

       /* Cursor cursor = db.query(TABLE_CHAT, new String[]{KEY_CHAT_ID, KEY_ID,
                        KEY_MESSAGE, KEY_BE_SELF, KEY_TIME, KEY_USERPIC, KEY_IS_READ}, KEY_ID + "=?",
                new String[]{userId}, null, null, null, null);
*/
        String selectQuery = "SELECT  * FROM " + TABLE_CHAT + " WHERE " + KEY_ID + "=" + userId +
                " order by time DESC Limit " + startIndex + "," + endIndex;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    boolean isSelf = (cursor.getInt(3) == 1);

                    MessageBean chatModel = new MessageBean();
                    Messages messages = new Messages();
                    chatModel.setAccount(cursor.getString(1));
                    messages.setMessage(cursor.getString(2));
                    messages.setFromImage(cursor.getString(5));
                    messages.setType(cursor.getString(7));
                    chatModel.setMessage(messages);
                    chatModel.setBeSelf(isSelf);
                    chatModel.setTimestamp(cursor.getString(4));
                    //chatModel.setBackground(Integer.parseInt(cursor.getString(5)));
                    // Adding userInfo to list

                    Log.e("chatdbbbb", "getChatList: " + new Gson().toJson(chatModel));
                    chatList.add(chatModel);
                } while (cursor.moveToNext());
            }
        }
        return chatList;
    }

    // code to get chat according to user
    public List<MessageBean> getChatAllList(String userId) {
        List<MessageBean> chatList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CHAT + " WHERE " + KEY_ID + "=" + userId +
                " order by time DESC ";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    boolean isSelf = (cursor.getInt(3) == 1);

                    MessageBean chatModel = new MessageBean();
                    Messages messages = new Messages();
                    chatModel.setAccount(cursor.getString(1));
                    messages.setMessage(cursor.getString(2));
                    messages.setFromImage(cursor.getString(5));
                    messages.setType(cursor.getString(7));
                    chatModel.setMessage(messages);
                    chatModel.setBeSelf(isSelf);
                    chatModel.setTimestamp(cursor.getString(4));
                    //chatModel.setBackground(Integer.parseInt(cursor.getString(5)));
                    // Adding userInfo to list

                    Log.e("chatdbbbb", "getChatList: " + new Gson().toJson(chatModel));
                    chatList.add(chatModel);
                } while (cursor.moveToNext());
            }
        }
        db.close();
        return chatList;
    }


    public List<MessageBean> getChatListWithLimit(String userId,int from,int to) {
        List<MessageBean> chatList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CHAT + " WHERE " + KEY_ID + "=" + userId +
                " order by time DESC limit "+ from +","+ to;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    boolean isSelf = (cursor.getInt(3) == 1);

                    MessageBean chatModel = new MessageBean();
                    Messages messages = new Messages();
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
        db.close();
        return chatList;
    }

}