package id.cuxxie.bakingapp.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import id.cuxxie.bakingapp.ContentProvider.Contract.InstructionContract;
import id.cuxxie.bakingapp.ContentProvider.Contract.RequirementContract;
import id.cuxxie.bakingapp.ContentProvider.Contract.StepContract;

/**
 * Created by hendr on 8/11/2017.
 */

public class BakingAppContentProvider  extends ContentProvider{
    public static final int ALL_INSTRUCTION = 100;
    public static final int INSTRUCTION_WITH_ID = 101;
    public static final int ALL_STEP_UNDER_INSTR = 200;
    public static final int STEP_WITH_ID = 201;
    public static final int ALL_REQ_UNDER_INSTR = 300;
    public static final int REQUIREMENT_WITH_ID = 301;
    private DBHelper dbHelper;
    private static final UriMatcher mUriMatcher = buildUriMatcher();
    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new DBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);
        Cursor returnCursor;
        switch (match) {
            case ALL_INSTRUCTION:
                returnCursor =  db.query(InstructionContract.InstructionEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ALL_STEP_UNDER_INSTR:
                if(selection == null)
                    throw new UnsupportedOperationException("WHERE Statement for instruction ID is required");
                returnCursor =  db.query(StepContract.StepEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ALL_REQ_UNDER_INSTR:
                if(selection == null)
                    throw new UnsupportedOperationException("WHERE Statement for instruction ID is required");
                returnCursor =  db.query(RequirementContract.RequirementEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case REQUIREMENT_WITH_ID:
                String id = uri.getPathSegments().get(1);
                returnCursor =  db.query(RequirementContract.RequirementEntry.TABLE_NAME, projection, "id = ?",new String[]{id}, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case ALL_INSTRUCTION:
                long id = db.insert(InstructionContract.InstructionEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(InstructionContract.InstructionEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case ALL_STEP_UNDER_INSTR:
                long stepId = db.insert(StepContract.StepEntry.TABLE_NAME,null,values);
                if(stepId > 0){
                    returnUri = ContentUris.withAppendedId(StepContract.StepEntry.CONTENT_URI, stepId);
                }
                else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case ALL_REQ_UNDER_INSTR:
                long reqId = db.insert(RequirementContract.RequirementEntry.TABLE_NAME,null,values);
                if(reqId > 0){
                    returnUri = ContentUris.withAppendedId(RequirementContract.RequirementEntry.CONTENT_URI, reqId);
                }
                else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);
        int tasksDeleted;
        switch (match) {
            case INSTRUCTION_WITH_ID:
                String id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(InstructionContract.InstructionEntry.TABLE_NAME, "id=?", new String[]{id});
                break;
            case STEP_WITH_ID:
                String stepid = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(StepContract.StepEntry.TABLE_NAME, "rowid=?", new String[]{stepid});
                break;
            case REQUIREMENT_WITH_ID:
                String reqid = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(RequirementContract.RequirementEntry.TABLE_NAME, "id=?", new String[]{reqid});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (tasksDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return tasksDeleted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);
        int taskUpdated;
        switch (match) {
            case ALL_REQ_UNDER_INSTR:
                taskUpdated = db.update(RequirementContract.RequirementEntry.TABLE_NAME,values, "id=?",selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (taskUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return taskUpdated;
    }


    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(InstructionContract.AUTHORITY, InstructionContract.PATH_INSTRUCTION, ALL_INSTRUCTION);
        uriMatcher.addURI(InstructionContract.AUTHORITY, InstructionContract.PATH_INSTRUCTION + "/#", INSTRUCTION_WITH_ID);
        uriMatcher.addURI(StepContract.AUTHORITY, StepContract.PATH_STEP, ALL_STEP_UNDER_INSTR);
        uriMatcher.addURI(StepContract.AUTHORITY,StepContract.PATH_STEP+"/#", STEP_WITH_ID);
        uriMatcher.addURI(RequirementContract.AUTHORITY, RequirementContract.PATH_REQUIREMENT, ALL_REQ_UNDER_INSTR);
        uriMatcher.addURI(RequirementContract.AUTHORITY, RequirementContract.PATH_REQUIREMENT+"/#", REQUIREMENT_WITH_ID);
        return uriMatcher;
    }
}
