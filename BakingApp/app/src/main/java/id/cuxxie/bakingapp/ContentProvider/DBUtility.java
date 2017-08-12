package id.cuxxie.bakingapp.ContentProvider;

import android.content.ContentValues;
import android.database.Cursor;

import id.cuxxie.bakingapp.ContentProvider.Contract.InstructionContract;
import id.cuxxie.bakingapp.ContentProvider.Contract.RequirementContract;
import id.cuxxie.bakingapp.ContentProvider.Contract.StepContract;
import id.cuxxie.bakingapp.Model.Instruction;
import id.cuxxie.bakingapp.Model.Requirement;
import id.cuxxie.bakingapp.Model.Step;

/**
 * Created by hendr on 8/11/2017.
 */

public class DBUtility {
    public static ContentValues convertRequirementToContentValues(Requirement requirement){
        ContentValues contentValues = new ContentValues();
        if(requirement.getId() != 0)
            contentValues.put(RequirementContract.RequirementEntry.COLUMN_ID,requirement.getId());

        contentValues.put(RequirementContract.RequirementEntry.COLUMN_INSTRUCTION_ID,requirement.getInstructionid());
        contentValues.put(RequirementContract.RequirementEntry.COLUMN_MEASURE,requirement.getMeasure());
        contentValues.put(RequirementContract.RequirementEntry.COLUMN_NAME,requirement.getName());
        contentValues.put(RequirementContract.RequirementEntry.COLUMN_QUANTITY,requirement.getQuantity());

        int status = 0;
        if(requirement.isStatus())
            status = 1;
        contentValues.put(RequirementContract.RequirementEntry.COLUMN_STATUS,status);
        return contentValues;
    }
    public static Requirement convertCursorToRequirement(Cursor cursor){
        int id = cursor.getColumnIndex(RequirementContract.RequirementEntry.COLUMN_ID);
        int instrIdIdx = cursor.getColumnIndex(RequirementContract.RequirementEntry.COLUMN_INSTRUCTION_ID);
        int measureIdx = cursor.getColumnIndex(RequirementContract.RequirementEntry.COLUMN_MEASURE);
        int nameIdx = cursor.getColumnIndex(RequirementContract.RequirementEntry.COLUMN_NAME);
        int quantIdx = cursor.getColumnIndex(RequirementContract.RequirementEntry.COLUMN_QUANTITY);
        int statusIdx = cursor.getColumnIndex(RequirementContract.RequirementEntry.COLUMN_STATUS);
        return new Requirement(cursor.getInt(id),cursor.getInt(quantIdx),cursor.getString(measureIdx),cursor.getString(nameIdx),cursor.getInt(statusIdx), cursor.getInt(instrIdIdx));
    }

    public static ContentValues convertStepToContentValues(Step step)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(StepContract.StepEntry.COLUMN_ID,step.getId());
        contentValues.put(StepContract.StepEntry.COLUMN_DESC,step.getDescription());
        contentValues.put(StepContract.StepEntry.COLUMN_INSTRUCTION_ID,step.getInstructionId());
        contentValues.put(StepContract.StepEntry.COLUMN_SHORT_DESC,step.getShortDescription());
        contentValues.put(StepContract.StepEntry.COLUMN_THUMBNAIL,step.getThubmnailURL());
        contentValues.put(StepContract.StepEntry.COLUMN_VIDEO,step.getVideoURL());
        return contentValues;
    }

    public static Step convertCursorToStep(Cursor cursor)
    {
        int rowid = cursor.getColumnIndex(StepContract.StepEntry.COLUMN_ROWID);
        int id = cursor.getColumnIndex(StepContract.StepEntry.COLUMN_ID);
        int descIdx = cursor.getColumnIndex(StepContract.StepEntry.COLUMN_DESC);
        int instrIdIdx = cursor.getColumnIndex(StepContract.StepEntry.COLUMN_INSTRUCTION_ID);
        int shortDescIdx = cursor.getColumnIndex(StepContract.StepEntry.COLUMN_SHORT_DESC);
        int thumbIdx = cursor.getColumnIndex(StepContract.StepEntry.COLUMN_THUMBNAIL);
        int videoIdx = cursor.getColumnIndex(StepContract.StepEntry.COLUMN_VIDEO);
        //(int id, int instructionId ,String shortDescription, String description, String videoURL, String thubmnailURL)
        return new Step(cursor.getInt(rowid),cursor.getInt(id),cursor.getInt(instrIdIdx),cursor.getString(shortDescIdx),cursor.getString(descIdx),cursor.getString(videoIdx),cursor.getString(thumbIdx));
    }

    public static ContentValues convertInstructionToContentValues(Instruction instruction)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(InstructionContract.InstructionEntry.COLUMN_ID,instruction.getId());
        contentValues.put(InstructionContract.InstructionEntry.COLUMN_NAME,instruction.getName());
        contentValues.put(InstructionContract.InstructionEntry.COLUMN_SERVINGS,instruction.getServings());
        contentValues.put(InstructionContract.InstructionEntry.COLUMN_IMAGE,instruction.getImage());
        return contentValues;
    }

    public static Instruction convertCursorToInstruction(Cursor cursor)
    {
        int id = cursor.getColumnIndex(InstructionContract.InstructionEntry.COLUMN_ID);
        int name = cursor.getColumnIndex(InstructionContract.InstructionEntry.COLUMN_NAME);
        int image = cursor.getColumnIndex(InstructionContract.InstructionEntry.COLUMN_IMAGE);
        int servings = cursor.getColumnIndex(InstructionContract.InstructionEntry.COLUMN_SERVINGS);
        return new Instruction(cursor.getInt(id),cursor.getString(name),cursor.getInt(servings),cursor.getString(image));
    }
}
