package com.example.mundim.Classes.Auxiliary

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.example.mundim.Fragments.REQUEST_CAMERA_PERMISSION

/**
 * Shows OK/Cancel confirmation dialog about camera permission.
 */
class ConfirmationDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(activity)
            .setMessage("Precisamos da sua câmera para tirar fotos dos pacientes!")
            .setPositiveButton(android.R.string.ok) { _, _ ->
                parentFragment!!.requestPermissions(arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                parentFragment!!.activity?.finish()
            }
            .create()
}