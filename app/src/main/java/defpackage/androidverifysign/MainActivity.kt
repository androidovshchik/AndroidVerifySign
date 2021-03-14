package defpackage.androidverifysign

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Timber.d("--- PackageManager.GET_SIGNING_CERTIFICATES ---")
            packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
                .signingInfo.apply {
                    if (hasMultipleSigners()) {
                        Timber.d("--- Multiple signers ---")
                        apkContentsSigners.forEach {
                            Timber.d("MD5: ${it.toByteArray().hash("MD5")}")
                            Timber.d("SHA1: ${it.toByteArray().hash("SHA")}")
                            Timber.d("SHA256: ${it.toByteArray().hash("SHA256")}")
                        }
                    } else {
                        Timber.d("--- Single signer ---")
                        signingCertificateHistory.forEach {
                            Timber.d("MD5: ${it.toByteArray().hash("MD5")}")
                            Timber.d("SHA1: ${it.toByteArray().hash("SHA")}")
                            Timber.d("SHA256: ${it.toByteArray().hash("SHA256")}")
                        }
                    }
                }
        }
        Timber.d("--- PackageManager.GET_SIGNATURES ---")
        packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            .signatures.forEach {
                Timber.d(Base64.encode(it.toByteArray(), Base64.DEFAULT).toString(Charsets.UTF_8))
                Timber.d("MD5: ${it.toByteArray().hash("MD5")}")
                Timber.d("SHA1: ${it.toByteArray().hash("SHA")}")
                Timber.d("SHA256: ${it.toByteArray().hash("SHA256")}")
            }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}