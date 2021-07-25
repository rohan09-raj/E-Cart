package com.devronan.e_cart.ui.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devronan.e_cart.R
import com.devronan.e_cart.models.Address
import com.devronan.e_cart.ui.activities.AddEditAddressActivity
import com.devronan.e_cart.ui.activities.CheckoutActivity
import com.devronan.e_cart.utils.Constants
import kotlinx.android.synthetic.main.list_item_address_layout.view.*

class AddressListAdapter(
    private val context: Context,
    private var addressList: ArrayList<Address>,
    private val selectAddress: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AddressListViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.list_item_address_layout,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemPosition = addressList[position]
        if (holder is AddressListViewHolder) {
            holder.itemView.tv_address_full_name.text = itemPosition.name
            holder.itemView.tv_address_type.text = itemPosition.type
            holder.itemView.tv_address_details.text =
                "${itemPosition.address}, ${itemPosition.pin_code}"
            holder.itemView.tv_address_mobile_number.text = itemPosition.mobile_number

            if (selectAddress) {
                holder.itemView.setOnClickListener {
                    val intent = Intent(context, CheckoutActivity::class.java)
                    intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS, itemPosition)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    fun notifyEditItem(activity: Activity, position: Int) {
        val intent = Intent(context, AddEditAddressActivity::class.java)
        intent.putExtra(Constants.EXTRA_ADDRESS_DETAILS, addressList[position])
        activity.startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)
        notifyItemChanged(position) // Notify any registered observers that the item at position has changed.
    }

    class AddressListViewHolder(view: View) : RecyclerView.ViewHolder(view)
}