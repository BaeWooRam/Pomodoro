package com.geekstudio.pomodoro.ui.recipient.list

import android.os.Bundle
import android.view.View
import com.geekstudio.data.db.RecipientEntity
import com.geekstudio.data.listener.TaskListener
import com.geekstudio.pomodoro.R
import com.geekstudio.pomodoro.databinding.ActivityRecipientBinding
import com.geekstudio.pomodoro.ui.base.BaseActivity
import com.geekstudio.pomodoro.ui.recipient.contacts.ContactsActivity
import com.geekstudio.pomodoro.util.ActivityUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception

class RecipientActivity : BaseActivity(), View.OnClickListener{
    private val viewModel by viewModel<RecipientViewModel>()
    lateinit var binding: ActivityRecipientBinding
    lateinit var adapter: RecipientRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initData()
    }

    private fun initView(){
        binding.ivAdd.setOnClickListener(this@RecipientActivity)

        adapter = RecipientRvAdapter(this@RecipientActivity)
        binding.rvRecipient.adapter = adapter
    }

    private fun initData(){
        viewModel.getAllRecipient(object :TaskListener<List<RecipientEntity>>{
            override fun onSuccess(result: List<RecipientEntity>) {
                adapter.setItemList(result)
            }

            override fun onFailure(e: Exception) {
                showMessage(getString(R.string.error_get_recipient))
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivAdd -> ActivityUtil.openActivity(this@RecipientActivity, ContactsActivity::class.java)
        }
    }
}