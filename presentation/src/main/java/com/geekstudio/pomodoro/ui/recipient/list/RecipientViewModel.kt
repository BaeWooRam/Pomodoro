package com.geekstudio.pomodoro.ui.recipient.list

import com.geekstudio.data.db.RecipientEntity
import com.geekstudio.data.repository.local.RecipientLocalDataSourceImp
import com.geekstudio.data.listener.TaskCompleteListener
import com.geekstudio.data.listener.TaskListener
import com.geekstudio.pomodoro.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.lang.Exception

class RecipientViewModel(
    private val recipientLocalDataSourceImpl: RecipientLocalDataSourceImp
) : BaseViewModel() {

    /**
     *
     */
    fun insertRecipient(recipient:RecipientEntity, taskCompleteListener: TaskCompleteListener){
        scope.launch {
            kotlin.runCatching {
                recipientLocalDataSourceImpl.insertRecipient(recipient)
            }.onSuccess {
                taskCompleteListener.onSuccess()
            }.onFailure {
                taskCompleteListener.onFailure(Exception(it))
            }
        }
    }

    /**
     *
     */
    fun getAllRecipient(taskListener: TaskListener<List<RecipientEntity>>){
        scope.launch {
            kotlin.runCatching {
                recipientLocalDataSourceImpl.getAllRecipient()
            }.onSuccess {
                taskListener.onSuccess(it)
            }.onFailure {
                taskListener.onFailure(Exception(it))
            }
        }
    }


    /**
     *
     */
    fun getRecipientFromPhoneNumber(phoneNumber:String, taskListener: TaskListener<List<RecipientEntity>>){
        scope.launch {
            kotlin.runCatching {
                recipientLocalDataSourceImpl.getSearchRecipientFromPhoneNumber(phoneNumber)
            }.onSuccess {
                taskListener.onSuccess(it)
            }.onFailure {
                taskListener.onFailure(Exception(it))
            }
        }
    }

    /**
     *
     */
    fun getRecipientFormName(name:String, taskListener: TaskListener<List<RecipientEntity>>){
        scope.launch {
            kotlin.runCatching {
                recipientLocalDataSourceImpl.getSearchRecipientFromName(name)
            }.onSuccess {
                taskListener.onSuccess(it)
            }.onFailure {
                taskListener.onFailure(Exception(it))
            }
        }
    }

}