package com.dead.ship.even.sank.flashbright250506.ui.launch


import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.dead.ship.even.sank.flashbright250506.R

class DialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.in_dialog_3)

        // 设置 Activity 为对话框样式
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setGravity(Gravity.BOTTOM) // 显示在底部
        window.setBackgroundDrawableResource(android.R.color.transparent) // 背景透明

        // 点击空白区域关闭 Activity
        findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.con_dialog_3)
            .setOnClickListener {
                finish()
            }
    }
}
