package maxeem.america.mars.model

import androidx.lifecycle.ViewModel
import maxeem.america.mars.misc.hash
import maxeem.america.mars.misc.timeMillis
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

open class BaseModel : ViewModel(), AnkoLogger {

    protected var isCleared = false

    override fun onCleared() {
        super.onCleared()
        info("$hash $timeMillis onCleared")
        isCleared = true
    }

}