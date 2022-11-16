package com.roy93group.launcher.ui.settings.feedChooser

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.roy93group.launcher.R
import com.roy93group.launcher.providers.color.theme.ColorTheme
import com.roy93group.launcher.storage.Settings
import com.roy93group.launcher.ui.settings.SettingsActivity
import io.posidon.android.conveniencelib.Device
import io.posidon.android.conveniencelib.getNavigationBarHeight
import io.posidon.android.conveniencelib.getStatusBarHeight
import io.posidon.android.conveniencelib.units.dp
import io.posidon.android.conveniencelib.units.toPixels
import io.posidon.android.conveniencelib.vibrate

class FeedSourcesChooserActivity : SettingsActivity() {

    companion object {
        @SuppressLint("NotifyDataSetChanged", "InflateParams")
        fun sourceEditPopup(
            parent: View,
            settings: Settings,
            feedUrls: ArrayList<String>,
            adapter: RecyclerView.Adapter<*>,
            i: Int = -1
        ) {
            val context = parent.context
            context.vibrate(14)
            val content =
                LayoutInflater.from(context)
                    .inflate(R.layout.view_feed_chooser_option_edit_dialog, null)
            val dialog = PopupWindow(
                /* contentView = */ content,
                /* width = */Device.screenWidth(context),
                /* height = */WRAP_CONTENT,
                /* focusable = */true
            )

            content.run {
                setBackgroundResource(R.drawable.card)
                backgroundTintList = ColorStateList.valueOf(Color.RED)
            }

            val textColor = Color.RED
            val hintColor = Color.RED
            val input = content.findViewById<EditText>(R.id.title)
            input.setTextColor(textColor)
            input.setHintTextColor(hintColor)

            content.findViewById<TextView>(R.id.done).run {
                setTextColor(ColorTheme.titleColorForBG(Color.RED))
                backgroundTintList = ColorStateList.valueOf(Color.RED)
                setOnClickListener {
                    dialog.dismiss()

                    if (i == -1) feedUrls.add(input.text.toString())
                    else feedUrls[i] = input.text.toString()

                    adapter.notifyDataSetChanged()
                    settings.edit(context) {
                        "feed:rss_sources" set feedUrls.toTypedArray()
                    }
                }
            }

            if (i == -1) {
                val suggestions = Suggestions(context)
                content.findViewById<ExpandableListView>(R.id.list).apply {
                    visibility = View.VISIBLE
                    setAdapter(SuggestionsAdapter(suggestions))
                    setOnChildClickListener { _, _, topicI, sourceI, _ ->
                        input.text.run {
                            clear()
                            insert(0, suggestions[topicI][sourceI].url)
                        }
                        true
                    }
                    divider = null
                }
                content.findViewById<TextView>(R.id.remove).visibility = View.GONE
            } else {
                val url = feedUrls[i]
                content.findViewById<TextView>(R.id.remove).run {
                    val hsl = FloatArray(3)
                    ColorUtils.colorToHSL(Color.RED, hsl)
                    hsl[0] = 0f
                    hsl[1] = hsl[1].coerceAtLeast(0.3f)
                    val bg = ColorUtils.HSLToColor(hsl)
                    backgroundTintList = ColorStateList.valueOf(bg)
                    setTextColor(ColorTheme.titleColorForBG(bg))
                    setOnClickListener {
                        dialog.dismiss()
                        feedUrls.remove(url)
                        adapter.notifyDataSetChanged()
                        settings.edit(it.context) {
                            "feed:rss_sources" set feedUrls.toTypedArray()
                        }
                    }
                }
                input.text = Editable.Factory().newEditable(url)
            }
            dialog.showAtLocation(parent, Gravity.BOTTOM, 0, 0)
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_settings_feed_chooser)

        val grid = findViewById<RecyclerView>(R.id.recycler)
        grid.layoutManager = GridLayoutManager(this, 2)
        val padding = 4.dp.toPixels(this)
        grid.setPadding(padding, getStatusBarHeight(), padding, getNavigationBarHeight() + padding)

        val feedUrls =
            settings.getStrings("feed:rss_sources")?.let { arrayListOf(*it) } ?: arrayListOf()

        grid.adapter = FeedChooserAdapter(settings, feedUrls)
        val fab = findViewById<ImageView>(R.id.btAdd)
        fab.backgroundTintList = ColorStateList.valueOf(Color.RED)
        fab.imageTintList =
            ColorStateList.valueOf(ColorTheme.titleColorForBG(Color.RED))
        fab.setOnClickListener {
            sourceEditPopup(
                parent = it,
                settings = settings,
                feedUrls = feedUrls,
                adapter = grid.adapter!!
            )
        }
        (fab.layoutParams as FrameLayout.LayoutParams).bottomMargin =
            20.dp.toPixels(this) + getNavigationBarHeight()
    }

}
