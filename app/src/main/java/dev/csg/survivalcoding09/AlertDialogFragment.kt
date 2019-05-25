package dev.csg.survivalcoding09


import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

// 생성자에 콜백 리스너 넘기는거
class AlertDialogFragment(private val onClickListener: (String)-> Unit
                          , private val onLongClickListener: (String)-> Unit): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setTitle("권한이 필요합니다.")
            builder.setMessage("사진 정보를 얻으려면 외부 저장소 권한이 필수입니다.")
                .setPositiveButton("수락") { _, _ ->
                    onClickListener.invoke("수락")
                }
                .setNegativeButton("취소"){ _, _ ->
                    onClickListener.invoke("취소")
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")    }


}
