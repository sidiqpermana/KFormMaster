package com.thejuki.kformmaster.view

import android.content.Context
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.github.vivchar.rendererrecyclerviewadapter.ViewHolder
import com.github.vivchar.rendererrecyclerviewadapter.ViewState
import com.github.vivchar.rendererrecyclerviewadapter.ViewStateProvider
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewBinder
import com.thejuki.kformmaster.R
import com.thejuki.kformmaster.helper.FormBuildHelper
import com.thejuki.kformmaster.model.FormPasswordEditTextElement
import com.thejuki.kformmaster.state.FormEditTextViewState

/**
 * Form Password EditText ViewBinder
 *
 * View Binder for [FormPasswordEditTextElement]
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class FormPasswordEditTextViewBinder(private val context: Context, private val formBuilder: FormBuildHelper, @LayoutRes private val layoutID: Int?) : BaseFormViewBinder() {
    val viewBinder = ViewBinder(layoutID
            ?: R.layout.form_element, FormPasswordEditTextElement::class.java, { model, finder, _ ->
        val textViewTitle = finder.find(R.id.formElementTitle) as? AppCompatTextView
        val mainViewLayout = finder.find(R.id.formElementMainLayout) as? LinearLayout
        val textViewError = finder.find(R.id.formElementError) as? AppCompatTextView
        val dividerView = finder.find(R.id.formElementDivider) as? View
        val itemView = finder.getRootView() as View
        val editTextValue = finder.find(R.id.formElementValue) as com.thejuki.kformmaster.widget.ClearableEditText
        baseSetup(model, dividerView, textViewTitle, textViewError, itemView, mainViewLayout, editTextValue)

        editTextValue.setText(model.valueAsString)
        editTextValue.hint = model.hint ?: ""

        // Password
        editTextValue.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        // If an InputType is provided, use it instead
        model.inputType?.let { editTextValue.setRawInputType(it) }

        // If imeOptions are provided, use them instead of actionNext
        model.imeOptions?.let { editTextValue.imeOptions = it }

        setEditTextFocusEnabled(model, editTextValue, itemView)
        setOnFocusChangeListener(context, model, formBuilder)
        addTextChangedListener(model, formBuilder)
        setOnEditorActionListener(model, formBuilder)
        setClearableListener(model)

        editTextValue.setOnClickListener {
            // Invoke onClick Unit
            model.onClick?.invoke()
        }

    }, object : ViewStateProvider<FormPasswordEditTextElement, ViewHolder> {
        override fun createViewStateID(model: FormPasswordEditTextElement): Int {
            return model.id
        }

        override fun createViewState(holder: ViewHolder): ViewState<ViewHolder> {
            return FormEditTextViewState(holder)
        }
    })

    private fun setEditTextFocusEnabled(model: FormPasswordEditTextElement,
                                        editTextValue: AppCompatEditText,
                                        itemView: View) {
        itemView.setOnClickListener {
            // Invoke onClick Unit
            model.onClick?.invoke()

            editTextValue.requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            editTextValue.setSelection(editTextValue.text?.length ?: 0)
            imm.showSoftInput(editTextValue, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}
