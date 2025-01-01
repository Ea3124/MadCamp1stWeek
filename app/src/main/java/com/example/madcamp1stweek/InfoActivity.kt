// InfoActivity.kt
package com.example.madcamp1stweek

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class InfoActivity : AppCompatActivity(), SignupFragment.SignupListener {

    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonGoToSignup: Button

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        // 뷰 참조
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonGoToSignup = findViewById(R.id.buttonGoToSignup)

        // 데이터베이스 초기화
        db = AppDatabase.getDatabase(this)
        userDao = db.userDao()

        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                coroutineScope.launch {
                    val user = withContext(Dispatchers.IO) {
                        userDao.getUser(username, password)
                    }
                    if (user != null) {
                        if (user.loginCount == 0) {
                            // 첫 로그인인 경우
                            withContext(Dispatchers.IO) {
                                userDao.updateUser(user.copy(loginCount = user.loginCount + 1))
                            }
                            val intent = Intent(this@InfoActivity, FirstActivity::class.java) // 첫 로그인 전용 Activity
                            intent.putExtra("NICK_NAME", user.nickName)
                            startActivity(intent)
                        } else {
                            // 첫 로그인이 아닌 경우
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@InfoActivity, "로그인 성공! 환영합니다, ${user.nickName}님.", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@InfoActivity, MainActivity::class.java,)
                                intent.putExtra("NICK_NAME", user.nickName)
                                startActivity(intent)
                                finish()
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@InfoActivity, "아이디 또는 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }

        buttonGoToSignup.setOnClickListener {
            // SignupFragment 팝업으로 띄우기
            val signupFragment = SignupFragment()
            signupFragment.show(supportFragmentManager, "SignupFragment")
        }
    }

    override fun onSignupSuccess() {
        // 회원가입 성공 후 추가 작업이 필요하면 여기에 작성
        Toast.makeText(this, "회원가입이 성공적으로 완료되었습니다. 로그인해주세요.", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}
