package maxeem.america.mars.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import maxeem.america.mars.BuildConfig
import maxeem.america.mars.api.MarsApi
import maxeem.america.mars.api.MarsApiService
import maxeem.america.mars.api.MarsApiStatus
import maxeem.america.mars.api.MarsProperty
import maxeem.america.mars.misc.Conf
import maxeem.america.mars.misc.hash
import maxeem.america.mars.misc.timeMillis
import org.jetbrains.anko.info

class ListingModel : BaseModel() {

    private val _properties = MutableLiveData<List<MarsProperty>?>()
    val properties: LiveData<List<MarsProperty>?> = _properties

    private val _status = MutableLiveData<MarsApiStatus?>()
    val status : LiveData<MarsApiStatus?> = _status

    val hasData  = _status.map { it is MarsApiStatus.Success }
    val hasError = _status.map { it is MarsApiStatus.Error }

    private var job : Job? = null

    init {
        info("$hash $timeMillis init")
        retrieve(Conf.filter)
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
    }.apply {
        job = this
        invokeOnCompletion {
            if (this == job)
                job = null
        }
    }

    fun retrieve(filter: MarsApiService.Filter) {
        if (!isCleared)
            retrieveMarsRealEstateProperties(filter)
    }

}
