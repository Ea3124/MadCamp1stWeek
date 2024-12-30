// SignupFragment.kt
package com.example.madcamp1stweek

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.coroutines.*

class SignupFragment : DialogFragment() {

    private lateinit var editTextNickName: EditText
    private lateinit var editTextID: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextPasswordConfirm: EditText  // 비밀번호 확인 필드 추가
    private lateinit var buttonSignUp: Button
    private lateinit var buttonCancel: Button

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    // Interface to communicate back to the Activity
    interface SignupListener {
        fun onSignupSuccess()
    }

    private var listener: SignupListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SignupListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement SignupListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명 설정
        val view = inflater.inflate(R.layout.fragment_signup, container, false)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰 참조
        editTextNickName = view.findViewById(R.id.editTextNickName)
        editTextID = view.findViewById(R.id.editTextID)
        editTextPassword = view.findViewById(R.id.editTextPassword)
        editTextPasswordConfirm = view.findViewById(R.id.editTextPasswordConfirm)
        buttonSignUp = view.findViewById(R.id.buttonSignUp)
        buttonCancel = view.findViewById(R.id.buttonCancel)

        // 데이터베이스 초기화
        db = AppDatabase.getDatabase(requireContext())
        userDao = db.userDao()

        buttonSignUp.setOnClickListener {
            val nickName = editTextNickName.text.toString().trim()
            val id = editTextID.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val passwordConfirm = editTextPasswordConfirm.text.toString().trim()  // 비밀번호 확인 값 가져오기

            if (nickName.isEmpty() || id.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                Toast.makeText(requireContext(), "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if (password != passwordConfirm) {  // 비밀번호 일치 여부 확인
                Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }else {
                coroutineScope.launch {
                    val existingUser = withContext(Dispatchers.IO) {
                        userDao.getUserById(id)
                    }
                    if (existingUser != null) {
                        Toast.makeText(requireContext(), "이미 사용 중인 아이디입니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        val newUser = User(nickName = nickName, id = id, password = password)
                        withContext(Dispatchers.IO) {
                            userDao.insertUser(newUser)
                        }
                        Toast.makeText(requireContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        listener?.onSignupSuccess()
                        dismiss()
                    }
                }
            }
        }

        buttonCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}
