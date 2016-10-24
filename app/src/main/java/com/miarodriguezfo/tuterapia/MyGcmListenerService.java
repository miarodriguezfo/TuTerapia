package com.miarodriguezfo.tuterapia;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Miguel Rodriguez on 23/10/2016.
 */
public class MyGcmListenerService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
