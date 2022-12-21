package com.android.demoeditor.screens.dialog

import android.app.Dialog
import android.content.Context
import com.android.demoeditor.R
import android.graphics.Color
import android.graphics.drawable.ColorDrawable


/**object LoadingDialog {

    private val TAG = this::class.java.simpleName

    class LoadingDialogFragment : DialogFragment() {
        private lateinit var binding: LoadingDialogBinding

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val activity = requireActivity()
            val layoutInflater = LayoutInflater.from(activity)
            binding = DataBindingUtil.inflate(layoutInflater, R.layout.loading_dialog, null, false)

            return Dialog(requireContext()).apply {
                setContentView(binding.root)
                window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            }


//                .Builder(activity)
//                .setView(binding.loadingDialogContainer)
//                .create().apply {
//                    window?.setBackgroundDrawableResource(android.R.color.transparent)
//                }

        }
    }


    private val dialog = LoadingDialogFragment()

    fun show(fragmentManager: FragmentManager) {
        dialog.show(fragmentManager, TAG)
    }

    fun hide() {
        dialog.dismiss()
    }


} */


class LoadingDialog2(context: Context) {

    private val dialog = Dialog(context).apply {
        setContentView(R.layout.loading_dialog)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        create()
    }


    fun show() {
        dialog.show()
    }

    fun hide() {
        dialog.dismiss()
    }


}