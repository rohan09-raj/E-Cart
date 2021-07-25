package com.devronan.e_cart.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devronan.e_cart.R
import com.devronan.e_cart.models.Order
import com.devronan.e_cart.ui.activities.OrderDetailsActivity
import com.devronan.e_cart.utils.Constants
import com.devronan.e_cart.utils.GlideLoader
import kotlinx.android.synthetic.main.list_item_product_layout.view.*

open class OrdersListAdapter(
    private val context: Context,
    private var list: ArrayList<Order>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OrdersListViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.list_item_product_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemPosition = list[position]

        if (holder is OrdersListViewHolder) {
            GlideLoader(context).loadProductPicture(
                itemPosition.image,
                holder.itemView.iv_item_image
            )
            holder.itemView.tv_item_name.text = itemPosition.title
            holder.itemView.tv_item_price.text = "Rs. ${itemPosition.total_amount}"
            holder.itemView.ib_delete_product.visibility = View.GONE
            holder.itemView.setOnClickListener {
                val intent = Intent(context, OrderDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_ORDER_DETAILS, itemPosition)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class OrdersListViewHolder(view: View) : RecyclerView.ViewHolder(view)
}