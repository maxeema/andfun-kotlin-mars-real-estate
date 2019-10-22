/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy factoryOf the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package maxeem.america.mars.api

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface MarsApiService {

    enum class Filter(val value: String) {
        BUY("buy"), RENT("rent"), ALL("all");
        companion object {
            fun of(v: String) = values().first { it.value == v }
        }
    }

    @GET("realestate")
    fun getPropertiesAsync(@Query("filter") type: String) : Deferred<List<MarsProperty>>

}
