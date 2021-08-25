package com.geekstudio.pomodoro.ui.recipient.comment

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.geekstudio.data.db.RecipientEntity
import com.geekstudio.data.listener.TaskCompleteListener
import com.geekstudio.data.listener.TaskListener
import com.geekstudio.pomodoro.R
import com.geekstudio.pomodoro.databinding.ActivityCommentBinding
import com.geekstudio.pomodoro.databinding.ActivityRecipientBinding
import com.geekstudio.pomodoro.dto.RecipientDto
import com.geekstudio.pomodoro.listener.onThrottleClick
import com.geekstudio.pomodoro.mapper.convertDtoToRecipientEntity
import com.geekstudio.pomodoro.observer.BaseUiObserver
import com.geekstudio.pomodoro.observer.UiObserverManager
import com.geekstudio.pomodoro.ui.base.BaseActivity
import com.geekstudio.pomodoro.ui.custom.NoItemView
import com.geekstudio.pomodoro.ui.dialog.RecipientMenuDialog
import com.geekstudio.pomodoro.ui.recipient.RecipientComponentViewHolder
import com.geekstudio.pomodoro.ui.recipient.contacts.ContactsActivity
import com.geekstudio.pomodoro.util.ActivityUtil
import org.koin.androidx.viewmodel.ext.android.viewModel


class CommentActivity : BaseActivity(), View.OnClickListener {
    private val viewModel by viewModel<CommentViewModel>()
    lateinit var binding: ActivityCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.btnSave.onThrottleClick(this@CommentActivity, 1000)

        binding.etWorkComment.text = Editable.Factory.getInstance().newEditable(viewModel.getNotificationWorkContent())
        binding.etRestComment.text = Editable.Factory.getInstance().newEditable(viewModel.getNotificationRestContent())
    }

    override fun onClick(v: View?) {
        viewModel.setNotificationComment(binding.etWorkComment.text.toString(), binding.etRestComment.text.toString())
    }
}