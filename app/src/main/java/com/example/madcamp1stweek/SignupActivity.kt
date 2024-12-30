// SignupActivity.kt
package com.example.madcamp1stweek

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class SignupActivity : AppCompatActivity() {

    private lateinit var editTextNickName: EditText
    private lateinit var editTextID: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignUp: Button

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_signup)

        // 뷰 참조
        editTextNickName = findViewById(R.id.editTextNickName)
        editTextID = findViewById(R.id.editTextID)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSignUp = findViewById(R.id.buttonSignUp)

        // 데이터베이스 초기화
        db = AppDatabase.getDatabase(this)
        userDao = db.userDao()

        buttonSignUp.setOnClickListener {
            val nickName = editTextNickName.text.toString().trim()
            val id = editTextID.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (nickName.isEmpty() || id.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                coroutineScope.launch {
                    val existingUser = withContext(Dispatchers.IO) {
                        userDao.getUserById(id)
                    }
                    if (existingUser != null) {
                        Toast.makeText(this@SignupActivity, "이미 사용 중인 아이디입니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        val newUser = User(nickName = nickName, id = id, password = password)
                        withContext(Dispatchers.IO) {
                            userDao.insertUser(newUser)
                        }
                        Toast.makeText(this@SignupActivity, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        // 로그인 화면으로 이동
                        val intent = Intent(this@SignupActivity, InfoActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}
