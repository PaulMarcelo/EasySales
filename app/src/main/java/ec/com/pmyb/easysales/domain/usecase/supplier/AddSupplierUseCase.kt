package ec.com.pmyb.easysales.domain.usecase.supplier

import ec.com.pmyb.easysales.domain.repository.SupplierRepository
import ec.com.pmyb.easysales.domain.model.vo.Supplier
import javax.inject.Inject

class AddSupplierUseCase
@Inject
constructor(private val supplierRepository: SupplierRepository) {
    operator fun invoke(supplier: Supplier): Result<Boolean> {
        return supplierRepository.register(supplier)
    }
}