# GlassOS Launcher - Architecture Documentation

## Overview

GlassOS Launcher is built with a modern Android architecture using:
- **Kotlin** for type-safe code
- **Jetpack Compose** for UI
- **MVVM** (Model-View-ViewModel) pattern
- **LiveData** for state management
- **Coroutines** for async operations

## Architecture Layers

### 1. Presentation Layer (UI)

Located in: `/app/src/main/java/com/glassos/launcher/`

#### Main Composables

- **LauncherActivity**: The root Activity that hosts all Compose content
- **LauncherScreen**: Main Compose function orchestrating all UI
- **HomeScreenWorkspace**: Grid-based home screen with horizontal paging
- **AppDrawer**: Full-screen app grid with search
- **DynamicIsland**: Floating panel at the top
- **ControlCenter**: Quick settings panel
- **Widgets**: Clock, Weather, Battery, Music widgets

#### Component Structure

```
ui/
├── LauncherActivity.kt          # Activity + Main Composable
components/
├── GlassUIComponents.kt         # Glass morphism design system
├── Widgets.kt                   # Built-in widgets
├── DynamicIsland.kt            # System information display
├── ControlCenter.kt            # Quick settings
├── AppDrawer.kt                # App grid with search
└── HomeScreen.kt               # Home screen workspace
```

#### Design System

The glass UI system provides reusable components with morphism effects:

```kotlin
// Base component with blur effect
GlassSurface(
    cornerRadius = 24f,
    backgroundColor = Color.White.copy(alpha = 0.1f)
) { 
    // Content
}

// Specialized variants
GlassCard()      // Smaller glass container
GlassDock()      // Bottom dock area
GlassButton()    // Interactive element
GlassSlider()    // Control slider
```

### 2. ViewModel Layer

Located in: `/app/src/main/java/com/glassos/launcher/managers/`

#### LauncherStateManager

Central state management class inheriting from `ViewModel`:

```kotlin
class LauncherStateManager : ViewModel() {
    // Home screen
    private val _homeScreenItems: MutableLiveData<List<HomeScreenItem>>
    private val _currentPage: MutableLiveData<Int>
    
    // App drawer
    private val _appDrawerVisible: MutableLiveData<Boolean>
    private val _appDrawerSearchQuery: MutableLiveData<String>
    
    // Dynamic Island
    private val _dynamicIslandNotifications: MutableLiveData<...>
    private val _dynamicIslandExpanded: MutableLiveData<Boolean>
    
    // Control Center
    private val _controlCenterState: MutableLiveData<ControlCenterState>
    private val _controlCenterSettings: MutableLiveData<ControlCenterSettings>
    
    // ... and more state properties
}
```

**Key Methods:**
- State update methods: `setHomeScreenItems()`, `setCurrentPage()`, `toggleAppDrawer()`, etc.
- Action methods: `lockScreen()`, `toggleWifi()`, `setBrightness()`, etc.
- Reset: `resetToDefaults()`

#### AppManager

Manages installed apps:

```kotlin
class AppManager(context: Context) {
    // Load apps asynchronously
    fun loadInstalledApps()
    
    // Filter and search
    fun getAppsByName(query: String): List<AppInfo>
    fun getAllApps(): List<AppInfo>
    
    // App info retrieval
    fun getAppIcon(packageName: String): Drawable?
    fun getAppLabel(packageName: String): String
    fun canLaunchApp(packageName: String): Boolean
}
```

### 3. Data Layer

Located in: `/app/src/main/java/com/glassos/launcher/models/`

#### Data Models

```kotlin
// App and Home Screen
data class AppInfo(...)              // Single installed app
data class HomeScreenItem(...)       // Item on home screen
data class GridConfig(...)           // Grid settings

// Dynamic Island & System
data class DynamicIslandNotification(...)  // Notification
data class MediaSessionInfo(...)          // Media playback
data class BatteryInfo(...)               // Battery status

// Control Center
enum class ControlCenterState { COLLAPSED, EXPANDED, HIDDEN }
data class ControlCenterSettings(...)     // Settings state

// System Info
data class WeatherInfo(...)  // Weather data
```

### 4. Theme Layer

Located in: `/app/src/main/java/com/glassos/launcher/theme/`

#### Theme Customization

- **Color.kt**: Color palette definition
- **Theme.kt**: Material3 theme configuration
- **Typography.kt**: Text styles

```kotlin
// Dark theme colors
val DarkBackground = Color(0xFF000000)
val SurfaceColor = Color(0xFF1F1F1F)
val GlassWhite = Color(0x80FFFFFF)
val GlassDark = Color(0x1F000000)
```

## Data Flow

### State Management Flow

```
User Action
    ↓
Composable Event Handler
    ↓
LauncherStateManager.updateState()
    ↓
LiveData.postValue()
    ↓
Composable recomoses (via observeAsState())
    ↓
UI Updated
```

### App Loading Flow

```
LauncherActivity.onCreate()
    ↓
AppManager.loadInstalledApps()
    ↓
PackageManager.getInstalledPackages()
    ↓
Filter & Sort Apps
    ↓
_installedApps.postValue(appList)
    ↓
AppDrawer observes & displays
```

## Component Integration

### Home Screen Workspace

Displays apps in a 4x4 grid across multiple pages:

```kotlin
HomeScreenWorkspace(
    gridConfig = GridConfig(),           // Grid size
    items = homeScreenItems,             // App items
    currentPage = currentPage,           // Current page
    onPageChange = { ... },              // Page scroll handler
    onItemClick = { app -> launch(app) } // Launch app
)
```

### App Drawer

Full-screen overlay for browsing all apps:

```kotlin
AppDrawer(
    visible = appDrawerVisible,
    apps = installedApps,
    searchQuery = searchQuery,
    onSearchQueryChange = { query -> ... },
    onAppClick = { app -> launch(app) },
    onDismiss = { ... }
)
```

### Dynamic Island

Floating component showing system info:

```kotlin
DynamicIsland(
    expanded = dynamicIslandExpanded,
    batteryInfo = batteryInfo,
    mediaInfo = mediaSession,
    onToggleExpanded = { ... }
)
```

### Control Center

Quick settingspanel accessed from top-right:

```kotlin
ControlCenter(
    state = controlCenterState,
    settings = controlCenterSettings,
    onToggleWifi = { enabled -> ... },
    onBrightnessChange = { value -> ... },
    // ... more handlers
)
```

## State Observing Pattern

All Composables observe LiveData using `observeAsState()`:

```kotlin
@Composable
fun LauncherScreen(stateManager: LauncherStateManager) {
    // Observe all state
    val appDrawerVisible by stateManager.appDrawerVisible.observeAsState(false)
    val currentPage by stateManager.currentPage.observeAsState(0)
    val installedApps by appManager.installedApps.observeAsState(emptyList())
    
    // Composables automatically recompose on state change
    Box {
        if (appDrawerVisible) {
            AppDrawer(...)
        }
    }
}
```

## Gesture System

Gestures are handled via `PointerInput`:

```kotlin
Box(..., modifier = Modifier
    .pointerInput(Unit) {
        detectTapGestures(
            onDoubleTap = { stateManager.lockScreen() },
            onTap = { /* dismiss overlays */ }
        )
    }
)
```

### Gesture Actions
- **Swipe Up**: Open App Drawer
- **Double Tap**: Lock Screen
- **Tap on Dynamic Island**: Expand/Collapse
- **Long Press App**: Show options (framework ready)

## Performance Optimization

### Recomposition Prevention

1. **State Scoping**: Only necessary state is exposed
2. **Remember Blocks**: Expensive calculations cached
3. **Key Blocks**: Stable list rendering with keys
4. **Async Loading**: Apps loaded off main thread

### Memory Management

1. **Coroutines**: Async operations with proper scoping
2. **LiveData**: Automatic cleanup on Activity destruction
3. **Drawable Caching**: Icon loading optimization
4. **Database**: (Future) App state persistence

## Extension Points

### Adding New Widgets

1. Create new @Composable in `components/Widgets.kt`
2. Add data model in `models/Models.kt`
3. Add state to `LauncherStateManager`
4. Integrate into home screen

### Adding New Gestures

1. Add handler in `detectDragGestures` or `PointerInput`
2. Add action method to `LauncherStateManager`
3. Update UI response

### Customizing Glass Effects

1. Modify `RenderEffect.createBlurEffect()` parameters
2. Adjust `Color.White.copy(alpha = ...)` transparency
3. Change border radius in `RoundedCornerShape()`

## Thread Safety

- **Main Thread**: All UI operations via Compose
- **Default Thread**: App loading via Coroutines
- **LiveData**: Thread-safe state posting via `postValue()`

## Lifecycle

### Activity Lifecycle

```
onCreate()
    └── Initialize AppManager
    └── Initialize LauncherStateManager
    └── Set up Compose content
    
onStart()
    └── Begin observing LiveData
    
onResume()
    └── Update system state
    
onPause()
    └── Save state
    
onDestroy()
    └── Cleanup (automatic via ViewModel)
```

### ViewModel Lifecycle

- Created when Activity created
- Persists across configuration changes
- Automatically cleared when Activity destroyed

## Future Architecture Enhancements

- [ ] Room database for app preferences
- [ ] Datastore for persistent settings
- [ ] Remote data source for weather API
- [ ] Media session notifications
- [ ] Navigation Component integration
- [ ] Dependency Injection (Hilt)
- [ ] Repository pattern for data access
- [ ] usecase/interactor layer integration

## Testing Architecture

### Unit Tests
- Test `LauncherStateManager` logic
- Test `AppManager` filtering
- Test model data classes

### UI Tests
- Test Compose components
- Test gesture handling
- Test state observers

### Integration Tests
- Test app launching
- Test state propagation
- Test full user flows

---

**Architecture designed for maintainability, scalability, and performance**
