package com.citytratters.ui.activity

import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import com.citytratters.R
import com.citytratters.base.BaseActivity
import com.citytratters.base.IResponseParser
import com.citytratters.constants.MyConfig
import com.citytratters.model.response.LoginResponseModel
import com.citytratters.myPreferance.MyPreference
import com.citytratters.network.RestResponse
import com.citytratters.utils.FingerprintHandler
import com.telkomyellow.utils.UiUtils
import kotlinx.android.synthetic.main.activity_welcomeback.*
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import java.util.concurrent.Executors
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey


class WelcomeBackActivity : BaseActivity() {

    private var fingerprintManager: FingerprintManager? = null
    private var keyguardManager: KeyguardManager? = null
    private lateinit var keyStore: KeyStore
    private var keyGenerator: KeyGenerator? = null
    private val KEY_NAME = "example_key"
    private var cipher: Cipher? = null
    private var cryptoObject: FingerprintManager.CryptoObject? = null

    val REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 1 //used as a number to verify whether this is where the activity results from

    companion object {
        fun getIntent(ctx: Context): Intent {
            return Intent(ctx, WelcomeBackActivity::class.java)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_welcomeback
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        subscribe()


        val executor = Executors.newSingleThreadExecutor()
        val activity: AppCompatActivity = this
        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errString == "No fingerprints enrolled.") {
                        pinOrPassword()
                    } else {
                        if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                            runOnUiThread{
                            Toast.makeText(
                                this@WelcomeBackActivity,
                                "Operation is canceled by user",
                                Toast.LENGTH_LONG
                            ).show()}
                        } else {
                            runOnUiThread{
                            Toast.makeText(
                                this@WelcomeBackActivity,
                                "Operation is dismissed by user",
                                Toast.LENGTH_LONG
                            ).show()

                        }}
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    runOnUiThread{
                       // apiCallForLogin()
                        val intent = Intent(
                            this@WelcomeBackActivity,
                            MainActivity::class.java
                        )
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    runOnUiThread {
                        Toast.makeText(
                            this@WelcomeBackActivity,
                            "Invalid credential",
                            Toast.LENGTH_LONG
                        ).show()

                    }


                }
            })
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Verify it's you...")
            .setSubtitle("Select option from below")
            .setDescription(" ")
            .setNegativeButtonText("Back")
            .build()


        tvVerify.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                val mKeyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
                if (!mKeyguardManager.isKeyguardSecure) {
                    //pin na hoy eno code karo
                    //Toast.makeText(this@WelcomeBackActivity, "PIN nathi rakhyo", Toast.LENGTH_LONG).show()
                }else{
                    biometricPrompt.authenticate(promptInfo)
                }
            } else {
                pinOrPassword()
            }
        }

        tvLoginWithUserNameAndPassword.setOnClickListener {
                val i = Intent(this, SignInActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
        }

    }

    private fun apiCallForLogin() {
        vmCommon.apiLogin(MyPreference.getPreference(this@WelcomeBackActivity,MyConfig.SharedPreferences.PREF_KEY_LOGIN_ID),MyPreference.getPreference(this@WelcomeBackActivity,MyConfig.SharedPreferences.PREF_KEY_LOGIN_ID))
    }

    private fun subscribe() {
        vmCommon.eventLogin.observe(this, androidx.lifecycle.Observer {
            observationOfAPI(it, object : IResponseParser<LoginResponseModel>(this) {
                override fun onSuccess(it: RestResponse<LoginResponseModel>) {
                    super.onSuccess(it)
                    if (it.status != RestResponse.Status.ERROR) {
                        val loginResponseModel: LoginResponseModel? = it.data!!
                        val intent = Intent(
                            this@WelcomeBackActivity,
                            MainActivity::class.java
                        )
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                         startActivity(intent)

                        /* val mKeyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
                        if (!mKeyguardManager.isKeyguardSecure) {
                            val intent = Intent(this@SignInActivity, DigitalMembersCardActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }else{}*/

                    } else {
                        UiUtils.showAlertDialog(
                            this@WelcomeBackActivity,
                            getString(R.string.server_error)
                        )
                    }
                }
            })
        })
    }


    private fun pinOrPassword() {
        val mKeyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        if (!mKeyguardManager.isKeyguardSecure) {
            Toast.makeText(this@WelcomeBackActivity, "PIN nathi rakhyo", Toast.LENGTH_LONG).show()
        } else {
            try {
                val keyStore = KeyStore.getInstance("AndroidKeyStore")
                keyStore.load(null)
                val keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
                )

                keyGenerator.init(
                    KeyGenParameterSpec.Builder(
                        KEY_NAME,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true) // Require that the user has unlocked in the last 30 seconds
                        .setUserAuthenticationValidityDurationSeconds(30)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build()
                )
                keyGenerator.generateKey()
            } catch (e: NoSuchAlgorithmException) {
                throw java.lang.RuntimeException("Failed to create a symmetric key", e)
            } catch (e: NoSuchProviderException) {
                throw java.lang.RuntimeException("Failed to create a symmetric key", e)
            } catch (e: InvalidAlgorithmParameterException) {
                throw java.lang.RuntimeException("Failed to create a symmetric key", e)
            } catch (e: KeyStoreException) {
                throw java.lang.RuntimeException("Failed to create a symmetric key", e)
            } catch (e: CertificateException) {
                throw java.lang.RuntimeException("Failed to create a symmetric key", e)
            } catch (e: IOException) {
                throw java.lang.RuntimeException("Failed to create a symmetric key", e)
            }
            val intent = mKeyguardManager.createConfirmDeviceCredentialIntent(null, null)
            intent?.let { startActivityForResult(it, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS) }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS) {
            // Challenge completed, proceed with using cipher
            if (resultCode == RESULT_OK) {
                val intent = Intent(
                    this@WelcomeBackActivity,
                    MainActivity::class.java
                )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Toast.makeText(
                    this@WelcomeBackActivity,
                    "Verification Failed Please try again",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun fingerAuthentication() {
        if (getManagers()) {
            generateKey()
            if (cipherInit()) {
                cipher?.let {
                    cryptoObject = FingerprintManager.CryptoObject(it)
                }
                val helper = FingerprintHandler(this@WelcomeBackActivity)
                if (fingerprintManager != null && cryptoObject != null) {
                    helper.startAuth(fingerprintManager!!, cryptoObject!!)
                }
                cipher?.let {
                    cryptoObject = FingerprintManager.CryptoObject(it)
                }
            }

        }
    }

    private fun getManagers(): Boolean {
        keyguardManager = getSystemService(Context.KEYGUARD_SERVICE)
                as KeyguardManager
        fingerprintManager = getSystemService(Context.FINGERPRINT_SERVICE)
                as FingerprintManager

        if (keyguardManager?.isKeyguardSecure == false) {

            Toast.makeText(
                this,
                "Lock screen security not enabled in Settings",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.USE_FINGERPRINT
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                this,
                "Fingerprint authentication permission not enabled",
                Toast.LENGTH_LONG
            ).show()

            return false
        }

        if (fingerprintManager?.hasEnrolledFingerprints() == false) {
            Toast.makeText(
                this,
                "Register at least one fingerprint in Settings",
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }

    private fun generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                "AndroidKeyStore"
            )
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(
                "Failed to get KeyGenerator instance", e
            )
        } catch (e: NoSuchProviderException) {
            throw RuntimeException("Failed to get KeyGenerator instance", e)
        }

        try {
            keyStore?.load(null)
            keyGenerator?.init(
                KeyGenParameterSpec.Builder(
                    KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                        KeyProperties.ENCRYPTION_PADDING_PKCS7
                    )
                    .build()
            )
            keyGenerator?.generateKey()
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidAlgorithmParameterException) {
            throw RuntimeException(e)
        } catch (e: CertificateException) {
            throw RuntimeException(e)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private fun cipherInit(): Boolean {
        try {
            cipher = Cipher.getInstance(
                KeyProperties.KEY_ALGORITHM_AES + "/"
                        + KeyProperties.BLOCK_MODE_CBC + "/"
                        + KeyProperties.ENCRYPTION_PADDING_PKCS7
            )
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to get Cipher", e)
        } catch (e: NoSuchPaddingException) {
            throw RuntimeException("Failed to get Cipher", e)
        }

        try {
            keyStore?.load(null)
            val key = keyStore?.getKey(KEY_NAME, null) as SecretKey
            cipher?.init(Cipher.ENCRYPT_MODE, key)
            return true
        } catch (e: KeyPermanentlyInvalidatedException) {
            return false
        } catch (e: KeyStoreException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: CertificateException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: UnrecoverableKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: IOException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    override fun onResume() {
        super.onResume()
    }
}
