package io.entropy.babar;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class TaskListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    SimpleCursorAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText("No tasks. Weeeeee!!!");

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
    public void onListItemClick(ListView l, View v, int position, final long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you've completed this task?")
                .setTitle("Complete Task")
                .setPositiveButton("Complete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().getContentResolver().delete(
                                Uri.withAppendedPath(TaskMetaData.CONTENT_URI, String.valueOf(id)),
                                null,
                                null
                        );
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.show();
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
