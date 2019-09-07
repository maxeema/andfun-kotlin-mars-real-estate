package maxeem.america.mars.misc

import androidx.core.content.edit
import maxeem.america.mars.api.MarsApiService
import maxeem.america.mars.app
import org.jetbrains.anko.defaultSharedPreferences

object Conf {

    private const val KEY_FILTER = "filter"

    private val prefs by lazy { app.defaultSharedPreferences }

    var filter: MarsApiService.Filter
        get() = MarsApiService.Filter.of(prefs.getString(KEY_FILTER, maxeem.america.mars.misc.MARS_API_DEF_FILTER.value)!!)
        set(filter) {
            prefs.edit { putString(KEY_FILTER, filter.value) }
        }

}
