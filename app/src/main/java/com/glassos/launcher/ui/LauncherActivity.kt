package com.glassos.launcher.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.glassos.launcher.components.AppDrawer
import com.glassos.launcher.components.BatteryWidget
import com.glassos.launcher.components.ClockWidget
import com.glassos.launcher.components.ControlCenter
import com.glassos.launcher.components.DynamicIsland
import com.glassos.launcher.components.HomeScreenWorkspace
import com.glassos.launcher.components.MusicWidget
import com.glassos.launcher.components.WeatherWidget
import com.glassos.launcher.managers.AppManager
import com.glassos.launcher.managers.LauncherStateManager
import com.glassos.launcher.models.BatteryInfo
import com.glassos.launcher.models.ControlCenterState
import com.glassos.launcher.models.GridConfig
import com.glassos.launcher.models.HomeScreenItem
import com.glassos.launcher.theme.GlassOSTheme

class LauncherActivity : ComponentActivity() {
    private val stateManager: LauncherStateManager by viewModels()
    private lateinit var appManager: AppManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up system window flags
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        // Make launcher handle home button
        setDefaultKeyMode(DEFAULT_KEYS_DISABLE)

        // Initialize managers
        appManager = AppManager(this)

        // Set up initial home screen items
        initializeHomeScreen()

        setContent {
            GlassOSTheme {
                LauncherScreen(
                    stateManager = stateManager,
                    appManager = appManager,
                    onAppLaunch = { packageName -> launchApp(packageName) }
                )
            }
        }
    }

    private fun initializeHomeScreen() {
        // Load default home screen items
        val defaultItems = mutableListOf<HomeScreenItem>()
        
        // Add some default system apps
        val apps = listOf("com.android.chrome", "com.android.camera", "com.android.messaging")
        apps.forEach { pkg ->
            if (appManager.canLaunchApp(pkg)) {
                defaultItems.add(
                    HomeScreenItem(
                        id = pkg,
                        type = "app",
                        label = appManager.getAppLabel(pkg),
                        packageName = pkg,
                        gridX = defaultItems.size % 4,
                        gridY = defaultItems.size / 4
                    )
                )
            }
        }

        stateManager.setHomeScreenItems(defaultItems)
    }

    private fun launchApp(packageName: String) {
        try {
            val intent = packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        // Override back button to prevent exiting launcher
        super.onBackPressed()
    }
}

@Composable
private fun LauncherScreen(
    stateManager: LauncherStateManager,
    appManager: AppManager,
    onAppLaunch: (String) -> Unit
) {
    val context = LocalContext.current
    
    // Observe state
    val appDrawerVisible by stateManager.appDrawerVisible.observeAsState(false)
    val controlCenterState by stateManager.controlCenterState.observeAsState(ControlCenterState.HIDDEN)
    val searchQuery by stateManager.appDrawerSearchQuery.observeAsState("")
    val installedApps by appManager.installedApps.observeAsState(emptyList())
    val homeScreenItems by stateManager.homeScreenItems.observeAsState(emptyList())
    val currentPage by stateManager.currentPage.observeAsState(0)
    val gridConfig by stateManager.gridConfig.observeAsState(GridConfig())
    val mediaSession by stateManager.mediaSession.observeAsState()
    val batteryInfo by stateManager.batteryInfo.observeAsState(BatteryInfo())
    val controlCenterSettings by stateManager.controlCenterSettings.observeAsState()
    val screenLocked by stateManager.screenLocked.observeAsState(false)
    val showDock by stateManager.showDock.observeAsState(true)

    // Track gesture state
    val gestureState = remember { mutableStateOf("") }
    val lastTapTime = remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        // Simulate battery updates
        while (true) {
            kotlinx.coroutines.delay(5000)
            val newBattery = BatteryInfo(
                level = (50..100).random(),
                isCharging = Math.random() < 0.3
            )
            stateManager.updateBatteryInfo(newBattery)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        stateManager.lockScreen()
                    },
                    onTap = {
                        if (appDrawerVisible) {
                            stateManager.setAppDrawerVisible(false)
                        }
                        if (controlCenterState != ControlCenterState.HIDDEN) {
                            stateManager.setControlCenterState(ControlCenterState.HIDDEN)
                        }
                    }
                )
            }
    ) {
        // Main launcher content
        if (!screenLocked) {
            // Home screen
            HomeScreenWorkspace(
                gridConfig = gridConfig,
                items = homeScreenItems,
                currentPage = currentPage,
                onPageChange = { stateManager.setCurrentPage(it) },
                onItemClick = { item ->
                    if (item.packageName != null) {
                        onAppLaunch(item.packageName)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Static UI elements
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Dynamic Island at top
                DynamicIsland(
                    expanded = false,
                    batteryInfo = batteryInfo,
                    mediaInfo = mediaSession,
                    onToggleExpanded = { stateManager.toggleDynamicIslandExpanded() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            // Dock at bottom
            if (showDock) {
                DockView(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp),
                    apps = installedApps.take(4),
                    onAppClick = { onAppLaunch(it.packageName) }
                )
            }
        } else {
            // Lock screen placeholder
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Text(
                    text = "Locked",
                    color = Color.White
                )
            }
        }

        // App Drawer (overlays everything)
        AppDrawer(
            visible = appDrawerVisible,
            apps = installedApps,
            searchQuery = searchQuery,
            onSearchQueryChange = { stateManager.setSearchQuery(it) },
            onAppClick = { app ->
                stateManager.setAppDrawerVisible(false)
                onAppLaunch(app.packageName)
            },
            onDismiss = { stateManager.setAppDrawerVisible(false) },
            modifier = Modifier.fillMaxSize()
        )

        // Control Center (overlays everything)
        if (controlCenterState != ControlCenterState.HIDDEN) {
            ControlCenter(
                state = controlCenterState,
                settings = controlCenterSettings ?: com.glassos.launcher.models.ControlCenterSettings(),
                onToggleWifi = { stateManager.toggleWifi(it) },
                onToggleBluetooth = { stateManager.toggleBluetooth(it) },
                onToggleFlashlight = { stateManager.toggleFlashlight(it) },
                onBrightnessChange = { stateManager.setBrightness(it) },
                onVolumeChange = { stateManager.setVolume(it) },
                onClose = { stateManager.setControlCenterState(ControlCenterState.HIDDEN) },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Gesture handlers for swipe gestures
        LaunchedEffect(Unit) {
            // Handle app drawer swipe-up
            while (true) {
                kotlinx.coroutines.delay(100)
                // Gesture detection would be integrated with PointerInputScope
            }
        }
    }
}

@Composable
private fun DockView(
    modifier: Modifier = Modifier,
    apps: List<com.glassos.launcher.models.AppInfo>,
    onAppClick: (com.glassos.launcher.models.AppInfo) -> Unit
) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .background(Color.White.copy(alpha = 0.05f), shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
            .padding(8.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        apps.forEach { app ->
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White.copy(alpha = 0.1f), shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { onAppClick(app) })
                    },
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Text(
                    text = app.label.firstOrNull()?.toString() ?: "?",
                    color = Color.White
                )
            }
        }
    }
}
