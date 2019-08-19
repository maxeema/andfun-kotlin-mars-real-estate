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

package maxeem.america.mars.adapter

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import maxeem.america.mars.R
import maxeem.america.mars.api.MarsProperty

@BindingAdapter("visibleOn")
fun View.visibleOn(condition: Boolean?) {
    visibility = if (condition == true) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("srcOf")
fun ImageView.imageFrom(prop: MarsProperty?) = prop?.also {
    Glide.with(context)
        .load(prop.imgSrcUrl.toUri().buildUpon().scheme("https").build())
        .apply(RequestOptions()
            .placeholder(R.drawable.ic_loading_icon)
            .error(R.drawable.ic_broken_image))
    .into(this)
}

@BindingAdapter("dataOf")
fun RecyclerView.dataOf(data: List<MarsProperty>?) = data?.also {
    (adapter as PhotoGridAdapter).submitList(data)
}
