package com.devronan.e_cart.ui.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.devronan.e_cart.R
import com.devronan.e_cart.firestore.FirestoreClass
import com.devronan.e_cart.models.User
import com.devronan.e_cart.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.edit_text_password
import kotlinx.android.synthetic.main.activity_register.*


class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        //Click event assigned to Forgot Password text
        tv_forgot_password.setOnClickListener(this)
        //Click event assigned to Login button
        button_login.setOnClickListener(this)
        //Click event assigned to Register text
        tv_register.setOnClickListener(this)

        edit_text_password.typeface = Typeface.DEFAULT
        edit_text_password.transformationMethod = PasswordTransformationMethod()
    }

    fun userLoggedInSuccess(user: User) {
        //Hide the progress dialog
        hideProgressDialog()

        if (user.profileCompleted == 0) {
            //If the user profile is incomplete then launch the UserProfileActivity
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else {
            //Redirect the user to the MainActivity after login
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        }
        finish()
    }

    //In login screen, the clickable components are Login Button, Forgot Password text and Register text
    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.tv_forgot_password -> {
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }

                R.id.button_login -> {
                    loginRegisteredUser()
                }

                R.id.tv_register -> {
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(edit_text_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_message_enter_email_id), true)
                false
            }

            TextUtils.isEmpty(edit_text_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_message_enter_password), true)
                false
            }

            else -> {
                true
            }
        }
    }

    private fun loginRegisteredUser() {
        if (validateLoginDetails()) {

            //Show the progress dialog
            showProgressDialog(resources.getString(R.string.please_wait))

            //Get the text from edit text and trim the space
            val email = edit_text_email.text.toString().trim { it <= ' ' }
            val password = edit_text_password.text.toString().trim { it <= ' ' }

            //Login using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        FirestoreClass().getUserDetails(this@LoginActivity)
                    } else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }

        }
    }
}