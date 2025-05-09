package com.dead.ship.even.sank.flashbright250506.ui.launch

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LaunchViewModel : ViewModel() {

    fun uninstallApp(mContext: Context, pkgName:String) {
        runCatching {
            val intent = Intent(Intent.ACTION_DELETE)
            intent.setData(Uri.parse("package:$pkgName"))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            mContext.startActivity(intent)
        }.onFailure { it.printStackTrace() }
    }
}