package io.entropy.babar;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class TaskListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    SimpleCursorAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText("No tasks");

        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.two_line_list_item,
                null,
                new String[] {TaskMetaData.TaskTable.DESCRIPTION, TaskMetaData.TaskTable.DUE_DATE},
                new int[] {android.R.id.text1, android.R.id.text2},
                0);

        setListAdapter(mAdapter);
        setListShown(false);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("FragmentComplexList", "Item clicked: " + id);
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                TaskMetaData.CONTENT_URI,
                new String[] {TaskMetaData.TaskTable._ID, TaskMetaData.TaskTable.DESCRIPTION, TaskMetaData.TaskTable.DUE_DATE},
                null,
                null,
                null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);

        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
