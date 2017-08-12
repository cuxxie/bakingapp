package id.cuxxie.bakingapp;

/**
 * Created by hendr on 8/12/2017.
 */


import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import id.cuxxie.bakingapp.ContentProvider.BakingAppContentProvider;
import id.cuxxie.bakingapp.ContentProvider.Contract.InstructionContract;
import id.cuxxie.bakingapp.ContentProvider.Contract.RequirementContract;
import id.cuxxie.bakingapp.ContentProvider.Contract.StepContract;
import id.cuxxie.bakingapp.ContentProvider.DBHelper;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created by hendri on 7/10/17.
 */

@RunWith(AndroidJUnit4.class)
public class ContentProviderTest {
    private final Context mContext = InstrumentationRegistry.getTargetContext();
    @Before
    public void setUp() {
        /* Use TaskDbHelper to get access to a writable database */
        DBHelper dbHelper = new DBHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(InstructionContract.InstructionEntry.TABLE_NAME, null, null);
        database.delete(RequirementContract.RequirementEntry.TABLE_NAME, null, null);
        database.delete(StepContract.StepEntry.TABLE_NAME, null, null);
    }


    @Test
    public void testProviderRegistry() {

        String packageName = mContext.getPackageName();
        String taskProviderClassName = BakingAppContentProvider.class.getName();
        ComponentName componentName = new ComponentName(packageName, taskProviderClassName);

        try {
            PackageManager pm = mContext.getPackageManager();
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            String actualAuthority = providerInfo.authority;
            String expectedAuthority = packageName;

            String incorrectAuthority =
                    "Error: TaskContentProvider registered with authority: " + actualAuthority +
                            " instead of expected authority: " + expectedAuthority;
            assertEquals(incorrectAuthority,
                    actualAuthority,
                    expectedAuthority);

        } catch (PackageManager.NameNotFoundException e) {
            String providerNotRegisteredAtAll =
                    "Error: TaskContentProvider not registered at " + mContext.getPackageName();
            fail(providerNotRegisteredAtAll);
        }
    }


    private static final Uri TEST_TASKS = InstructionContract.InstructionEntry.CONTENT_URI;
    // Content URI for a single task with id = 1
    private static final Uri TEST_TASK_WITH_ID = TEST_TASKS.buildUpon().appendPath("1").build();

    @Test
    public void testUriMatcher() {

        /* Create a URI matcher that the TaskContentProvider uses */
        UriMatcher testMatcher = BakingAppContentProvider.buildUriMatcher();

        /* Test that the code returned from our matcher matches the expected TASKS int */
        String tasksUriDoesNotMatch = "Error: The TASKS URI was matched incorrectly.";
        int actualTasksMatchCode = testMatcher.match(TEST_TASKS);
        int expectedTasksMatchCode = BakingAppContentProvider.ALL_INSTRUCTION;
        assertEquals(tasksUriDoesNotMatch,
                actualTasksMatchCode,
                expectedTasksMatchCode);

        /* Test that the code returned from our matcher matches the expected TASK_WITH_ID */
        String taskWithIdDoesNotMatch =
                "Error: The TASK_WITH_ID URI was matched incorrectly.";
        int actualTaskWithIdCode = testMatcher.match(TEST_TASK_WITH_ID);
        int expectedTaskWithIdCode = BakingAppContentProvider.INSTRUCTION_WITH_ID;
        assertEquals(taskWithIdDoesNotMatch,
                actualTaskWithIdCode,
                expectedTaskWithIdCode);
    }


    @Test
    public void testInsertInstruction() {

        /* Create values to insert */
        ContentValues testTaskValues = new ContentValues();
        testTaskValues.put(InstructionContract.InstructionEntry.COLUMN_ID, 1);
        testTaskValues.put(InstructionContract.InstructionEntry.COLUMN_NAME, "Test cuy");
        testTaskValues.put(InstructionContract.InstructionEntry.COLUMN_IMAGE,"Movie PAK");
        testTaskValues.put(InstructionContract.InstructionEntry.COLUMN_SERVINGS,6);

        /* TestContentObserver allows us to test if notifyChange was called appropriately */
        TestUtilities.TestContentObserver taskObserver = TestUtilities.getTestContentObserver();

        ContentResolver contentResolver = mContext.getContentResolver();

        /* Register a content observer to be notified of changes to data at a given URI (tasks) */
        contentResolver.registerContentObserver(
                /* URI that we would like to observe changes to */
                InstructionContract.InstructionEntry.CONTENT_URI,
                /* Whether or not to notify us if descendants of this URI change */
                true,
                /* The observer to register (that will receive notifyChange callbacks) */
                taskObserver);


        Uri uri = contentResolver.insert(InstructionContract.InstructionEntry.CONTENT_URI, testTaskValues);


        Uri expectedUri = ContentUris.withAppendedId(InstructionContract.InstructionEntry.CONTENT_URI, 1);

        String insertProviderFailed = "Unable to insert item through Provider";
        assertEquals(insertProviderFailed, uri, expectedUri);

        /*
         * If this fails, it's likely you didn't call notifyChange in your insert method from
         * your ContentProvider.
         */
        taskObserver.waitForNotificationOrFail();

        /*
         * waitForNotificationOrFail is synchronous, so after that call, we are done observing
         * changes to content and should therefore unregister this observer.
         */
        contentResolver.unregisterContentObserver(taskObserver);
    }


    @Test
    public void testQueryInstruction() {

        /* Get access to a writable database */
        DBHelper dbHelper = new DBHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        /* Create values to insert */
        ContentValues testTaskValues = new ContentValues();
        testTaskValues.put(InstructionContract.InstructionEntry.COLUMN_ID, 1);
        testTaskValues.put(InstructionContract.InstructionEntry.COLUMN_NAME, "Test cuy");
        testTaskValues.put(InstructionContract.InstructionEntry.COLUMN_IMAGE,"Movie PAK");
        testTaskValues.put(InstructionContract.InstructionEntry.COLUMN_SERVINGS,6);

        /* Insert ContentValues into database and get a row ID back */
        long taskRowId = database.insert(
                /* Table to insert values into */
                InstructionContract.InstructionEntry.TABLE_NAME,
                null,
                /* Values to insert into table */
                testTaskValues);

        String insertFailed = "Unable to insert directly into the database";
        assertTrue(insertFailed, taskRowId != -1);

        /* We are done with the database, close it now. */
        database.close();

        /* Perform the ContentProvider query */
        Cursor taskCursor = mContext.getContentResolver().query(
                InstructionContract.InstructionEntry.CONTENT_URI,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Sort order to return in Cursor */
                null);


        String queryFailed = "Query failed to return a valid Cursor";
        assertTrue(queryFailed, taskCursor != null);

        /* We are done with the cursor, close it now. */
        taskCursor.close();
    }

    @Test
    public void testDeleteInstruction() {
        /* Access writable database */
        DBHelper helper = new DBHelper(InstrumentationRegistry.getTargetContext());
        SQLiteDatabase database = helper.getWritableDatabase();

        /* Create values to insert */
        ContentValues testTaskValues = new ContentValues();
        testTaskValues.put(InstructionContract.InstructionEntry.COLUMN_ID, 1);
        testTaskValues.put(InstructionContract.InstructionEntry.COLUMN_NAME, "Test cuy");
        testTaskValues.put(InstructionContract.InstructionEntry.COLUMN_IMAGE,"Movie PAK");
        testTaskValues.put(InstructionContract.InstructionEntry.COLUMN_SERVINGS,6);

        /* Insert ContentValues into database and get a row ID back */
        long taskRowId = database.insert(
                /* Table to insert values into */
                InstructionContract.InstructionEntry.TABLE_NAME,
                null,
                /* Values to insert into table */
                testTaskValues);

        /* Always close the database when you're through with it */
        database.close();

        String insertFailed = "Unable to insert into the database";
        assertTrue(insertFailed, taskRowId != -1);


        /* TestContentObserver allows us to test if notifyChange was called appropriately */
        TestUtilities.TestContentObserver taskObserver = TestUtilities.getTestContentObserver();

        ContentResolver contentResolver = mContext.getContentResolver();

        /* Register a content observer to be notified of changes to data at a given URI (tasks) */
        contentResolver.registerContentObserver(
                /* URI that we would like to observe changes to */
                InstructionContract.InstructionEntry.CONTENT_URI,
                /* Whether or not to notify us if descendants of this URI change */
                true,
                /* The observer to register (that will receive notifyChange callbacks) */
                taskObserver);

        /* The delete method deletes the previously inserted row with id = 1 */
        Uri uriToDelete = InstructionContract.InstructionEntry.CONTENT_URI.buildUpon().appendPath("1").build();
        int tasksDeleted = contentResolver.delete(uriToDelete, null, null);

        String deleteFailed = "Unable to delete item in the database";
        assertTrue(deleteFailed, tasksDeleted != 0);

        taskObserver.waitForNotificationOrFail();

        contentResolver.unregisterContentObserver(taskObserver);
    }


    @Test
    public void testInsertStep() {

        /* Create values to insert */
        ContentValues testTaskValues = new ContentValues();
        testTaskValues.put(StepContract.StepEntry.COLUMN_ID, 1);
        testTaskValues.put(StepContract.StepEntry.COLUMN_INSTRUCTION_ID,1);
        testTaskValues.put(StepContract.StepEntry.COLUMN_SHORT_DESC, "Test cuy");
        testTaskValues.put(StepContract.StepEntry.COLUMN_DESC,"Movie PAK");

        /* TestContentObserver allows us to test if notifyChange was called appropriately */
        TestUtilities.TestContentObserver taskObserver = TestUtilities.getTestContentObserver();

        ContentResolver contentResolver = mContext.getContentResolver();

        /* Register a content observer to be notified of changes to data at a given URI (tasks) */
        contentResolver.registerContentObserver(
                /* URI that we would like to observe changes to */
                StepContract.StepEntry.CONTENT_URI,
                /* Whether or not to notify us if descendants of this URI change */
                true,
                /* The observer to register (that will receive notifyChange callbacks) */
                taskObserver);


        Uri uri = contentResolver.insert(StepContract.StepEntry.CONTENT_URI, testTaskValues);


        Uri expectedUri = ContentUris.withAppendedId(StepContract.StepEntry.CONTENT_URI, 1);

        String insertProviderFailed = "Unable to insert item through Provider";
//        assertEquals(insertProviderFailed, uri, expectedUri);
        String ids = uri.getPathSegments().get(1);
        assertNotNull(ids);
        assertNotSame(ids,"");
        /*
         * If this fails, it's likely you didn't call notifyChange in your insert method from
         * your ContentProvider.
         */
        taskObserver.waitForNotificationOrFail();

        /*
         * waitForNotificationOrFail is synchronous, so after that call, we are done observing
         * changes to content and should therefore unregister this observer.
         */
        contentResolver.unregisterContentObserver(taskObserver);
    }


    @Test
    public void testQueryStep() {

        /* Get access to a writable database */
        DBHelper dbHelper = new DBHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        /* Create values to insert */
        ContentValues testTaskValues = new ContentValues();
        testTaskValues.put(StepContract.StepEntry.COLUMN_ID, 1);
        testTaskValues.put(StepContract.StepEntry.COLUMN_INSTRUCTION_ID,1);
        testTaskValues.put(StepContract.StepEntry.COLUMN_SHORT_DESC, "Test cuy");
        testTaskValues.put(StepContract.StepEntry.COLUMN_DESC,"Movie PAK");

        /* Insert ContentValues into database and get a row ID back */
        long taskRowId = database.insert(
                /* Table to insert values into */
                StepContract.StepEntry.TABLE_NAME,
                null,
                /* Values to insert into table */
                testTaskValues);

        String insertFailed = "Unable to insert directly into the database";
        assertTrue(insertFailed, taskRowId != -1);

        /* We are done with the database, close it now. */
        database.close();

        /* Perform the ContentProvider query */
        Cursor taskCursor = mContext.getContentResolver().query(
                StepContract.StepEntry.CONTENT_URI,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                "rowid = ?",
                /* Values for "where" clause */
                new String[]{String.valueOf(taskRowId)},
                /* Sort order to return in Cursor */
                null);


        String queryFailed = "Query failed to return a valid Cursor";
        assertTrue(queryFailed, taskCursor != null);

        /* We are done with the cursor, close it now. */
        taskCursor.close();
    }

    @Test
    public void testDeleteStep() {
        /* Access writable database */
        DBHelper helper = new DBHelper(InstrumentationRegistry.getTargetContext());
        SQLiteDatabase database = helper.getWritableDatabase();

        /* Create values to insert */
        ContentValues testTaskValues = new ContentValues();

        testTaskValues.put(StepContract.StepEntry.COLUMN_ID, 1);
        testTaskValues.put(StepContract.StepEntry.COLUMN_INSTRUCTION_ID,1);
        testTaskValues.put(StepContract.StepEntry.COLUMN_SHORT_DESC, "Test cuy");
        testTaskValues.put(StepContract.StepEntry.COLUMN_DESC,"Movie PAK");

        /* Insert ContentValues into database and get a row ID back */
        long taskRowId = database.insert(
                /* Table to insert values into */
                StepContract.StepEntry.TABLE_NAME,
                null,
                /* Values to insert into table */
                testTaskValues);

        /* Always close the database when you're through with it */
        database.close();

        String insertFailed = "Unable to insert into the database";
        assertTrue(insertFailed, taskRowId != -1);


        /* TestContentObserver allows us to test if notifyChange was called appropriately */
        TestUtilities.TestContentObserver taskObserver = TestUtilities.getTestContentObserver();

        ContentResolver contentResolver = mContext.getContentResolver();

        /* Register a content observer to be notified of changes to data at a given URI (tasks) */
        contentResolver.registerContentObserver(
                /* URI that we would like to observe changes to */
                StepContract.StepEntry.CONTENT_URI,
                /* Whether or not to notify us if descendants of this URI change */
                true,
                /* The observer to register (that will receive notifyChange callbacks) */
                taskObserver);

        /* The delete method deletes the previously inserted row with id = 1 */
        Uri uriToDelete = StepContract.StepEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(taskRowId)).build();
        int tasksDeleted = contentResolver.delete(uriToDelete, null, null);

        String deleteFailed = "Unable to delete item in the database";
        assertTrue(deleteFailed, tasksDeleted != 0);

        taskObserver.waitForNotificationOrFail();

        contentResolver.unregisterContentObserver(taskObserver);
    }


    @Test
    public void testInsertRequirement() {

        /* Create values to insert */
        ContentValues testTaskValues = new ContentValues();
        testTaskValues.put(RequirementContract.RequirementEntry.COLUMN_NAME, "Test cuy");

        /* TestContentObserver allows us to test if notifyChange was called appropriately */
        TestUtilities.TestContentObserver taskObserver = TestUtilities.getTestContentObserver();

        ContentResolver contentResolver = mContext.getContentResolver();

        /* Register a content observer to be notified of changes to data at a given URI (tasks) */
        contentResolver.registerContentObserver(
                /* URI that we would like to observe changes to */
                RequirementContract.RequirementEntry.CONTENT_URI,
                /* Whether or not to notify us if descendants of this URI change */
                true,
                /* The observer to register (that will receive notifyChange callbacks) */
                taskObserver);


        Uri uri = contentResolver.insert(RequirementContract.RequirementEntry.CONTENT_URI, testTaskValues);


        Uri expectedUri = ContentUris.withAppendedId(RequirementContract.RequirementEntry.CONTENT_URI, 1);

        String insertProviderFailed = "Unable to insert item through Provider";
        String ids = uri.getPathSegments().get(1);
        assertNotNull(ids);
        assertNotSame(ids,"");

        /*
         * If this fails, it's likely you didn't call notifyChange in your insert method from
         * your ContentProvider.
         */
        taskObserver.waitForNotificationOrFail();

        /*
         * waitForNotificationOrFail is synchronous, so after that call, we are done observing
         * changes to content and should therefore unregister this observer.
         */
        contentResolver.unregisterContentObserver(taskObserver);
    }


    @Test
    public void testQueryRequirement() {

        /* Get access to a writable database */
        DBHelper dbHelper = new DBHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        /* Create values to insert */
        ContentValues testTaskValues = new ContentValues();
        testTaskValues.put(RequirementContract.RequirementEntry.COLUMN_INSTRUCTION_ID,1);
        testTaskValues.put(RequirementContract.RequirementEntry.COLUMN_NAME, "Test cuy");

        /* Insert ContentValues into database and get a row ID back */
        long taskRowId = database.insert(
                /* Table to insert values into */
                RequirementContract.RequirementEntry.TABLE_NAME,
                null,
                /* Values to insert into table */
                testTaskValues);

        String insertFailed = "Unable to insert directly into the database";
        assertTrue(insertFailed, taskRowId != -1);

        /* We are done with the database, close it now. */
        database.close();

        /* Perform the ContentProvider query */
        Cursor taskCursor = mContext.getContentResolver().query(
                RequirementContract.RequirementEntry.CONTENT_URI,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                "instructionid = ?",
                /* Values for "where" clause */
                new String[]{"1"},
                /* Sort order to return in Cursor */
                null);


        String queryFailed = "Query failed to return a valid Cursor";
        assertTrue(queryFailed, taskCursor != null);

        /* We are done with the cursor, close it now. */
        taskCursor.close();
    }

    @Test
    public void testDeleteRequirement() {
        /* Access writable database */
        DBHelper helper = new DBHelper(InstrumentationRegistry.getTargetContext());
        SQLiteDatabase database = helper.getWritableDatabase();

        /* Create values to insert */
        ContentValues testTaskValues = new ContentValues();
        testTaskValues.put(RequirementContract.RequirementEntry.COLUMN_NAME, "Test cuy");

        /* Insert ContentValues into database and get a row ID back */
        long taskRowId = database.insert(
                /* Table to insert values into */
                RequirementContract.RequirementEntry.TABLE_NAME,
                null,
                /* Values to insert into table */
                testTaskValues);

        /* Always close the database when you're through with it */
        database.close();

        String insertFailed = "Unable to insert into the database";
        assertTrue(insertFailed, taskRowId != -1);


        /* TestContentObserver allows us to test if notifyChange was called appropriately */
        TestUtilities.TestContentObserver taskObserver = TestUtilities.getTestContentObserver();

        ContentResolver contentResolver = mContext.getContentResolver();

        /* Register a content observer to be notified of changes to data at a given URI (tasks) */
        contentResolver.registerContentObserver(
                /* URI that we would like to observe changes to */
                RequirementContract.RequirementEntry.CONTENT_URI,
                /* Whether or not to notify us if descendants of this URI change */
                true,
                /* The observer to register (that will receive notifyChange callbacks) */
                taskObserver);

        /* The delete method deletes the previously inserted row with id = 1 */
        Uri uriToDelete = RequirementContract.RequirementEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(taskRowId)).build();
        int tasksDeleted = contentResolver.delete(uriToDelete, null, null);

        String deleteFailed = "Unable to delete item in the database";
        assertTrue(deleteFailed, tasksDeleted != 0);

        taskObserver.waitForNotificationOrFail();

        contentResolver.unregisterContentObserver(taskObserver);
    }

    @After
    public void tearDown() {
        /* Use TaskDbHelper to get access to a writable database */
        DBHelper dbHelper = new DBHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
//        database.delete(InstructionContract.InstructionEntry.TABLE_NAME, null, null);
//        database.delete(RequirementContract.RequirementEntry.TABLE_NAME, null, null);
//        database.delete(StepContract.StepEntry.TABLE_NAME, null, null);
    }
}
