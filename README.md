# dagger-ksp-component-bug
This project is meant to serve as a simple repro for https://github.com/google/dagger/issues/4395.

```
$ ./gradlew :app:assembleDebug

app/build/generated/ksp/debug/java/dev/jonamireh/daggerkspcomponentbug/wiring/DaggerAppComponent.java:37: error: AppComponentImpl is not abstract and does not override abstract method inject(TestActivity) in TestActivityInjector
  private static final class AppComponentImpl implements AppComponent {
                       ^
```
