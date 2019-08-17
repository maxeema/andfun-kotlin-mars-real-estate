package maxeem.america.mars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import maxeem.america.mars.databinding.FragmentDetailsBinding
import org.jetbrains.anko.info

class DetailsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        info("$hash $timeMillis onCreateView, savedInstanceState: $savedInstanceState")

        val binding = FragmentDetailsBinding.inflate(inflater)
        val model : DetailsModel by viewModels { DetailsModel.factoryOf(DetailsFragmentArgs.fromBundle(arguments!!).property) }

        binding.lifecycleOwner = this
        binding.model = model

        return binding.root
    }

}
