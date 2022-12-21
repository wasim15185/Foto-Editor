package com.android.demoeditor.screens.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
 import com.android.demoeditor.databinding.EditTextDialogBinding
import android.os.Bundle
 import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


object EditTextDialog {
    private const val KEY_INITIAL_TEXT = "KEY_INITIAL_COLOR"
    private const val KEY_IS_TEXT_EDITED_TEXT = "KEY_IS_TEXT_EDITED_TEXT"
    private const val KEY_REQUEST_CODE = "KEY_REQUEST_CODE"
    private const val TAG = "EditTextDialog"

     interface Callback {
          /**
           * Call at close dialog
           *
           * @param requestCode requestCode of show parameter
           * @param resultCode `Activity.RESULT_OK` or `Activity.RESULT_CANCELED`
           * @param text selected text
           */
          fun onEditTextDialogResult(requestCode: Int, resultCode: Int, text: String,isTextIsAlreadyEdited:Boolean)
     }


    /**
     * Show dialog
     *
     * @param fragment Fragment
     * @param requestCode use in listener call
     * @param initialText is initial text
     * @param isTextIsAlReadyEdited is for text is edited or not
     */
    fun show(
        fragment: Fragment,
        requestCode: Int = 0,
        initialText: String = " ",
        isTextIsAlReadyEdited:Boolean=false

    ) {
        show(
            fragment.childFragmentManager,
            requestCode,
            initialText,
            isTextIsAlReadyEdited
        )
    }

    private fun show(
        fragmentManager: FragmentManager,
        requestCode: Int = 0,
        initialText: String  ,
        isTextIsAlReadyEdited: Boolean

    ) {
        if (fragmentManager.findFragmentByTag(TAG) != null) return
        if (fragmentManager.isStateSaved) return
        EditTextDialogFragment().also {
            it.arguments = bundleOf(
                KEY_INITIAL_TEXT to initialText,
                KEY_REQUEST_CODE to requestCode,
                KEY_IS_TEXT_EDITED_TEXT to isTextIsAlReadyEdited
            )
        }.show(fragmentManager, TAG)
    }






    class EditTextDialogFragment : DialogFragment() {

        private lateinit var binding: EditTextDialogBinding

        private var isTextIsAlreadyEdited:Boolean =false

//        /** The system calls this to get the DialogFragment's layout, regardless
//        of whether it's being displayed as a dialog or an embedded fragment. */
//        override fun onCreateView(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?
//        ): View {
//            // Inflate the layout to use as dialog or embedded fragment
//            //inflater.inflate(R.layout.edit_text_dialog, container, false)
//            binding=EditTextDialogBinding.inflate(layoutInflater, container, false)
//
//            return binding.root
//        }


        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val activity = requireActivity()

            binding=EditTextDialogBinding.inflate(LayoutInflater.from(activity))


            if (savedInstanceState != null) {
                val text =savedInstanceState.getString(KEY_INITIAL_TEXT,"Hello")
                isTextIsAlreadyEdited=savedInstanceState.getBoolean(KEY_IS_TEXT_EDITED_TEXT,false)
                binding.editText.setText(text)


            }else{

                val arguments = requireArguments()
                val text = arguments.getString(KEY_INITIAL_TEXT,"Hello")
                isTextIsAlreadyEdited=arguments.getBoolean(KEY_IS_TEXT_EDITED_TEXT,false)

                binding.editText.setText(text)


            }


            binding.editText.apply {
                showKeyboard()
                selectAll()
            }




            binding.okBtn .setOnClickListener {
                notifySelect()
                dismiss()
            }

            binding.cancelBtn.setOnClickListener {

                dismiss()
            }


          return  AlertDialog
                .Builder(activity)
                .setView(binding.editTextDialogContainer)
                .create().apply {
                    window?.setBackgroundDrawableResource(android.R.color.transparent)
              }


        }


//        private fun showSoftKeyboard(view: View) {
//            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
//            imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
//
//        }



//        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//            super.onViewCreated(view, savedInstanceState)
//
//
//        }

        private fun notifySelect() {
            val requestCode = requireArguments().getInt(KEY_REQUEST_CODE)
            extractCallback()?.onEditTextDialogResult(
                requestCode,
                Activity.RESULT_OK,
               binding.editText.text.toString(),
                isTextIsAlreadyEdited
            )
        }


        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            outState.putString(KEY_INITIAL_TEXT, binding.editText.text.toString())
            outState.putBoolean(KEY_IS_TEXT_EDITED_TEXT,isTextIsAlreadyEdited)
        }

        override fun onCancel(dialog: DialogInterface) {
            val requestCode = requireArguments().getInt(KEY_REQUEST_CODE)
            extractCallback()?.onEditTextDialogResult(requestCode, Activity.RESULT_CANCELED," ",isTextIsAlreadyEdited )
        }

        private fun extractCallback(): Callback? {
            return parentFragment as? Callback ?: activity as? Callback
        }



    }

    /**
     * extension function
     */
    fun EditText.showKeyboard() {
        post {
            requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
    }


}