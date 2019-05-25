package dev.csg.survivalcoding09

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_READ_EXTERNAL_STORAGE = 1000
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // 다이얼로그
                // fragment 생성자에 콜백 리스너 new 시킴 , it = string 인자 fragment 에서 넘겨준 거 받아온 거)
                AlertDialogFragment(onClickListener = {
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_READ_EXTERNAL_STORAGE)

                    toast(it)
                },onLongClickListener = {

                }).show(supportFragmentManager, "dialog")


            } else {
                // 권한 요청
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_READ_EXTERNAL_STORAGE)
            }

        } else {
            // 권한 승인이 된 경우
            getAllPhotos()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_READ_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    getAllPhotos()

                } else {
                    // permission denied, boo! Disable the
                    toast("권한 거부 됨")
                    // 다이얼로그
                    AlertDialogFragment(onClickListener = {
                        ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            REQUEST_READ_EXTERNAL_STORAGE)
                    },onLongClickListener = {
                        // 리스너 계속 달기
                    }).show(supportFragmentManager, "dialog")
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    // 사진 가져오기
    private fun getAllPhotos() {
        val cursor = contentResolver
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null,
                        null,
                        null,
                        "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC") // 찍은 날짜
    }
}
