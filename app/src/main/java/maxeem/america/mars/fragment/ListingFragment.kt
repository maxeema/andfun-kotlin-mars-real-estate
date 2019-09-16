package maxeem.america.mars.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import maxeem.america.mars.R
import maxeem.america.mars.adapter.MarsPropertiesAdapter
import maxeem.america.mars.api.MarsApiStatus
import maxeem.america.mars.api.MarsProperty
import maxeem.america.mars.app
import maxeem.america.mars.databinding.FragmentListingBinding
import maxeem.america.mars.misc.*
import maxeem.america.mars.model.ListingModel
import org.jetbrains.anko.dip
import org.jetbrains.anko.info

class ListingFragment : BaseFragment() {

    private lateinit var binding : FragmentListingBinding
    private val model : ListingModel by viewModels()

    private val busy = ObservableBoolean(true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        info("$hash $timeMillis onCreateView, savedInstanceState: $savedInstanceState")
        
        if (savedInstanceState != null && model.status.value != MarsApiStatus.Loading)
            busy.set(false)

        binding = FragmentListingBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.model = model
        binding.busy = busy

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.aboutFragment ->
                    findNavController().navigate(AboutFragmentDirections.actionGlobalAboutFragment()).let { true }
                else -> false
            }
        }

        binding.tabs.selectedItemId = Util.filterToTab(Conf.filter)

        binding.refresh.setOnRefreshListener { fetch() }
        binding.recycler.setHasFixedSize(true)
        binding.recycler.addItemDecoration(object: RecyclerView.ItemDecoration() {
            val gap = app.dip(2); val spanCount = (binding.recycler.layoutManager as GridLayoutManager).spanCount
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.set(if (binding.recycler.getChildAdapterPosition(view)%spanCount != 0) gap else 0, gap, 0, 0)
            }
        })
        binding.recycler.adapter = MarsPropertiesAdapter().apply {
            onClick = View.OnClickListener { val property = it.getTag(R.id.mars_property_tag) as MarsProperty
                if (!busy.get())
                    findNavController().navigate(ListingFragmentDirections.showDetails(property))
            }
        }
        model.properties.observe(viewLifecycleOwner) { properties ->
            info("observe properties?.size: ${properties?.size}")
            properties ?: return@observe
        }
        model.statusEvent.observe(viewLifecycleOwner) { status ->
            info("observe statusEvent: $status")
            status ?: return@observe
            model.consumeStatusEvent()
            if (status == MarsApiStatus.Loading)
                return@observe
            viewOwner?.delayed(500) {
                busy.set(false)
                binding.refresh.isRefreshing = false
                binding.refresh.isEnabled = true
                binding.appbar.setExpanded(true)
            }
        }
        binding.error.onClick { fetch() }
        binding.tabs.setOnNavigationItemSelectedListener { fetch(it.itemId) }
        binding.tabs.setOnNavigationItemReselectedListener { /*ingore re-selecting */ }

        return binding.root
    }

    private fun fetch(@MenuRes tab: Int = binding.tabs.selectedItemId) : Boolean {
        if (busy.get()) return false
        //
        busy.set(true)
        Conf.filter = Util.tabToFilter(tab)
        (binding.recycler.adapter as MarsPropertiesAdapter).submitList(emptyList())
        viewLifecycleOwner.delayed(300) {
            binding.recycler.itemAnimator?.isRunning {
                viewOwner?.doOnLifecycle { model.retrieve(Conf.filter) }
            }
        }
        return true
    }

}
