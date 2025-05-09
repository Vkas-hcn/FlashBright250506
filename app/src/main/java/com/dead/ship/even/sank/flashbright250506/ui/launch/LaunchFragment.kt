package com.dead.ship.even.sank.flashbright250506.ui.launch

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dead.ship.even.sank.flashbright250506.databinding.FragmentLaunchBinding
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.dead.ship.even.sank.flashbright250506.ui2.ViewPager2Provider
import androidx.core.net.toUri

class LaunchFragment : Fragment() {

    private var _binding: FragmentLaunchBinding? = null
    private val binding get() = _binding!!
    private lateinit var appAdapter: AppListAdapter
    private val allApps = mutableListOf<AppInfo>()
    private val filteredApps = mutableListOf<AppInfo>()
    private var viewPager2: ViewPager2? = null
    private var isViewPager2Set = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLaunchBinding.inflate(inflater, container, false)
        val root = binding.root
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addDataScheme("package")
        }
        requireContext().registerReceiver(packageChangeReceiver, filter)
        setupRecyclerView()
        binding.etSearch.addTextChangedListener {
            filterApps(it.toString())
        }
        binding.flIcon.setOnClickListener {
            if (viewPager2 == null) {
                Log.e("LaunchFragment", "ViewPager2 is null!")
            } else {
                Log.d("LaunchFragment", "Switching to HomeFragment")
                viewPager2?.setCurrentItem(0, true)
            }
        }
        if (isViewPager2Set) {
            viewPager2?.let {
                setViewPager2(it)
            }
        }

        return root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ViewPager2Provider) {
            viewPager2 = context.getViewPager2()
        }
    }
    fun setViewPager2(viewPager2: ViewPager2) {
        this.viewPager2 = viewPager2
        isViewPager2Set = true
        Log.d("LaunchFragment", "ViewPager2 set successfully")
    }



    private fun setupRecyclerView() {
        appAdapter = AppListAdapter(filteredApps, onClick = { position ->
            launchApp(filteredApps[position])

        }, onLongClick = { position ->
            showAppOptionsDialog(filteredApps[position])
        })
        binding.rvAppList.layoutManager = GridLayoutManager(context, 4)
        binding.rvAppList.adapter = appAdapter
    }

    private fun loadInstalledApps() {
        val pm = requireContext().packageManager
        val context = requireContext()
        val mainIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val launcherPackages = pm.queryIntentActivities(mainIntent, PackageManager.MATCH_ALL)
            .map { it.activityInfo.packageName }
            .toSet()

        allApps.clear()
        pm.getInstalledApplications(PackageManager.MATCH_ALL).forEach { app ->
            if (app.packageName in launcherPackages && app.packageName != context.packageName) {
                allApps.add(AppInfo(
                    pm.getApplicationLabel(app).toString(),
                    pm.getApplicationIcon(app),
                    app.packageName
                ))
            }
        }
        allApps.sortBy { it.name }

        filteredApps.clear()
        filteredApps.addAll(allApps)
        appAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        loadInstalledApps()
    }

    private fun filterApps(query: String) {
        filteredApps.clear()
        if (query.isEmpty()) {
            filteredApps.addAll(allApps)
        } else {
            for (app in allApps) {
                if (app.name.contains(query, ignoreCase = true)) {
                    filteredApps.add(app)
                }
            }
        }
        appAdapter.notifyDataSetChanged()
    }

    private fun launchApp(appInfo: AppInfo) {
        val intent = requireContext().packageManager.getLaunchIntentForPackage(appInfo.packageName)
        if (intent != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "Unable to launch this app", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAppOptionsDialog(appInfo: AppInfo) {
        val options = arrayOf("Uninstall the app", "See the application details")
        AlertDialog.Builder(requireContext())
            .setTitle(appInfo.name)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> uninstallApp(appInfo.packageName)
                    1 -> openAppDetails(appInfo.packageName)
                }
            }
            .show()
    }

    private fun uninstallApp(packageName: String) {
        runCatching {
            val intent = Intent(Intent.ACTION_DELETE)
            val uri = Uri.parse("package:$packageName")
            intent.data = uri
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            requireContext().startActivity(intent)
        }.onFailure {
            it.printStackTrace()
        }
    }
    private val packageChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_PACKAGE_REMOVED) {
                loadInstalledApps()
            }
        }
    }

    private fun openAppDetails(packageName: String) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = "package:$packageName".toUri()
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


