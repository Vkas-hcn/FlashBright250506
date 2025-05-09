package com.dead.ship.even.sank.flashbright250506.ui.home

import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.dead.ship.even.sank.flashbright250506.R
import com.dead.ship.even.sank.flashbright250506.databinding.ActivityMainBinding
import com.dead.ship.even.sank.flashbright250506.databinding.FragmentHomeBinding
import com.dead.ship.even.sank.flashbright250506.ui2.LightActivity
import com.dead.ship.even.sank.flashbright250506.ui2.MainActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var isFlashOn = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initView()
        return root
    }

    private fun initView(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    if (requireActivity() is MainActivity) {
                        (requireActivity() as MainActivity).onBackPressedFun()
                    }
                }
            }
        })
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        binding.homeState = true
        val drawerLayout: DrawerLayout = binding.drawerLayout
        binding.imgMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.customDrawer.setOnClickListener { }

        binding.imgFlash.setOnClickListener {
            toggleFlashlight()
        }
        binding.imgTO.setOnClickListener {
            toggleFlashlight()
        }

        binding.tvShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=${requireActivity().packageName}"
            )
            try {
                startActivity(Intent.createChooser(intent, "Share via"))
            } catch (ex: Exception) {
                Toast.makeText(requireActivity(), "分享失败", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvPP.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://play.google.com")
            startActivity(intent)
        }
        binding.imgHome.setOnClickListener {
            binding.homeState = true
        }
        binding.imgLight.setOnClickListener {
            binding.homeState = false
        }
        binding.imgLightBut.setOnClickListener {
            startActivity(Intent(requireActivity(), LightActivity::class.java))
        }
    }
    /**
     * 切换闪光灯状态
     */
    private fun toggleFlashlight() {
        val cameraManager = requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val cameraId = cameraManager.cameraIdList[0] // 默认后置摄像头
            if (isFlashOn) {
                cameraManager.setTorchMode(cameraId, false) // 关闭闪光灯
                binding.imgFlash.setImageResource(R.drawable.icon_y) // 恢复原图
                binding.imgTO.setImageResource(R.drawable.icon_t_o) // 恢复原图
            } else {
                cameraManager.setTorchMode(cameraId, true) // 打开闪光灯
                binding.imgFlash.setImageResource(R.drawable.icon_tun) // 替换为“开”的图标
                binding.imgTO.setImageResource(R.drawable.icon_on) // 替换为“开”的图标
            }
            isFlashOn = !isFlashOn // 更新状态
        } catch (e: Exception) {
            Toast.makeText(requireActivity(), "The flash cannot be controlled, please check the permissions or device support", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}