package io.entropy.babar;

import android.app.Application;
import android.content.Intent;

public class BabarApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, BabarService.class));
    }
}
