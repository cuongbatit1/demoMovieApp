package com.studio.king.demomovie.utils

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.studio.king.demomovie.R

fun showMessageDialog(
    context: Context,
    title: String? = null,
    message: String?,
    onOk: Runnable? = null,
    int: Int = R.string.key_txt_ok_title
) {
    com.afollestad.materialdialogs.MaterialDialog(context).show {
        cancelOnTouchOutside(false)
        cancelable(false)
        title?.let {
            title(text = it)
        }
        message?.let {
            message(text = it)
        }
        positiveButton(int) { onOk?.run() }
    }
}



fun showYesNoDialog(
    context: Context,
    title: String? = null,
    message: String? = null,
    onOk: Runnable? = null,
    onNo: Runnable? = null,
    textYes: String = "Yes",
    textNo: String = "No"
) : com.afollestad.materialdialogs.MaterialDialog{
    return com.afollestad.materialdialogs.MaterialDialog(context).show {
        cancelOnTouchOutside(false)
        cancelable(false)
        title?.let {
            title(text = it)
        }
        message?.let {
            message(text = it)
        }
        positiveButton(text = textYes) { onOk?.run() }
        negativeButton(text = textNo) { onNo?.run() }
    }
}


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
fun getDiskDir(context: Context): String {
    return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
        Environment.getExternalStorageDirectory().toString() + "/globic"
    } else {
        context.getDir("globic", Context.MODE_PRIVATE).absolutePath
    }
}

/**
 * Display the Snackbar with the [Snackbar.LENGTH_SHORT] duration.
 *
 * @param message the message text.
 */

fun snackBarCustom(view: View, message: String) {
    val mSnackBar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
    val view: View? = mSnackBar.view
    val mainTextView =
        mSnackBar.view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        mainTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
    else
        mainTextView.gravity = Gravity.CENTER_HORIZONTAL
    mainTextView.gravity = Gravity.CENTER_HORIZONTAL
    mainTextView.setTextColor(Color.WHITE)
    view?.setBackgroundColor(Color.parseColor("#008577"))
    mSnackBar.show()
}