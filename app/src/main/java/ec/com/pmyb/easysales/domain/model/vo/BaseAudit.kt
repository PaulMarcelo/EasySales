package ec.com.pmyb.easysales.domain.model.vo

 open class BaseAudit {
    var created_at: String = ""
    var updated_at: String = ""
    var created_by: String = ""
    var updated_by: String = ""
    var created_ip: String = ""
    var updated_ip: String = ""
    var status: Int = 0
 }
