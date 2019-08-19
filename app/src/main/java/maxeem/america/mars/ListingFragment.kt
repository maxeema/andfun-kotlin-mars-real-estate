package maxeem.america.mars

import android.graphics.Rect
import android.os.Bundle
import android.view.*
import androidx.annotation.MenuRes
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import maxeem.america.mars.adapter.PhotoGridAdapter
import maxeem.america.mars.api.MarsApiStatus
import maxeem.america.mars.api.MarsProperty
import maxeem.america.mars.databinding.FragmentListingBinding
import org.jetbrains.anko.dip
import org.jetbrains.anko.info

class ListingFragment : BaseFragment() {

    private lateinit var binding : FragmentListingBinding
    private val model : ListingModel by viewModels()

    private val busy = ObservableBoolean(true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        info("$hash $timeMillis onCreateView, savedInstanceState: $savedInstanceState")
        binding = FragmentListingBinding.inflate(inflater, container, false)

        compatActivity()?.setSupportActionBar(binding.toolbar)

        binding.lifecycleOwner = this
        binding.model = model
        binding.busy = busy

        binding.bottomNav.selectedItemId = Util.filterToTab(Conf.filter)

        binding.refresh.setOnRefreshListener { fetch() }
        binding.recycler.addItemDecoration(object: RecyclerView.ItemDecoration() {
            val gap = context!!.dip(2); val spanCount = (binding.recycler.layoutManager as GridLayoutManager).spanCount
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.set(if (binding.recycler.getChildAdapterPosition(view)%spanCount != 0) gap else 0, gap, 0, 0)
            }
        })
        binding.recycler.adapter = PhotoGridAdapter().apply {
            onClick = View.OnClickListener { val property = it.getTag(R.id.mars_property_tag) as MarsProperty
                if (!busy.get())
                    findNavController().navigate(ListingFragmentDirections.showDetails(property))
            }
        }
        model.status.observe(viewLifecycleOwner) {
            it ?: return@observe
            if (it == MarsApiStatus.Loading) return@observe
            compatActivity()?.delayed(500) {
                busy.set(false)
                binding.refresh.isRefreshing = false
                binding.refresh.isEnabled = true
                binding.appbar.setExpanded(true)
            }
        }
        binding.error.setOnClickListener { fetch() }
        binding.bottomNav.setOnNavigationItemSelectedListener { fetch(it.itemId) }
        binding.bottomNav.setOnNavigationItemReselectedListener {  /*ingore re-selecting */ }
        binding.fabCall.setOnClickListener { /*TODO implement 'Call us' action */ }

        return binding.root
    }

    private fun fetch(@MenuRes tab: Int = binding.bottomNav.selectedItemId) : Boolean {
        if (busy.get()) return false
        //
        busy.set(true)
        Conf.filter = Util.tabToFilter(tab)
        (binding.recycler.adapter as PhotoGridAdapter).submitList(emptyList())
        compatActivity()?.delayed(300) {
            binding.recycler.itemAnimator?.isRunning {
                if (lifecycle.currentState < Lifecycle.State.CREATED) return@isRunning
                model.retrieve(Conf.filter)
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = true.apply {
        // TODO show About
    }

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState)
        info("$hash $timeMillis onCreate, savedInstanceState: $savedInstanceState")
        setHasOptionsMenu(true)
    }

}
