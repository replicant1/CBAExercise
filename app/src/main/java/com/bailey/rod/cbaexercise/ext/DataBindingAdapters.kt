package com.bailey.rod.cbaexercise.ext

import android.text.Html
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bailey.rod.cbaexercise.view.AccountOverviewListAdapter

@BindingAdapter("android:text")
fun setText(textView: TextView, txModel: AccountOverviewListAdapter.TxTransactionListItemModel) {
    val detailsText = Html.fromHtml(
        if (txModel.isPending)
            "<b>PENDING:</b> ${txModel.description}"
        else
            txModel.description,
        Html.FROM_HTML_MODE_LEGACY
    )
    textView.setText(detailsText)
}