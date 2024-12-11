package ec.com.pmyb.easysales.data.datasourse

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ec.com.pmyb.easysales.domain.model.vo.Supplier
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SupplierFireStoreDBDataSource @Inject constructor() {
    private val NAME_COLLECTION = "supplier"
    private val firestore: FirebaseFirestore = Firebase.firestore

    fun register(supplier: Supplier) {
        firestore.collection(NAME_COLLECTION)
            .document(supplier.supplierCode)
            .set(supplier)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
    }


    suspend fun getSuppliers(): List<Supplier> {
        return firestore.collection(NAME_COLLECTION)
            .get()
            .await()
            .documents
            .mapNotNull { it.toObject(Supplier::class.java) }
    }

    suspend fun deleteSupplier(supplierId: String) {
            val documentRef = firestore.collection(NAME_COLLECTION).document(supplierId)
            documentRef.delete().await()
    }

    fun updateSupplier(supplierId: String, newNameSupplier: String) {
        val userRef = firestore.collection(NAME_COLLECTION).document(supplierId)
        userRef.update("supplierName", newNameSupplier)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
    }


}