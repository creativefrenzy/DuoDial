package com.privatepe.host.sqlite;

public class Chat {
    String _id;
    String _name;
    String _text_sent;
    String _text_get;
    String _date;
    String _time_sent;
    String _time_get;
    String _image;
    Integer _isRead;
    String _time;
    String _chatType;

    public Chat(String id, String name, String text_sent, String text_get, String date, String time_sent, String time_get, String image, Integer isRead, String time,String _chatType){
        this._id = id;
        this._name = name;
        this._text_sent = text_sent;
        this._text_get = text_get;
        this._date = date;
        this._time_sent = time_sent;
        this._time_get = time_get;
        this._image = image;
        this._isRead = isRead;
        this._time = time;
        this._chatType=_chatType;
    }

    public Chat() {

    }

    public String get_time() {
        return _time;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public Integer get_isRead() {
        return _isRead;
    }

    public void set_isRead(Integer _isRead) {
        this._isRead = _isRead;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_text_sent(String _text_sent) {
        this._text_sent = _text_sent;
    }

    public void set_text_get(String _text_get) {
        this._text_get = _text_get;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public void set_time_sent(String _time_sent) {
        this._time_sent = _time_sent;
    }

    public void set_time_get(String _time_get) {
        this._time_get = _time_get;
    }

    public void set_image(String _image) {
        this._image = _image;
    }

    public String get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public String get_text_sent() {
        return _text_sent;
    }

    public String get_text_get() {
        return _text_get;
    }

    public String get_date() {
        return _date;
    }

    public String get_time_sent() {
        return _time_sent;
    }

    public String get_time_get() {
        return _time_get;
    }

    public String get_image() {
        return _image;
    }

    public String get_chatType() {
        return _chatType;
    }
    public void set_chatType(String _chatType) {
        this._chatType = _chatType;
    }
}