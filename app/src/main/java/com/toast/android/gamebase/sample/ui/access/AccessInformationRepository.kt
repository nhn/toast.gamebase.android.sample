package com.toast.android.gamebase.sample.ui.access

import com.toast.android.gamebase.sample.R

class AccessInformationRepository() {
    fun getAccessInformation(): List<AccessInformation> {
        return listOf(
            AccessInformation(
                "광고 식별자(선택)",
                "Gamebase IDEA API 호출 시 필요",
                R.drawable.person,
                "person"
            ),
            AccessInformation(
                "카메라 (선택)",
                "Game 고객센터 API 호출 시 필요.\n사용자가 문의사항에 사진 또는 동영상 첨부시 사용.",
                R.drawable.photo_camera,
                "photo_camera"
            ),
            AccessInformation(
                "사진 (선택)",
                "Gamebase 고객센터 API 호출 시 필요.\n사용자가 문의사항에 앨범에서 사진 또는 동영상 첨부시 사용.",
                R.drawable.photo,
                "photo"
            )
        )
    }
}