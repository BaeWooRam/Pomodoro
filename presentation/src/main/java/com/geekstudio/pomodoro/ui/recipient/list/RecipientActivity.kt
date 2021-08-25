package com.geekstudio.pomodoro.ui.recipient.list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.geekstudio.data.db.RecipientEntity
import com.geekstudio.data.listener.TaskCompleteListener
import com.geekstudio.data.listener.TaskListener
import com.geekstudio.pomodoro.R
import com.geekstudio.pomodoro.databinding.ActivityRecipientBinding
import com.geekstudio.pomodoro.dto.RecipientDto
import com.geekstudio.pomodoro.mapper.convertDtoToRecipientEntity
import com.geekstudio.pomodoro.observer.BaseUiObserver
import com.geekstudio.pomodoro.observer.UiObserverManager
import com.geekstudio.pomodoro.ui.base.BaseActivity
import com.geekstudio.pomodoro.ui.custom.NoItemView
import com.geekstudio.pomodoro.ui.dialog.RecipientMenuDialog
import com.geekstudio.pomodoro.ui.recipient.RecipientComponentViewHolder
import com.geekstudio.pomodoro.ui.recipient.comment.CommentActivity
import com.geekstudio.pomodoro.ui.recipient.contacts.ContactsActivity
import com.geekstudio.pomodoro.util.ActivityUtil
import org.koin.androidx.viewmodel.ext.android.viewModel


class RecipientActivity : BaseActivity(), View.OnClickListener, RecipientMenuDialog.MenuListener,
    SwipeRefreshLayout.OnRefreshListener, NoItemView.OnNoItemButtonClickListener {
    private val viewModel by viewModel<RecipientViewModel>()
    lateinit var binding: ActivityRecipientBinding
    lateinit var adapter: RecipientRvAdapter
    lateinit var uiObserver: BaseUiObserver
    private var recipientMenuDialog: RecipientMenuDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initObserver()
        initData()
    }

    private fun initView() {
        //button
        binding.ivAdd.setOnClickListener(this@RecipientActivity)
        binding.ivComment.setOnClickListener(this@RecipientActivity)

        //NoItem
        binding.noItemView.setting(
            NoItemView.NoItemSetting(
                R.drawable.ic_tomato,
                getString(R.string.recipient_no_item_content),
                getString(R.string.recipient_no_item_button_name),
                this@RecipientActivity
            )
        )

        //Refresh
        binding.swipeRefreshLayout.setOnRefreshListener(this@RecipientActivity)

        //Adapter
        adapter = RecipientRvAdapter(this@RecipientActivity)
        binding.rvRecipient.adapter = adapter

        //Dialog
        recipientMenuDialog = RecipientMenuDialog(this@RecipientActivity, this@RecipientActivity)
    }

    private fun initObserver() {
        uiObserver = object : BaseUiObserver {
            override fun getType(): BaseUiObserver.UiType {
                return BaseUiObserver.UiType.Recipient
            }

            override fun update(data: Bundle?) {
                if (data == null)
                    return

                data.getParcelable<RecipientDto>(RecipientComponentViewHolder.BUNDLE_RECIPIENT_DATA)
                    ?.also {
                        recipientMenuDialog?.target = convertDtoToRecipientEntity(it)
                        recipientMenuDialog?.show()
                    }
            }
        }

        UiObserverManager.registerObserver(uiObserver)
    }

    private fun initData() {
        if (adapter.itemCount > 0)
            adapter.clearItemList()

        viewModel.getAllRecipient(object : TaskListener<List<RecipientEntity>> {
            override fun onSuccess(result: List<RecipientEntity>) {
                Log.d(javaClass.simpleName, "onSuccess() result = $result")

                runOnUiThread {
                    showNoItemView(result.isEmpty())
                    adapter.setItemList(result)
                }
            }

            override fun onFailure(e: Exception) {
                Log.d(javaClass.simpleName, "onFailure() e = ${e.message}")
                showNoItemView(true)
                showMessage(getString(R.string.error_get_recipient))
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivAdd -> goToContactsActivity()
            R.id.ivComment -> goToNotificationCommentActivity()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        UiObserverManager.removeObserver(BaseUiObserver.UiType.Recipient)
    }

    override fun onDelete(target: RecipientEntity) {
        viewModel.deleteRecipient(target, object : TaskCompleteListener {
            override fun onSuccess() {
                Log.d(javaClass.simpleName, "deleteRecipient onSuccess()")

                runOnUiThread {
                    adapter.remove(target)
                    showNoItemView(adapter.itemCount <= 0)
                }
                showMessage(getString(R.string.recipient_delete_complete))
            }

            override fun onFailure(e: Exception) {
                Log.d(javaClass.simpleName, "deleteRecipient onFailure() e = ${e.message}")
                showMessage(getString(R.string.error_delete_recipient))
            }
        })
    }

    override fun onModify(target: RecipientEntity) {
        showMessage("아직 준비중")
    }

    override fun onRefresh() {
        initData()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun onButtonClick() {
        goToContactsActivity()
    }

    private fun showNoItemView(isShow:Boolean){
        if (isShow) {
            binding.noItemView.visibility = View.VISIBLE
            binding.rvRecipient.visibility = View.GONE
        } else {
            binding.noItemView.visibility = View.GONE
            binding.rvRecipient.visibility = View.VISIBLE
        }
    }

    private fun goToContactsActivity() {
        ActivityUtil.openActivity(this@RecipientActivity, ContactsActivity::class.java)
    }

    private fun goToNotificationCommentActivity() {
        ActivityUtil.openActivity(this@RecipientActivity, CommentActivity::class.java)
    }
}