package maxeem.america.mars.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import maxeem.america.mars.BuildConfig
import maxeem.america.mars.api.MarsApiService
import maxeem.america.mars.api.MarsApiStatus
import maxeem.america.mars.api.MarsProperty
import maxeem.america.mars.misc.*
import org.jetbrains.anko.info
import org.koin.core.KoinComponent
import org.koin.core.inject

private data class JobInfo(val job: Job, val filter: MarsApiService.Filter)

class ListingModel : BaseModel(), KoinComponent {

    val properties = MutableLiveData<List<MarsProperty>?>().asImmutable()

    val status = MutableLiveData<MarsApiStatus?>().asImmutable()
    val hasData  = status.map { it is MarsApiStatus.Success }
    val hasError = status.map { it is MarsApiStatus.Error }

    val statusEvent = status.map { Consumable(it) }

    private var jobInfo : JobInfo? = null

    private val mars : MarsApiService by inject()

    init {
        info("$hash $timeMillis init")
        retrieve(Conf.filter)
    }

    fun retrieve(filter: MarsApiService.Filter) {
        jobInfo?.takeUnless { it.job.isCompleted }?.let {
            if (it.filter == filter)
                return@retrieve
            it.job.cancel()
        }
        viewModelScope.launch {
            jobInfo = JobInfo(this as Job, filter)
            status.asMutable().value = MarsApiStatus.Loading
            runCatching {
                val res = mars.getPropertiesAsync(filter.value).await()
                if (BuildConfig.DEBUG)
                    println("filter: $filter, result size: ${res.size}")
                if (!isActive)
                    return@launch
                properties.asMutable().value = res
                status.asMutable().value = MarsApiStatus.Success
            }.onFailure { err ->
                properties.asMutable().value = null
                status.asMutable().value = MarsApiStatus.Error.of(err)
            }
        }.invokeOnCompletion {
            println("completed: $this")
        }
    }

}
