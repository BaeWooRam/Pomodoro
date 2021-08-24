package com.geekstudio.pomodoro.observer

import android.os.Bundle

/**
 * Ui 업데이트 관련 관리자
 */
object UiObserverManager {
    private val observerHashMap:HashMap<String, BaseUiObserver> = hashMapOf()

    fun registerObserver(baseUiObserver: BaseUiObserver){
        observerHashMap[baseUiObserver.getType().name] = baseUiObserver
    }

    /**
     * 등록된 해당 Observer 지움
     */
    fun removeObserver(type: BaseUiObserver.UiType){
        observerHashMap.remove(type.name)
    }

    /**
     * 등록된 모든 Observer 지움
     */
    fun clear(){
        observerHashMap.clear()
    }

    @Synchronized
    fun notifyAllUpdate(){
        observerHashMap.values.forEach { observer ->
            observer.update(Bundle())
        }
    }

    @Synchronized
    fun notifyAllUpdate(data: Bundle){
        observerHashMap.values.forEach { observer ->
            observer.update(data)
        }
    }

    @Synchronized
    fun notifyUpdate(type: BaseUiObserver.UiType, data: Bundle){
        observerHashMap[type.name]?.update(data)
    }

    @Synchronized
    fun notifyUpdate(type: BaseUiObserver.UiType){
        observerHashMap[type.name]?.update(null)
    }
}