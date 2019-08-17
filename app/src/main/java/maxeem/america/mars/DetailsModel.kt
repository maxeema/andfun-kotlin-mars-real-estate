package maxeem.america.mars

import androidx.lifecycle.*
import maxeem.america.mars.api.MarsProperty
import maxeem.america.mars.api.isRental

class DetailsModel(marsProperty: MarsProperty) : ViewModel() {

    companion object {
        fun factoryOf(property: MarsProperty) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>) = DetailsModel(property) as T
        }
    }

    private val _property = MutableLiveData(marsProperty)
    val property = _property as LiveData<MarsProperty>
    val isRental = Transformations.map(property) { it.isRental }
    val price = Transformations.map(property) { it.price }

}
