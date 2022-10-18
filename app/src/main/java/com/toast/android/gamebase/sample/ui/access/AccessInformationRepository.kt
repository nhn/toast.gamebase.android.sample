package com.toast.android.gamebase.sample.ui.access


import com.toast.android.gamebase.sample.GamebaseApplication
import com.toast.android.gamebase.sample.R

class AccessInformationRepository() {
    fun getAccessInformation(): List<AccessInformation> {
        return listOf(
            AccessInformation(
                GamebaseApplication.ApplicationContext()
                    .getString(R.string.access_info_ad_id_title),
                GamebaseApplication.ApplicationContext()
                    .getString(R.string.access_info_ad_id_subtitle),
                R.drawable.person,
                GamebaseApplication.ApplicationContext()
                    .getString(R.string.access_info_ad_id_content_description)
            ),
            AccessInformation(
                GamebaseApplication.ApplicationContext()
                    .getString(R.string.access_info_camera_title),
                GamebaseApplication.ApplicationContext()
                    .getString(R.string.access_info_camera_subtitle),
                R.drawable.photo_camera,
                GamebaseApplication.ApplicationContext()
                    .getString(R.string.access_info_camera_content_description)
            ),
            AccessInformation(
                GamebaseApplication.ApplicationContext()
                    .getString(R.string.access_info_photo_title),
                GamebaseApplication.ApplicationContext()
                    .getString(R.string.access_info_photo_subtitle),
                R.drawable.photo,
                GamebaseApplication.ApplicationContext()
                    .getString(R.string.access_info_photo_content_description)
            )
        )
    }
}