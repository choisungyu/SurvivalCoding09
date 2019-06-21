package dev.csg.survivalcoding09

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import dev.csg.survivalcoding09.databinding.ItemPhotoBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_READ_EXTERNAL_STORAGE = 1000
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                // 다이얼로그
                // fragment 생성자에 콜백 리스너 new 시킴 , it = string 인자 fragment 에서 넘겨준 거 받아온 거)
                AlertDialogFragment(onClickListener = {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_READ_EXTERNAL_STORAGE
                    )

                    toast(it)
                }, onLongClickListener = {

                    toast(it)
                }).show(supportFragmentManager, "dialog")


            } else {
                // 권한 요청
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_READ_EXTERNAL_STORAGE
                )
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

                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            REQUEST_READ_EXTERNAL_STORAGE
                        )
                    }, onLongClickListener = {

                        toast(it)
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
            .query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC"
            ) // 찍은 날짜

        // adapter 에 뿌려줄 items
        val items = arrayListOf<Photo>()
        cursor?.let {

            while (cursor.moveToNext()) {
                val uri =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))

                items.add(Photo(uri))
            }
            cursor.close()
        }
        val adapter = PhotoAdapter() // PhotoAdapter adapter = new PhotoAdapter();


        adapter.items = items
        adapter.notifyDataSetChanged()
        view_pager.adapter = adapter

        timer(period = 3000) {
            runOnUiThread {
                if (view_pager.currentItem < adapter.itemCount - 1) {
                    view_pager.currentItem = view_pager.currentItem + 1

                } else {
                    view_pager.currentItem = 0
                }
            }
        }

    }
}

data class Photo(val uri: String)

class PhotoAdapter(): RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
    var items = arrayListOf<Photo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)

//        val binding = ItemPhotoBinding.bind(view)
        return PhotoViewHolder(ItemPhotoBinding.bind(view))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.binding.photo = items[position]
    }

    class PhotoViewHolder(val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root)
}