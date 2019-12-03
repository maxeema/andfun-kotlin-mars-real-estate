package maxeem.america.mars.misc

class Consumable<T>(private var data: T?) {

    fun consume() = data?.also { data = null }

}