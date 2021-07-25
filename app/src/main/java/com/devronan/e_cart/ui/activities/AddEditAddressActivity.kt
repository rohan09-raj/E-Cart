package com.devronan.e_cart.ui.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.devronan.e_cart.R
import com.devronan.e_cart.firestore.FirestoreClass
import com.devronan.e_cart.models.Address
import com.devronan.e_cart.utils.Constants
import kotlinx.android.synthetic.main.activity_add_edit_address.*
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_user_profile.*

class AddEditAddressActivity : BaseActivity() {

    private var mAddressDetails: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_address)

        setUpActionBar()

        if (intent.hasExtra(Constants.EXTRA_ADDRESS_DETAILS)) {
            mAddressDetails = intent.getParcelableExtra(Constants.EXTRA_ADDRESS_DETAILS)
        }

        if (mAddressDetails != null) {
            if (mAddressDetails!!.id.isNotEmpty()) {
                tv_title.text = resources.getString(R.string.title_edit_address)
                button_submit_address.text = resources.getString(R.string.button_label_update)

                edit_text_full_name.setText(mAddressDetails?.name)
                edit_text_phone_number.setText(mAddressDetails?.mobile_number)
                edit_text_address.setText(mAddressDetails?.address)
                edit_text_pin_code.setText(mAddressDetails?.pin_code)
                edit_text_additional_note.setText(mAddressDetails?.additional_note)

                when (mAddressDetails?.type) {
                    Constants.HOME -> {
                        radio_button_home.isChecked = true
                    }
                    Constants.OFFICE -> {
                        radio_button_office.isChecked = true
                    }
                    else -> {
                        radio_button_other.isChecked = true
                        text_input_layout_other_details.visibility = View.VISIBLE
                        edit_text_other_details.setText(mAddressDetails?.other_details)
                    }
                }
            }
        }

        button_submit_address.setOnClickListener {
            saveAddressToFirestore()
        }

        radio_group_type.setOnCheckedChangeListener { _, checkedID ->
            if (checkedID == R.id.radio_button_other) {
                text_input_layout_other_details.visibility = View.VISIBLE
            } else {
                text_input_layout_other_details.visibility = View.GONE
            }
        }
    }

    private fun setUpActionBar() {

        setSupportActionBar(toolbar_add_edit_address_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_add_edit_address_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun saveAddressToFirestore() {
        val fullName: String = edit_text_full_name.text.toString().trim { it <= ' ' }
        val phoneNumber: String = edit_text_phone_number.text.toString().trim { it <= ' ' }
        val address: String = edit_text_address.text.toString().trim { it <= ' ' }
        val pinCode: String = edit_text_pin_code.text.toString().trim { it <= ' ' }
        val additionalNote: String = edit_text_additional_note.text.toString().trim { it <= ' ' }
        val otherDetails: String = edit_text_other_details.text.toString().trim { it <= ' ' }

        if (validateData()) {
            showProgressDialog(resources.getString(R.string.please_wait))

            val addressType: String =
                when {
                    radio_button_home.isChecked -> {
                        Constants.HOME
                    }
                    radio_button_office.isChecked -> {
                        Constants.OFFICE
                    }
                    else -> {
                        Constants.OTHER
                    }
                }

            val addressModel = Address(
                FirestoreClass().getCurrentUserId(),
                fullName,
                phoneNumber,
                address,
                pinCode,
                additionalNote,
                addressType,
                otherDetails
            )
            if (mAddressDetails != null && mAddressDetails!!.id.isNotEmpty()) {
                FirestoreClass().updateAddress(
                    this@AddEditAddressActivity,
                    addressModel,
                    mAddressDetails!!.id
                )
            } else {
                FirestoreClass().addAddress(this@AddEditAddressActivity, addressModel)
            }
        }
    }

    private fun validateData(): Boolean {
        return when {
            TextUtils.isEmpty(edit_text_full_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_message_enter_full_name), true)
                false
            }
            TextUtils.isEmpty(edit_text_phone_number.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.error_message_enter_phone_number),
                    true
                )
                false
            }
            TextUtils.isEmpty(edit_text_address.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_message_enter_address), true)
                false
            }
            TextUtils.isEmpty(edit_text_pin_code.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_message_enter_pin_code), true)
                false
            }
            radio_button_other.isChecked && TextUtils.isEmpty(
                edit_text_other_details.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.error_message_enter_other_details),
                    true
                )
                false
            }
            else -> {
                true
            }
        }
    }

    fun addUpdateAddressSuccess() {
        val notifySuccessMessage: String =
            if (mAddressDetails != null && mAddressDetails!!.id.isNotEmpty()) {
                resources.getString(R.string.message_your_address_updated_successfully)
            } else {
                resources.getString(R.string.your_address_added_successful_message)
            }

        Toast.makeText(
            this@AddEditAddressActivity,
            notifySuccessMessage,
            Toast.LENGTH_SHORT
        ).show()

        setResult(RESULT_OK)
        finish()
    }
}