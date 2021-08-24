package com.geekstudio.pomodoro.ui.recipient.contacts

import android.os.Bundle
import android.util.Log
import com.geekstudio.data.contacts.Contacts
import com.geekstudio.data.listener.TaskCompleteListener
import com.geekstudio.data.listener.TaskListener
import com.geekstudio.pomodoro.R
import com.geekstudio.pomodoro.databinding.ActivityContactsBinding
import com.geekstudio.pomodoro.dto.ContactsDto
import com.geekstudio.pomodoro.mapper.convertDtoToContacts
import com.geekstudio.pomodoro.observer.BaseUiObserver
import com.geekstudio.pomodoro.observer.UiObserverManager
import com.geekstudio.pomodoro.permission.Permission
import com.geekstudio.pomodoro.ui.base.BasePermissionActivity
import com.geekstudio.pomodoro.ui.recipient.RecipientComponentViewHolder
import org.koin.androidx.viewmodel.ext.android.viewModel


class ContactsActivity : BasePermissionActivity() {
    private val viewModel by viewModel<ContactsViewModel>()
    lateinit var binding: ActivityContactsBinding
    lateinit var adapter:ContactsRvAdapter
    lateinit var uiObserver: BaseUiObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initObserver()
        initData()
        executeCheckPermission()
    }

    override fun getPermissionListener(): Permission.PermissionListener {
        return object :Permission.PermissionListener{
            override fun onGrantedPermission() {
            }

            override fun onDenyPermission(denyPermissions: Array<String>) {
                showMessage(getString(R.string.error_deny_permission))
                finish()
            }

        }
    }

    override fun getCheckPermission(): Array<String> = arrayOf(android.Manifest.permission.READ_CONTACTS)

    private fun initView(){
        adapter = ContactsRvAdapter(this@ContactsActivity)
        binding.rvContacts.adapter = adapter
    }

    private fun initObserver(){
        uiObserver = object :BaseUiObserver{
            override fun getType(): BaseUiObserver.UiType {
                return BaseUiObserver.UiType.Contacts
            }

            override fun update(data: Bundle?) {
                if(data == null)
                    return

                data.getParcelable<ContactsDto>(RecipientComponentViewHolder.BUNDLE_CONTACTS_DATA)?.also {
                    viewModel.saveRecipient(convertDtoToContacts(it), object :TaskCompleteListener{
                        override fun onSuccess() {
                            showMessage(getString(R.string.contacts_save_complete))
                        }

                        override fun onFailure(e: Exception) {
                            showMessage(e.message ?: "에러 발생")
                        }
                    })
                }
            }
        }

        UiObserverManager.registerObserver(uiObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        UiObserverManager.removeObserver(BaseUiObserver.UiType.Contacts)
    }

    private fun initData(){
        viewModel.getContacts(object :TaskListener<List<Contacts>>{
            override fun onSuccess(result: List<Contacts>) {
                Log.d(javaClass.simpleName, "onSuccess() result = $result")

                runOnUiThread {
                    adapter.setItemList(result)
                }
            }

            override fun onFailure(e: Exception) {
                Log.d(javaClass.simpleName, "onFailure() e = ${e.message}")
                showMessage(getString(R.string.error_get_contacts))
            }
        })
    }
}