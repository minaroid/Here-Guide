package com.george888.mina.hereguide.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.george888.mina.hereguide.HereApp;
import com.george888.mina.hereguide.R;

public class MyBroadcast extends BroadcastReceiver {
    private HereApp app;

    @Override
    public void onReceive(Context context, Intent intent) {
        app = ((HereApp) context.getApplicationContext());

        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            app.testConnection();
            if(!app.isConnected()){
                Toast.makeText(context, R.string.msg_offline,Toast.LENGTH_SHORT).show();
            }
        }
    }

}
