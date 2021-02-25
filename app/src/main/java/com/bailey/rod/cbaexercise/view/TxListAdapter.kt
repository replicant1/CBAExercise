package com.bailey.rod.cbaexercise.view

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bailey.rod.cbaexercise.data.XAccountOverview
import com.bailey.rod.cbaexercise.data.XAccountTransaction
import com.bailey.rod.cbaexercise.databinding.TxListItemDateViewBinding
import com.bailey.rod.cbaexercise.databinding.TxListItemHeaderViewBinding
import com.bailey.rod.cbaexercise.databinding.TxListItemTxViewBinding
import com.bailey.rod.cbaexercise.ext.daysAgoLabel
import com.bailey.rod.cbaexercise.ext.getDollarString
import com.google.gson.Gson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TxListAdapter(
    private val context: Context,
    private val accountSummary: XAccountOverview
) :
    RecyclerView.Adapter<TxListAdapter.TxViewHolder>() {

    private val listItemModels: List<TxListItemModel> = createListItemModels()

    interface TxListItemModel

    data class TxAccountHeadingListItemModel(
        val accountName: String?,
        val accountNumber: String?,
        val availableFunds: Float?,
        val accountBalance: Float?
    ) : TxListItemModel

    data class TxDateHeadingListItemModel(
        val date: String?,
        val daysAgo: String?
    ) : TxListItemModel

    data class TxTransactionListItemModel(
        val effectiveDate: String?,
        val description: String?,
        val amount: Float?,
        val isAtm: Boolean?,
        val isPending: Boolean,
        val atmId: String?
    ) : TxListItemModel

    open class TxViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    inner class TxAccountHeadingViewHolder(val binding: TxListItemHeaderViewBinding) :
        TxViewHolder(binding)

    inner class TxDateHeadingViewHolder(val binding: TxListItemDateViewBinding) :
        TxViewHolder(binding)

    inner class TxTransactionViewHolder(val binding: TxListItemTxViewBinding) :
        TxViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TxViewHolder {
        when (viewType) {
            VIEW_TYPE_ACCOUNT_HEADING -> {
                val binding = TxListItemHeaderViewBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
                return TxAccountHeadingViewHolder(binding)
            }
            VIEW_TYPE_TX -> {
                val binding = TxListItemTxViewBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
                return TxTransactionViewHolder(binding)
            }
            VIEW_TYPE_TX_DATE_HEADING -> {
                val binding = TxListItemDateViewBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
                return TxDateHeadingViewHolder(binding)
            }
            else -> {
                throw IllegalArgumentException("Unexpected view type $viewType")
            }
        }
    }

    override fun onBindViewHolder(holder: TxViewHolder, position: Int) {
        when (holder) {
            is TxAccountHeadingViewHolder -> {
                bindAccountHeadingViewHolder(
                    holder,
                    listItemModels[position] as TxAccountHeadingListItemModel
                )
            }
            is TxTransactionViewHolder -> {
                bindTxViewHolder(holder, listItemModels[position] as TxTransactionListItemModel)
            }
            is TxDateHeadingViewHolder -> {
                bindDateHeadingViewHolder(
                    holder,
                    listItemModels[position] as TxDateHeadingListItemModel
                )
            }
        }
    }

    private fun bindDateHeadingViewHolder(
        holder: TxDateHeadingViewHolder,
        model: TxDateHeadingListItemModel
    ) {
        holder.binding.dateHeadingModel = model
        holder.binding.root.setOnClickListener(null)
    }

    private fun bindTxViewHolder(
        holder: TxTransactionViewHolder,
        model: TxTransactionListItemModel
    ) {
        holder.binding.tvTxDetails.text = Html.fromHtml(
            if (model.isPending) "<b>PENDING:</b> ${model.description}" else model.description,
            Html.FROM_HTML_MODE_LEGACY
        )
        holder.binding.tvTxAmount.text = model.amount?.getDollarString()

        if (model.isAtm == true) {
            holder.binding.ivTxAtm.visibility = View.VISIBLE
            holder.binding.root.setOnClickListener() {
                if (model.atmId != null) {
                    val atmData = findAtmById(model.atmId)
                    if (atmData != null) {
                        val action = AccountOverviewFragmentDirections
                            .actionAccountOverviewFragmentToAtmOnMapFragment(atmData)
                        Navigation.findNavController(holder.binding.root).navigate(action)
                    }
                }
            }
        } else {
            holder.binding.ivTxAtm.visibility = View.GONE
            holder.binding.root.setOnClickListener(null)
        }
    }

    private fun bindAccountHeadingViewHolder(
        holder: TxAccountHeadingViewHolder,
        model: TxAccountHeadingListItemModel
    ) {
        holder.binding.accountHeadingModel = model
        holder.binding.root.setOnClickListener(null)
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
                        LocalDate.now().daysAgoLabel(context, txDate)
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