package com.bunnying.demo.gtoast

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import com.bunnying.library.gtoast.GToast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addButton("success", View.OnClickListener {
            GToast.Builder(this)
                .success("success")
                .shot()
        })
        addButton("error", View.OnClickListener {
            GToast.Builder(this)
                .error("error")
                .shot()
        })
        addButton("info", View.OnClickListener {
            GToast.Builder(this)
                .info("info")
                .shot()
        })
        addButton("normal", View.OnClickListener {
            GToast.Builder(this)
                .normal("normal")
                .shot()
        })
        addButton("heart", View.OnClickListener {
            GToast.Builder(this)
                .heart("heart")
                .shot()
        })
        addButton("custom", View.OnClickListener {
            GToast.Builder(this)
                .setText("custom")
                .setIcon(R.drawable.ic_launcher_foreground)
                .setIconColor(Color.YELLOW)
                .setBackgroundColor(Color.parseColor("#44ff0000"))
                .setGravity(Gravity.CENTER, 200, 200)
                .shot()
        })
    }

    private fun addButton(text: String?, onClickListener: View.OnClickListener?) {
        layoutMain?.addView(
            AppCompatButton(this).apply {
                this.text = text
                this.setOnClickListener(onClickListener)
            },
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        )
    }
}