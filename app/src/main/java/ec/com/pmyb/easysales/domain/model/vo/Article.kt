package ec.com.pmyb.easysales.domain.model.vo

class Article : BaseAudit() {
    var register_date: String = ""
    var local_code: String = ""
    var supplier_code: String = ""
    var name: String = ""
    var sale_price: Double = 0.0
    var supplier_price: Double = 0.0
    var quantity: Int = 0
    var category: String = ""
    var comment: String = ""
    var status_local: String = ""
}
