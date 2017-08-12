package id.cuxxie.bakingapp.ContentProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import id.cuxxie.bakingapp.ContentProvider.Contract.InstructionContract;
import id.cuxxie.bakingapp.ContentProvider.Contract.RequirementContract;
import id.cuxxie.bakingapp.ContentProvider.Contract.StepContract;
import id.cuxxie.bakingapp.Model.Instruction;
import id.cuxxie.bakingapp.Model.Requirement;

/**
 * Created by hendr on 8/11/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bakingDb.db";
    private static final int VERSION = 2;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_STEPS = "CREATE TABLE "  + StepContract.StepEntry.TABLE_NAME + " (" +
                StepContract.StepEntry.COLUMN_ROWID                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StepContract.StepEntry.COLUMN_ID                + " INTEGER, " +
                StepContract.StepEntry.COLUMN_INSTRUCTION_ID    + " INTEGER," +
                StepContract.StepEntry.COLUMN_SHORT_DESC + " TEXT , " +
                StepContract.StepEntry.COLUMN_DESC + " TEXT, " +
                StepContract.StepEntry.COLUMN_VIDEO + " TEXT, " +
                StepContract.StepEntry.COLUMN_THUMBNAIL + " TEXT " +
                ");";

        final String CREATE_TABLE_REQ = "CREATE TABLE "  + RequirementContract.RequirementEntry.TABLE_NAME + " (" +
                RequirementContract.RequirementEntry.COLUMN_ID                + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                RequirementContract.RequirementEntry.COLUMN_INSTRUCTION_ID    + " INTEGER ," +
                RequirementContract.RequirementEntry.COLUMN_QUANTITY + " INTEGER , " +
                RequirementContract.RequirementEntry.COLUMN_MEASURE + " TEXT, " +
                RequirementContract.RequirementEntry.COLUMN_NAME + " TEXT, " +
                RequirementContract.RequirementEntry.COLUMN_STATUS + " BOOLEAN " +
                ");";

        final String CREATE_TABLE_INSTRUCTION = "CREATE TABLE "  + InstructionContract.InstructionEntry.TABLE_NAME + " (" +
                InstructionContract.InstructionEntry.COLUMN_ID                + " INTEGER PRIMARY KEY , " +
                InstructionContract.InstructionEntry.COLUMN_NAME    + " TEXT," +
                InstructionContract.InstructionEntry.COLUMN_SERVINGS + " INTEGER , " +
                InstructionContract.InstructionEntry.COLUMN_IMAGE + " TEXT " +
                ");";

        db.execSQL(CREATE_TABLE_INSTRUCTION);
        db.execSQL(CREATE_TABLE_STEPS);
        db.execSQL(CREATE_TABLE_REQ);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StepContract.StepEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RequirementContract.RequirementEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + InstructionContract.InstructionEntry.TABLE_NAME);
        onCreate(db);
    }
}
