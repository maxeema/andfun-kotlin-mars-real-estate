package maxeem.america.mars.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateMargins
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import maxeem.america.mars.app
import maxeem.america.mars.databinding.FragmentDetailsBinding
import maxeem.america.mars.misc.hash
import maxeem.america.mars.misc.timeMillis
import maxeem.america.mars.model.DetailsModel
import org.jetbrains.anko.dip
import org.jetbrains.anko.info

class DetailsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
        = FragmentDetailsBinding.inflate(inflater).apply {
            info("$hash $timeMillis onCreateView, savedInstanceState: $savedInstanceState" +
                    "\n nav args: ${navArgs<DetailsFragmentArgs>().value}")
            this.lifecycleOwner = viewLifecycleOwner
            this.model = viewModels<DetailsModel> { DetailsModel.factoryOf( navArgs<DetailsFragmentArgs>().value.property) }.value
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                appbar.setExpanded(false)
                with (fab.layoutParams as CoordinatorLayout.LayoutParams) {
                    anchorId = colToolbar.id
                    anchorGravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                    //
                    updateMargins(bottom = 0)
                    gravity = Gravity.NO_GRAVITY
                }
                (description.layoutParams as ViewGroup.MarginLayoutParams).updateMargins(bottom = app.dip(32))
            }
        }.root

}
