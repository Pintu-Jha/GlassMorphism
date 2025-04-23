package com.awesomeproject.glass

import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.LocalLifecycleOwner

class GlassViewManager : SimpleViewManager<View>() {
    override fun getName() = "GlassView"

    override fun createViewInstance(reactContext: ThemedReactContext): View {
        return ComposeView(reactContext).apply {
            // Set the appropriate lifecycle owners before setting content
            ViewTreeLifecycleOwner.set(this, reactContext as LifecycleOwner)
            ViewTreeSavedStateRegistryOwner.set(this, reactContext as androidx.savedstate.SavedStateRegistryOwner)
            
            // Use a safer composition strategy
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            
            // Add attach state listener to ensure safe composition
            var isAttached = false
            addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    isAttached = true
                    if (isAttached) {
                        setContent {
                            SafeGlassEffectWrapper()
                        }
                    }
                }
                
                override fun onViewDetachedFromWindow(v: View) {
                    isAttached = false
                }
            })
        }
    }
}