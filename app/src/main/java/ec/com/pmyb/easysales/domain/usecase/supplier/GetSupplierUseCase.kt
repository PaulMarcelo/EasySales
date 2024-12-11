package ec.com.pmyb.easysales.domain.usecase.supplier

import ec.com.pmyb.easysales.domain.repository.SupplierRepository
import ec.com.pmyb.easysales.domain.model.vo.Supplier
import javax.inject.Inject

class GetSupplierUseCase @Inject constructor(private val supplierRepository: SupplierRepository) {
    suspend operator fun invoke(): Result<List<Supplier>> {
        return supplierRepository.getSuppliers()
    }
}