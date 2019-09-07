package maxeem.america.mars.misc

import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import maxeem.america.mars.app

fun LifecycleOwner.delayed(delay: Long, stateAtLeast: Lifecycle.State = Lifecycle.State.CREATED,
                           token: Any? = null, code: (owner: LifecycleOwner)->Unit) {
    app.handler.postDelayed(delay, token) {
        doOnLifecycle(stateAtLeast, code = code)
    }
}
fun LifecycleOwner.doOnLifecycle(stateAtLeast: Lifecycle.State = Lifecycle.State.CREATED, code: (owner: LifecycleOwner)->Unit) {
    if (lifecycle.currentState.isAtLeast(stateAtLeast))
        code(this)
}

fun Fragment.delayed(delay: Long, stateAtLeast: Lifecycle.State = Lifecycle.State.CREATED, token: Any? = null, code: ()->Unit) {
    app.handler.postDelayed(delay, token) {
        if (!(isDetached || isRemoving) && lifecycle.currentState.isAtLeast(stateAtLeast))
            code()
    }
}

fun AppCompatActivity.delayed(delay: Long, stateAtLeast: Lifecycle.State = Lifecycle.State.CREATED, token: Any? = null, code: ()->Unit) {
    app.handler.postDelayed(delay, token) {
        if (!(isFinishing || isDestroyed) && lifecycle.currentState.isAtLeast(stateAtLeast))
            code()
    }
}

