package com.prcsteel.platform.order.model.enums;

public enum InvoiceInBordereauxStatus {
		Zero("0","全部"),
		No("1","未到票"),
		Part("2","部分到票"),
		All("3","已到票");
		
		// 成员变量
		private String key;

		private String value;
		
		public String getKey() {
			return key;
		}


		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		// 构造方法
		private InvoiceInBordereauxStatus(String key, String value) {
			this.key = key;
			this.value = value;
		}
}
