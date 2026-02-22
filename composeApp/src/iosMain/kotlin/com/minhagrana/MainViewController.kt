package com.minhagrana

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UITextField
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
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
