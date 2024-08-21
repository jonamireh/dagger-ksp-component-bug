package dev.jonamireh.daggerkspcomponentbug.wiring

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dev.jonamireh.daggerkspcomponentbug.TestActivityInjector

@Component
interface AppComponent : TestActivityInjector {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}