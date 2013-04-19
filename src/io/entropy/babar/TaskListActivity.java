package io.entropy.babar;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class TaskListActivity extends Activity {
    public static final String TAG = TaskListActivity.class.getName();

    private ServiceConnection mServiceConnection;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // hide elephants
        bindService(new Intent(this, BabarService.class),
                mServiceConnection = new ServiceConnection() {
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        Log.i(TAG, "onServiceConnected");
                    }

                    public void onServiceDisconnected(ComponentName name) {
                        Log.i(TAG, "onServiceDisconnected");
                    }
                }, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mServiceConnection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tasks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_task_item:
            {
                CreateTaskDialog dialog = new CreateTaskDialog();
                dialog.show(getFragmentManager(), CreateTaskDialog.class.getName());
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
