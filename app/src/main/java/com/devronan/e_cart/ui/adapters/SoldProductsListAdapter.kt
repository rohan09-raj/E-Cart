package com.devronan.e_cart.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devronan.e_cart.R
import com.devronan.e_cart.models.SoldProduct
import com.devronan.e_cart.ui.activities.SoldProductDetailsActivity
import com.devronan.e_cart.utils.Constants
import com.devronan.e_cart.utils.GlideLoader
import kotlinx.android.synthetic.main.list_item_product_layout.view.*

open class SoldProductsListAdapter(
    private val context: Context,
    private var soldProductsList: ArrayList<SoldProduct>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SoldProductsListViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.list_item_product_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = soldProductsList[position]

        if (holder is SoldProductsListViewHolder) {

            GlideLoader(context).loadProductPicture(
                model.image,
                holder.itemView.iv_item_image
            )

            holder.itemView.tv_item_name.text = model.title
            holder.itemView.tv_item_price.text = "Rs. ${model.price}"

            holder.itemView.ib_delete_product.visibility = View.GONE

            holder.itemView.setOnClickListener {
                val intent = Intent(context, SoldProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS, model)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return soldProductsList.size
    }

    class SoldProductsListViewHolder(view: View) : RecyclerView.ViewHolder(view)
}