package id.cuxxie.bakingapp.Data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.lang.ref.WeakReference;

/**
 * Copied from answer by Necronet in Stackoverflow
 * question page: https://stackoverflow.com/questions/11961857/implementing-asyncqueryhandler
 */
public class CustomAsyncQueryHandler extends AsyncQueryHandler {

    private WeakReference<AsyncQueryListener> mListener;

    public interface AsyncQueryListener {
        void onQueryComplete(int token, Object cookie, Cursor cursor);
        void onInsertOrDeleteComplete(boolean isInsert);
    }

    public CustomAsyncQueryHandler(ContentResolver cr, AsyncQueryListener listener) {
        super(cr);
        mListener = new WeakReference<AsyncQueryListener>(listener);
    }

    public CustomAsyncQueryHandler(ContentResolver cr) {
        super(cr);
    }

    /**
     * Assign the given {@link AsyncQueryListener} to receive query events from
     * asynchronous calls. Will replace any existing listener.
     */
    public void setQueryListener(AsyncQueryListener listener) {
        mListener = new WeakReference<AsyncQueryListener>(listener);
    }

    /** {@inheritDoc} */
    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        final AsyncQueryListener listener = mListener.get();
        if (listener != null) {
            listener.onQueryComplete(token, cookie, cursor);
        } else if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        final AsyncQueryListener listener = mListener.get();
        if(listener != null){
            listener.onInsertOrDeleteComplete(true);
        }
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        final AsyncQueryListener listener = mListener.get();
        if(listener != null){
            listener.onInsertOrDeleteComplete(false);
        }
    }
}