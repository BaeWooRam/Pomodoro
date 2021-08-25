package com.geekstudio.pomodoro.ui.recipient.contacts

import com.geekstudio.data.contacts.Contacts
import com.geekstudio.data.db.RecipientEntity
import com.geekstudio.data.repository.local.RecipientLocalDataSourceImp
import com.geekstudio.data.listener.TaskCompleteListener
import com.geekstudio.data.listener.TaskListener
import com.geekstudio.data.repository.local.ContactLocalDataSourceImp
import com.geekstudio.pomodoro.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val contactLocalDataSourceImp: ContactLocalDataSourceImp,
    private val recipientLocalDataSourceImp: RecipientLocalDataSourceImp
) : BaseViewModel() {

    /**
     *
     */
    fun getContacts(taskListener: TaskListener<List<Contacts>>){
        scope.launch {
            kotlin.runCatching {
                contactLocalDataSourceImp.getContactList()
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
    fun saveRecipient(contacts: Contacts, taskCompleteListener: TaskCompleteListener){
        scope.launch {
            if(recipientLocalDataSourceImp.isExist(contacts.phoneNumber)) {
                taskCompleteListener.onFailure(RecipientLocalDataSourceImp.RecipientIsExistException())
                return@launch
            }

            kotlin.runCatching {
                val target = RecipientEntity(0, contacts.name, contacts.phoneNumber, RecipientEntity.Type.Sms)
                recipientLocalDataSourceImp.insertRecipient(target)
            }.onSuccess {
                taskCompleteListener.onSuccess()
            }.onFailure {
                taskCompleteListener.onFailure(Exception(it))
            }
        }
    }
}