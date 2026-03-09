package com.glassos.launcher.managers

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.glassos.launcher.models.AppInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppManager(private val context: Context) {
    private val packageManager: PackageManager = context.packageManager
    
    private val _installedApps = MutableLiveData<List<AppInfo>>(emptyList())
    val installedApps: LiveData<List<AppInfo>> = _installedApps

    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        loadInstalledApps()
    }

    private fun loadInstalledApps() {
        scope.launch {
            try {
                val packages = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    packageManager.getInstalledPackages(
                        PackageManager.PackageInfoFlags.of(0)
                    )
                } else {
                    @Suppress("DEPRECATION")
                    packageManager.getInstalledPackages(0)
                }

                val appList = mutableListOf<AppInfo>()

                for (packageInfo in packages) {
                    try {
                        val appInfo = packageInfo.applicationInfo
                        if (appInfo != null) {
                            val isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                            
                            val label = try {
                                packageManager.getApplicationLabel(appInfo).toString()
                            } catch (e: Exception) {
                                packageInfo.packageName
                            }

                            val icon = try {
                                packageManager.getApplicationIcon(appInfo)
                            } catch (e: Exception) {
                                packageManager.defaultActivityIcon
                            }

                            if (isSystemApp) {
                                // Skip system apps unless they're launchable
                                val intent = packageManager.getLaunchIntentForPackage(packageInfo.packageName)
                                if (intent != null) {
                                    appList.add(
                                        AppInfo(
                                            packageName = packageInfo.packageName,
                                            label = label,
                                            icon = icon,
                                            isSystemApp = true
                                        )
                                    )
                                }
                            } else {
                                appList.add(
                                    AppInfo(
                                        packageName = packageInfo.packageName,
                                        label = label,
                                        icon = icon,
                                        isSystemApp = false
                                    )
                                )
                            }
                        }
                    } catch (e: Exception) {
                        // Skip apps that fail to load
                    }
                }

                // Sort alphabetically
                appList.sortBy { it.label }
                _installedApps.postValue(appList)
            } catch (e: Exception) {
                _installedApps.postValue(emptyList())
            }
        }
    }

    fun getAppsByName(query: String): List<AppInfo> {
        return _installedApps.value?.filter {
            it.label.contains(query, ignoreCase = true)
        } ?: emptyList()
    }

    fun getAllApps(): List<AppInfo> {
        return _installedApps.value ?: emptyList()
    }

    fun getAppIcon(packageName: String): Drawable? {
        return try {
            packageManager.getApplicationIcon(packageName)
        } catch (e: Exception) {
            null
        }
    }

    fun getAppLabel(packageName: String): String {
        return try {
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            packageName
        }
    }

    fun canLaunchApp(packageName: String): Boolean {
        return packageManager.getLaunchIntentForPackage(packageName) != null
    }
}
