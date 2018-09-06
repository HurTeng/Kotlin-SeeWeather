package com.xiecc.seeWeather.component

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class RxBus private constructor() {
    private val mBus: Subject<Any>

    init {
        mBus = PublishSubject.create()
    }

    private object RxBusHolder {
        val sInstance = RxBus()
    }

    fun post(o: Any) {
        mBus.onNext(o)
    }

    fun <T> toObservable(eventType: Class<T>): Observable<T> {
        return mBus.ofType(eventType)
    }

    companion object {

        val default: RxBus
            get() = RxBusHolder.sInstance
    }
}
