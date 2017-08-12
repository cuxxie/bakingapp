package id.cuxxie.bakingapp.Data;

import android.database.Cursor;

import org.json.JSONObject;

import java.util.ArrayList;

import id.cuxxie.bakingapp.Model.Instruction;

/**
 * Created by hendr on 8/11/2017.
 */

public class DataRetrieverListener {
    private APICallback caller;

    public DataRetrieverListener(APICallback caller) {
        this.caller = caller;
    }

    public void syncOperationComplete()
    {
        caller.didCompleteSyncData();
    }

    public void syncOperationFailed()
    {
        caller.errorOnAPICall();
    }

    public void onQueryInstructionFinished(ArrayList<Instruction> instructions) {
        caller.didCompleteDataQueryWithInstruction(instructions);
    }

    public interface APICallback{
        void didCompleteSyncData();
        void errorOnAPICall();
        void didCompleteDataQueryWithInstruction(ArrayList<Instruction> instructions);
        void didCompleteInsertOrDeleteOperation(boolean isInsert);
    }
}
