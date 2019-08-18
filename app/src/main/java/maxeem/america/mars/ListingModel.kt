package maxeem.america.mars

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import maxeem.america.mars.api.MarsApi
import maxeem.america.mars.api.MarsApiService
import maxeem.america.mars.api.MarsApiStatus
import maxeem.america.mars.api.MarsProperty
import org.jetbrains.anko.AnkoLogger

class ListingModel : ViewModel(), AnkoLogger {

    private val _properties = MutableLiveData<List<MarsProperty>?>()
    val properties: LiveData<List<MarsProperty>?> = _properties

    private val _status = MutableLiveData<MarsApiStatus?>()
    val status : LiveData<MarsApiStatus?> = _status

    val isLoading = Transformations.map(_status) { it is MarsApiStatus.Loading }
    val hasData   = Transformations.map(_status) { it is MarsApiStatus.Success }
    val hasError  = Transformations.map(_status) { it is MarsApiStatus.Error }

    init {
        retrieveMarsRealEstateProperties(MarsApiService.Filter.SHOW_BUY)
    }

    private fun retrieveMarsRealEstateProperties(filter: MarsApiService.Filter) = viewModelScope.launch {
        _status.value = MarsApiStatus.Loading
        runCatching {
            val res = MarsApi.retrofitService.getPropertiesAsync(filter.value).await()
            if (BuildConfig.DEBUG)
                println("filter: $filter, result: $res")
            _properties.value = res
            _status.value = MarsApiStatus.Success
        }.onFailure { err ->
            _status.value = MarsApiStatus.Error.of(err)
        }
    }

    fun updateFilter(filter: MarsApiService.Filter) {
        retrieveMarsRealEstateProperties(filter)
    }

}
