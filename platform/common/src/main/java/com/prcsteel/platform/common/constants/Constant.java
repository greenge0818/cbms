package com.prcsteel.platform.common.constants;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by rolyer on 15-7-21.
 */
public class Constant {

    public static final String NO = "0";
    public static final String YES = "1";
    public static final String SYSTEMNAME = "系统";
    public static final String IMAGE_SUFFIX = "[jpg][gif][png][jpeg]";
    public static final Long MAX_IMG_SIZE = 2L;//2M
    public static final Long M_SIZE = 1024L * 1024L;//1M的size
    //盖章扫描件或数码照，支持jpeg、jpg、png、gif 图片格式，小于2M；
    public static final String SUCCESS = "成功";
    public static final String FAIL = "失败";
    public static final String ADD = "添加";
    public static final String EDIT = "编辑";
    public static final String SUBMIT = "提交审核";
    public static final String ADD_REBATE_SUCCESS = "充值成功";
    public static final String REDUCE_REBATE_SUCCESS = "操作成功";
    public static final String OPERATION_SAVE = "save";
    public static final String OPERATION_SUBMIT = "submit";
    public static final String OPERATION_ACTIVE = "active";
    public static final String OPERATION_CLOSE = "close";
    public static final String OPERATION_DEDUCTION = "deduction";
    public static final String OPERATION_RESTITUTION = "restitution";

    public static final String FROMBANKTRANSACTIONJOB = "fromBankTransactionJob";

    public static final DecimalFormat amountFormat = new DecimalFormat("0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
    public static final DecimalFormat weightFormat = new DecimalFormat("0.000000");// 构造方法的字符格式这里如果小数不足4位,会以0补足.

    //businessException code
    public static final String EXCEPTIONCODE_SYSTEM = "0001";//系统错误
    public static final String EXCEPTIONCODE_UNKONWN = "0002";//未知错误
    public static final String EXCEPTIONCODE_BUSINESS = "0003";//业务错误
    public static final String ERROR_MISSING_REQUIRED_PARAM = "0004";//必须的参数为空
    public static final String ERROR_RESOURCE_NOT_FOUND = "0005";//必须的参数为空

    public static final int MEMCACHESESSIONOUT = 30*60*60; //默认存30分钟

    public static final String DATABASE_VERSION_KEY = "cbms_database_version";
    public static final String ORDERSTATUSNAME_PICKEDUP="PICKEDUP";
    public static final String ORDERSTATUSNAME_FILLEDUP="FILLEDUP";

    public static final Integer WEIGHT_PRECISION = 6;    // 重量小数点精度
    public static final Integer MONEY_PRECISION = 2;    // 金额小数点精度
    public static final int INVOICE_IN_DETAIL_ORDER_ITEM_INACTIVE = 0;
    public static final int INVOICE_IN_DETAIL_ORDER_ITEM_ACTIVE = 1;

    public static final String INVOICE_IN_ALLOW_AMOUNT_DEVIATION="invoice_in_allow_amount_deviation";//进项发票金额与订单详情金额允许的误差值
    public static final String INVOICE_IN_ALLOW_WEIGHT_DEVIATION="invoice_in_allow_weight_deviation";//进项发票重量与订单详情重量允许的误差值
    public static final String INVOICE_IN_FILTER_TEXT="invoice_in_filter_text";//发票输入项上的过滤文本

    // cache key
    public static final String CACHE_NAME = "mycache";
    public static final String CACHE_SETTING_ID = "setting_id_";
    public static final String CACHE_SETTING_TYPE = "setting_type_";
    public static final String CACHE_All_CATEGORY = "AllCategory";
    public static final String CACHE_All_CATEGORY_GROUP = "AllCategoryAndGroup";
    public static final String CACHE_ALL_CATEGORY_MODEL = "AllCategoryModel";
    public static final String CACHE_All_CATEGORY_GROUP_INNER = "AllCategoryGroupInner";
    public static final String CACHE_All_PARENT_CATEGORY_GROUP_OUTER = "AllParentCategoryGroupOuter";
    public static final String CACHE_All_CATEGORY_GROUP_OUTER = "AllCategoryGroupOuter";
    public static final String CACHE_CITY_LIST_BY_PROVINCE_ID = "city_listByProvinceId_";
    public static final String CACHE_CITY_SELECT_BY_PRIMARYKEY = "city_selectByPrimaryKey_";
    public static final String CACHE_CITY_ALL = "city_all_";
    public static final String CACHE_ORDER_PROCESS_BY_USERId = "getOrderProcessByUserId";
    public static final String CACHE_All_OFFICE_STAFF = "getOfficeStaffIds";
    public static final String CACHE_DISTRICT_LIST_BY_CITYID = "listByCityId";
    public static final String CACHE_DISTRICT_SELECT_BY_PRIMARYKEY = "district_selectByPrimaryKey_";
    public static final String CACHE_ALL_ORGANIZATION = "organization_queryAll";
    public static final String CACHE_PERMISSION_BY_PARENTID = "permission_parentId_";
    public static final String CACHE_ALL_PROVINCE_LIST = "province_listAll";
    public static final String CACHE_PROVINCE_SELECT_BY_PRIMARYKEY = "province_selectByPrimaryKey_";
    public static final String CACHE_ROLE_PERMISSION = "role_permission_";
    public static final String CACHE_USER_ERMISSION = "user_permission_";
    public static final String CACHE_TODAYS_MIN_PURCHASE_ORDER_ID = "min_purchase_order_";
    /*** 资源导出文件名称****/
    public static final String EXCEL_EXPORT_FILE_NAME="资源文件.xls";
    /*** excel模板文件名称****/
    public static final String EXCEL_TEMPLET_FILE_NAME="templet.xls";
    /*** excel模板文件内容分隔符****/
    public static final String EXCEL_TEMPLET_FIEL_CONTENT_SEPARATOR=";;;";
    /*** excel模板文件规格分隔符****/
    public static final String EXCEL_TEMPLET_FIEL_NORMS_SEPARATOR="*";
    /*** excel模板文件数值小数保留位数****/
    public static final Integer EXCEL_TEMPLET_BIGDECIMAL_SCALE=3;

    public static final String TYPE_OF_SPEC_SETTING_TYPE = "typeofspec";

    public static final String REPORT_DAY_COL_NAME_DAYTOTAL = "dayTotal";
    public static final String REPORT_DAY_ROW_DATA_TYPE = "rowDataType";

    public static final String INQUIRYORDER_SAVE_STATUS_SAVED = "已保存";
    public static final String INQUIRYORDER_SAVE_STATUS_UNSAVE = "未保存";
    public static final String INQUIRYORDER_SAVE_STATUS_INQUIRY = "已询价";
    public static final String INQUIRYORDER_SAVE_STATUS_UNINQUIRY = "未询价";

    public static final String LOGINUSER = "LOGINUSER"; // 用于将当前登录用户保存在Session中
    public static final String USER_ROLE = "USER_ROLE"; // 用于将当前登录用户权限保存在Session中
    public static final String USER_ORGANIZATION = "USER_ORGANIZATION"; // 用于将当前登录用户权限保存在Session中

    public static final String SHIRO = "shiro";
    public static final String USER_NAME = "userName";
    public static final String LOGOUT_URL = "logoutUrl";
    public static final String ORG_NAME = "orgName";

    public static final String ACL_DOMAIN = "aclDomain";
    public static final String ACCOUNT_DOMAIN = "accountDomain";
    public static final String ORDER_DOMAIN = "orderDomain";
    public static final String CORE_DOMAIN = "coreDomain";
    public static final String SMARTMATCH_DOMAIN = "smartmatchDomain";
    public static final String ASS_DOMAIN = "assDomain";
    public static final String KUANDAO_DOMAIN = "kuandaoDomain";
    public static final String QUARTZ_JOB_ENABLED = "true";

    public static final String TIME_FORMAT_DAY = "yyyy-MM-dd";

    public static final String REFUND_TYPE = "到账异常处理线下退款";

    public static final String PERMISSION_TRADER_HIDDEN = "sys:field:user";//交易员不显示搜索框权限
    public static final String PERMISSION_ORG_LIMIT = "sys:field:org";//服务中心搜索权限

    public static final String PERMISSION_FINANCE_ORDER = "order:query:financeorder";//只查看融资订单权限

    public static final String PERMISSION_VIEW_SUFFIX = "view";

    public static final String HTTP_HEAD_REQUEST_WITH = "X-Requested-With";
    public static final String AJAX_REQUEST_HEAD_VALUE = "xmlhttprequest";


    //public static final String TEMPLATE_BUYER = "template_buyer";
    //public static final String TEMPLATE_SELLER = "template_seller";

    public static final String TEMPLATE_PATH = "template";
    public static final String TEMPLATE_BUYER = "buyer";
    public static final String TEMPLATE_SELLER = "seller";

    public static final String TEMPLATE_PRINT_PIC = "template_print_pic";//打印图片模板


    public static final String  SYS_START_TIME = "2015-08-01 00:00:00";//系统开始时间

    public static final String TEMPLATE_BUYER_PARAM = "is_start_template_buyer";//是否开启新的买家模板

    // 文件临时保存目录
    public static final String FILESAVETEMPPATH = "temp" + File.separator ;

    public static final int MEMCACHESESSIONOUTTOWECHAT = 2*60*60; //默认存7200秒
    
    public static final String SOURCE_TO_PICK = "PHONE";

    public static final String SEND_SMS_SUCCESS = "短信发送成功";

    public static final String FROM = "SMART";

    public static final String SECRET = "f3b6d77cceea9adc612a124c14ca508b";//系统间通信密钥
    
    public static final String DATEFORMAT_YYYYMMDD_HHMMSS ="yyyy-MM-dd_HH:mm:ss";

    public static final String SYS_TEMPLATE_COMPANY_NAME = "";//系统默认模板公司名称
    public static final String SYS_TEMPLATE_CONTRACT_NO= "XS-CPB-000005-1502-0001";//系统默认模板合同编号

    /**
     * 超市Rest 调用查询类型：热门资源 add by peanut on 2016/06/23
     */
    public static final String REST_HOT_RESOURCE_TYPE = "HOT";
    /**
     * 超市Rest 缓存的键值：热门资源 add by caosulin on 2016/07/20
     */
    public static final String REST_HOT_RESOURCE_CACHE_KEY = "HOT_RESOURCE_CACHE_KEY";
    /**
     * 超市Rest 缓存超时时间：热门资源 add by caosulin on 2016/07/20
     */
    public static final int REST_HOT_RESOURCE_CACHE_FAILURE_TIME = 20*60;//失效时间,20分钟

    /**
     * 掌柜appRest 缓存的键值：日常资源 add by zhoucai on 2016/07/20
     */
    public static final String REST_DAILY_RESOURCE_CACHE_KEY = "DAILY_RESOURCE_CACHE_KEY";
    /**
     * 掌柜appRest 有限排序城市：日常资源 add by zhoucai on 2016/07/20
     */
    public static final String REST_DAILY_RESOURCE_CITY = "上海";
    /**
     * 超市Rest 缓存超时时间：日常资源 add by zhoucai on 2016/07/20
     */
    public static final int REST_DAILY_RESOURCE_CACHE_FAILURE_TIME = 10*60;//失效时间,10分钟
    /**
     * 超市Rest 调用查询类型：普通资源 add by peanut on 2016/06/23
     */
    public static final String REST_NORMAL_RESOURCE_TYPE = "NORMAL";

    /**
     * 默认页码 add by peanut on 2016/06/23
     */
    public static final int DEFAULT_PAGE_INDEX = 1;

    /**
     * 默认热门资源条数 add by peanut on 2016/06/23
     */
    public static final int DEFAULT_HOT_RESOURCE_PAGE_SIZE = 10;

    /**
     * 默认普通资源条数 add by peanut on 2016/06/27
     */
    public static final int DEFAULT_NORMAL_RESOURCE_PAGE_SIZE = 15;

    /**
     * 超市列表默认排序方式 add by peanut on 2016/06/22
     */
    public static final String DEFAULT_ORDER_WAY = "ASC";

    /**
     * 超市列表默认根据价格排序 add by peanut on 2016/06/22
     */
    public static final String DEFAULT_ORDERBY = "price";

    /**
     * 订单买家状态类型
     */
    public enum BUYER_TYPE {
        CHECKUNPASSED,//0审核不通过
        NEW,//1新建（待审核） 待审核
        UNASSOCIATED,//2审核通过(待关联) 待关联
        ASSOCIATED,//3关联完成（待提货申请+待申请付款）
        PICKUPUNPASS,//4提货申请不通过
        PICKUPPASSED,//5提货申请通过（待开放货函）
        TOBESECONDARY,//6.开完放货函（待二次结算）
        DONESECONDARY,//7二次结算完成(待开票申请)
        TOBEINVOICED//8开票申请完成（待开票）
    }

    /**
     * 风险控制开关label,见setting表，type='ControlPinSettings'
     * @author tuxianming
     * @date  20160517
     */
    public enum ControlPinSettings{
    	with_twice_balance_control, //根据买家客户二结余额控制提交销项申请设置
    	with_consume_apply_control,	//根据对应的销项票申请状态控制进项票寄出设置
    	with_deal_control			//根据交易凭证审核状态控制销项申请设置
    }
    
    /**
     * 销项票--生成销项清单--三种生成方式 
     * @author tuxianming
     * @date 20160517
     */
    public enum InvoiceOutSendStatus {
    	SEND("1"),                  //可寄出
        UNSEND("2"),                //不可寄出
        ALL("3")      				//w全部
        ;

        private String code;

        InvoiceOutSendStatus(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
    
    
    /**
     * 代运营交易单管理TAB页
     */
    public enum ConsignOrderTab {
        INDEX,//全部
        ASSOCIATE, //待关联/待收款
        PAYMENT,  //待审核付款
        APPLYPAYMENT, //待申请付款
        REVIEW_PAYMENT,//待审核付款交易单付款
        AUDIT_SECONDSETTLE,//待审核付款二次结算付款
        AUDIT_CASH_PAYMENT,//待审核付款提现付款
        PRINTPENDINGPAYAPPLY,//待打印付款申请（核算会计）
        PRINTPENDINGPAYAPPLY_ORDER,//待打印付款申请 订单
        PRINTPENDINGPAYAPPLY_SECONDSETTLE,//待打印付款申请 二结
        PRINTPENDINGPAYAPPLY_WITHDRAW,//待打印付款申请 提现
        CONFIRMPAYMENT, //待确认已付款（出纳）
        CONFIRMPAYMENT_ORDER,//待确认已付款 订单
        CONFIRMPAYMENT_SECONDSETTLE,//待确认已付款 二结
        CONFIRMPAYMENT_WITHDRAW,//待确认已付款 提现
        ConfirmPaymentBankCode,//待确认已付款 客户银行账号审核
        APPROVAL,  //待审核
        PICKUP,  //待提货
        FILLUP, //待放货
        SECONDSETTLEMENT,//待二次结算
        TRADECOMPLETE, //交易完成
        TRADECLOSE,//交易关闭
        TRADECLOSEAPPROVAL,//交易关闭待审核
        BANKTRANSACTION,//到账异常处理
        BANKTRANSACTION_TRANSFER,//到账异常查询
        BANKTRANSACTION_ERROR,//到账异常疑似支付错误
        INVOICE,  //待开票 已二次结算
        CHANGELIST, //合同变更
        
        CUSTBASEPRICE,//基价管理
        BUSINESSLOOK,//业务找货
        PURCHASEORDER,//询价单待处理
        APPOINTED,//询价单待指派
        PREBILLING,//add by zhoucai@prcsteel.com 20160822 询价单待开单

        PAYMENTADVANCE,//预付款申请
    	PAYMENTPRINT,//待打印预付款申请
        PAYMENTCONFIRM,//预付款确认
        TRADECERTIFICATE;//预付款确认

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    /**
     * 角色状态
     */
    public enum ROLE_STATUS {
        LOCKED,         //锁定
        NORMAL          //正常
    }

    /**
     * 用户状态
     */
    public enum USER_STATUS {
        LOCKED(0),         //锁定
        NORMAL(1);         //正常

        private int value;

        USER_STATUS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    /**
     * 银行类型
     */
    public enum bankType {
        SPD, // 浦发
        ICBC; // 工行
    }



    /**
     * 流水类型
     */
    public enum TransType {
        ORDER(0),       //订单
        SPD(1),         //浦发
        ICBC(2);        //工行

        private int value;

        TransType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    /**
     * 权限操作类型
     */
    public enum PermissionOperateType {
        DEL,     //删除权限
        ADD    //增加权限
    }

    /**
     * 操作状态
     */
    public enum OperateStatus {
        SUCCESS,            //成功
        FAIL,               //失败
        DUPLICATE,          //重复
        INVALID_PARAMETER   //非法参数
    }

    /**
     * 寄售订单状态
     */
    public enum ConsignOrderStatus {
        NEW("1", "新建（待审核） 待审核"),
        NEWDECLINED("-1", "订单关闭-审核不通过"),
        NEWAPPROVED("2", "审核通过(待关联) 待关联"),
        CLOSEREQUEST1("3", "请求关闭1"),
        CLOSEAPPROVED("-2", "订单关闭-请求通过"), //订单关闭-请求1通过、订单关闭-请求2通过
        RELATED("4", "已关联"),
        CLOSEREQUEST2("5", "请求关闭2"),
        SECONDSETTLE("6", "待二次结算"),
        INVOICEREQUEST("7", "待开票申请"),
        INVOICE("8", "待开票"),
        FINISH("10", "交易完成"),
        SYSCLOSED("-3", "订单关闭-5点半未关联（待关联）订单");

        private String code;
        private String name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        ConsignOrderStatus(String code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    /**
     * 付款状态
     */
    public enum PayStatus {
        REQUESTED,    //已申请/待审核
        APPROVED,     //已通过审核
        CONFIRMED,    //已确认
        DECLINED,     //审核不通过
        CONFIRMEDPAY  //已确认付款
, APPLYPRINTED
    }

    /**
     * 付款申请类型
     */
    public enum PayRequestType {
        PAYMENT("1", "初次付款"),
        SECONDSETTLE("2", "二次结算"),
        WITHDRAW("3", "提现");

        private String code;
        private String name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        PayRequestType(String code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    /**
     * 票据类型
     */
    public enum BILL_TYPE {
        JS,//寄售
        DH,//订货
        XS//销售
    }

    /**
     * 寄售交易单付款状态
     */
    public enum ConsignOrderPayStatus {
        APPLY, // 待申请（未申请／审核不通过）
        REQUESTED,//已申请/待审核
        APPROVED, //已通过审核待确认付款
        PAYED  //已确认付款
    }

    /**
     * 寄售订单提货状态
     */
    public enum ConsignOrderPickupStatus {
        NO_ENTRY(1, "未录入"),
        PART_ENTRY(2, "部分录入"),
        ALL_ENTRY(3, "全录入");

        private int code;
        private String name;

        ConsignOrderPickupStatus(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * 寄售订单放货状态
     */
    public enum ConsignOrderFillUpStatus {
        NO_PRINT_ALL(0, "未全打印"),
        PRINT_ALL(1, "全部打印"),
        ALL_MATCHES(2, "全部匹配"),
        INITIAL(-1, "初始化");

        private int code;
        private String name;

        ConsignOrderFillUpStatus(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * 订单状态变化时的状态类型
     */
    public enum OrderStatusType {
        MAIN, // 订单状态
        PAY,//付款状态
        SECONDPAY,// 二次结算付款
        PICKUP,// 提货状态
        FILLUP // 放货状态
    }

    /**
     * 角色数据查看类型
     */
    public enum ROLE_TYPE {
        ONLY_ME(0),        //仅自己
        ALL(1),            //所有
        SAME_LEVEL(2),     //同级+下线
        LOWER_LEVEL(3);     //下级

        ROLE_TYPE(int value) {
            this.value = value;
        }

        private int value;

        public int getValue() {
            return value;
        }
    }

    /**
     * 客户类型
     */
    public enum ACCOUNT_TYPE {
        BUYER,
        SELLER;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    /**
     * 风险控制 -- 上传的凭证 状态
	 * tuxianming 20160525 
     */
    public enum CREDENTIAL_UPLOAD_STATUS {
    	PENDING_REVISION,  	//待校对
    	PENDING_COLLECT, 	//已校对待集齐
    	ALREADY_COLLECT, 	//已集齐
    	OLD_DATA,		//老数据 
    	;
    }

    /**
     * 异常处理状态类型
     */
    public enum TRANSACTION_TYPE {
        NORMAL,//正常
        UNPROCESSED,//未处理
        REFUND,//已处理，线下退款
        CHARGE;//已处理，新建账户并充值

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
}
