package com.moczul.sample

import android.util.Log
import com.moczul.ok2curl.logger.Loggable

class AndroidLogger : Loggable {

    override fun log(message: String) {
        Log.v(TAG, message)
    }

    companion object {
        const val TAG = "Ok2Curl"
    }
}