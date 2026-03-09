package com.glassos.launcher.managers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.glassos.launcher.models.BatteryInfo
import com.glassos.launcher.models.ControlCenterSettings
import com.glassos.launcher.models.ControlCenterState
import com.glassos.launcher.models.DynamicIslandNotification
import com.glassos.launcher.models.GridConfig
import com.glassos.launcher.models.HomeScreenItem
import com.glassos.launcher.models.MediaSessionInfo

class LauncherStateManager : ViewModel() {
    
    // Home screen state
    private val _homeScreenItems = MutableLiveData<List<HomeScreenItem>>(emptyList())
    val homeScreenItems: LiveData<List<HomeScreenItem>> = _homeScreenItems

    private val _currentPage = MutableLiveData<Int>(0)
    val currentPage: LiveData<Int> = _currentPage

    private val _gridConfig = MutableLiveData<GridConfig>(GridConfig())
    val gridConfig: LiveData<GridConfig> = _gridConfig

    // App drawer state
    private val _appDrawerVisible = MutableLiveData<Boolean>(false)
    val appDrawerVisible: LiveData<Boolean> = _appDrawerVisible

    private val _appDrawerSearchQuery = MutableLiveData<String>("")
    val appDrawerSearchQuery: LiveData<String> = _appDrawerSearchQuery

    // Dynamic Island state
    private val _dynamicIslandNotifications = MutableLiveData<List<DynamicIslandNotification>>(emptyList())
    val dynamicIslandNotifications: LiveData<List<DynamicIslandNotification>> = _dynamicIslandNotifications

    private val _dynamicIslandExpanded = MutableLiveData<Boolean>(false)
    val dynamicIslandExpanded: LiveData<Boolean> = _dynamicIslandExpanded

    private val _mediaSession = MutableLiveData<MediaSessionInfo?>(null)
    val mediaSession: LiveData<MediaSessionInfo?> = _mediaSession

    private val _batteryInfo = MutableLiveData<BatteryInfo>(BatteryInfo())
    val batteryInfo: LiveData<BatteryInfo> = _batteryInfo

    // Control Center state
    private val _controlCenterState = MutableLiveData<ControlCenterState>(ControlCenterState.HIDDEN)
    val controlCenterState: LiveData<ControlCenterState> = _controlCenterState

    private val _controlCenterSettings = MutableLiveData<ControlCenterSettings>(ControlCenterSettings())
    val controlCenterSettings: LiveData<ControlCenterSettings> = _controlCenterSettings

    // Notifications state
    private val _systemNotifications = MutableLiveData<List<DynamicIslandNotification>>(emptyList())
    val systemNotifications: LiveData<List<DynamicIslandNotification>> = _systemNotifications

    // Gesture state
    private val _screenLocked = MutableLiveData<Boolean>(false)
    val screenLocked: LiveData<Boolean> = _screenLocked

    // UI State
    private val _showDock = MutableLiveData<Boolean>(true)
    val showDock: LiveData<Boolean> = _showDock

    // Home screen actions
    fun setHomeScreenItems(items: List<HomeScreenItem>) {
        _homeScreenItems.value = items
    }

    fun setCurrentPage(page: Int) {
        _currentPage.value = page
    }

    fun toggleAppDrawer() {
        _appDrawerVisible.value = !(_appDrawerVisible.value ?: false)
    }

    fun setAppDrawerVisible(visible: Boolean) {
        _appDrawerVisible.value = visible
    }

    fun setSearchQuery(query: String) {
        _appDrawerSearchQuery.value = query
    }

    // Dynamic Island actions
    fun addDynamicIslandNotification(notification: DynamicIslandNotification) {
        val current = _dynamicIslandNotifications.value ?: emptyList()
        _dynamicIslandNotifications.value = current + notification
    }

    fun removeDynamicIslandNotification(id: String) {
        _dynamicIslandNotifications.value = _dynamicIslandNotifications.value?.filter { it.id != id }
    }

    fun toggleDynamicIslandExpanded() {
        _dynamicIslandExpanded.value = !(_dynamicIslandExpanded.value ?: false)
    }

    fun setDynamicIslandExpanded(expanded: Boolean) {
        _dynamicIslandExpanded.value = expanded
    }

    fun updateMediaSession(session: MediaSessionInfo?) {
        _mediaSession.value = session
    }

    fun updateBatteryInfo(info: BatteryInfo) {
        _batteryInfo.value = info
    }

    // Control Center actions
    fun setControlCenterState(state: ControlCenterState) {
        _controlCenterState.value = state
    }

    fun toggleWifi(enabled: Boolean) {
        val current = _controlCenterSettings.value ?: ControlCenterSettings()
        _controlCenterSettings.value = current.copy(wifiEnabled = enabled)
    }

    fun toggleBluetooth(enabled: Boolean) {
        val current = _controlCenterSettings.value ?: ControlCenterSettings()
        _controlCenterSettings.value = current.copy(bluetoothEnabled = enabled)
    }

    fun toggleFlashlight(enabled: Boolean) {
        val current = _controlCenterSettings.value ?: ControlCenterSettings()
        _controlCenterSettings.value = current.copy(flashlightEnabled = enabled)
    }

    fun setBrightness(value: Int) {
        val current = _controlCenterSettings.value ?: ControlCenterSettings()
        _controlCenterSettings.value = current.copy(brightness = value)
    }

    fun setVolume(value: Int) {
        val current = _controlCenterSettings.value ?: ControlCenterSettings()
        _controlCenterSettings.value = current.copy(volume = value)
    }

    fun updateMediaInfo(info: MediaSessionInfo?) {
        val current = _controlCenterSettings.value ?: ControlCenterSettings()
        _controlCenterSettings.value = current.copy(mediaInfo = info)
    }

    // Notifications
    fun addSystemNotification(notification: DynamicIslandNotification) {
        val current = _systemNotifications.value ?: emptyList()
        _systemNotifications.value = current + notification
    }

    fun removeSystemNotification(id: String) {
        _systemNotifications.value = _systemNotifications.value?.filter { it.id != id }
    }

    // Gesture actions
    fun lockScreen() {
        _screenLocked.value = true
    }

    fun unlockScreen() {
        _screenLocked.value = false
    }

    fun setShowDock(show: Boolean) {
        _showDock.value = show
    }

    fun resetToDefaults() {
        _homeScreenItems.value = emptyList()
        _currentPage.value = 0
        _appDrawerVisible.value = false
        _appDrawerSearchQuery.value = ""
        _dynamicIslandNotifications.value = emptyList()
        _dynamicIslandExpanded.value = false
        _mediaSession.value = null
        _batteryInfo.value = BatteryInfo()
        _controlCenterState.value = ControlCenterState.HIDDEN
        _controlCenterSettings.value = ControlCenterSettings()
        _systemNotifications.value = emptyList()
        _screenLocked.value = false
        _showDock.value = true
    }
}
