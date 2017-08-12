package id.cuxxie.bakingapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import id.cuxxie.bakingapp.ContentProvider.Contract.InstructionContract;
import id.cuxxie.bakingapp.ContentProvider.Contract.RequirementContract;
import id.cuxxie.bakingapp.ContentProvider.Contract.StepContract;
import id.cuxxie.bakingapp.ContentProvider.DBUtility;
import id.cuxxie.bakingapp.Model.Instruction;
import id.cuxxie.bakingapp.Model.Requirement;
import id.cuxxie.bakingapp.Model.Step;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hendr on 8/11/2017.
 */

public class DataRetriever implements Callback , CustomAsyncQueryHandler.AsyncQueryListener{
    private final OkHttpClient okHttpClient;
    private static DataRetriever self;
    private DataRetrieverListener callerHandler;
    private Context context;
    private static final String BASE_URL = "http://go.udacity.com/android-baking-app-json";
    private DataRetriever() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        System.out.println("url: " + chain.request().url());
                        return chain.proceed(chain.request());
                    }
                })
                .build();
    }

    public static DataRetriever getInstance(Context context)
    {
        if(self == null){
            self = new DataRetriever();
        }
        self.context = context;
        return self;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public void removeContext()
    {
        this.context = null;
    }

    private void callURLGet(String url, boolean isAsync, Object caller)
    {
        Log.v("URL Call",url);
        Request request = new Request.Builder().cacheControl(new CacheControl.Builder()
                .maxStale(2, TimeUnit.HOURS)
                .build())
                .url(url).tag(caller.hashCode())
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response;
            if(isAsync) {
                call.enqueue(this);
            }
            else{
                response = call.execute();
                this.onResponse(call,response);
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            this.onFailure(call, ex);
            callerHandler.syncOperationFailed();
        }
    }

    public void downloadDataAndSaveDeltaToDB(Object caller, DataRetrieverListener callerHandler)
    {
        this.callerHandler = callerHandler;
        callURLGet(BASE_URL,true,caller);
    }


    public static ArrayList<Instruction> convertJsonInstructionArrayToPOJO(JSONArray resultArray)
    {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Instruction> instructionArrayList = new ArrayList<>();
        try {
            instructionArrayList = mapper.readValue(resultArray.toString(),new TypeReference<ArrayList<Instruction>>(){});
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return instructionArrayList;
    }


    @Override
    public void onResponse(Call call, Response response) throws IOException {
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(response.body().string());
            saveToLocalDB(convertJsonInstructionArrayToPOJO(jsonArray));
        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        this.callerHandler.syncOperationFailed();
    }

    private void saveToLocalDB(ArrayList<Instruction> arrayList)
    {
        ArrayList<Step> steps = new ArrayList<>();
        ArrayList<Requirement> requirements = new ArrayList<>();
        for(Instruction instruction : arrayList)
        {
            if(isInstructionExist(instruction))
                continue;

            for(Step step: instruction.getSteps())
            {
                step.setInstructionId(instruction.getId());
                steps.add(step);
            }

            for(Requirement requirement: instruction.getRequirements())
            {
                requirement.setInstructionid(instruction.getId());
                requirements.add(requirement);
            }
            ContentValues cv = DBUtility.convertInstructionToContentValues(instruction);
            Uri mNewUri = context.getContentResolver().insert(
                    InstructionContract.InstructionEntry.CONTENT_URI,   // the user dictionary content URI
                    cv                          // the values to insert
            );
        }
        for(Step step: steps)
        {
            if(isStepExist(step))
                continue;

            ContentValues cv = DBUtility.convertStepToContentValues(step);
            Uri mNewUri = context.getContentResolver().insert(
                    StepContract.StepEntry.CONTENT_URI,   // the user dictionary content URI
                    cv                          // the values to insert
            );
        }
        for(Requirement requirement:requirements)
        {
            if(isRequirementExist(requirement))
                continue;

            ContentValues cv = DBUtility.convertRequirementToContentValues(requirement);
            Uri mNewUri = context.getContentResolver().insert(
                    RequirementContract.RequirementEntry.CONTENT_URI,   // the user dictionary content URI
                    cv                          // the values to insert
            );
        }
        if (arrayList.size() > 0)
            this.callerHandler.syncOperationComplete();
        else
            this.callerHandler.syncOperationFailed();
    }

    private boolean isStepExist(Step step){
        Cursor mCursor = context.getContentResolver().query(
                StepContract.StepEntry.CONTENT_URI,
                null,                       // The columns to return for each row
                "id=? AND instructionid=? AND shortDescription=? AND description=?"    ,               // Either null, or the word the user entered
                new String[]{String.valueOf(step.getId()),String.valueOf(step.getInstructionId()),step.getShortDescription(),step.getDescription()},                    // Either empty, or the string the user entered
                null);                       // The sort order for the returned rows
        return mCursor.getCount() > 0;
    }

    private boolean isRequirementExist(Requirement requirement){
        Cursor mCursor = context.getContentResolver().query(
                RequirementContract.RequirementEntry.CONTENT_URI,
                null,                       // The columns to return for each row
                "instructionid=? AND quantity=? AND measure=? AND name=?"    ,               // Either null, or the word the user entered
                new String[]{String.valueOf(requirement.getInstructionid()),String.valueOf(requirement.getQuantity()),requirement.getMeasure(),requirement.getName()},                    // Either empty, or the string the user entered
                null);                       // The sort order for the returned rows
        return mCursor.getCount() > 0;
    }

    private boolean isInstructionExist(Instruction instruction){
        Cursor mCursor = context.getContentResolver().query(
                InstructionContract.InstructionEntry.CONTENT_URI,
                null,
                "id=?",
                new String[]{String.valueOf(instruction.getId())},
                null
        );
        return mCursor.getCount() > 0;
    }

    public void updateRequirementStatus(Requirement requirement, Context context, boolean status, Object caller)
    {
        requirement.setStatus(status);
        ContentValues cv = DBUtility.convertRequirementToContentValues(requirement);
        String id = String.valueOf(requirement.getId());
        CustomAsyncQueryHandler customAsyncQueryHandler = new CustomAsyncQueryHandler(context.getContentResolver(),this);
        customAsyncQueryHandler.startUpdate(caller.hashCode(),null, RequirementContract.RequirementEntry.CONTENT_URI,cv,"id=?",new String[]{id});
    }

    public void loadAllInstruction(DataRetrieverListener listener, Context context, Object caller)
    {
        this.context = context;
        this.callerHandler = listener;
        CustomAsyncQueryHandler customAsyncQueryHandler = new CustomAsyncQueryHandler(context.getContentResolver(),this);
        customAsyncQueryHandler.startQuery(caller.hashCode(),null, InstructionContract.InstructionEntry.CONTENT_URI,null,null,null,null);
    }

    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {
        ArrayList<Instruction> instructions = new ArrayList<>();
        cursor.moveToFirst();
        do{
            ArrayList<Requirement> requirements = new ArrayList<>();
            ArrayList<Step> steps = new ArrayList<>();
            Instruction instruction = DBUtility.convertCursorToInstruction(cursor);

            Cursor requirementCursor = getAllRequirementsUnderInstruction(instruction.getId());
            requirementCursor.moveToFirst();
            do{
                Requirement requirement = DBUtility.convertCursorToRequirement(requirementCursor);
                requirements.add(requirement);
            }while (requirementCursor.moveToNext());

            Cursor stepCursor = getAllStepsUnderInstruction(instruction.getId());
            stepCursor.moveToNext();
            do{
                Step step = DBUtility.convertCursorToStep(stepCursor);
                steps.add(step);
            }while (stepCursor.moveToNext());
            instruction.setRequirements(requirements);
            instruction.setSteps(steps);
            instructions.add(instruction);
        }
        while (cursor.moveToNext());
        callerHandler.onQueryInstructionFinished(instructions);
    }

    @Override
    public void onInsertOrDeleteComplete(boolean isInsert) {

    }

    private Cursor getAllStepsUnderInstruction(int instructionId)
    {
        Cursor cursor = context.getContentResolver().query(StepContract.StepEntry.CONTENT_URI,
                null,
                "instructionid=?",
                new String[]{String.valueOf(instructionId)},
                null);
        return cursor;
    }

    private Cursor getAllRequirementsUnderInstruction(int instructionId)
    {
        Cursor cursor = context.getContentResolver().query(RequirementContract.RequirementEntry.CONTENT_URI,
                null,
                "instructionid=?",
                new String[]{String.valueOf(instructionId)},
                null);
        return cursor;
    }
}
