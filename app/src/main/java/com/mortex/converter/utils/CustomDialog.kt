package com.mortex.converter.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mortex.converter.R
import com.mortex.converter.data.model.CurrencyRate
import com.mortex.converter.ui.adapter.CurrencyRateAdapter
import com.mortex.converter.ui.adapter.CurrencySelected


open class CustomDialog : DialogFragment() {

    lateinit var adapter: CurrencyRateAdapter
    lateinit var currencySelected: CurrencySelected
    var rates = listOf<CurrencyRate>()
    var isForSell = false

    companion object {
        const val TAG = "CustomDialog"
        private const val TITLE = "TITLE"

        fun newInstance(
            rateList: List<CurrencyRate>,
            listener: CurrencySelected,
            isForSell: Boolean
        ): CustomDialog {
            val args = Bundle()
            val fragment = CustomDialog()
            fragment.arguments = args
            fragment.rates = rateList
            fragment.currencySelected = listener
            fragment.isForSell = isForSell
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.simple_dialog_view, container, false)

        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView(view, rates, currencySelected)
    }

    private fun setupView(view: View, rates: List<CurrencyRate>, listener: CurrencySelected) {
        val rv = view.findViewById<RecyclerView>(R.id.rate_rv)
        adapter = CurrencyRateAdapter(listener,isForSell)
        rv.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv.adapter = adapter

        adapter.setItems(rates)

    }

    override fun onResume() {
        super.onResume()

        val window = dialog!!.window ?: return
        val params = window.attributes
        params.width = 600
        params.height = 800
        window.attributes = params
    }
}