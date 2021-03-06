package com.example.suas.counter

import zendesk.suas.*


fun main(args: Array<String>) {
    val store = store()
    store.addListener(Counter::class.java) { (value) ->
        println("Kotlin - State changed to $value")
    }

    store.dispatch(IncrementAction(10))
    store.dispatch(IncrementAction(1))
    store.dispatch(DecrementAction(5))
}

fun store(): Store {
    // Create a store with a CounterReducer
    // LoggerMiddleware for advanced logging
    return Suas.createStore(CounterReducer())
            .withMiddleware(LoggerMiddleware())
            .build()
}

class CounterReducer : Reducer<Counter>() {
    override fun reduce(oldState: Counter, action: Action<*>): Counter? {
        return when (action) {
            is IncrementAction -> {
                oldState.copy(value = oldState.value + action.value)
            }
            is DecrementAction -> {
                oldState.copy(value = oldState.value - action.value)
            }
            else -> null
        }
    }

    override fun getInitialState(): Counter = Counter()

}

data class IncrementAction(val value: Int) : Action<Int>("increment", value)

data class DecrementAction(val value: Int) : Action<Int>("decrement", value)

data class Counter(val value: Int = 0)