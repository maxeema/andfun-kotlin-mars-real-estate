package maxeem.america.mars.api

sealed class MarsApiStatus {

    object Loading : MarsApiStatus()
    object Success : MarsApiStatus()

    open class Error protected constructor(err: Throwable) : MarsApiStatus() {
        companion object {
            fun of (err: Throwable) = when (err.javaClass) {
                in arrayOf<Class<out Throwable>>(
                        java.net.UnknownHostException::class.java,
                        java.net.ConnectException::class.java,
                        java.net.SocketTimeoutException::class.java) -> ConnectionError(err)
                else -> Error(err)
            }}
    }
    class ConnectionError(err: Throwable) : Error(err)

}