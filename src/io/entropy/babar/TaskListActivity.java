package io.entropy.babar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class TaskListActivity extends Activity {
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        // show elephants
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
