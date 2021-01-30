package com.bailey.rod.cbaexercise.ui

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bailey.rod.cbaexercise.R
import com.bailey.rod.cbaexercise.data.XAccountActivitySummary
import com.bailey.rod.cbaexercise.data.XAccountTransaction
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class TxListAdapter(private val accountSummary: XAccountActivitySummary) :
    RecyclerView.Adapter<TxListAdapter.TxViewHolder>() {

    /**
     * Includes both pending and non-pending transactions sorted into reverse order
     * by 'effectiveDate'.
     */
    private val sortedTx: List<XAccountTransaction>

    init {
        sortedTx = sortPendingAndNonPendingTxDateDescending()
    }

    open class TxViewHolder(root: View) : RecyclerView.ViewHolder(root) {
    }

    data class TxAccountHeadingViewHolder(val root: View) : TxViewHolder(root) {
        var accountNameTextView : TextView = root.findViewById(R.id.tv_account_name)
        var accountNumberTextView : TextView = root.findViewById(R.id.tv_account_number)
        var availableFundsTextView : TextView = root.findViewById(R.id.tv_available_funds_value)
        var accountBalance : TextView = root.findViewById(R.id.tv_account_balance_value)
    }

    data class TxDateHeadingViewHolder(val root: View) : TxViewHolder(root) {
        var txDateTextView : TextView = root.findViewById(R.id.tv_tx_date)
        var txAgeTextView : TextView = root.findViewById(R.id.tv_tx_age)
    }

    data class TxTransactionViewHolder(val root: View) : TxViewHolder(root) {
        var txDetailsTextView : TextView = root.findViewById(R.id.tv_tx_details)
        var txAmountTextView : TextView = root.findViewById(R.id.tv_tx_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TxViewHolder {
        when (viewType) {
            VIEW_TYPE_ACCOUNT_HEADING -> {
                return TxAccountHeadingViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.tx_list_item_header_view, parent, false
                    )
                )
            }
            VIEW_TYPE_TX -> {
                return TxTransactionViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.tx_list_item_tx_view, parent, false
                    )
                )
            }
            else -> {
                return TxAccountHeadingViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.tx_list_item_date_view, parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: TxViewHolder, position: Int) {
        if (holder is TxAccountHeadingViewHolder) {
            holder.accountNameTextView.text = accountSummary.account?.accountName
            holder.accountNumberTextView.text = accountSummary.account?.accountNumber
            holder.availableFundsTextView.text = getCurrencyAmountAsString(accountSummary.account?.available)
            holder.accountBalance.text = getCurrencyAmountAsString(accountSummary.account?.balance)
        } else if (holder is TxTransactionViewHolder){
            val tx = sortedTx[position - 1] // -1 allows for account header in list
            val htmlDetails = if (tx.pending) "<b>PENDING:</b> ${tx.description}" else tx.description
            holder.txDetailsTextView.text = Html.fromHtml(htmlDetails)
            holder.txAmountTextView.text = getCurrencyAmountAsString(tx.amount)
        }
//        if (!sortedTx.isNullOrEmpty()) {
//            val tx = sortedTx[position]
//            println("At position $position, tx=$tx")
//            holder.textView1.text = if (tx.pending) "Pending ${tx.effectiveDate}" else tx.effectiveDate
//            holder.textView2.text = tx.description + " $" + tx.amount
//        }
    }

    override fun getItemCount(): Int {
        return (sortedTx.size + 1) // +1 = account heading
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return VIEW_TYPE_ACCOUNT_HEADING
        } else {
            return VIEW_TYPE_TX
        }
    }

    private fun getCurrencyAmountAsString(dollars: Float?) : String {
        val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        format.currency = Currency.getInstance("AUD")
        return format.format(dollars)
    }

    private fun sortPendingAndNonPendingTxDateDescending(): List<XAccountTransaction> {
        val dateTimeFormatter : DateTimeFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy")
        val pendingAndNonPendingTx = (accountSummary.transactions ?: emptyList()) + (accountSummary.pendingTransactions ?: emptyList())
        return pendingAndNonPendingTx.sortedByDescending {
            LocalDate.parse(it.effectiveDate, dateTimeFormatter)
        }
    }

    companion object {
        const val VIEW_TYPE_ACCOUNT_HEADING = 0
        const val VIEW_TYPE_TX_DATE_HEADING = 1
        const val VIEW_TYPE_TX = 2
    }
}