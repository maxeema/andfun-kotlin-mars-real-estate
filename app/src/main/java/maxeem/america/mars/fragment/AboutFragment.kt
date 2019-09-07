package maxeem.america.mars.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import maxeem.america.mars.R
import maxeem.america.mars.app
import maxeem.america.mars.databinding.FragmentAboutBinding
import maxeem.america.mars.misc.*
import maxeem.america.mars.packageInfo
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class AboutFragment : Fragment(), AnkoLogger {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
        = FragmentAboutBinding.inflate(inflater, container, false).apply {
            info("$hash $timeMillis onCreateView, savedInstanceState: $savedInstanceState")
            lifecycleOwner = viewLifecycleOwner
            compatActivity()?.apply {
                setSupportActionBar(toolbar)
                NavigationUI.setupActionBarWithNavController(this, findNavController())
            }
            author.apply {
                val mail = Intent(Intent.ACTION_SENDTO)
                        .setData("mailto:${R.string.author_email.asString()}".toUri())
                isClickable = mail.resolveActivity(app.packageManager) != null
                if (isClickable) onClick {
                    startActivity(mail.apply {
                        putExtra(Intent.EXTRA_SUBJECT, R.string.email_subject.asString(R.string.app_name.asString()))
                    })
                }
            }
            version.text = app.packageInfo.versionName.substringBefore('-')
            googlePlay.onClick {
                Intent(Intent.ACTION_VIEW).apply {
                    data = "https://play.google.com/store/apps/details?id=${app.packageName}".toUri()
                    `package` = "com.android.vending"
                    if (resolveActivity(app.packageManager) == null)
                        `package` = null
                    startActivity(this)
                }
            }
        }.root

}