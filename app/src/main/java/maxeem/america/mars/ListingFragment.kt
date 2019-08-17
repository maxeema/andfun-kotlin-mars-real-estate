package maxeem.america.mars

import android.os.Bundle
import android.view.*
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import maxeem.america.mars.adapter.PhotoGridAdapter
import maxeem.america.mars.api.MarsApiService
import maxeem.america.mars.api.MarsApiStatus
import maxeem.america.mars.api.MarsProperty
import maxeem.america.mars.databinding.FragmentListingBinding
import org.jetbrains.anko.info

class ListingFragment : BaseFragment() {

    private val model : ListingModel by viewModels()
    private lateinit var binding : FragmentListingBinding

    private val busy = ObservableBoolean(true)
    private var filtering = false to null as MarsApiService.Filter?

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        info("$hash $timeMillis onCreateView, savedInstanceState: $savedInstanceState")
        binding = FragmentListingBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.model = model

        binding.recycler.adapter = PhotoGridAdapter().apply {
            onClick = View.OnClickListener { val property = it.getTag(R.id.mars_property_tag) as MarsProperty
                if (!busy.get())
                    findNavController().navigate(ListingFragmentDirections.showDetails(property))
            }
        }
        model.status.observe(viewLifecycleOwner) {
            it?.takeIf { it != MarsApiStatus.Loading }.apply {
                busy.set(false)
                activity?.invalidateOptionsMenu()
            }
        }

        model.isLoading.observe(viewLifecycleOwner) { activity?.invalidateOptionsMenu() }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (!busy.get() && model.status.value == MarsApiStatus.Success)
            inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        busy.set(true)
        filtering = true to
                when (item.itemId) {
                    R.id.show_rent_menu -> MarsApiService.Filter.SHOW_RENT
                    R.id.show_buy_menu -> MarsApiService.Filter.SHOW_BUY
                    else -> MarsApiService.Filter.SHOW_ALL
                }
        activity!!.invalidateOptionsMenu()
        // Fix bug with short items blinking on changing data in Recycler with ListAdapter's animation,
        // so clear the data, wait for the animation and then post new
        (binding.recycler.adapter as PhotoGridAdapter).submitList(emptyList())
        compatActivity()?.delayed(300) {
            binding.recycler.itemAnimator?.isRunning {
                if (lifecycle.currentState < Lifecycle.State.CREATED) return@isRunning
                model.updateFilter(filtering.second!!)
                filtering = false to null
            }
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState)
        info("$hash $timeMillis onCreate, savedInstanceState: $savedInstanceState")
        setHasOptionsMenu(true)
    }

}
