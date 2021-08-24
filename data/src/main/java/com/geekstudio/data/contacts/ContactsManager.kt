package com.geekstudio.data.contacts

import android.content.Context
import android.os.Build
import android.provider.ContactsContract
import android.util.Log

class ContactsManager(private val context: Context) {
    private val debugTag = javaClass.simpleName

    private val listProjection: Array<out String> = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.LOOKUP_KEY,
        getContactsContractName(),
        ContactsContract.Contacts.HAS_PHONE_NUMBER
    )

    private fun getContactsContractName() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        else
            ContactsContract.Contacts.DISPLAY_NAME

    private val detailProjection: Array<out String> = arrayOf(
        ContactsContract.Data._ID,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
    )

    private val detailSelection: String = "${ContactsContract.Data.CONTACT_ID} = ? AND length(${ContactsContract.CommonDataKinds.Phone.NUMBER}) > 7"

    fun getContacts(): List<Contacts> {
        val list = ArrayList<Contacts>()

        val cursor = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            listProjection,
            null,
            null,
            null
        )

        Log.d(debugTag, "getContacts() cursor count= ${cursor?.count ?: 0}")

        while (cursor?.moveToNext() == true) {
            val id = cursor.getString(0)
            val lookUpKey = cursor.getString(1)
            val displayName = cursor.getString(2)
            val hasNumber = cursor.getString(3)

            Log.d(debugTag, "getContacts() id = $id, lookUpKey = $lookUpKey, displayName = $displayName, hasNumber = $hasNumber")

            getDetailContacts(id)?.let {
                list.add(it)
            }
        }

        return list
    }

    private fun getDetailContacts(id:String): Contacts?{
        val cursor = context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            detailProjection,
            detailSelection,
            arrayOf(id),
            null
        )

        Log.d(debugTag, "getDetailContacts() cursor count= ${cursor?.count ?: 0}")

        if(cursor?.moveToFirst() == true){
            val id = cursor.getString(0)
            val displayName = cursor?.getString(2)
            val phoneNumber = cursor?.getString(1)

            Log.d(debugTag, "getDetailContacts() id = $id, displayName = $displayName, phoneNumber = $phoneNumber")
            return Contacts(displayName, phoneNumber)
        }

        return null
    }
}