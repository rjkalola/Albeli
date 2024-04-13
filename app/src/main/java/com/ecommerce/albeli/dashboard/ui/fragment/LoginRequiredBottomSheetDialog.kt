package com.ecommerce.albeli.dashboard.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.ecommerce.albeli.R
import com.ecommerce.albeli.authentication.ui.activity.LoginActivity
import com.ecommerce.albeli.common.utils.AppConstants
import com.ecommerce.albeli.databinding.DialogLoginRequiredBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class LoginRequiredBottomSheetDialog(mContext: Context?) :
    BottomSheetDialog(mContext!!) {

    private lateinit var binding: DialogLoginRequiredBinding

    companion object {
        private lateinit var mContext: Context


        fun newInstance(
            context: Context?,
        ): LoginRequiredBottomSheetDialog {
            this.mContext = context!!
            return LoginRequiredBottomSheetDialog(context)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sheetView: View = layoutInflater.inflate(R.layout.dialog_login_required, null)
        setContentView(sheetView)
        binding = DataBindingUtil.bind(sheetView)!!

        binding.txtLogin.setOnClickListener {
            val intent = Intent(mContext, LoginActivity::class.java)
            intent.putExtra(AppConstants.IntentKey.FROM_DASHBOARD, true)
            mContext.startActivity(intent)
            dismiss()
        }

        binding.imgClose.setOnClickListener {
            dismiss()
        }

    }

    override fun onStart() {
        super.onStart()
        this.window!!.setLayout(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
    }


}