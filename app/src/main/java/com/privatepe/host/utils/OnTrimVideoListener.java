package com.privatepe.host.utils;

import android.net.Uri;

public interface OnTrimVideoListener {

    void getResult(final Uri uri);

    void cancelAction();
}
