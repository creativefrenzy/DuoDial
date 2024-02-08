package com.privatepe.app.nGiftSec.ui;

import android.util.Log;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/* compiled from: GlobalWinNotifyManager.kt */
public final  class GlobalWinNotifyManager {

    public GlobalWinLayout f11834d;

    public GlobalWinNotifyManagerThread f11835e;

    public volatile LinkedList<Object> f11831a = new LinkedList<>();

    public ReentrantLock f11832b = new ReentrantLock();

    public Condition f11833c = this.f11832b.newCondition();

    public boolean f11836f = true;

    /* compiled from: GlobalWinNotifyManager.kt */
    /* loaded from: classes3.dex */
    public final class GlobalWinNotifyManagerThread extends Thread {
        public GlobalWinNotifyManagerThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (true) {
                GlobalWinNotifyManager jVar = GlobalWinNotifyManager.this;
                if (jVar.f11836f && !isInterrupted()) {
                    try {
                        jVar.f11832b.lock();
                        if (GlobalWinNotifyManager.this.f11831a.size() == 0) {
                            GlobalWinNotifyManager.this.f11833c.await();
                        }
                        GlobalWinLayout globalWinLayout = GlobalWinNotifyManager.this.f11834d;
                        if (globalWinLayout != null && globalWinLayout.isAniming()) {
                            GlobalWinNotifyManager.this.f11832b.unlock();
                            Thread.sleep(2000L);
                        } else {
                            Object removeFirst = GlobalWinNotifyManager.this.f11831a.removeFirst();
                            GlobalWinLayout globalWinLayout2 = GlobalWinNotifyManager.this.f11834d;
                            if (globalWinLayout2 != null) {
                                globalWinLayout2.setUp(removeFirst);
                            }
                            GlobalWinNotifyManager.this.f11832b.unlock();
                        }
                    } catch (Exception e2) {
                        Log.e("GlobalWinNotifyManager", e2 +"MyThread");
                        currentThread().interrupt();
                    }
                } else {
                    return;
                }
            }
        }
    }

    public final void a(GlobalWinLayout globalWinLayout) {
        this.f11834d = globalWinLayout;
    }

    public final void a(Object obj) {
       SingleThread singleThreadVar = SingleThread.f11843b;
        SingleThread.a().execute(new Runnable() {
            @Override
            public void run() {
                boolean isAlive = false;
                try {
                    f11832b.lock();
                    try {
                        f11831a.add(obj);
                        GlobalWinNotifyManagerThread aVar = f11835e;
                        isAlive = aVar != null && aVar.isAlive();
                    } catch (Exception e2) {
                        Log.e("GlobalWinNotifyManager", e2 +" showNotify");
                    }
                    if (f11835e != null && isAlive) {
                        f11833c.signal();
                        f11832b.unlock();
                    }
                    f11835e = new GlobalWinNotifyManagerThread();
                    GlobalWinNotifyManagerThread aVar2 = f11835e;
                    if (aVar2 != null) {
                        aVar2.start();
                    }
                    if (f11832b != null && f11832b.isLocked()) f11832b.unlock();
                } catch (IllegalMonitorStateException illegalMonitorStateException) {
                    Log.e("GlobalWinNotifyManager", illegalMonitorStateException +" showNotify");

                } catch (Throwable th) {
                    f11832b.unlock();
                    throw th;
                }
            }
        });
    }

}
