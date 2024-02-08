package com.privatepe.app.nGiftSec.ui;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class SingleThread {

    public static final ExecutorService f11842a = Executors.newSingleThreadExecutor();

    public static final SingleThread f11843b = null;

    public static final ExecutorService a() {
        ExecutorService executorService = f11842a;
        return executorService;
    }
}
