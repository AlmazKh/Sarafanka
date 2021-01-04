package com.almaz.sarafanka.presentation.contacts

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import com.almaz.sarafanka.R
import com.almaz.sarafanka.core.model.Contact
import com.almaz.sarafanka.presentation.base.BaseFragment
import com.almaz.sarafanka.utils.extensions.observe
import com.almaz.sarafanka.utils.extensions.toVisible
import com.faltenreich.skeletonlayout.applySkeleton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_contacts.*

private const val CONTACTS_READ_REQ_CODE = 100

class ContactsFragment : BaseFragment<ContactsViewModel>(ContactsViewModel::class.java) {
    override val layoutId = R.layout.fragment_contacts
    lateinit var adapter: ContactsAdapter

    override fun setupView() {
//        viewModel.loadingState.loading()
//        tvDefault.text = "Загрузка контактов..."
        adapter = ContactsAdapter(rootActivity, ::onContactClick)
        rv_contacts.adapter = adapter
        /*viewModel.contactsLiveData.observe(viewLifecycleOwner, Observer {
//            viewModel.loadingState.ready()
//            tvDefault.visibility = View.GONE
            adapter.submitGlobalList(it)
        })*/

        skeletons.add(rv_contacts.applySkeleton(R.layout.row_contact, 6))

        et_search_contacts.addTextChangedListener {
            adapter.filter.filter(it)
        }
        fetchContacts()
    }

    override fun onStart() {
        super.onStart()
        rootActivity.bottom_nav.toVisible()
    }

    override fun subscribe(viewModel: ContactsViewModel) {
        super.subscribe(viewModel)
        observe(viewModel.contactsLiveData, ::bindContacts)
    }

    private fun bindContacts(list: List<Contact>) {
        adapter.submitGlobalList(list)
    }

//    override fun onRefresh() {
//        super.onRefresh()
//        fetchContacts()
//    }

    private fun fetchContacts() {
        if (ActivityCompat.checkSelfPermission(
                rootActivity,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.fetchContacts(rootActivity.application)
        } else {
            val provideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                rootActivity,
                Manifest.permission.READ_CONTACTS
            )

            if (provideRationale) {
                AlertDialog.Builder(rootActivity).apply {
                    setTitle("Permission")
                    setMessage("Need contacts read permission for fetching and displaying contacts")
                    setPositiveButton("Ok") { _, _ ->
                        requestPermissions(
                            arrayOf(Manifest.permission.READ_CONTACTS),
                            CONTACTS_READ_REQ_CODE
                        )
                    }
                    create()
                    show()
                }
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    CONTACTS_READ_REQ_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CONTACTS_READ_REQ_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.fetchContacts(rootActivity.application)
        }
    }

    private fun onContactClick(contact: Contact) {
        contact.user?.let {
            rootActivity.navController.navigate(
                R.id.action_contactsFragment_to_otherProfileFragment,
                bundleOf("profile" to it.id)
            )
        }
        if (contact.user == null) {
            val uri = Uri.parse("smsto:${contact.numbers.last()}")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.putExtra(
                "sms_body", "Привет! Не знаешь как заработать на своих навыках?" +
                        "Названиешь каждому знакомому с просьбой помочь? Хватит! " +
                        "Скорее скачивай приложение \"Sarafanka\"!\n\n" +
                        "Переходи в Play Store: (будет ссылка)"
            )
            startActivity(intent)
        }
/*
        if (ActivityCompat.checkSelfPermission(
                rootActivity,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            SmsManager.getDefault()
                .sendTextMessage(
                    contact.numbers.first(),
                    null,
                    "Скачивай сарафанку, а то че как лох",
                    null,
                    null
                );
        } else {
            val provideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                rootActivity,
                Manifest.permission.SEND_SMS
            )

            if (provideRationale) {
                AlertDialog.Builder(rootActivity).apply {
                    setTitle("Permission")
                    setMessage("Need contacts read permission for fetching and displaying contacts")
                    setPositiveButton("Ok") { _, _ ->
                        ActivityCompat.requestPermissions(
                            rootActivity,
                            arrayOf(Manifest.permission.SEND_SMS),
                            CONTACTS_SMS_CODE
                        )
                    }
                    create()
                    show()
                }
            } else {
                ActivityCompat.requestPermissions(
                    rootActivity,
                    arrayOf(Manifest.permission.SEND_SMS),
                    CONTACTS_SMS_CODE
                )
            }
        }*/
    }
}
