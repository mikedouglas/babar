package io.entropy.babar;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BabarService extends Service {
    public static final String TAG = BabarService.class.getName();

    private List<Elephant> elephants = new ArrayList<Elephant>();

    private int _xLast, _yLast;
    private int _xOrig, _yOrig;

    private int bindCount = 0;

    @Override
    public IBinder onBind(Intent intent) {
        bindCount++;
        for (Elephant e : elephants) {
            e.hide();
        }
        return null;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);

        bindCount++;
        for (Elephant e : elephants) {
            e.hide();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        bindCount--;
        if (bindCount == 0) {
            for (Elephant e : elephants) {
                e.show();
            }
        }
        return true;
    }

    private Cursor mCursor;

    private ContentObserver dbObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            cleanUpAndCreateElephants();
        }
    };

    public void cleanUpAndCreateElephants() {
        if (mCursor != null) {
            mCursor.close();
        }

        final WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        for (Elephant el : elephants) {
            el.stop();
            wm.removeView(el);
        }
        elephants.clear();

        mCursor = getContentResolver().query(TaskMetaData.CONTENT_URI,
                        new String[] {TaskMetaData.TaskTable._ID, TaskMetaData.TaskTable.DESCRIPTION, TaskMetaData.TaskTable.DUE_DATE},
                        null,
                        null,
                        null);
        mCursor.registerContentObserver(dbObserver);

        mCursor.moveToFirst();
        while (!mCursor.isAfterLast()) {
            Log.i(TAG, "CURSOR: " + Arrays.toString(mCursor.getColumnNames()));
            elephants.add(new Elephant(this, mCursor.getString(1), mCursor.getString(2)));
            mCursor.moveToNext();
        }

        /*
        elephants.add(new Elephant(this, "Do your taxes"));
        elephants.add(new Elephant(this, "Clean the garage"));
        */

        for (Elephant elephant : elephants) {
            wm.addView(elephant, elephant.createLayoutParams());
            elephant.start();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "BabarService started.");

        cleanUpAndCreateElephants();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "BabarService finished.");
    }
}
