package com.devronan.e_cart.ui.activities

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.devronan.e_cart.R
import com.devronan.e_cart.firestore.FirestoreClass
import com.devronan.e_cart.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.edit_text_password
import kotlinx.android.synthetic.main.activity_register.tv_login

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setUpActionBar()

        tv_login.setOnClickListener {
            onBackPressed()
        }

        button_register.setOnClickListener {
            registerUser()
        }

        edit_text_password.typeface = Typeface.DEFAULT
        edit_text_password.transformationMethod = PasswordTransformationMethod()
        edit_text_confirm_password.typeface = Typeface.DEFAULT
        edit_text_confirm_password.transformationMethod = PasswordTransformationMethod()
    }

    private fun setUpActionBar() {

        setSupportActionBar(toolbar_register_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_register_activity.setNavigationOnClickListener { onBackPressed() }
    }

    //Function to validate the entries entered by the user
    private fun validateRegistrationDetails(): Boolean {
        return when {
            TextUtils.isEmpty(edit_text_first_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.error_message_enter_first_name),
                    true
                )
                false
            }
            TextUtils.isEmpty(edit_text_last_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_message_enter_last_name), true)
                false
            }
            TextUtils.isEmpty(edit_text_email_id.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_message_enter_email_id), true)
                false
            }
            TextUtils.isEmpty(edit_text_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_message_enter_password), true)
                false
            }
            TextUtils.isEmpty(edit_text_confirm_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.error_message_enter_confirm_password),
                    true
                )
                false
            }
            edit_text_password.text.toString()
                .trim { it <= ' ' } != edit_text_confirm_password.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(
                    resources.getString(R.string.error_message_password_and_confirm_password_mismatch),
                    true
                )
                false
            }
            !check_box_terms_condition.isChecked -> {
                showErrorSnackBar(
                    resources.getString(R.string.error_message_agree_terms_and_condition),
                    true
                )
                false
            }
            else -> {
                true
            }
        }
    }

    private fun registerUser() {
        //Check with validate function whether entries are valid or not
        if (validateRegistrationDetails()) {

            showProgressDialog(resources.getString(R.string.please_wait))

            val email: String = edit_text_email_id.text.toString().trim { it <= ' ' }
            val password: String = edit_text_password.text.toString().trim { it <= ' ' }

            //Create an instance and register a user with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    //If the registration is successfully done
                    if (task.isSuccessful) {
                        //Firebase registered user
                        val firebaseUser: FirebaseUser = task.result!!.user!!

                        val user = User(
                            firebaseUser.uid,
                            edit_text_first_name.text.toString().trim { it <= ' ' },
                            edit_text_last_name.text.toString().trim { it <= ' ' },
                            edit_text_email_id.text.toString().trim { it <= ' ' }
                        )

                        FirestoreClass().registerUser(this@RegisterActivity, user)

                    } else {
                        //If the registration is not successful
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    fun userRegistrationSuccess() {
        //Hide the progress dialog
        hideProgressDialog()
        Toast.makeText(this@RegisterActivity, R.string.register_success, Toast.LENGTH_SHORT).show()
    }
}
