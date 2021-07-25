package com.devronan.e_cart.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devronan.e_cart.R
import com.devronan.e_cart.models.Product
import com.devronan.e_cart.ui.activities.ProductDetailsActivity
import com.devronan.e_cart.utils.Constants
import com.devronan.e_cart.utils.GlideLoader
import kotlinx.android.synthetic.main.list_item_dashboard_layout.view.*

open class DashboardItemsListAdapter(
    private val context: Context,
    private var dashboardList: ArrayList<Product>

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DashboardItemsViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.list_item_dashboard_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemPosition = dashboardList[position]
        if (holder is DashboardItemsViewHolder) {
            GlideLoader(context).loadProductPicture(
                itemPosition.image,
                holder.itemView.iv_dashboard_item_image
            )
            holder.itemView.tv_dashboard_item_title.text = itemPosition.title
            holder.itemView.tv_dashboard_item_price.text = "Rs. ${itemPosition.price}"
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, itemPosition.product_id)
                intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, itemPosition.user_id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return dashboardList.size
    }

    class DashboardItemsViewHolder(view: View) : RecyclerView.ViewHolder(view)
}