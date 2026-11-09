package com.callhistory.app

import android.telecom.Call
import android.telecom.CallScreeningService

class CallScreeningServiceImpl : CallScreeningService() {
    override fun onScreenCall(details: Call.Details) {
        val direction = when (details.callDirection) {
            Call.Details.DIRECTION_INCOMING -> "Incoming"
            Call.Details.DIRECTION_OUTGOING -> "Outgoing"
            else -> "Call"
        }
        getSharedPreferences("call_history", MODE_PRIVATE).edit()
            .putString("number", details.handle?.schemeSpecificPart.orEmpty())
            .putString("direction", direction).apply()
        respondToCall(details, CallResponse.Builder()
            .setDisallowCall(false).setRejectCall(false).setSilenceCall(false).build())
    }
}
