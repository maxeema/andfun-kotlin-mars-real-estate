package maxeem.america.mars.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import maxeem.america.mars.R
import maxeem.america.mars.api.MarsProperty
import maxeem.america.mars.api.isRental
import maxeem.america.mars.databinding.GridItemBinding

class PhotoGridAdapter : ListAdapter<MarsProperty, PhotoGridAdapter.MarsPropertyViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<MarsProperty>() {
        override fun areItemsTheSame(oldItem: MarsProperty, newItem: MarsProperty) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MarsProperty, newItem: MarsProperty) = oldItem == newItem
    }

    var onClick : View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MarsPropertyViewHolder(GridItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: MarsPropertyViewHolder, position: Int) {
        val marsProperty = getItem(position)
        holder.bind(marsProperty, onClick)
    }

    class MarsPropertyViewHolder(private var binding: GridItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(marsProperty: MarsProperty, clickListener: View.OnClickListener?) {
            binding.property = marsProperty
            binding.isRental = marsProperty.isRental
            binding.root.setTag(R.id.mars_property_tag, marsProperty)
            binding.root.setOnClickListener(clickListener)
            binding.executePendingBindings()
        }
    }

}