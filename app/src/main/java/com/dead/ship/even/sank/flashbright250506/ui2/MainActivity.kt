package com.dead.ship.even.sank.flashbright250506.ui2

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View.OnTouchListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.dead.ship.even.sank.flashbright250506.R
import com.dead.ship.even.sank.flashbright250506.databinding.ActivityMainBinding
import com.dead.ship.even.sank.flashbright250506.ui.launch.DialogActivity
import com.dead.ship.even.sank.flashbright250506.ui2.App.Companion.localStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


interface ViewPager2Provider {
    fun getViewPager2(): ViewPager2
}

class MainActivity : AppCompatActivity(), ViewPager2Provider {
    private lateinit var binding: ActivityMainBinding
    private var homeButtonCallbackId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = ViewPagerAdapter(this)
        binding.viewPager2.adapter = adapter
        binding.inDialog1.conDialog1.setOnClickListener { }
        binding.inDialog2.conDialog2.setOnClickListener { }
        binding.inDialog1.tvContinue.setOnClickListener {
            openHomeScreenSettings()
        }
        binding.inDialog2.tvSetDefault.setOnClickListener {
            openHomeScreenSettings()
        }
        binding.inDialog2.tvIgnore.setOnClickListener {
            binding.inDialog2.conDialog2.isVisible = false
        }
        binding.viewPager2.setOnTouchListener { v, event ->
            // 返回 true 表示事件已处理，阻止子视图处理
            true
        }
        homeButtonCallbackId = ReliableHomeButtonDetection.registerHomeButtonCallback {
            // Home 按键被按下时执行的代码
            Log.e("HomeButton", "检测到 Home 按键被按下")
            scrollToPage(1)
        }
    }

    override fun onDestroy() {
        // 当 Activity 销毁时取消注册回调
        if (homeButtonCallbackId >= 0) {
            ReliableHomeButtonDetection.unregisterHomeButtonCallback(homeButtonCallbackId)
        }
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        setDefaultLauncher()
    }

    fun onBackPressedFun() {
        val currentPage = binding.viewPager2.currentItem

        when {
            binding.inDialog1.conDialog1.isVisible -> {
                binding.inDialog1.conDialog1.isVisible = false
                setDefaultLauncher()
            }

            binding.inDialog2.conDialog2.isVisible -> {
                binding.inDialog2.conDialog2.isVisible = false
            }

            currentPage == 0 -> {
                scrollToPage(1)
            }

            else -> {
            }
        }
    }

    private fun scrollToPage(position: Int) {
        if (isDefaultLauncher(this)) {
            binding.viewPager2.setCurrentItem(position, true)
        }
    }

    private fun enableSwipe(enable: Boolean) {
        binding.viewPager2.isUserInputEnabled = enable
    }

    override fun getViewPager2(): ViewPager2 {
        return binding.root.findViewById(R.id.viewPager2)
    }

    private fun setDefaultLauncher() {
        if (isDefaultLauncher(this)) {
            enableSwipe(true)
            binding.inDialog1.conDialog1.isVisible = false
            binding.inDialog2.conDialog2.isVisible = false
        } else {
            enableSwipe(false)
            if (localStorage.getBoolean(App.dialogState, false)) {
                binding.inDialog2.conDialog2.isVisible = true
                binding.inDialog1.conDialog1.isVisible = false
            } else {
                binding.inDialog1.conDialog1.isVisible = true
                binding.inDialog2.conDialog2.isVisible = false
                localStorage.saveBoolean(App.dialogState, true)
            }

        }
    }

    private fun isDefaultLauncher(context: Context): Boolean {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        val packageManager = context.packageManager
        val resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (resolveInfo != null) {
            val defaultLauncherPackage = resolveInfo.activityInfo.packageName
            val currentPackage = context.packageName
            return defaultLauncherPackage == currentPackage
        }
        return false
    }

    private fun openHomeScreenSettings() {
        val intent = Intent()
        intent.setAction("android.settings.HOME_SETTINGS")
        if (this.packageManager.queryIntentActivities(intent, 0).size > 0) {
            this.startActivity(intent)
            openSystemSettings()
        } else {
            Toast.makeText(this, "The settings page cannot be opened", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openSystemSettings() {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(500)
            val intent = Intent(this@MainActivity, DialogActivity::class.java)
            startActivity(intent)
        }
    }

}
