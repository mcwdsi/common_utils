package edu.ufl.bmi.util.cdm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class CommonDataModelTable implements Iterable<CommonDataModelField> {
	String tableName;
	String tableNameLower;
	String tableDescription;
	int tableOrderInCdm;
	CommonDataModel cdm;
	
	ArrayList<CommonDataModelField> fieldList;
	
	public CommonDataModelTable(CommonDataModel cdm, String tableName) {
		this.tableName = tableName;
		this.tableNameLower = tableName.toLowerCase();
		this.tableDescription = "";
		this.tableOrderInCdm = -1;
		fieldList = new ArrayList<CommonDataModelField>();
		this.cdm = cdm;
	}
	
	public void setTableOrderInCdm(int sequenceNumber) {
		this.tableOrderInCdm = sequenceNumber;
	}
	
	public void addField(CommonDataModelField field) {
		if (field.getFieldOrderInTable() < 0) {
			field.setFieldOrderInTable((fieldList.size() + 1));
		}
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
}
