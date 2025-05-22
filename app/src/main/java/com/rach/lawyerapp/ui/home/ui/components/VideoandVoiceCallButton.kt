package com.rach.lawyerapp.ui.home.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton

@Composable
fun VideoOrVoiceCallButton(isVideoCallBoolean: Boolean, onClick: (ZegoSendCallInvitationButton) -> Unit) {
    AndroidView(
        factory = {context ->
            val button = ZegoSendCallInvitationButton(context)
            button.setIsVideoCall(isVideoCallBoolean)
            button.resourceID = "zego_data"
            button
        }
    ){zegoCallButton ->
        zegoCallButton.setOnClickListener{ _ -> onClick(zegoCallButton)}
    }
}