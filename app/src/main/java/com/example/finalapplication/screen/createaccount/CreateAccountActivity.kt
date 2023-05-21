package com.example.finalapplication.screen.createaccount

import android.app.ProgressDialog
import android.content.Intent
import android.view.View
import android.widget.TextView
import com.example.finalapplication.data.model.Account
import com.example.finalapplication.data.model.User
import com.example.finalapplication.databinding.ActivityCreateAccountBinding
import com.example.finalapplication.screen.login.LoginActivity
import com.example.finalapplication.screen.main.MainActivity
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.base.BaseActivity
import com.example.finalapplication.utils.showMessage
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateAccountActivity :
    BaseActivity<ActivityCreateAccountBinding>(ActivityCreateAccountBinding::inflate) {

    private val createAccountViewModel: CreateAccountViewModel by viewModel()
    private lateinit var loading: ProgressDialog

    override fun handleEvent() {
        binding.textLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.buttonSignup.setOnClickListener {
            val email = binding.textEmail.text.toString().trim()
            val password = binding.textPassword.text.toString().trim()
            val name = binding.textUsername.text.toString().trim()
            val confirmPassword = binding.textConfirmPassword.text.toString().trim()
            val user = User(Constant.ROLE_CLIENT, name, Account(email, password))
            if(validateSignUp(confirmPassword, user)) createAccountViewModel.createAccount(user)
        }
    }

    private fun validateSignUp(confirmPassword: String, user: User): Boolean {
        var result = true
        if(!user.validateName()){
            Constant.ERROR_NAME_EMPTY.showMessage(applicationContext)
            result =false
        }
        if(user.userAccount?.validateEmail()==false){
            Constant.ERROR_VALIDATE_MAIL.showMessage(applicationContext)
            result = false
        }
        if(user.userAccount?.validatePassWord()==false){
            Constant.ERROR_PASSWORD_SHORT.showMessage(applicationContext)
            result = false
        }
        if(user.userAccount?.validateConfirmPassword(confirmPassword) == false){
            Constant.ERROR_CONFIRM_PASSWORD.showMessage(applicationContext)
            result = false
        }
        return result
    }

    override fun initData() {
        loading = ProgressDialog(this)
        loading.setMessage(Constant.MSG_SIGN_IN)
        loading.setCancelable(false)
        val email = intent.getStringExtra(Constant.INTENT_EMAIL)
        if(email!= null){
            (binding.textEmail as TextView).text = email.toString()
            (binding.textEmail as TextView).isEnabled = false
        }
    }

    override fun bindData() {
        createAccountViewModel.isRegisterSuccess.observe(this) { result ->
            if (result == true) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        createAccountViewModel.errorMessage.observe(this) { msg ->
            msg.showMessage(applicationContext)
        }
        createAccountViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) loading.show()
            else loading.dismiss()
        }
    }
}
