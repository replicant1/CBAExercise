package com.bailey.rod.cbaexercise.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bailey.rod.cbaexercise.data.XAccountActivitySummary
import com.bailey.rod.cbaexercise.data.XAccountTransaction
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    data class TxViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        var textView1: TextView = root.findViewById(android.R.id.text1)
        var textView2: TextView = root.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TxViewHolder {
        return TxViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_2, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TxViewHolder, position: Int) {
        if (!sortedTx.isNullOrEmpty()) {
            val tx = sortedTx[position]
            println("At position $position, tx=$tx")
            holder.textView1.text = if (tx.pending) "Pending ${tx.effectiveDate}" else tx.effectiveDate
            holder.textView2.text = tx.description + " $" + tx.amount
        }
    }

    override fun getItemCount(): Int {
        return sortedTx.size ?: 0
    }

    private fun sortPendingAndNonPendingTxDateDescending(): List<XAccountTransaction> {
        val dateTimeFormatter : DateTimeFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy")
        val pendingAndNonPendingTx = (accountSummary.transactions ?: emptyList()) + (accountSummary.pendingTransactions ?: emptyList())
        return pendingAndNonPendingTx.sortedByDescending {
            LocalDate.parse(it.effectiveDate, dateTimeFormatter)
        }
    }

    companion object {
        const val VIEW_TYPE_LIST_HEADING = 0
        const val VIEW_TYPE_DATE_HEADING = 1
        const val VIEW_TYPE_TX = 2
    }
}