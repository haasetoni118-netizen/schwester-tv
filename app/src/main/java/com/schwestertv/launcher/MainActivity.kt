package com.schwestertv.launcher

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * MainActivity = die eigene Startseite (Launcher) für den Fernseher.
 *
 * Jeder Button öffnet entweder direkt die passende App, oder – falls diese
 * noch nicht installiert ist – automatisch den Play Store an der richtigen
 * Stelle, damit man sie mit einem Klick nachinstallieren kann.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnGoogle).setOnClickListener {
            openWebsite("https://www.google.com")
        }

        findViewById<Button>(R.id.btnYoutube).setOnClickListener {
            openAppOrStore("com.google.android.youtube")
        }

        findViewById<Button>(R.id.btnNetflix).setOnClickListener {
            openAppOrStore("com.netflix.mediaclient")
        }

        findViewById<Button>(R.id.btnMinecraft).setOnClickListener {
            openAppOrStore("com.mojang.minecraftpe")
        }

        findViewById<Button>(R.id.btnSubway).setOnClickListener {
            openAppOrStore("com.kiloo.subwaysurf")
        }
    }

    /** Öffnet eine Webseite im installierten Browser. */
    private fun openWebsite(url: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.error_no_store), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Startet eine installierte App über ihren Paketnamen.
     * Ist die App nicht installiert, wird automatisch der Play Store
     * an der richtigen Stelle geöffnet (erst über die Play-Store-App,
     * sonst über den Browser als Rückfallebene).
     */
    private fun openAppOrStore(packageName: String) {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            startActivity(launchIntent)
            return
        }

        Toast.makeText(this, getString(R.string.error_no_app), Toast.LENGTH_SHORT).show()

        try {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        } catch (e: ActivityNotFoundException) {
            // Kein Play-Store-Gerät? Dann die normale Webseite öffnen.
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }
}
