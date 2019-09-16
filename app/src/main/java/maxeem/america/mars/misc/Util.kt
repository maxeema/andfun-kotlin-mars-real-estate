package maxeem.america.mars.misc

import androidx.annotation.MenuRes
import androidx.core.text.HtmlCompat
import maxeem.america.mars.R
import maxeem.america.mars.api.MarsApiService

object Util {

    @JvmStatic
    fun fromHtml(s: String) = HtmlCompat.fromHtml(s, HtmlCompat.FROM_HTML_MODE_COMPACT)

    fun filterToTab(filter: MarsApiService.Filter) = when(filter) {
        MarsApiService.Filter.BUY -> R.id.tab_sale
        MarsApiService.Filter.RENT -> R.id.tab_rent
        else -> R.id.tab_all
    }
    fun tabToFilter(@MenuRes tab: Int) = when(tab) {
        R.id.tab_sale -> MarsApiService.Filter.BUY
        R.id.tab_rent -> MarsApiService.Filter.RENT
        else -> MarsApiService.Filter.ALL
    }

}
