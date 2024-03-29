package com.example.collaborationstation

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collaborationstation.databinding.ActivityChatMainBinding
import com.example.collaborationstation.databinding.DialogLoginBinding
import kotlinx.coroutines.Job

class ChatMainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "ChatMainActivity"
    }

    private var viewModel: ChatMainViewModel = ChatMainViewModel()
    lateinit var binding: ActivityChatMainBinding
    private lateinit var fetchJob: Job

    lateinit var chatListAdapter: ChatListAdapter

    var boolLogin: Boolean = false // 로그인 여부

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        observeData()
    }

    private fun initView() {
        // Toolbar 사용 설정
        setSupportActionBar(binding.tbTop)
        binding.tbTop.title = ""

        // Toolbar 의 navigation icon 클릭 이벤트 시 뒤로가기
        binding.tbTop.setNavigationOnClickListener {
            onBackPressed()
        }

        // 로그인 여부 설정
        boolLogin = !CommonUtil.userName.isNullOrEmpty()
        if (boolLogin) {
            binding.tvToolbarUser.text = "(${CommonUtil.userName})"
        }

        // RecyclerView 설정
        chatListAdapter = ChatListAdapter()
        binding.recyclerView.run {
            layoutManager = LinearLayoutManager(this@ChatMainActivity)
            adapter = chatListAdapter
        }
        binding.refreshLayout.isEnabled = false

        // 메세지 보내기
        binding.btnSend.setOnClickListener {
            if (CommonUtil.userName.isNullOrEmpty()) {
                Toast.makeText(this, "로그인을 진행하세요.", Toast.LENGTH_LONG).show()
            }
            if (binding.etInput.text.isNullOrEmpty()) {
                Toast.makeText(this, "메세지를 입력하세요.", Toast.LENGTH_LONG).show()
            } else {
                viewModel.fetchWriteData(CommonUtil.userName, binding.etInput.text.toString())
            }
        }

        // 데이터 통신하기
        fetchJob = viewModel.fetchReadData()
    }

    /*
    * Toolbar 의 OptionsMenu 가 최초로 생성될 때 호출
    * Toolbar 에 menu_chat.xml 을 inflate 함
    */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_chat, menu)
        return true
    }

    /*
    * Toolbar 의 OptionsMenu 가 화면에 보여질 때마다 호출
    * */
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        // item(0):login, item(1):logout
        if (boolLogin) { //로그인 O -> 로그인 안보이게, 로그아웃 보이게
            menu?.getItem(0)?.isVisible = false //login
            menu?.getItem(1)?.isVisible = true //logout
        } else { //로그인 X -> 로그인 보이게, 로그아웃 안보이게
            menu?.getItem(0)?.isVisible = true
            menu?.getItem(1)?.isVisible = false
        }
        return super.onPrepareOptionsMenu(menu)
    }

    /*
    * Toolbar 의 OptionsMenu 아이템이 클릭될 때 호출
    * */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_login -> {
                showLoginDialog()
                true
            }
            R.id.menu_logout -> {
                CommonUtil.userName = ""
                initView()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    /*
    * viewModel.chatMainLiveData 관찰하는 메소드
    * */
    private fun observeData() = viewModel.chatMainLiveData.observe(this) {
        viewModel.chatMainLiveData.observe(this) {
            when (it) {
                is ChatMainState.UnInitialized -> initView()
                is ChatMainState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE // 프로그래스 바 보여주기
                }
                is ChatMainState.Success -> {
                    handleSuccessState(it)
                }
                is ChatMainState.Error -> {
                    handleErrorState()
                }
                else -> {
                }
            }
        }
    }

    private fun handleSuccessState(state: ChatMainState.Success) {
        binding.progressBar.visibility = View.GONE
        if (state.chatList.isEmpty()) {
            binding.tvEmptyList.visibility = View.VISIBLE
        } else {
            binding.tvEmptyList.visibility = View.GONE
            chatListAdapter.setChatItems(state.chatList)
        }
        // 키보드 내리기
        val inputManager: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(binding.etInput.windowToken, 0)
        binding.recyclerView.scrollToPosition(chatListAdapter.itemCount - 1) //스크롤 맨 아래로
        binding.etInput.text.clear()
    }

    private fun handleErrorState() {
        binding.tvEmptyList.visibility = View.VISIBLE
        Toast.makeText(this, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
    }

    /*
    * 로그인하는 Dialog 띄우기
    * */
    private fun showLoginDialog() {

        val dialogViewBinding = DialogLoginBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this)
        val dialog = builder.setView(dialogViewBinding.root).create()

        dialogViewBinding.btnConfirm.setOnClickListener {
            CommonUtil.userName = dialogViewBinding.etName.text.toString()
            initView()
            dialog.dismiss()
        }
        dialogViewBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}