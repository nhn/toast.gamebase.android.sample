/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.toast.android.gamebase.sample.data

import com.toast.android.gamebase.sample.R

class AccessInformationRepository {
    fun getAccessInformation(): List<AccessInformation> {
        return listOf(
            AccessInformation(
                R.string.access_info_ad_id_title,
                R.string.access_info_ad_id_subtitle,
                R.drawable.person,
                R.string.access_info_ad_id_content_description
            ),
            AccessInformation(
                R.string.access_info_camera_title,
                R.string.access_info_camera_subtitle,
                R.drawable.photo_camera,
                R.string.access_info_camera_content_description
            ),
            AccessInformation(
                R.string.access_info_photo_title,
                R.string.access_info_photo_subtitle,
                R.drawable.photo,
                R.string.access_info_photo_content_description
            )
        )
    }
}