package com.example.finalapplication.screen.homestaff

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.example.finalapplication.databinding.FragmentHomeStaffBinding
import com.example.finalapplication.screen.login.LoginActivity
import com.example.finalapplication.screen.mainstaff.MainStaffActivity
import com.example.finalapplication.screen.mainstaff.MainStaffViewModel
import com.example.finalapplication.screen.requireverify.RequireverifyFragment
import com.example.finalapplication.screen.requireverify.VerifyingFragment
import com.example.finalapplication.utils.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeStaffFragment :
    BaseFragment<FragmentHomeStaffBinding>(FragmentHomeStaffBinding::inflate) {

    private val viewModel by sharedViewModel<MainStaffViewModel>()

    override fun bindData() {

        viewModel.logoutSuccess.observe(this) { data ->
            if (data) {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }

    }

    override fun handleEvent() {
        binding.apply {
            buttonRequireVerify.setOnClickListener {
                (activity as? MainStaffActivity)?.setVisibleNavBar(false)
                (activity as? MainStaffActivity)?.addNewFragment(RequireverifyFragment())
            }
            buttonVerifying.setOnClickListener {
                (activity as? MainStaffActivity)?.setVisibleNavBar(false)
                (activity as? MainStaffActivity)?.addNewFragment(VerifyingFragment())
            }
            buttonLogout.setOnClickListener {
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Bạn có chắc chắn muốn thoát?")
                    .setTitle("Attention")
                    .setPositiveButton(
                        "Đồng ý"
                    ) { dialog, _ ->
                        viewModel.logout()
                        dialog.cancel()
                    }
                    .setNegativeButton(
                        "Huỷ bỏ"
                    ) { dialog, _ ->
                        dialog.cancel()
                    }
                builder.create().show()
            }
        }
    }

    override fun initData() {
        (activity as? MainStaffActivity)?.setVisibleNavBar(true)
    }
}
