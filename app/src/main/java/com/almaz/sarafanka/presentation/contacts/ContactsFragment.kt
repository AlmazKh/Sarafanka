package com.almaz.sarafanka.presentation.contacts

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.almaz.sarafanka.R
import com.almaz.sarafanka.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_contacts.*

class ContactsFragment : BaseFragment<ContactsViewModel>(ContactsViewModel::class.java) {
    override val layoutId = R.layout.fragment_contacts
    private val CONTACTS_READ_REQ_CODE = 100

    @RequiresApi(Build.VERSION_CODES.M)
    override fun setupView() {
        tvDefault.text = "Fetching contacts..."
        val adapter = ContactsAdapter(rootActivity)
        rvContacts.adapter = adapter
        viewModel.contactsLiveData.observe(this, Observer {
            tvDefault.visibility = View.GONE
            adapter.contacts = it
        })
        if (ActivityCompat.checkSelfPermission(rootActivity, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            viewModel.fetchContacts(rootActivity.application)
        } else {
            val provideRationale = ActivityCompat.shouldShowRequestPermissionRationale(rootActivity, Manifest.permission.READ_CONTACTS)

            if (provideRationale) {
                AlertDialog.Builder(rootActivity).apply {
                    setTitle("Permission")
                    setMessage("Need contacts read permission for fetching and displaying contacts")
                    setPositiveButton("Ok") { _, _ ->
                        ActivityCompat.requestPermissions(rootActivity, arrayOf(Manifest.permission.READ_CONTACTS), CONTACTS_READ_REQ_CODE)
                    }
                    create()
                    show()
                }
            } else {
                ActivityCompat.requestPermissions(rootActivity, arrayOf(Manifest.permission.READ_CONTACTS), CONTACTS_READ_REQ_CODE)
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CONTACTS_READ_REQ_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.fetchContacts(rootActivity.application)
        }
    }
}
