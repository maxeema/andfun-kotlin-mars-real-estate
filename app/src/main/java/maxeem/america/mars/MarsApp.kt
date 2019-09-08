package maxeem.america.mars

import android.app.Application
import android.os.Handler
import org.jetbrains.anko.AnkoLogger

val app = MarsApp.instance

class MarsApp : Application(), AnkoLogger {

    companion object {
        private var initializer : (()-> MarsApp)? = null
        val instance by lazy { requireNotNull(initializer).apply{ initializer = null }()  }
        @JvmStatic
        val BUILD by lazy { app.packageInfo.versionName.substringAfter('-').toUpperCase() }
    }

    init { initializer = { this } }

    val handler by lazy { Handler(app.mainLooper) }
}

val MarsApp.packageInfo
    get() = packageManager.getPackageInfo(packageName, 0)

