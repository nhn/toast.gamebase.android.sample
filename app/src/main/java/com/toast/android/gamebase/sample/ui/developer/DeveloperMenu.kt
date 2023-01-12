package com.toast.android.gamebase.sample.ui.developer

// should define following values into string-array and string value in strings.xml
data class DeveloperMenu(val category: Int, val id: String, val name: String) {
    companion object {
        const val AUTH_SUSPEND_WITHDRAWAL = "developer_menu_auth_suspend_withdrawal"
        const val AUTH_SUSPEND_WITHDRAWAL_CANCEL = "developer_menu_auth_suspend_withdrawal_cancel"
        const val PURCHASE_ACTIVATED_SUBSCRIPTION = "developer_menu_purchase_activated_subscription"
        const val PURCHASE_NOT_CONSUMED_LIST = "developer_menu_purchase_not_consumed_list"
        const val PUSH_CURRENT_SETTING = "developer_menu_push_current_setting"
        const val PUSH_DETAIL_SETTING = "developer_menu_push_detail_setting"
        const val LOGGER_INITIALIZE = "developer_menu_logger_initialize"
        const val SEND_LOG = "developer_menu_send_log"
        const val TERMS_INFO = "developer_menu_terms_info"
        const val TERMS_DETAIL_SETTING = "developer_menu_terms_detail_setting"
        const val TERMS_AGREEMENT_SAVE = "developer_menu_terms_agreement_save"
        const val SHOW_IMAGE_NOTICE = "developer_menu_show_image_notice"
        const val IMAGE_NOTICE_DETAIL_SETTING = "developer_menu_image_notice_detail_setting"
        const val OPEN_WEBVIEW = "developer_menu_open_webview"
        const val OPEN_OUTSIDE_BROWSER = "developer_menu_open_outside_browser"
        const val WEBIVEW_DETAIL_SETTING = "developer_menu_webview_detail_setting"
        const val SHOW_ALERT = "developer_menu_show_alert"
        const val SHOW_SHORT_TOAST = "developer_menu_show_short_toast"
        const val SHOW_LONG_TOAST = "developer_menu_show_long_toast"
        const val USER_LEVEL_INFO_SETTING = "developer_menu_user_level_info_setting"
        const val USER_LEVEL_UP_INFO_SETTING = "developer_menu_user_level_up_info_setting"
        const val CONTACT_URL = "developer_menu_contact_url"
        const val CONTACT_DETAIL_SETTING = "developer_menu_contact_detail_setting"
        const val DEVICE_LANGUAGE = "developer_menu_etc_device_language"
        const val DISPLAY_LANGUAGE = "developer_menu_etc_display_language"
        const val DEVICE_COUNTRY_CODE = "developer_menu_etc_device_country_code"
        const val USIM_COUNTRY_CODE = "developer_menu_etc_usim_country_code"
        const val COUNTRY_CODE = "developer_menu_etc_country_code"
        const val OPEN_SOURCE_LICENSES = "developer_menu_open_source_licenses"
    }
}