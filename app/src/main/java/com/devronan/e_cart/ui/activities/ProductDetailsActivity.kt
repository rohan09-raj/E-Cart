package com.devronan.e_cart.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.devronan.e_cart.R
import com.devronan.e_cart.firestore.FirestoreClass
import com.devronan.e_cart.models.CartItem
import com.devronan.e_cart.models.Product
import com.devronan.e_cart.utils.Constants
import com.devronan.e_cart.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.list_item_cart_layout.view.*

class ProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private var mProductID: String = ""
    private var mProductOwnerID: String = ""
    private lateinit var mProductDetails: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        setUpActionBar()

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductID = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            mProductOwnerID = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }

        if (FirestoreClass().getCurrentUserId() == mProductOwnerID) {
            button_add_to_cart.visibility = View.GONE
            button_go_to_cart.visibility = View.GONE
        } else {
            button_add_to_cart.visibility = View.VISIBLE
        }

        getProductDetails()

        button_add_to_cart.setOnClickListener(this)
        button_go_to_cart.setOnClickListener(this)
    }

    private fun setUpActionBar() {

        setSupportActionBar(toolbar_product_details_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_product_details_activity.setNavigationOnClickListener { onBackPressed() }
    }

    fun getProductDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductDetails(this, mProductID)
    }

    fun productDetailsSuccess(product: Product) {
        mProductDetails = product
        GlideLoader(this@ProductDetailsActivity).loadUserPicture(
            product.image,
            iv_product_details_image
        )
        tv_product_details_title.text = product.title
        tv_product_details_price.text = "Rs. ${product.price}"
        tv_product_details_description.text = product.description
        tv_product_details_available_quantity.text = product.stock_quantity

        if (product.stock_quantity.toInt() == 0) {
            hideProgressDialog()
            button_add_to_cart.visibility = View.GONE
            tv_product_details_available_quantity.text =
                resources.getString(R.string.label_out_of_stock)
            tv_product_details_available_quantity.setTextColor(
                ContextCompat.getColor(
                    this@ProductDetailsActivity,
                    R.color.colorSnackBarError
                )
            )
        } else {
            if (FirestoreClass().getCurrentUserId() == product.user_id) {
                hideProgressDialog()
            } else {
                FirestoreClass().checkIfItemExistInCart(this, mProductID)
            }
        }
    }

    private fun addToCart() {
        val cartItem = CartItem(
            FirestoreClass().getCurrentUserId(),
            mProductOwnerID,
            mProductID,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addCartItems(this, cartItem)
    }

    fun addToCartSuccess() {
        hideProgressDialog()
        Toast.makeText(
            this@ProductDetailsActivity,
            resources.getString(R.string.item_added_to_cart_success_message),
            Toast.LENGTH_SHORT
        ).show()

        button_add_to_cart.visibility = View.GONE
        button_go_to_cart.visibility = View.VISIBLE
    }

    fun productExistsInCart() {
        hideProgressDialog()
        button_add_to_cart.visibility = View.GONE
        button_go_to_cart.visibility = View.VISIBLE
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.button_add_to_cart -> {
                    addToCart()
                }
                R.id.button_go_to_cart -> {
                    val intent = Intent(this@ProductDetailsActivity, CartListActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}