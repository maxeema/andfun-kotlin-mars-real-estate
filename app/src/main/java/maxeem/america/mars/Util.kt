package maxeem.america.mars

import androidx.annotation.MenuRes
import maxeem.america.mars.api.MarsApiService

object Util {

    fun filterToTab(filter: MarsApiService.Filter) = when(filter) {
        MarsApiService.Filter.BUY -> R.id.tab_sale
        else -> R.id.tab_rent
    }

    fun tabToFilter(@MenuRes tab: Int) = when(tab) {
        R.id.tab_sale -> MarsApiService.Filter.BUY
        else -> MarsApiService.Filter.RENT
    }

}
