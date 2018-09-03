package com.geomoby.demoapp.logic.geomoby;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.geomoby.GeoMoby;
import com.geomoby.classes.GeomobyActionBasic;
import com.geomoby.classes.GeomobyActionData;
import com.geomoby.demoapp.R;
import com.geomoby.demoapp.logic.system.NotificationManager;
import com.geomoby.demoapp.ui.discount.DiscountActivity;
import com.geomoby.demoapp.ui.main.MainActivity;

public class GeoMobyActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) return;

        switch(action) {
            case GeoMoby.GeomobyActionData:
                GeomobyActionData geomobyActionData = (GeomobyActionData)intent.getSerializableExtra(GeoMoby.GeomobyActionDataContent);
                if (geomobyActionData != null) {
                    String key = "id";
                    String value = geomobyActionData.getValue(key);
                    if (value != null) {
                        Intent openIntent = new Intent(context, DiscountActivity.class);
                        openIntent.putExtra(key, value);
                        NotificationManager.sendNotification(context, openIntent, "", "Data Action Received!", R.mipmap.data);
                    }
                }
                break;

            case GeoMoby.GeomobyActionBasic:
                GeomobyActionBasic geomobyActionBasic = (GeomobyActionBasic)intent.getSerializableExtra(GeoMoby.GeomobyActionBasicContent);
                if (geomobyActionBasic != null) {
                    Intent openIntent = new Intent(context, MainActivity.class);
                    NotificationManager.sendNotification(context, openIntent, geomobyActionBasic.getTitle(), geomobyActionBasic.getBody(), R.mipmap.message);
                }
                break;
        }
    }
}
