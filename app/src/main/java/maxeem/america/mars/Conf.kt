package maxeem.america.mars

import androidx.core.content.edit
import maxeem.america.mars.api.MarsApiService
import org.jetbrains.anko.defaultSharedPreferences

object Conf {

    private const val KEY_FILTER = "filter"

    private val prefs by lazy { app.defaultSharedPreferences }

    var filter: MarsApiService.Filter
        get() = MarsApiService.Filter.of(prefs.getString(KEY_FILTER, MARS_API_DEF_FILTER.value)!!)
        set(filter) {
            prefs.edit(commit = true) { putString(KEY_FILTER, filter.value) }
        }

}
