package ec.com.pmyb.easysales.domain.usecase.supplier

import ec.com.pmyb.easysales.domain.repository.SupplierRepository
import javax.inject.Inject

class RemoveSupplierUseCase @Inject constructor(private val supplierRepository: SupplierRepository) {
    suspend operator fun invoke(supplierId: String): Result<Boolean> {
        return supplierRepository.deleteSupplier(supplierId)
    }
}