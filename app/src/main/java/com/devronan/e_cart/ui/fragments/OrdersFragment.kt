package com.devronan.e_cart.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.devronan.e_cart.R
import com.devronan.e_cart.databinding.FragmentOrdersBinding
import com.devronan.e_cart.firestore.FirestoreClass
import com.devronan.e_cart.models.Order
import com.devronan.e_cart.ui.adapters.OrdersListAdapter
import kotlinx.android.synthetic.main.fragment_orders.*

class OrdersFragment : BaseFragment() {

    private var _binding: FragmentOrdersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun populateOrdersListInUI(ordersList: ArrayList<Order>) {
        hideProgressDialog()

        if (ordersList.size > 0) {
            rv_order_items.visibility = View.VISIBLE
            tv_no_orders_found.visibility = View.GONE

            rv_order_items.layoutManager = LinearLayoutManager(activity)
            rv_order_items.setHasFixedSize(true)
            val ordersAdapter = OrdersListAdapter(requireActivity(), ordersList)
            rv_order_items.adapter = ordersAdapter
        } else {
            rv_order_items.visibility = View.GONE
            tv_no_orders_found.visibility = View.VISIBLE
        }
    }

    private fun getOrdersList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getOrdersList(this@OrdersFragment)
    }

    override fun onResume() {
        super.onResume()
        getOrdersList()
    }
}