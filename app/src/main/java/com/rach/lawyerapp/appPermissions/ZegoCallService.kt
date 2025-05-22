package com.rach.lawyerapp.appPermissions

import android.app.Application
import android.util.Log
import com.zegocloud.uikit.internal.ZegoUIKitLanguage
import com.zegocloud.uikit.plugin.invitation.ZegoInvitationType
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.config.DurationUpdateListener
import com.zegocloud.uikit.prebuilt.call.config.ZegoCallDurationConfig
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig
import com.zegocloud.uikit.prebuilt.call.event.CallEndListener
import com.zegocloud.uikit.prebuilt.call.event.ErrorEventsListener
import com.zegocloud.uikit.prebuilt.call.event.SignalPluginConnectListener
import com.zegocloud.uikit.prebuilt.call.event.ZegoCallEndReason
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoTranslationText
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoUIKitPrebuiltCallConfigProvider
import im.zego.zim.enums.ZIMConnectionEvent
import im.zego.zim.enums.ZIMConnectionState
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ZegoCallService @Inject constructor() {

    private var currentCallType: ZegoInvitationType? = null
    private var isCaller: Boolean = false

    fun initialize(
        application: Application,
        appId: Long,
        appSignIn: String,
        userId: String,
        userName: String,
        // you can pass here viewmodel
    ) {


        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig().apply {
            translationText = ZegoTranslationText(ZegoUIKitLanguage.ENGLISH)
            provider = ZegoUIKitPrebuiltCallConfigProvider { invitationData ->
                isCaller = invitationData?.inviter?.userID == userId
                currentCallType = when (invitationData?.type) {
                    ZegoInvitationType.VIDEO_CALL.value -> ZegoInvitationType.VIDEO_CALL
                    ZegoInvitationType.VOICE_CALL.value -> ZegoInvitationType.VOICE_CALL
                    else -> null
                }

                Log.d("tum", "Zego Call Type Received: $currentCallType")

                getCallConfig(isVideoCall = currentCallType == ZegoInvitationType.VIDEO_CALL)
            }

            notificationConfig = ZegoNotificationConfig().apply {
                channelID = "CallInvitation"
                channelName = "CallInvitation"
                sound = "zego_data"
            }
        }

        ZegoUIKitPrebuiltCallService.init(
            application,
            appId,
            appSignIn,
            userId, userName, callInvitationConfig
        )

        // Notification Part of ZegoCloud
        ZegoUIKitPrebuiltCallService.enableFCMPush()
        setupEventListeners()
    }

    private fun getCallConfig(isVideoCall: Boolean): ZegoUIKitPrebuiltCallConfig {
        return (if (isVideoCall) {
            ZegoUIKitPrebuiltCallConfig.oneOnOneVideoCall()
        } else {
            ZegoUIKitPrebuiltCallConfig.oneOnOneVoiceCall()
        }).apply {
            durationConfig = ZegoCallDurationConfig().apply {
                isVisible = true
                durationUpdateListener = DurationUpdateListener { seconds ->
                    if (isCaller && seconds % 10 == 0L && seconds > 0) {
                        // logic for deduct money
                    }
                }

            }
        }
    }

    // you can pass here viewmodel
    private fun setupEventListeners() {
        ZegoUIKitPrebuiltCallService.events.errorEventsListener =
            ErrorEventsListener { errorCode, message ->
                Timber.e("Zego Error: code = $errorCode, message = $message")
            }
        ZegoUIKitPrebuiltCallService.events.invitationEvents.pluginConnectListener =
            SignalPluginConnectListener { state: ZIMConnectionState, event: ZIMConnectionEvent, extendedData: JSONObject ->
                Timber.d("Zego Plugin Connection: state = $state, event = $event, extendedData = $extendedData")
            }

        ZegoUIKitPrebuiltCallService.events.callEvents.callEndListener =
            CallEndListener { callEndReason, _ ->
                if (callEndReason == ZegoCallEndReason.LOCAL_HANGUP || callEndReason == ZegoCallEndReason.REMOTE_HANGUP) {
                    //viewModel.resetDeduction()
                    currentCallType = null
                    ZegoUIKitPrebuiltCallService.endCall()
                }
            }
    }

    fun unInitialize() {
        ZegoUIKitPrebuiltCallService.unInit()
    }
}