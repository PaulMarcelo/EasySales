package ec.com.pmyb.easysales.data.datasourse

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ec.com.pmyb.easysales.domain.model.ui.UserSession
import ec.com.pmyb.easysales.domain.model.vo.Permission
import ec.com.pmyb.easysales.domain.model.vo.Profile
import ec.com.pmyb.easysales.domain.model.vo.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UsersFireStoreDBDataSource @Inject constructor() {
    private val NAME_COLLECTION_USER = "users"
    private val NAME_COLLECTION_PROFILE = "profile"
//    private val NAME_COLLECTION_PERMISSION = "permission"
    private val firestore: FirebaseFirestore = Firebase.firestore

    suspend fun getUserByEmail(email: String): UserSession? {
        // Obtiene el usuario por email
        val documentUser = firestore.collection(NAME_COLLECTION_USER)
            .document(email)
            .get()
            .await()

        if (!documentUser.exists()) return null

        val user = documentUser.toObject(User::class.java) ?: return null

        // Obtiene el perfil del usuario
        val documentProfile = firestore.collection(NAME_COLLECTION_PROFILE)
            .document(user.profile)
            .get()
            .await()

        val profile = documentProfile.toObject(Profile::class.java) ?: return null

//        // Obtiene los permisos del perfil
//        val documentPermission = firestore.collection(NAME_COLLECTION_PERMISSION)
//            .document(profile.name)
//            .get()
//            .await()
//
//        val permission = documentPermission.toObject(Permission::class.java) ?: return null

        return UserSession(
            user = user,
            profile = profile
        )
    }


}
