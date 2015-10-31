package com.horde.samantha.samantha;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of App Widget functionality.
 */
public class SamanthaWidget extends AppWidgetProvider {
    /**
     * 브로드캐스트를 수신할때, Override된 콜백 메소드가 호출되기 직전에 호출됨
     */

    public static final String SAMANTHA_WIDGET_ACTION = "samantha.widget.WIDGET_UPDATE";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(SAMANTHA_WIDGET_ACTION) || action.equals(Intent.ACTION_DATE_CHANGED)) {
            AppWidgetManager gm = AppWidgetManager.getInstance(context);
            int[] ids = gm.getAppWidgetIds(new ComponentName(context, SamanthaWidget.class));
            this.onUpdate(context, gm, ids);
        } else {
            super.onReceive(context, intent);
        }
    }

    /**
     * 위젯을 갱신할때 호출됨
     * 주의 : Configure Activity를 정의했을때는 위젯 등록시 처음 한번은 호출이 되지 않습니다
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
                updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Map<String, Object> map = getImageWithTitle(context);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.samantha_widget);
        views.setTextViewText(R.id.textViewTitle, (String) map.get("title"));
        views.setTextViewText(R.id.textViewMode, (String) map.get("mode"));
        views.setImageViewResource(R.id.imageViewModeWidgetBackground, (Integer) map.get("image"));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private Map<String, Object> getImageWithTitle(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("widget", Context.MODE_PRIVATE);
        Map<String, Object> map = new HashMap<>();
        map.put("title", sharedPreferences.getString("title", "title"));
        map.put("image", sharedPreferences.getInt("image", R.drawable.sleep));
        map.put("mode", sharedPreferences.getString("mode", "mode"));
        return map;
    }

    /**
     * 위젯이 처음 생성될때 호출됨
     * 동일한 위젯이 생성되도 최초 생성때만 호출됨
     */
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    /**
     * 위젯의 마지막 인스턴스가 제거될때 호출됨
     * onEnabled()에서 정의한 리소스 정리할때
     */
    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /**
     * 위젯이 사용자에 의해 제거될때 호출됨
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }
}

