package top.heue.utils.base.helper

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentPagerAdapterHelper : FragmentStateAdapter {
    private var activity: AppCompatActivity
    private lateinit var fragments: MutableList<Class<out Fragment>>

    constructor(activity: AppCompatActivity) : super(activity) {
        this.activity = activity
    }

    constructor(
        activity: AppCompatActivity,
        fragmentClass: MutableList<Class<out Fragment>>
    ) : this(
        activity
    ) {
        fragments = fragmentClass
    }

    override fun getItemCount(): Int = if (this::fragments.isInitialized) fragments.size
    else 0

    override fun createFragment(position: Int): Fragment = fragments[position].newInstance()

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun getItem(position: Int): Fragment? =
        activity.supportFragmentManager.findFragmentById(getItemId(position).toInt())

    /** 添加一个fragment，返回id */
    fun addItem(fragmentClass: Class<out Fragment>): Int {
        fragments.add(fragmentClass)
        val s = fragments.size - 1
        notifyItemInserted(s)
        return s
    }
}