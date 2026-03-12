package com.phonetruth.detector

import android.Manifest
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class AppInfo(
    val name: String,
    val packageName: String,
    val isHidden: Boolean,
    val hasDeviceAdmin: Boolean,
    val hasAccessibility: Boolean,
    val dangerousPermissions: List<String>
)

class MainActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var statusText: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        statusText = findViewById(R.id.statusText)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        scanApps()
    }
    
    private fun scanApps() {
        statusText.text = "Scanning..."
        
        Thread {
            val apps = getAllApps()
            runOnUiThread {
                statusText.text = "Found ${apps.size} apps (${apps.count { it.isHidden }} hidden)"
                recyclerView.adapter = AppAdapter(apps)
            }
        }.start()
    }
    
    private fun getAllApps(): List<AppInfo> {
        val pm = packageManager
        val installedApps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        val devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val enabledAccessibilityServices = getEnabledAccessibilityServices()
        
        return installedApps.map { appInfo ->
            val appName = try {
                pm.getApplicationLabel(appInfo).toString()
            } catch (e: Exception) {
                appInfo.packageName
            }
            
            val isHidden = isAppHidden(appInfo)
            val hasDeviceAdmin = isDeviceAdmin(devicePolicyManager, appInfo.packageName)
            val hasAccessibility = enabledAccessibilityServices.contains(appInfo.packageName)
            val dangerousPerms = getDangerousPermissions(appInfo.packageName)
            
            AppInfo(
                name = appName,
                packageName = appInfo.packageName,
                isHidden = isHidden,
                hasDeviceAdmin = hasDeviceAdmin,
                hasAccessibility = hasAccessibility,
                dangerousPermissions = dangerousPerms
            )
        }.sortedByDescending { 
            // Sort by suspiciousness
            (if (it.isHidden) 1000 else 0) +
            (if (it.hasDeviceAdmin) 500 else 0) +
            (if (it.hasAccessibility) 500 else 0) +
            (it.dangerousPermissions.size * 10)
        }
    }
    
    private fun isAppHidden(appInfo: ApplicationInfo): Boolean {
        val intent = packageManager.getLaunchIntentForPackage(appInfo.packageName)
        return intent == null && (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0
    }
    
    private fun isDeviceAdmin(dpm: DevicePolicyManager, packageName: String): Boolean {
        return dpm.activeAdmins?.any { 
            it.packageName == packageName 
        } ?: false
    }
    
    private fun getEnabledAccessibilityServices(): Set<String> {
        val enabledServices = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return emptySet()
        
        return enabledServices.split(':')
            .map { it.substringBefore('/') }
            .toSet()
    }
    
    private fun getDangerousPermissions(packageName: String): List<String> {
        val dangerousPermissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.GET_ACCOUNTS
        )
        
        return try {
            val packageInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_PERMISSIONS
            )
            
            packageInfo.requestedPermissions?.filter { permission ->
                dangerousPermissions.contains(permission) &&
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}

class AppAdapter(private val apps: List<AppInfo>) : RecyclerView.Adapter<AppAdapter.ViewHolder>() {
    
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.appName)
        val packageText: TextView = view.findViewById(R.id.packageName)
        val warningsText: TextView = view.findViewById(R.id.warnings)
        val permissionsText: TextView = view.findViewById(R.id.permissions)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val app = apps[position]
        
        holder.nameText.text = app.name
        holder.packageText.text = app.packageName
        
        val warnings = mutableListOf<String>()
        if (app.isHidden) warnings.add("⚠️ HIDDEN APP (no launcher icon)")
        if (app.hasDeviceAdmin) warnings.add("🔴 DEVICE ADMIN ENABLED")
        if (app.hasAccessibility) warnings.add("🔴 ACCESSIBILITY SERVICE ACTIVE")
        
        holder.warningsText.text = warnings.joinToString("\n")
        holder.warningsText.visibility = if (warnings.isEmpty()) View.GONE else View.VISIBLE
        
        if (app.dangerousPermissions.isNotEmpty()) {
            val permText = "Permissions: " + app.dangerousPermissions.joinToString(", ") {
                it.substringAfterLast('.')
            }
            holder.permissionsText.text = permText
            holder.permissionsText.visibility = View.VISIBLE
        } else {
            holder.permissionsText.visibility = View.GONE
        }
    }
    
    override fun getItemCount() = apps.size
}
