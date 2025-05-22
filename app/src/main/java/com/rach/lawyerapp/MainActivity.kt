package com.rach.lawyerapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.auth.FirebaseAuth
import com.rach.lawyerapp.navigation.MainNavigation
import com.rach.lawyerapp.appPermissions.PermissionsHandler
import com.rach.lawyerapp.appPermissions.ZegoCallService
import com.rach.lawyerapp.ui.theme.LawyerAppTheme
import com.rach.lawyerapp.ui.wallet.viewModel.RazorPayViewModel
import com.rach.lawyerapp.utils.K
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private lateinit var razorPayViewModel: RazorPayViewModel

    @Inject
    lateinit var zegoCallService: ZegoCallService

    @Inject
    lateinit var permissionsHandler: PermissionsHandler
  //  private val currentUserId by lazy { FirebaseAuth.getInstance().currentUser }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LawyerAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Inject ViewModel with Hilt
                    razorPayViewModel = hiltViewModel()
                    MainNavigation(razorPayViewModel = razorPayViewModel)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                permissionsHandler.requestPermission(this@MainActivity) { allGranted ->
                    if (allGranted) {
                        val user = FirebaseAuth.getInstance().currentUser
                        if (user != null){
                            zegoCallService.initialize(
                                application = application,
                                appId = K.APP_ID,
                                appSignIn = K.APP_SIGN,
                                userId = user.uid,
                                userName = user.displayName ?: "Guest"
                            )
                        }
                    } else {
                        Timber.e("Required permissions not granted")
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        zegoCallService.unInitialize()
    }

}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
annotation class AppPreview