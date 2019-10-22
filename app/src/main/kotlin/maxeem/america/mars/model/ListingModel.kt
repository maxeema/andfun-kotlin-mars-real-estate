package maxeem.america.mars.model

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
import maxeem.america.mars.misc.*
import org.jetbrains.anko.info

class ListingModel : BaseModel() {

    val properties = MutableLiveData<List<MarsProperty>?>().asImmutable()

    val status = MutableLiveData<MarsApiStatus?>().asImmutable()
    val hasData  = status.map { it is MarsApiStatus.Success }
    val hasError = status.map { it is MarsApiStatus.Error }

    val statusEvent = MutableLiveData<MarsApiStatus?>().asImmutable()
    fun consumeStatusEvent() { statusEvent.asMutable().value = null }

    private var job : Job? = null

    init {
        info("$hash $timeMillis init")
        retrieve(Conf.filter)
    }

    private fun retrieveMarsRealEstateProperties(filter: MarsApiService.Filter) = viewModelScope.launch {
        status.asMutable().value = MarsApiStatus.Loading
        statusEvent.asMutable().value = status.value
        runCatching {
            val res = MarsApi.retrofitService.getPropertiesAsync(filter.value).await()
            if (BuildConfig.DEBUG)
                println("filter: $filter, result: $res")
            properties.asMutable().value = res
            status.asMutable().value = MarsApiStatus.Success
        }.onFailure { err ->
            properties.asMutable().value = null
            status.asMutable().value = MarsApiStatus.Error.of(err)
        }
        statusEvent.asMutable().value = status.value
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