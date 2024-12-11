package ec.com.pmyb.easysales.domain.usecase.supplier

import ec.com.pmyb.easysales.domain.repository.SupplierRepository
import javax.inject.Inject

class UpdateSupplierUseCase @Inject constructor(private val supplierRepository: SupplierRepository) {
    operator fun invoke(supplierId: String, newNameSupplier: String): Result<Boolean> {
        return supplierRepository.updateSupplier(supplierId, newNameSupplier)
    }
}