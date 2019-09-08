package maxeem.america.mars.misc

import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import maxeem.america.mars.app

/**
 * Extensions
 */

val Any.hash get() = hashCode()

fun Int.asString() = app.getString(this)
fun Int.asString(vararg args: Any) = app.getString(this, *args)
fun Int.asQuantityString(quantity: Int, vararg args: Any) = app.resources.getQuantityString(this, quantity, *args)
fun Int.asDrawable() = app.getDrawable(this)

fun String.fromHtml() = Util.fromHtml(this)

fun Fragment.compatActivity() = activity as AppCompatActivity?

fun Fragment.materialAlert(@StringRes msg: Int, code: (MaterialAlertDialogBuilder.()->Unit)? = null)
        = materialAlert(app.getString(msg), code)
fun Fragment.materialAlert(msg: CharSequence, code: (MaterialAlertDialogBuilder.()->Unit)? = null)
        = MaterialAlertDialogBuilder(context).apply {
            setMessage(msg)
            code?.invoke(this)
        }.show()

fun View.onClick(l: ()->Unit) = setOnClickListener { l() }

fun <T> MutableLiveData<T>.asImmutable() = this as LiveData<T>
fun <T> LiveData<T>.asMutable()          = this as MutableLiveData<T>
