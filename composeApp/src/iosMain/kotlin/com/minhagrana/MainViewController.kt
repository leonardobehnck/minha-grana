package com.minhagrana

import androidx.compose.ui.window.ComposeUIViewController
import com.minhagrana.database.DatabaseInitializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.UIKit.UITextField
import platform.UIKit.UIViewController

private object IosAppInitializer : KoinComponent {
    private val databaseInitializer: DatabaseInitializer by inject()

    fun initialize() {
        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            databaseInitializer.initialize()
        }
    }
}

fun MainViewController(): UIViewController {
    IosAppInitializer.initialize()
    val controller = ComposeUIViewController { App() }

    warmupKeyboard(controller)

    return controller
}

private fun warmupKeyboard(controller: UIViewController) {
    val textField = UITextField()
    textField.hidden = true
    controller.view.addSubview(textField)

    platform.darwin.dispatch_async(platform.darwin.dispatch_get_main_queue()) {
        textField.becomeFirstResponder()
        textField.resignFirstResponder()
        textField.removeFromSuperview()
    }
}
