package edu.ufl.bmi.util.cdm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.lang.Comparable;
import java.util.TreeSet;

public class CommonDataModelTable implements Iterable<CommonDataModelField>, 
	Comparable<CommonDataModelTable> {

	String tableName;
	String tableNameLower;
	String tableDescription;
	int tableOrderInCdm;
	CommonDataModel cdm;
	
	ArrayList<CommonDataModelField> fieldList;
	TreeSet<CommonDataModelField> fieldSet;
	HashMap<String, CommonDataModelField> fieldNameToField;
	
	public CommonDataModelTable(CommonDataModel cdm, String tableName) {
		this.tableName = tableName;
		this.tableNameLower = tableName.toLowerCase();
		this.tableDescription = "";
		this.tableOrderInCdm = -1;
		fieldList = new ArrayList<CommonDataModelField>();
		this.fieldSet = new TreeSet<CommonDataModelField>();
		this.cdm = cdm;
		this.fieldNameToField = new HashMap<String, CommonDataModelField>();
	}
	
	public void setTableOrderInCdm(int sequenceNumber) {
		this.tableOrderInCdm = sequenceNumber;
	}
	
	public void addField(CommonDataModelField field) {
		if (field.getFieldOrderInTable() < 0) {
			field.setFieldOrderInTable((fieldList.size() + 1));
		}
		fieldList.add(field);
		fieldSet.add(field);
		fieldNameToField.put(field.getFieldName(), field);
	}
	
	public String getName() {
		return this.tableName;
	}
	
	public String getDescription() {
		return this.tableDescription;
	}
	
	public int getTableOrderInCdm() {
		return this.tableOrderInCdm;
	}

	public Iterator<CommonDataModelField> iterator() {
		return fieldList.iterator();
	}

	public Iterator<CommonDataModelField> getAllFieldsInOrder() {
		return fieldSet.iterator();
	}
	
	public CommonDataModel getCdm() {
		return this.cdm;
	}
	
	@Override
	public boolean equals(Object o) {
		boolean eq = false;
		if (o instanceof CommonDataModelTable) {
			CommonDataModelTable t = (CommonDataModelTable)o;
			eq = cdm.equals(t.cdm) && this.tableNameLower.equals(t.tableNameLower);
		}
		return eq;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(cdm, tableNameLower);
	}

	public void setCommonDataModel(CommonDataModel commonDataModel) {
		if (this.cdm == null) this.cdm = commonDataModel;
	}

	public void setDescription(String description) {
		this.tableDescription = description;
	}

	@Override
	public int compareTo(CommonDataModelTable o) {
		int thisOrder = this.getTableOrderInCdm();
		int thatOrder = o.getTableOrderInCdm();
		if (thisOrder < thatOrder) {
			return -1;
		} else if (thisOrder > thatOrder) {
			return 1;
		} else {
			return 0;
		}
	}

	public CommonDataModelField getFieldByName(String fieldName) {
		return fieldNameToField.get(fieldName);
	}
}
