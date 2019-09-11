/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy factoryOf the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package maxeem.america.mars.misc

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.TooltipCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import maxeem.america.mars.R
import maxeem.america.mars.adapter.PhotoGridAdapter
import maxeem.america.mars.api.MarsProperty
import maxeem.america.mars.app
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.info
import org.jetbrains.anko.withAlpha

@BindingAdapter("visibleOn")
fun View.visibleOn(condition: Boolean?) {
    visibility = if (condition == true) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("textHtml")
fun TextView.textHtml(str: String) {
    text = str.fromHtml()
}

@BindingAdapter("srcOf")
fun ImageView.srcOf(prop: MarsProperty?) = prop?.also {
    app.info("srcOf ${prop.imgSrcUrl}")
    Glide.with(context)
        .load(prop.imgSrcUrl.toUri().buildUpon().scheme("https").build())
            .listener(object: RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean) = false
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    if (resource is BitmapDrawable) // make Mars pics more Mars
                        resource.colorFilter = PorterDuffColorFilter(Color.RED.withAlpha(0x22), PorterDuff.Mode.DARKEN)
                    return false
                }
            })
        .apply(RequestOptions()
            .placeholder(R.drawable.ic_loading_icon)
            .error(R.drawable.ic_broken_image))
    .into(this)
}

@BindingAdapter("dataOf")
fun RecyclerView.dataOf(data: List<MarsProperty>?) = data?.also {
    (adapter as PhotoGridAdapter).submitList(data)
}

@BindingAdapter("tooltipCompat")
fun View.tooltipCompat(str: String) {
    TooltipCompat.setTooltipText(this, str)
}

@BindingAdapter("onClickNotImplemented")
fun View.onClickNotImplemented(msg: CharSequence?) {
    onClick {
        longSnackbar(R.string.not_implemented).apply {
            anchorView = this@onClickNotImplemented
        }
    }
}
