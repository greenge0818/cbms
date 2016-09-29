package constant;

public class WorkFlowConstant {
	//流程参数命名
	public static String VAR_ID ="id";//记录Id
	public static String VAR_AUDIT_USER="auditUser";//审核人员
	public static String VAR_TYPE="type";//流程类型
	public static String VAR_USER_ID="userId";//流程发起人Id
	public static String VAR_LOGIN_NAME="loginName";//流程发起人姓名
	public static String VAR_LOGIN_ID="loginId";//流程发起人loginId
	public static String VAR_AUDIT_PASS="auditPass";//审核是否通过

	public static String DOC_PROCESS_DEFINITION_KEY="customerDocAudit";//process的Id，唯一标示,对应文件*.bpmn属性process id=
	public static String DOC_AUDIT_DOC ="auditDoc";//流程定义的Id
	public static String DOC_EDIT_DOC = "editDoc";

	public static String INV_PROCESS_DEFINITION_KEY="customerInvoiceAudit";
	public static String INV_AUDIT_INVOICE ="auditInvoice";
	public static String INV_EDIT_INVOICE ="editInvoice";

	public static String BAN_PROCESS_DEFINITION_KEY="customerBankAudit";
	public static String BAN_AUDIT_BANK ="auditBank";
	public static String BAN_EDIT_BANK ="editBank";

	public static String TEMPLATE_PROCESS_DEFINITION_KEY="customerContractTemplateAudit";
	public static String TEMPLATE_AUDIT_TEMPLATE ="auditTemplate";
	public static String TEMPLATE_EDIT_TEMPLATE ="editTemplate";


	//卖家代运营协议流程参数定义
	public static String SELLER_AGREEMENT_PROCESS_DEFINITION_KEY = "sellerConsignAgreementAudit";	//process的Id，唯一标示,对应文件*.bpmn属性process id=
	public static String SELLER_AGREEMENT_MANAGER_AUDIT = "managerAudit";//流程节点定义的Id
	public static String SELLER_AGREEMENT_FINANCE_AUDIT ="financeAudit"; //流程节点定义的Id
	public static String SELLER_AGREEMENT_SERVICE_AUDIT = "serviceAudit"; //流程节点定义的Id
	public static String SELLER_AGREEMENT_EDIT_AGREEMENT = "editAgreement";//流程节点定义的Id
	public static String SELLER_AGREEMENT_UPLOAD_IMG = "uploadImg";//流程节点定义的Id

	public static String VAR_MANAGER_AUDIT_USER ="managerAuditUser";//总经理审核人员
	public static String VAR_FINANCE_AUDIT_USER ="financeAuditUser";//财务审核人员
	public static String VAR_SERVICE_AUDIT_USER ="serviceAuditUser";//综服部审核人员
	public static String VAR_AGREEMENT_ISUPLOAD = "isUpload"; //协议是否上传


	//买家年度采购协议流程参数定义 流程定义及节点id整个acitviti不能一样
	public static String BUYER_AGREEMENT_PROCESS_DEFINITION_KEY = "annualPurchaseAgreementAudit";	//process的Id，唯一标示,对应文件*.bpmn属性process id=
	public static String BUYER_AGREEMENT_MANAGER_AUDIT = "managerAuditBuyer";//流程节点定义的Id
	public static String BUYER_AGREEMENT_FINANCE_AUDIT ="financeAuditBuyer"; //流程节点定义的Id
	public static String BUYER_AGREEMENT_SERVICE_AUDIT = "serviceAuditBuyer"; //流程节点定义的Id
	public static String BUYER_AGREEMENT_EDIT_AGREEMENT = "editAgreementBuyer";//流程节点定义的Id
	public static String BUYER_AGREEMENT_UPLOAD_IMG = "uploadImgBuyer";//流程节点定义的Id








}
