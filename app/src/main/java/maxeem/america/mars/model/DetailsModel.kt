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
    
    //
    private val mockDesc1 = R.string.for_rent_sample1.asString()
    private val mockDesc2 = R.string.for_rent_sample2.asString()
            
    private var mockDesc : String? = null
    private var descFor : MarsProperty? = null
    val description = property.map { 
        if (descFor != it) {
            descFor = it
            mockDesc = if (mockDesc == mockDesc1) mockDesc2 else mockDesc1
        }
        mockDesc
    }

}
