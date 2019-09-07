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
import androidx.navigation.ui.NavigationUI
import maxeem.america.mars.databinding.FragmentDetailsBinding
import maxeem.america.mars.misc.compatActivity
import maxeem.america.mars.misc.hash
import maxeem.america.mars.misc.timeMillis
import maxeem.america.mars.model.DetailsModel
import org.jetbrains.anko.info

class DetailsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
        = FragmentDetailsBinding.inflate(inflater).apply {
            info("$hash $timeMillis onCreateView, savedInstanceState: $savedInstanceState")
            this.lifecycleOwner = viewLifecycleOwner
            this.model = viewModels<DetailsModel> { DetailsModel.factoryOf( DetailsFragmentArgs.fromBundle(arguments!!).property) }.value
            compatActivity()?.apply {
                setSupportActionBar(toolbar)
                NavigationUI.setupActionBarWithNavController(this, findNavController())
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
            }
        }.root

}
