package com.almaz.sarafanka.presentation.contacts

import android.app.Application
import android.database.Cursor
import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.almaz.sarafanka.core.interfaces.UserRepository
import com.almaz.sarafanka.core.model.Contact
import com.almaz.sarafanka.presentation.base.BaseViewModel
import com.almaz.sarafanka.utils.states.LoadingState
import com.almaz.sarafanka.utils.states.loading
import com.almaz.sarafanka.utils.states.ready
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val userRepository: UserRepository
) : BaseViewModel() {
    override val loadingState = MutableLiveData<LoadingState>(LoadingState.READY)

    val contactsLiveData = MutableLiveData<List<Contact>>()

    fun fetchContacts(application: Application) {
        loadingState.loading()
        viewModelScope.launch {
            val contactsListAsync = async { getPhoneContacts(application) }
            val contactNumbersAsync = async { getContactNumbers(application) }
//            val contactEmailAsync = async { getContactEmails(application) }

            val contacts = contactsListAsync.await()
            val contactNumbers = contactNumbersAsync.await()
//            val contactEmails = contactEmailAsync.await()

            contacts.forEach {
                contactNumbers[it.id]?.let { numbers ->
                    it.numbers = numbers.map { it.replace("[\\s,\\-,\\*,\\#]".toRegex(), "") }
                        .filter { it.length > 4 }.toHashSet()
                }
//                contactEmails[it.id]?.let { emails ->
//                    it.emails = emails
//                }
                if (it.numbers.isNotEmpty())
                    async {
                        for (x in it.numbers) {
                            it.user = getUserInDb(x)
                            if (it.user != null) {
                                break
                            } else {
                                if (x[0] == '8') {
                                    var numb = x
                                    numb = "+7${numb.substring(1)}"
                                    it.user = getUserInDb(numb)
                                }
                                if (it.user != null)
                                    break
                            }
                        }
                        if (it.user != null)
                            updateContact(it)
                    }
            }
            contactsLiveData.postValue(contacts.filter { it.numbers.isNotEmpty() })
            loadingState.ready()
        }
    }

    @Synchronized
    private fun updateContact(it: Contact){
        contactsLiveData.postValue(contactsLiveData.value?.toHashSet()?.apply { add(it) }?.toList()
            ?.sortedBy { (it.user == null).toString() + it.name })
    }

    private fun getPhoneContacts(application: Application): ArrayList<Contact> {
        val contactsList = ArrayList<Contact>()
        val contactsCursor = application.contentResolver?.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")
        if (contactsCursor != null && contactsCursor.count > 0) {
            val idIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts._ID)
            val nameIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            while (contactsCursor.moveToNext()) {
                val id = contactsCursor.getString(idIndex)
                val name = contactsCursor.getString(nameIndex)
                if (name != null) {
                    contactsList.add(Contact(id, name))
                }
            }
            contactsCursor.close()
        }
        return contactsList
    }

    private fun getContactNumbers(application: Application): HashMap<String, ArrayList<String>> {
        val contactsNumberMap = HashMap<String, ArrayList<String>>()
        val phoneCursor: Cursor? = application.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        if (phoneCursor != null && phoneCursor.count > 0) {
            val contactIdIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val numberIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (phoneCursor.moveToNext()) {
                val contactId = phoneCursor.getString(contactIdIndex)
                val number: String = phoneCursor.getString(numberIndex)
                //check if the map contains key or not, if not then create a new array list with number
                if (contactsNumberMap.containsKey(contactId)) {
                    contactsNumberMap[contactId]?.add(number)
                } else {
                    contactsNumberMap[contactId] = arrayListOf(number)
                }
            }
            //contact contains all the number of a particular contact
            phoneCursor.close()
        }
        return contactsNumberMap
    }

    private suspend fun getContactEmails(application: Application): HashMap<String, ArrayList<String>> {
        val contactsEmailMap = HashMap<String, ArrayList<String>>()
        val emailCursor = application.contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null,
            null,
            null,
            null)
        if (emailCursor != null && emailCursor.count > 0) {
            val contactIdIndex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID)
            val emailIndex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
            while (emailCursor.moveToNext()) {
                val contactId = emailCursor.getString(contactIdIndex)
                val email = emailCursor.getString(emailIndex)
                //check if the map contains key or not, if not then create a new array list with email
                if (contactsEmailMap.containsKey(contactId)) {
                    contactsEmailMap[contactId]?.add(email)
                } else {
                    contactsEmailMap[contactId] = arrayListOf(email)
                }
            }
            //contact contains all the emails of a particular contact
            emailCursor.close()
        }
        return contactsEmailMap
    }

    private suspend fun getUserInDb(phone: String) = userRepository.getUserInDb(phone)

}