package ec.com.pmyb.easysales

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.google.firebase.FirebaseApp


@HiltAndroidApp
class EasySalesApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        // Inicializa Firebase cuando la aplicación se crea
        FirebaseApp.initializeApp(this)
    }
}