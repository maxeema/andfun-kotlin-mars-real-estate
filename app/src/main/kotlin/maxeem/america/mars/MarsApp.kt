package maxeem.america.mars

import android.app.Application
import android.os.Handler
import maxeem.america.mars.api.MarsApi
import org.jetbrains.anko.AnkoLogger
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

val app = MarsApp.instance

private val appModule = module {
    single { MarsApi.service }
}

class MarsApp : Application(), AnkoLogger {

    companion object {
        private var initializer : (()-> MarsApp)? = null
        val instance by lazy { requireNotNull(initializer).apply{ initializer = null }()  }
        @JvmStatic
        val VERSION by lazy { app.packageInfo.versionName }
    }

    init { initializer = { this } }

    val handler by lazy { Handler(app.mainLooper) }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(); androidContext(this@MarsApp)
            modules(appModule)
        }
    }

}

val MarsApp.packageInfo
    get() = packageManager.getPackageInfo(packageName, 0)

