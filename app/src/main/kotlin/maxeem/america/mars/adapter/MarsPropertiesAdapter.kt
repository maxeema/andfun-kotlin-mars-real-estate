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
import maxeem.america.mars.databinding.PropertyItemBinding

class MarsPropertiesAdapter : ListAdapter<MarsProperty, MarsPropertiesAdapter.MarsPropertyViewHolder>(DiffCallback) {

    private companion object DiffCallback : DiffUtil.ItemCallback<MarsProperty>() {
        override fun areItemsTheSame(oldItem: MarsProperty, newItem: MarsProperty) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MarsProperty, newItem: MarsProperty) = oldItem == newItem
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = getItem(position)?.id ?: RecyclerView.NO_ID

    var onClick : View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MarsPropertyViewHolder(PropertyItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: MarsPropertyViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }

    class MarsPropertyViewHolder(private var binding: PropertyItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(marsProperty: MarsProperty, clickListener: View.OnClickListener?) {
            binding.apply {
                property = marsProperty
                isRental = marsProperty.isRental
                root.setTag(R.id.mars_property_tag, marsProperty)
                root.setOnClickListener(clickListener)
                executePendingBindings()
            }
        }
    }

}