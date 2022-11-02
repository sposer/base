package top.heue.utils.base.util.view

import android.os.Build
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


object TabUtil {
    /**
     * 绑定tab与pager
     */
    fun bindTab(
        tabLayout: TabLayout,
        viewPager2: ViewPager2,
        tabConfigurationStrategy: TabLayoutMediator.TabConfigurationStrategy
    ) {
        TabLayoutMediator(
            tabLayout, viewPager2,
            true,
            tabConfigurationStrategy
        )
            .attach()
    }

    fun hideToolTipText(tab: TabLayout.Tab) {
        tab.view.isLongClickable = false
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            tab.view.tooltipText = ""
        }
    }

    fun setDivider(tabLayout: TabLayout, @DrawableRes divider: Int, padding: Int = 0) {
        val linearLayout: LinearLayout = tabLayout.getChildAt(0) as LinearLayout
        linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        linearLayout.dividerDrawable = ContextCompat.getDrawable(
            tabLayout.context,
            divider
        )
        linearLayout.dividerPadding = padding
    }
}