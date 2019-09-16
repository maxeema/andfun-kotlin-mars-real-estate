package maxeem.america.mars.misc

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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

fun View.onClick(l: ()->Unit) = setOnClickListener { l() }
