package com.devronan.e_cart.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.devronan.e_cart.R
import com.devronan.e_cart.firestore.FirestoreClass
import com.devronan.e_cart.models.CartItem
import com.devronan.e_cart.ui.activities.CartListActivity
import com.devronan.e_cart.utils.Constants
import com.devronan.e_cart.utils.GlideLoader
import kotlinx.android.synthetic.main.list_item_cart_layout.view.*

open class CartItemsListAdapter(
    private val context: Context,
    private var cartList: ArrayList<CartItem>,
    private val updateCartItems: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CartItemsListViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.list_item_cart_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemPosition = cartList[position]
        if (holder is CartItemsListViewHolder) {
            GlideLoader(context).loadProductPicture(
                itemPosition.image,
                holder.itemView.iv_cart_item_image
            )
            holder.itemView.tv_cart_item_title.text = itemPosition.title
            holder.itemView.tv_cart_item_price.text = "Rs. ${itemPosition.price}"
            holder.itemView.tv_cart_quantity.text = itemPosition.cart_quantity

            if (itemPosition.cart_quantity == "0") {
                holder.itemView.ib_remove_cart_item.visibility = View.GONE
                holder.itemView.ib_add_cart_item.visibility = View.GONE
                if (updateCartItems) {
                    holder.itemView.ib_delete_cart_item.visibility = View.VISIBLE
                } else {
                    holder.itemView.ib_delete_cart_item.visibility = View.GONE
                }
                holder.itemView.tv_cart_quantity.text =
                    context.resources.getString(R.string.label_out_of_stock)
                holder.itemView.tv_cart_quantity.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorSnackBarError
                    )
                )
            } else {
                if (updateCartItems) {
                    holder.itemView.ib_remove_cart_item.visibility = View.VISIBLE
                    holder.itemView.ib_add_cart_item.visibility = View.VISIBLE
                    holder.itemView.ib_delete_cart_item.visibility = View.VISIBLE
                } else {
                    holder.itemView.ib_remove_cart_item.visibility = View.GONE
                    holder.itemView.ib_add_cart_item.visibility = View.GONE
                    holder.itemView.ib_delete_cart_item.visibility = View.GONE
                }
                holder.itemView.tv_cart_quantity.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorSecondaryText
                    )
                )
            }

            holder.itemView.ib_delete_cart_item.setOnClickListener {
                when (context) {
                    is CartListActivity -> {
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                    }
                }
                FirestoreClass().removeItemFromCart(context, itemPosition.id)
            }

            holder.itemView.ib_add_cart_item.setOnClickListener {
                val cartQuantity: Int = itemPosition.cart_quantity.toInt()

                if (cartQuantity < itemPosition.stock_quantity.toInt()) {
                    val itemHashMap = HashMap<String, Any>()
                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity + 1).toString()

                    if (context is CartListActivity) {
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                    }
                    FirestoreClass().updateMyCart(context, itemPosition.id, itemHashMap)
                } else {
                    if (context is CartListActivity) {
                        context.showErrorSnackBar(
                            context.resources.getString(
                                R.string.message_available_stock,
                                itemPosition.stock_quantity
                            ),
                            true
                        )
                    }
                }
            }

            holder.itemView.ib_remove_cart_item.setOnClickListener {
                if (itemPosition.cart_quantity == "1") {
                    FirestoreClass().removeItemFromCart(context, itemPosition.id)
                } else {
                    val cartQuantity: Int = itemPosition.cart_quantity.toInt()
                    val itemHashMap = HashMap<String, Any>()
                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity - 1).toString()

                    if (context is CartListActivity) {
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                    }
                    FirestoreClass().updateMyCart(context, itemPosition.id, itemHashMap)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    private class CartItemsListViewHolder(view: View) : RecyclerView.ViewHolder(view)
}