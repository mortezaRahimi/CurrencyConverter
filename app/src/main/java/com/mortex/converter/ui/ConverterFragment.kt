package com.mortex.converter.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mortex.converter.data.model.BalanceItem
import com.mortex.converter.data.model.CurrencyRate
import com.mortex.converter.databinding.FragmentConverterBinding
import com.mortex.converter.ui.adapter.CurrencySelected
import com.mortex.converter.ui.adapter.MyBalanceAdapter
import com.mortex.converter.utils.CustomDialog
import com.mortex.converter.utils.Functions.showDialog
import com.mortex.converter.utils.Functions.showToast
import com.mortex.converter.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class ConverterFragment : Fragment(), CurrencySelected {

    lateinit var buySpinner: CustomDialog
    lateinit var sellSpinner: CustomDialog
    private val viewModel: ConverterViewModel by viewModels()
    lateinit var binding: FragmentConverterBinding
    lateinit var balanceAdapter: MyBalanceAdapter
    private var myBalanceItems = arrayListOf<BalanceItem>()
    private val rates = arrayListOf<CurrencyRate>()
    private var sellCurrencyRate: CurrencyRate? = null
    private var buyCurrencyRate: CurrencyRate? = null
    private var firstTime = true
    private var counter = 0
    private var veryFirsTimeApiCall = true

    lateinit var mainHandler: Handler

    //to load rates every 5 seconds
    private val updateTextTask = object : Runnable {
        override fun run() {
            getRates()
            mainHandler.postDelayed(this, 5000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainHandler = Handler(Looper.getMainLooper())
    }


    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateTextTask)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updateTextTask)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentConverterBinding.inflate(inflater, container, false)

        init()

        getRates()

        return binding.root
    }

    private fun init() {

        viewModel.setBalance(1000.00)
        //balanceRV
        balanceAdapter = MyBalanceAdapter()
        binding.myBalanceRv.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.myBalanceRv.adapter = balanceAdapter

        //ratesDialog
        binding.btnSelectSellType.setOnClickListener {
            if (rates.size > 0) {
                sellSpinner = CustomDialog.newInstance(rates, this, true)
                sellSpinner.show(requireActivity().supportFragmentManager, CustomDialog.TAG)
            }
        }
        binding.btnSelectReceiveType.setOnClickListener {
            if (rates.size > 0) {
                buySpinner = CustomDialog.newInstance(rates, this, false)
                buySpinner.show(requireActivity().supportFragmentManager, CustomDialog.TAG)
            }
        }


        binding.btnSubmit.setOnClickListener {

            //check if have enough balance to sell or not sell and buy items selected
            var haveBalance = false
            for (i in myBalanceItems) {
                if (sellCurrencyRate?.currency == i.currency && binding.sellInputValue.text.toString()
                        .isNotEmpty() && i.amount >= binding.sellInputValue.text?.toString()
                        ?.toDouble() ?: 1000.00 && buyCurrencyRate?.currency?.isNotEmpty() == true
                ) {
                    haveBalance = true
                }
            }


            if (haveBalance) {
                counter += 1
                var receiveValue: Double

                var sellValue: Double = binding.sellInputValue.text.toString().toDouble()
                receiveValue = sellValue * buyCurrencyRate?.rate!!
                receiveValue = (receiveValue * 10000.0).roundToInt() / 10000.0
                binding.buyInputValue.setText(
                    receiveValue.toString(),
                    TextView.BufferType.EDITABLE
                )

                var desc =
                    "You have converted ${binding.sellInputValue.text.toString()} ${sellCurrencyRate!!.currency} to $receiveValue ${buyCurrencyRate!!.currency} "

                if (counter >= 5) {
                    sellValue += (0.7 * sellValue) / 100
                    desc = "$desc. Commission Fee - 0.70 EUR."
                }
                //change balance amounts from sell and buy amount
                setBalanceAmount(receiveValue, buyCurrencyRate!!.currency, true)
                setBalanceAmount(sellValue, sellCurrencyRate!!.currency, false)

                showConvertDialog(desc)
            }

        }
    }

    private fun showConvertDialog(msg: String) {
        showDialog(msg)
    }


    private fun getRates() {
        viewModel.getRates().observe(viewLifecycleOwner, {
            binding.loading.visibility = View.GONE
            when (it.status) {
                Resource.Status.ERROR -> {
                    showToast(it.message.toString())
                }

                Resource.Status.SUCCESS -> {
                    Toast.makeText(requireContext(), "Rates updated", Toast.LENGTH_SHORT).show()
                    val ratesHashmap = it.data?.rates?.toList()
                    for (i in ratesHashmap!!) {
                        val item = CurrencyRate(i.first, i.second)
                        rates.add(item)

                        //sort with USD and EUR and GBP on top
                        if (rates[rates.lastIndex].currency == "EUR" ||
                            rates[rates.lastIndex].currency == "USD" ||
                            rates[rates.lastIndex].currency == "GBP"
                        ) {
                            val index = rates.lastIndex
                            rates.removeAt(index)
                            rates.add(0, item)
                        }
                    }

                    if (veryFirsTimeApiCall) {
                        veryFirsTimeApiCall = false
                        balanceAdapter.setItems(createBalanceItems(rates))
                    }
                }

                Resource.Status.LOADING -> {
                    binding.loading.visibility = View.VISIBLE
                }
                Resource.Status.TOKEN_EXPIRED -> {
                    showToast("TOKEN is Expired")
                }
            }
        })
    }


    private fun setBalanceAmount(amount: Double, currency: String, add: Boolean) {
        for (i in myBalanceItems) {
            if (i.currency == currency) {
                if (add)
                    i.amount += amount
                else
                    i.amount -= amount
            }
        }
        balanceAdapter.setItems(myBalanceItems)
    }

    private fun createBalanceItems(currencies: ArrayList<CurrencyRate>): List<BalanceItem> {
        //create list of balance items based on currencies
        myBalanceItems.clear()
        for (i in currencies) {
            val item = BalanceItem(i.currency, 0.00)
            myBalanceItems.add(item)
            if (firstTime)
                if (item.currency == "EUR") {
                    item.amount = viewModel.getBalance()!!
                    firstTime = false
                }
        }
        return myBalanceItems
    }

    override fun sellCurrencySelected(currencyRate: CurrencyRate) {
        sellSpinner.dismiss()
        binding.sellCurrencyValue.text = currencyRate.currency
        sellCurrencyRate = currencyRate
    }

    override fun buyCurrencySelected(currencyRate: CurrencyRate) {
        buySpinner.dismiss()
        binding.receiveCurrencyValue.text = currencyRate.currency
        buyCurrencyRate = currencyRate
    }


}