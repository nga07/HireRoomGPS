package com.example.finalapplication.screen.login

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.finalapplication.data.model.Account
import com.example.finalapplication.databinding.ActivityLoginBinding
import com.example.finalapplication.screen.createaccount.CreateAccountActivity
import com.example.finalapplication.screen.main.MainActivity
import com.example.finalapplication.screen.mainstaff.MainStaffActivity
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.base.BaseActivity
import com.example.finalapplication.utils.showMessage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    private val loginViewModel: LoginViewModel by viewModel()

    private lateinit var loading: ProgressDialog
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private var email : String? = null
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK ) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    task.getResult(ApiException::class.java)
                    Log.v("aaaaaa", "login successs")
                    val acct = GoogleSignIn.getLastSignedInAccount(this)
                    email = acct?.email
                    Log.v("aaaaaa", email.toString())
                    loginViewModel.getUserByEmail(email.toString())
                } catch (e : ApiException){
                    Constant.ERROR_.showMessage(this)
                }
            }
        }

    override fun handleEvent() {
        binding.apply {
            textSignup.setOnClickListener {
                val intent = Intent(this@LoginActivity, CreateAccountActivity::class.java)
                startActivity(intent)
                finish()
            }
            buttonLogin.setOnClickListener {
                val email = textEmail.text.toString()
                val password = textPassword.text.toString()
                val account = Account(email, password)
                if (account.validateAccount()) {
                    Toast.makeText(
                        this@LoginActivity,
                        Constant.ERROR_ACCOUNT,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    loginViewModel.login(account)
                }
            }

            buttonLoginGoogle.setOnClickListener {
                val intent = gsc.signInIntent
                activityResultLauncher.launch(intent)
            }
            textForgotpassword.setOnClickListener {
                val email = textEmail.text.toString()
                if (email.isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        Constant.ERROE_EMAIL_EMPTY,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                loginViewModel.forgotPassword(email)
            }
        }
    }

    override fun initData() {
        loading = ProgressDialog(this)
        loading.setMessage(Constant.MSG_SIGN_IN)
        loading.setCancelable(false)
        gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this, gso)
    }

    override fun bindData() {
        loginViewModel.isLoginSuccess.observe(this@LoginActivity) { result ->
            if (result == true) {
                loginViewModel.getCurrentUser()
            }
        }
        loginViewModel.isLoading.observe(this) { data ->
            if (data) loading.show()
            else loading.hide()
        }
        loginViewModel.isResetSuccess.observe(this@LoginActivity) { result ->
            if (result)
                Toast.makeText(
                    applicationContext,
                    Constant.MSG_CHECK_MAIL,
                    Toast.LENGTH_SHORT
                ).show()
        }
        loginViewModel.errorMessage.observe(this@LoginActivity) { msg ->
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
        }
        var temp=0
        loginViewModel.user.observe(this) { data ->
            if (data != null) {
                if(temp>0) return@observe
                temp++
                if (data.role == Constant.ROLE_CLIENT) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                if(data.role == Constant.ROLE_STAFF){
                    val intent = Intent(this@LoginActivity, MainStaffActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        loginViewModel.userEmpty.observe(this){data ->
            if(data) {
                val intent = Intent(this@LoginActivity, CreateAccountActivity::class.java)
                intent.putExtra(Constant.INTENT_EMAIL,email.toString() )
                startActivity(intent)
                finish()
            }
        }
    }
}
