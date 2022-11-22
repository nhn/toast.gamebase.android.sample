package com.toast.android.gamebase.sample.ui.developer

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toast.android.gamebase.base.GamebaseError.UI_CONTACT_FAIL_INVALID_URL
import com.toast.android.gamebase.Gamebase
import com.toast.android.gamebase.base.GamebaseError
import com.toast.android.gamebase.base.purchase.PurchasableReceipt
import com.toast.android.gamebase.sample.GamebaseApplication
import com.toast.android.gamebase.sample.R
import com.toast.android.gamebase.sample.gamebase_manager.*
import com.toast.android.gamebase.sample.gamebase_manager.cancelWithdrawal
import com.toast.android.gamebase.sample.gamebase_manager.getCountryCodeOfDevice
import com.toast.android.gamebase.sample.gamebase_manager.getCountryCodeOfUSIM
import com.toast.android.gamebase.sample.gamebase_manager.getDeviceLanguage
import com.toast.android.gamebase.sample.gamebase_manager.getDisplayLanguage
import com.toast.android.gamebase.sample.gamebase_manager.getIntegratedCountryCode
import com.toast.android.gamebase.sample.gamebase_manager.getContactUrl
import com.toast.android.gamebase.sample.gamebase_manager.isSuccess
import com.toast.android.gamebase.sample.gamebase_manager.queryTokenInfo
import com.toast.android.gamebase.sample.gamebase_manager.requestActivatedPurchases
import com.toast.android.gamebase.sample.gamebase_manager.requestItemListOfNotConsumed
import com.toast.android.gamebase.sample.gamebase_manager.requestWithdrawal
import com.toast.android.gamebase.sample.gamebase_manager.showAlert
import com.toast.android.gamebase.sample.gamebase_manager.showToast
import com.toast.android.gamebase.sample.util.printWithIndent
import kotlinx.coroutines.launch

class DeveloperViewModel: ViewModel() {
    val showPurchaseDialog = mutableStateOf(false)
    var purchaseItemList = mutableListOf<PurchasableReceipt>()
        private set
    val menuMap: MutableMap<String, List<DeveloperMenu>> = createMenuMap()
    val isLoggerInitializeOpened = mutableStateOf(false)
    val isSendLogOpened = mutableStateOf(false)
    val isOpenWebViewOpened = mutableStateOf(false)
    val isOpenWebBrowserOpened = mutableStateOf(false)
    val isUserLevelInfoSettingOpened = mutableStateOf(false)
    val isUserLevelUpInfoSettingOpened = mutableStateOf(false)

    private val failedTitle: String =
        GamebaseApplication.instance.applicationContext.getString(R.string.failed)
    private val successTitle: String =
        GamebaseApplication.instance.applicationContext.getString(R.string.success)

    private fun createMenuMap(): MutableMap<String, List<DeveloperMenu>> {
        val developerMenuMap: MutableMap<String, List<DeveloperMenu>> = mutableMapOf()
        val context = GamebaseApplication.instance.applicationContext
        val menuFactory = DeveloperMenuFactory()

        developerMenuMap[context.resources.getString(R.string.developer_menu_category_auth)] =
            menuFactory.createMenu(context, R.array.Auth)
        developerMenuMap[context.resources.getString(R.string.developer_menu_category_purchase)] =
            menuFactory.createMenu(context, R.array.Purchase)
        developerMenuMap[context.resources.getString(R.string.developer_menu_category_push)] =
            menuFactory.createMenu(context, R.array.Push)
        developerMenuMap[context.resources.getString(R.string.developer_menu_category_logger)] =
            menuFactory.createMenu(context, R.array.Logger)
        developerMenuMap[context.resources.getString(R.string.developer_menu_category_terms)] =
            menuFactory.createMenu(context, R.array.Terms)
        developerMenuMap[context.resources.getString(R.string.developer_menu_category_image_notice)] =
            menuFactory.createMenu(context, R.array.ImageNotice)
        developerMenuMap[context.resources.getString(R.string.developer_menu_category_webview)] =
            menuFactory.createMenu(context, R.array.WebView)
        developerMenuMap[context.resources.getString(R.string.developer_menu_category_alert)] =
            menuFactory.createMenu(context, R.array.Alert)
        developerMenuMap[context.resources.getString(R.string.developer_menu_category_analytics)] =
            menuFactory.createMenu(context, R.array.Analytics)
        developerMenuMap[context.resources.getString(R.string.developer_menu_category_contact)] =
            menuFactory.createMenu(context, R.array.Contact)
        developerMenuMap[context.resources.getString(R.string.developer_menu_category_etc)] =
            menuFactory.createMenu(context, R.array.Etc)

        return developerMenuMap
    }


    fun onMenuClick(
        activity: Activity,
        developerMenuItem: DeveloperMenu,
        menuNavigator: DeveloperMenuNavigator
    ) {
        when (developerMenuItem.id) {
            DeveloperMenu.AUTH_SUSPEND_WITHDRAWAL -> requestWithdrawal(activity)
            DeveloperMenu.AUTH_SUSPEND_WITHDRAWAL_CANCEL -> cancelWithdrawal(activity)
            DeveloperMenu.PURCHASE_ACTIVATED_SUBSCRIPTION -> fetchActivatedPurchaseList(activity)
            DeveloperMenu.PURCHASE_NOT_CONSUMED_LIST -> fetchItemNotConsumedList(activity)
            DeveloperMenu.PUSH_CURRENT_SETTING -> fetchPushCurrentSetting(activity)
            DeveloperMenu.PUSH_DETAIL_SETTING -> menuNavigator.onPushSettingMenu()
            DeveloperMenu.CONTACT_URL -> requestContactUrl(activity)
            DeveloperMenu.CONTACT_DETAIL_SETTING -> menuNavigator.onContactDetailMenu()
            DeveloperMenu.TERMS_INFO -> fetchTermsCurrentSetting(activity)
            DeveloperMenu.TERMS_DETAIL_SETTING -> menuNavigator.onTermsSettingMenu()
            DeveloperMenu.TERMS_AGREEMENT_SAVE -> menuNavigator.onTermsCustomMenu()
            DeveloperMenu.SHOW_ALERT -> showAlertDialogWithCallback(activity)
            DeveloperMenu.SHOW_SHORT_TOAST -> showSampleToast(activity, Toast.LENGTH_SHORT)
            DeveloperMenu.SHOW_LONG_TOAST -> showSampleToast(activity, Toast.LENGTH_LONG)
            DeveloperMenu.LOGGER_INITIALIZE -> isLoggerInitializeOpened.value = true
            DeveloperMenu.SEND_LOG -> isSendLogOpened.value = true
            DeveloperMenu.SHOW_IMAGE_NOTICE -> showImageNotices(activity)
            DeveloperMenu.IMAGE_NOTICE_DETAIL_SETTING -> menuNavigator.onImageNoticeSettingMenu()
            DeveloperMenu.OPEN_WEBVIEW -> isOpenWebViewOpened.value = true
            DeveloperMenu.OPEN_OUTSIDE_BROWSER -> isOpenWebBrowserOpened.value = true
            DeveloperMenu.WEBIVEW_DETAIL_SETTING -> menuNavigator.onWebViewSettingMenu()
            DeveloperMenu.USER_LEVEL_INFO_SETTING -> isUserLevelInfoSettingOpened.value = true
            DeveloperMenu.USER_LEVEL_UP_INFO_SETTING -> isUserLevelUpInfoSettingOpened.value = true
            DeveloperMenu.DEVICE_LANGUAGE -> showMenuNameAlert(activity, developerMenuItem.id, getDeviceLanguage())
            DeveloperMenu.DISPLAY_LANGUAGE -> showMenuNameAlert(activity, developerMenuItem.id, getDisplayLanguage())
            DeveloperMenu.DEVICE_COUNTRY_CODE -> showMenuNameAlert(activity, developerMenuItem.id, getCountryCodeOfDevice())
            DeveloperMenu.USIM_COUNTRY_CODE -> showMenuNameAlert(activity, developerMenuItem.id, getCountryCodeOfUSIM())
            DeveloperMenu.COUNTRY_CODE -> showMenuNameAlert(activity, developerMenuItem.id, getIntegratedCountryCode())
        }
    }

    private fun requestWithdrawal(activity: Activity) {
        val context = GamebaseApplication.instance.applicationContext
        requestWithdrawal(activity) { data, exception ->
            if (isSuccess(exception)) {
                showAlert(
                    activity,
                    successTitle,
                    context.resources.getString(R.string.request_withdrawal_success) + "${data.gracePeriodDate}"
                )
            } else {
                showAlert(
                    activity,
                    failedTitle,
                    exception.printWithIndent()
                )
            }
        }
    }

    private fun cancelWithdrawal(activity: Activity) {
        val context = GamebaseApplication.instance.applicationContext
        cancelWithdrawal(activity) { exception ->
            if (isSuccess(exception)) {
                showAlert(
                    activity,
                    successTitle,
                    context.resources.getString(R.string.cancel_withdrawal_success)
                )
            } else {
                showAlert(
                    activity,
                    failedTitle,
                    exception.printWithIndent()
                )
            }
        }
    }

    private fun fetchActivatedPurchaseList(activity: Activity) {
        val context = activity as Context
        requestActivatedPurchases(activity) { list, exception ->
            if (isSuccess(exception)) {
                purchaseItemList = list as MutableList<PurchasableReceipt>
                showPurchaseDialog.value = true
            } else {
                showAlert(
                    activity,
                    context.resources.getString(R.string.failed),
                    exception.printWithIndent()
                )
            }
        }
    }

    private fun fetchItemNotConsumedList(activity: Activity) {
        val context = activity as Context
        requestItemListOfNotConsumed(activity) { list, exception ->
            if (isSuccess(exception)) {
                purchaseItemList = list as MutableList<PurchasableReceipt>
                showPurchaseDialog.value = true
            } else {
                showAlert(
                    activity,
                    context.resources.getString(R.string.failed),
                    exception.printWithIndent()
                )
            }
        }
    }

    private fun fetchPushCurrentSetting(activity: Activity) {
        queryTokenInfo(activity) { pushTokenInfo, exception ->
            if (isSuccess(exception)) {
                showAlert(activity, successTitle, pushTokenInfo.printWithIndent())
            } else {
                showAlert(activity, failedTitle, exception.printWithIndent())
            }
        }
    }

    private fun fetchTermsCurrentSetting(activity: Activity) {
        viewModelScope.launch {
            queryTerms(activity) { gamebaseQueryTermsResult, exception ->
                if (Gamebase.isSuccess(exception)) {
                    showAlert(activity, successTitle, gamebaseQueryTermsResult.printWithIndent());
                } else if (exception.code == GamebaseError.UI_TERMS_NOT_EXIST_FOR_DEVICE_COUNTRY) {
                    // Another country device.
                    // Pass the 'terms and conditions' step.
                    showAlert(
                        activity, failedTitle,
                        (activity as Context).getString(R.string.developer_terms_no_need_to_show_terms)
                    )
                } else {
                    showAlert(activity, failedTitle, exception.printWithIndent());
                }
            }
        }
    }

    private fun requestContactUrl(activity: Activity) {
        val title = (activity as Context).getString(R.string.developer_contact_url_alert_title)
        getContactUrl() { contactUrl, exception ->
            if (isSuccess(exception)) {
                // do job with Contact url
                showAlert(activity, title, contactUrl)
            } else if (exception?.code == UI_CONTACT_FAIL_INVALID_URL) { // 6911
                // Gamebase Console Service Center URL is invalid.
                // Please check the url field in the TOAST Gamebase Console.
                showAlert(
                    activity,
                    failedTitle,
                    (activity as Context).getString(R.string.developer_contact_url_invalid)
                )
            } else {
                // An error occur when requesting the contact web view url.
                exception?.message?.let {
                    showAlert(activity, failedTitle, it)
                } ?: showAlert(activity, failedTitle, (activity as Context).getString(R.string.unknown_error))
            }
        }
    }

    private fun showAlertDialogWithCallback(activity: Activity) {
        val resources = (activity as Context).resources
        showAlert(
            activity,
            resources.getString(R.string.developer_alert_sample_title),
            resources.getString(R.string.developer_alert_sample_message)
        ) { dialog, which -> {
                // create own callback
            }
        }
    }

    private fun showSampleToast(activity: Activity, duration: Int) {
        val resources = (activity as Context).resources
        val toastMessage = if (duration == Toast.LENGTH_LONG)
            resources.getString(R.string.developer_toast_long_sample_message)
        else
            resources.getString(R.string.developer_toast_short_sample_message)

        showToast(activity, toastMessage, duration)
    }

    private fun showMenuNameAlert(activity: Activity, menuId: String, message: String) {
        val context = activity as Context
        val menuTitle = context.getStringResourceByName(menuId)

        showAlert(activity, menuTitle, message)
    }

    fun updateOnLevelUp(activity: Activity, value: String) {
        try {
            onLevelUp(value.toInt(), System.currentTimeMillis())
        } catch (exception: NumberFormatException) {
            showAlert(activity, TAG, "level needs Int type : $exception")
        }
    }
}
