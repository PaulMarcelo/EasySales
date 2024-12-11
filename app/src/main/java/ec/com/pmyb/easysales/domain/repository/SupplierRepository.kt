package ec.com.pmyb.easysales.domain.repository

import ec.com.pmyb.easysales.data.datasourse.SupplierFireStoreDBDataSource
import ec.com.pmyb.easysales.domain.model.vo.Supplier
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupplierRepository @Inject constructor
    (
    private val remoteDataSource: SupplierFireStoreDBDataSource,
) {
    fun register(supplier: Supplier): Result<Boolean> {
        return try {
            remoteDataSource.register(supplier)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSuppliers(): Result<List<Supplier>> {
        return try {
            val suppliers = remoteDataSource.getSuppliers()
            Result.success(suppliers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteSupplier(supplierId: String): Result<Boolean>  {
        return try {
            remoteDataSource.deleteSupplier(supplierId)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun updateSupplier(supplierId: String, newNameSupplier: String): Result<Boolean>  {
        return try {
            remoteDataSource.updateSupplier(supplierId, newNameSupplier)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}