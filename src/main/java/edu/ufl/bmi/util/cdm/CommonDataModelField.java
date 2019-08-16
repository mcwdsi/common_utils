package edu.ufl.bmi.util.cdm;

import java.util.Objects;

public class CommonDataModelField implements Comparable<CommonDataModelField> {
	String fieldName;
	String fieldNameLower;
	String fieldDescription;
	int fieldOrderInTable;
	int fieldOrderInCdm;
	CommonDataModelTable table;
	
	public CommonDataModelField(CommonDataModelTable table, String fieldName) {
		this.fieldName = fieldName;
		this.fieldNameLower = fieldName.toLowerCase();
		this.fieldDescription = "";
		this.fieldOrderInTable = -1;
		this.fieldOrderInCdm = -1;
		this.table = table;
	}
	
	public CommonDataModelField(CommonDataModelTable table, String fieldName, String description, int fieldOrderInTable, int fieldOrderInCdm) {
		this.fieldName = fieldName;
		this.fieldNameLower = fieldName.toLowerCase();
		this.fieldDescription = description;
		this.fieldOrderInTable = fieldOrderInTable;
		this.fieldOrderInCdm = fieldOrderInCdm;
		this.table = table;
	}
	
	public int getFieldOrderInTable() {
		return fieldOrderInTable;
	}

	public void setFieldOrderInTable(int i) {
		if (fieldOrderInTable < 0) {
			this.fieldOrderInTable = i;
		} 
		//else some exception perhaps?
	}
	
	public String getFieldName() {
		return this.fieldName;
	}
	
	public String getFieldDescription() {
		return this.fieldDescription;
	}
	
	public int getFieldOrderInCdm() {
		return this.fieldOrderInCdm;
	}
	
	@Override
	public boolean equals(Object o) {
		boolean eq = false;
		if (o instanceof CommonDataModelField) {
			CommonDataModelField f = (CommonDataModelField)o;
			eq = this.table.equals(f.table) && this.fieldNameLower.equals(f.fieldNameLower);
		}
		return eq;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(table.getCdm(), table, fieldNameLower);
	}

	@Override
	public int compareTo(CommonDataModelField f) {
		int thatOrder = f.getFieldOrderInTable();
		int thisOrder = getFieldOrderInTable();
		if (thisOrder < thatOrder) {
			return -1;
		} else if (thisOrder > thatOrder) {
			return 1;
		} else {
			return 0;
		}
	}
}
