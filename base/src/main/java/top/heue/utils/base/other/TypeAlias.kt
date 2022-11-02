package top.heue.utils.base.other

import androidx.fragment.app.FragmentActivity
import top.heue.utils.base.base.BaseActivity
import top.heue.utils.base.helper.AlertDialogHelper
import top.heue.utils.base.helper.BindHelper
import top.heue.utils.base.helper.FragmentPagerAdapterHelper
import top.heue.utils.base.helper.ListenerHelper
import top.heue.utils.base.util.file.SharedPreferenceUtil

typealias BH = BindHelper
typealias LH = ListenerHelper
typealias AD = AlertDialogHelper
typealias SP = SharedPreferenceUtil
typealias FP = FragmentPagerAdapterHelper

fun activity(initializer: () -> FragmentActivity) = lazy { initializer.invoke() as BaseActivity }
