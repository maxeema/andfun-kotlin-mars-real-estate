package maxeem.america.mars.model

import androidx.lifecycle.*
import maxeem.america.mars.R
import maxeem.america.mars.api.MarsProperty
import maxeem.america.mars.api.isRental
import maxeem.america.mars.misc.asString
import maxeem.america.mars.misc.hash
import maxeem.america.mars.misc.timeMillis
import org.jetbrains.anko.info

class DetailsModel(marsProperty: MarsProperty) : BaseModel() {

    companion object {
        fun factoryOf(property: MarsProperty) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>) = DetailsModel(property) as T
        }
    }

    init {
        info("$hash $timeMillis init")
    }

    private val _property = MutableLiveData(marsProperty)
    val property = _property as LiveData<MarsProperty>

    val price    = property.map { it.price }
    val isRental = property.map { it.isRental }
    
    val description = R.string.mock_description.asString()

}
