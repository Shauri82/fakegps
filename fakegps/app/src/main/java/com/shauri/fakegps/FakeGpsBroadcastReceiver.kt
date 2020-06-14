package com.shauri.fakegps

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class FakeGpsBroadcastReceiver(var action: Runnable) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (STOP_MOCKING_ACTION.equals(intent.action)) {
            action?.run();
        }
    }

}