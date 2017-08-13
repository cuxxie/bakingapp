package id.cuxxie.bakingapp.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

import java.util.Random;

import id.cuxxie.bakingapp.ContentProvider.Contract.RequirementContract;
import id.cuxxie.bakingapp.ContentProvider.DBUtility;
import id.cuxxie.bakingapp.Model.Requirement;
import id.cuxxie.bakingapp.R;

/**
 * Created by hendr on 8/13/2017.
 */

public class RequirementWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;
        SharedPreferences sharedPreferences = context.getSharedPreferences("requirement", Context.MODE_PRIVATE);
        String textviewText = "";
        if(sharedPreferences.contains("instructionId"))
        {
            int instrId = sharedPreferences.getInt("instructionId",0);
            Cursor cursor = context.getContentResolver().query(RequirementContract.RequirementEntry.CONTENT_URI,
                    null,
                    RequirementContract.RequirementEntry.COLUMN_INSTRUCTION_ID+"=?",
                    new String[]{String.valueOf(instrId)},
                    null);
            cursor.moveToFirst();
            do{
                Requirement req =  DBUtility.convertCursorToRequirement(cursor);
                textviewText = textviewText + String.format(context.getString(R.string.ingredient_widget),req.getName(),String.valueOf(req.getQuantity()), req.getMeasure()) + "\n";
            }while(cursor.moveToNext());
        }
//        final int instrId =
        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
//            String number = String.format("%03d", (new Random().nextInt(900) + 100));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
            remoteViews.setTextViewText(R.id.widget_text_view, textviewText);

//            Intent intent = new Intent(context, RequirementWidget.class);
//            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
//                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setOnClickPendingIntent(R.id.actionButton, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
