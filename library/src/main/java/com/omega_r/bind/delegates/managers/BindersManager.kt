package com.omega_r.bind.delegates.managers

/**
 * Created by Anton Knyazev on 04.04.2019.
 */
@Suppress("unused")
open class BindersManager {

    private val autoInitList = mutableListOf<Lazy<*>>()

    fun <V> bind(bindType: BindType = BindType.STATIC, findInit: () -> V, objectInit: (V.() -> Unit)? = null): Lazy<V> {
        val lazy = createLazy(bindType, findInit, objectInit)
        if (bindType == BindType.RESETTABLE_WITH_AUTO_INIT) {
            autoInitList += lazy
        }
        return lazy
    }

    protected open fun <V> createLazy(
        bindType: BindType = BindType.STATIC,
        findInit: () -> V,
        objectInit: (V.() -> Unit)? = null
    ): Lazy<V> = BindLazy(findInit, objectInit)

    fun doAutoInit() {
        autoInitList.forEach { it.value }
    }

    enum class BindType {
        STATIC, RESETTABLE, RESETTABLE_WITH_AUTO_INIT
    }

    open class BindLazy<T>(initializer: () -> T, objectInitializer: (T.() -> Unit)?) : Lazy<T> {
        protected var initializer: (() -> T)? = initializer

        @Suppress("CanBePrimaryConstructorProperty")
        protected var objectInitializer: (T.() -> Unit)? = objectInitializer

        protected var realValue: Any? = UNINITIALIZED_VALUE

        override val value: T
            get() {
                if (realValue === UNINITIALIZED_VALUE) {
                    initializer!!().also { value ->
                        realValue = value
                        initializer = null
                        objectInitializer?.let {
                            objectInitializer!!(value)
                            objectInitializer = null
                        }
                    }
                }
                @Suppress("UNCHECKED_CAST")
                return realValue as T
            }

        override fun isInitialized(): Boolean = realValue !== UNINITIALIZED_VALUE

        override fun toString(): String = if (isInitialized()) value.toString() else "Lazy value not initialized yet."

        private fun writeReplace(): Any = InitializedLazyImpl(value)

        @Suppress("ClassName")
        protected object UNINITIALIZED_VALUE
    }

    internal class InitializedLazyImpl<out T>(override val value: T) : Lazy<T> {

        override fun isInitialized(): Boolean = true

        override fun toString(): String = value.toString()

    }


}