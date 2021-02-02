package com.bailey.rod.cbaexercise.ui

import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bailey.rod.cbaexercise.MapsActivity
import com.bailey.rod.cbaexercise.R
import com.bailey.rod.cbaexercise.data.XAccountActivitySummary
import com.bailey.rod.cbaexercise.data.XAccountTransaction
import com.bailey.rod.cbaexercise.getDollarString
import com.google.gson.Gson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TxListAdapter(private val accountSummary: XAccountActivitySummary) :
    RecyclerView.Adapter<TxListAdapter.TxViewHolder>() {

    private val listItemModels: List<TxListItemModel> = createListItemModels()

    interface TxListItemModel

    data class TxAccountHeadingListItemModel(
        val accountName: String?,
        val accountNumber: String?,
        val availableFunds: Float?,
        val accountBalance: Float?
    ) : TxListItemModel

    data class TxDateHeadingListItemModel(val date: String?, val daysAgo: String?) : TxListItemModel

    data class TxTransactionListItemModel(
        val effectiveDate: String?,
        val description: String?,
        val amount: Float?,
        val isAtm: Boolean?,
        val isPending: Boolean,
        val atmId: String?
    ) : TxListItemModel

    open class TxViewHolder(root: View) : RecyclerView.ViewHolder(root)

    data class TxAccountHeadingViewHolder(val root: View) : TxViewHolder(root) {
        var accountNameTextView: TextView = root.findViewById(R.id.tv_account_name)
        var accountNumberTextView: TextView = root.findViewById(R.id.tv_account_number)
        var availableFundsTextView: TextView = root.findViewById(R.id.tv_available_funds_value)
        var accountBalanceTextView: TextView = root.findViewById(R.id.tv_account_balance_value)
    }

    data class TxDateHeadingViewHolder(val root: View) : TxViewHolder(root) {
        var txDateTextView: TextView = root.findViewById(R.id.tv_tx_date)
        var txAgeTextView: TextView = root.findViewById(R.id.tv_tx_age)
    }

    data class TxTransactionViewHolder(val root: View) : TxViewHolder(root) {
        var txDetailsTextView: TextView = root.findViewById(R.id.tv_tx_details)
        var txAmountTextView: TextView = root.findViewById(R.id.tv_tx_amount)
        var txAtmImageView: ImageView = root.findViewById(R.id.iv_tx_atm)
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
            VIEW_TYPE_TX_DATE_HEADING -> {
                return TxDateHeadingViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.tx_list_item_date_view, parent, false
                    )
                )
            }
            else -> {
                throw IllegalArgumentException("Unexpected view type $viewType")
            }
        }
    }

    override fun onBindViewHolder(holder: TxViewHolder, position: Int) {
        when (holder) {
            is TxAccountHeadingViewHolder -> {
                val model: TxAccountHeadingListItemModel =
                    listItemModels[position] as TxAccountHeadingListItemModel
                holder.accountNameTextView.text = model.accountName
                holder.accountNumberTextView.text = model.accountNumber
                holder.availableFundsTextView.text = model.availableFunds?.getDollarString()
                holder.accountBalanceTextView.text = model.accountBalance?.getDollarString()
                holder.root.setOnClickListener(null)
            }
            is TxTransactionViewHolder -> {
                val model: TxTransactionListItemModel =
                    listItemModels[position] as TxTransactionListItemModel
                holder.txDetailsTextView.text = Html.fromHtml(
                    if (model.isPending) "<b>PENDING:</b> ${model.description}" else model.description,
                    Html.FROM_HTML_MODE_LEGACY
                )
                holder.txAmountTextView.text = model.amount?.getDollarString()

                if (model.isAtm == true) {
                    holder.txAtmImageView.visibility = View.VISIBLE
                    holder.root.setOnClickListener() {
                        if (model.atmId != null) {
                            val atmData = findAtmById(model.atmId)
                            if (atmData != null) {
                                val intent = Intent(holder.root.context, MapsActivity::class.java)
                                intent.putExtra(MapsActivity.ARG_ATM, atmData)
                                holder.root.context.startActivity(intent)
                            }
                        }
                    }
                } else {
                    holder.txAtmImageView.visibility = View.GONE
                    holder.root.setOnClickListener(null)
                }
            }
            is TxDateHeadingViewHolder -> {
                val model: TxDateHeadingListItemModel =
                    listItemModels[position] as TxDateHeadingListItemModel
                holder.txDateTextView.text = model.date
                holder.txAgeTextView.text = model.daysAgo
                holder.root.setOnClickListener(null)
            }
        }
    }

    override fun getItemCount(): Int {
        return listItemModels.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (listItemModels[position]) {
            is TxAccountHeadingListItemModel -> VIEW_TYPE_ACCOUNT_HEADING
            is TxDateHeadingListItemModel -> VIEW_TYPE_TX_DATE_HEADING
            is TxTransactionListItemModel -> VIEW_TYPE_TX
            else -> -1
        }
    }

    private fun findAtmById(atmId: String?): String? {
        if (atmId != null) {
            for (atm in accountSummary.atms ?: emptyList()) {
                if (atm.id.equals(atmId)) {
                    return Gson().toJson(atm)
                }
            }
        }
        return null
    }

    /**
     * @return List of model objects, one per list item, in the order they will appear in the tx list.
     * Pending and non-pending tx's both appear in the list. Sorted order is date descending. First item
     * is always an instance of TxAccountHeadingListItemModel. Following items are TxDateHeadingListItemModel
     * and TxTransactionListItemModel
     */
    private fun createListItemModels(): List<TxListItemModel> {
        // Put all tx into sorted list by date
        val pendingAndNonPendingTx =
            (accountSummary.transactions ?: emptyList()) + (accountSummary.pendingTransactions
                ?: emptyList())
        val sortedTx: List<XAccountTransaction> = pendingAndNonPendingTx.sortedByDescending {
            LocalDate.parse(it.effectiveDate, DATE_TIME_INPUT_FORMATTER)
        }

        val result = mutableListOf<TxListItemModel>()

        // First item is account heading list item
        result.add(
            TxAccountHeadingListItemModel(
                accountSummary.account?.accountName,
                accountSummary.account?.accountNumber,
                accountSummary.account?.available,
                accountSummary.account?.balance
            )
        )

        // Transactions sorted by descending date, with date headings separating distinct dates
        var currentDate: LocalDate? = null
        for ((i, tx) in sortedTx.withIndex()) {
            val txDate = LocalDate.parse(tx.effectiveDate, DATE_TIME_INPUT_FORMATTER)
            if (i == 0 || !txDate.isEqual(currentDate)) {
                result.add(
                    TxDateHeadingListItemModel(
                        DATE_TIME_OUTPUT_FORMATTER.format(txDate),
                        LocalDate.now().daysAgoLabel(txDate)
                    )
                )
                currentDate = txDate
            }
            result.add(
                TxTransactionListItemModel(
                    tx.effectiveDate,
                    tx.description,
                    tx.amount,
                    tx.atmId != null,
                    tx.pending,
                    tx.atmId
                )
            )
        }
        return result
    }

    companion object {
        const val VIEW_TYPE_ACCOUNT_HEADING = 0
        const val VIEW_TYPE_TX_DATE_HEADING = 1
        const val VIEW_TYPE_TX = 2

        val DATE_TIME_OUTPUT_FORMATTER: DateTimeFormatter =
            DateTimeFormatter.ofPattern("dd MMM yyyy")
        val DATE_TIME_INPUT_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy")
    }
}