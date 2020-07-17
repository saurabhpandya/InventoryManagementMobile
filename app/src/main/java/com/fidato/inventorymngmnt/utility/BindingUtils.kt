package com.fidato.inventorymngmnt.utility

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

class BindingUtils {

    companion object {
        private val TAG = BindingUtils::class.java.canonicalName

        @BindingAdapter("app:text")
        @JvmStatic
        fun setDoubleInTextView(tv: TextView, dbl: Double?) {

            try {
                //This will occur when view is first created or when the leading zero is omitted
                if (dbl == null && (tv.text.toString() == "" || tv.text.toString() == ".")) return

                //Check to see what's already there
                val tvDbl = tv.text.toString().toDouble()
                //If it's the same as what we've just entered then return
                // This is when then the double was typed rather than binded
                if (tvDbl == dbl)
                    return

                //If it's a new number then set it in the tv
                tv.text = dbl?.toString()

            } catch (nfe: java.lang.NumberFormatException) {

                //This is usually caused when tv.text is blank and we've entered the first digit
                tv.text = dbl?.toString() ?: ""

            }//catch

        }//setDoubleInTextView

        //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -//

        @InverseBindingAdapter(attribute = "app:text", event = "textAttrChanged")
        @JvmStatic
        fun getDoubleFromTextView(editText: TextView): Double? {

            return try {
                editText.text.toString().toDouble()
            } catch (e: NumberFormatException) {
                null
            }//catch

        }//getDoubleFromTextView

        @BindingAdapter("app:textInt")
        @JvmStatic
        fun setIntInTextView(tv: TextView, value: Int?) {

            try {
                //This will occur when view is first created or when the leading zero is omitted
                if (value == null && (tv.text.toString() == "" || tv.text.toString() == ".")) return

                //Check to see what's already there
                val tvDbl = tv.text.toString().toInt()
                //If it's the same as what we've just entered then return
                // This is when then the double was typed rather than binded
                if (tvDbl == value)
                    return

                //If it's a new number then set it in the tv
                tv.text = value?.toString()

            } catch (nfe: java.lang.NumberFormatException) {

                //This is usually caused when tv.text is blank and we've entered the first digit
                tv.text = value?.toString() ?: ""

            }//catch

        }//setIntInTextView

        //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -//

        @InverseBindingAdapter(attribute = "app:textInt", event = "textAttrChanged")
        @JvmStatic
        fun getIntFromTextView(editText: TextView): Int? {

            return try {
                editText.text.toString().toInt()
            } catch (e: NumberFormatException) {
                null
            }//catch

        }//getIntFromTextView

        @BindingAdapter("app:textAttrChanged")
        @JvmStatic
        fun setTextChangeListener(editText: TextView, listener: InverseBindingListener) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    listener.onChange()
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

            })
        }
    }


    /*companion object {
        @JvmOverloads
        @BindingAdapter("android:text")
        fun setFloat(view: TextView, value: Double) {
            if (value.isNaN())
                view.text = ""
            else
                view.text = "$value"
        }

        @JvmOverloads
        @InverseBindingAdapter(attribute = "android:text")
        fun getFloat(view: TextView): Double {
            val num = view.text.toString()
            if (num.isEmpty()) return 0.0
            try {
                return num as Double
            } catch (e: NumberFormatException) {
                return 0.0
            }
        }
    }*/

}