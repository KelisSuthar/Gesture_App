package com.example.gestureapp.UI.applist.creategestures

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.gesture.*
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.OnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.gesture_app.R
import com.github.gcacace.signaturepad.views.SignaturePad
import java.io.File


class CreateGesuresActivity : AppCompatActivity(), GestureOverlayView.OnGesturePerformedListener,
    OnGestureListener {
    var gestureLibrary: GestureLibrary? = null
    var gestureView: GestureOverlayView? = null
    var bitmap: Bitmap? = null
    var root_file = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
        "Notes"
    )
    var file = File(root_file, "gestures.txt")
    private var gestureDetector: GestureDetector? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_gesures)
        isStoragePermissionGranted()

//        generateNoteOnSD("gestures.txt", "")
        gestureView = findViewById(R.id.gv)
        gestureLibrary =
            GestureLibraries.fromFile("/storage/emulated/0/Documents/Notes/gestures.txt")
        Log.e("GESTURE_DATA", gestureLibrary!!.load().toString())
        Log.e("GESTURE_DATA", file.absolutePath)
//        if (!gestureLibrary!!.load()) {
//            finish()
//        }
        Log.e("GESTURE_DATA", gestureLibrary!!.gestureEntries.toString())
        gestureView!!.addOnGesturePerformedListener(this)

    }

    override fun onGesturePerformed(overlay: GestureOverlayView?, gesture: Gesture?) {
        val predictions: ArrayList<Prediction> = gestureLibrary!!.recognize(gesture)
        predictions.forEach {
            Log.e("GETSURE_LOAD", it.score.toString())
            Log.e("GETSURE_LOAD", it.name.toString())
        }

        if (overlay!!.gesture != null) {
            Log.e("GESTURE_DATA", gestureLibrary!!.gestureEntries.toString())
            gestureLibrary!!.addGesture("Test Name", overlay!!.gesture)
            Log.e("GESTURE_DATA", gestureLibrary!!.save().toString())
            Log.e("GESTURE_DATA", gestureLibrary!!.gestureEntries.toString())
            overlay?.clear(false)
        }
        overlay?.clear(false)


//        }

    }

    private fun setSignPadDialog() {
        var is_Signed = false

        val dialog = Dialog(
            this@CreateGesuresActivity,
            com.google.android.material.R.style.Base_Theme_AppCompat_Light_Dialog_Alert
        )
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.dialog_sign_pad_layout)
        dialog.setCancelable(true)
        val SIGN_PAD: SignaturePad = dialog.findViewById(R.id.signature_pad)
        val BTN_CLEAR: Button = dialog.findViewById(R.id.btnClear)
        val BTN_DONE: Button = dialog.findViewById(R.id.btnDone)
        val BTN_CANCEL: ImageView = dialog.findViewById(R.id.ivDialogCancel)

        BTN_CANCEL.setOnClickListener {
            SIGN_PAD.clear()
            dialog.dismiss()

        }
        BTN_CANCEL.visibility = View.GONE
        BTN_CLEAR.setOnClickListener {
            SIGN_PAD.clear()
            is_Signed = false
        }
        BTN_DONE.setOnClickListener {
            if (is_Signed) {
                bitmap = SIGN_PAD.signatureBitmap

                SIGN_PAD.clear()
                dialog.dismiss()
                if (isStoragePermissionGranted()) {
                    gestureLibrary =
                        GestureLibraries.fromRawResource(this@CreateGesuresActivity, R.raw.gestures)
                    if (!gestureLibrary!!.load()) {
                        finish();
                    }
                    gestureView!!.addOnGesturePerformedListener(this)
                }

            }

        }
        SIGN_PAD.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                //Event triggered when the pad is touched
                Log.e("SIGN_PAD", "START")
            }

            override fun onSigned() {
                //Event triggered when the pad is signed
                Log.e("SIGN_PAD", "SIGNED")
                is_Signed = true
            }

            override fun onClear() {
                //Event triggered when the pad is cleared
                Log.e("SIGN_PAD", "CLEAR")
            }
        })
        dialog.show()
    }

    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v("TAG", "Permission is granted")
                true
            } else {
                Log.v("TAG", "Permission is revoked")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    100
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted")
            true
        }
    }

    override fun onDown(e: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onShowPress(e: MotionEvent?) {
        TODO("Not yet implemented")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun onLongPress(e: MotionEvent?) {
        TODO("Not yet implemented")
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {

        return true
    }

}