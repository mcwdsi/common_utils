package edu.ufl.bmi.util.cdm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.Iterator;
import java.util.TreeSet;

public class CommonDataModel {
	/*
	 * Holds tables, which hold fields
	 * 
	 * Can have info associated with each table
	 * 
	 * Can have info associated with each field
	 * 
	 * In addition to specific fields, there's a map from additional 
	 * 	  type of info to the value for that table/field.
	 */
	
	String cdmName;
	String cdmNameLower;
	String cdmDescription;
	String cdmVersionIdentifier;
	String creator;
	Calendar versionReleaseDate;
	
	HashMap<String, CommonDataModelTable> _tableNameToTable;
	HashMap<Integer, CommonDataModelTable> tableOrderToTable;
	HashMap<String, Integer> tableNameToTableOrder;
	
	ArrayList<CommonDataModelTable> tableList;
	TreeSet<CommonDataModelTable> tableSet;
	
	public CommonDataModel(String name) {
		this.cdmName = name;
		this.cdmNameLower = name.toLowerCase();
		this.cdmVersionIdentifier = "";
		this.creator = "";
		this.cdmDescription = "";
		this.versionReleaseDate = null;
		this.tableList = new ArrayList<CommonDataModelTable>();
		this.tableSet = new TreeSet<CommonDataModelTable>();
		this._tableNameToTable = new HashMap<String, CommonDataModelTable>();
		this.tableOrderToTable = new HashMap<Integer, CommonDataModelTable>();
		this.tableNameToTableOrder = new HashMap<String, Integer>();
	}
	
	public CommonDataModel(String name, String version, String creator, String description, String versionDateMmDdYyyy) {
		this.cdmName = name;
		this.cdmNameLower = name.toLowerCase();
		this.creator = creator;
		this.cdmVersionIdentifier = version;
		this.versionReleaseDate = parseDateMmDdYyyy(versionDateMmDdYyyy);
		this.cdmDescription = description.trim();
		this.tableList = new ArrayList<CommonDataModelTable>();
		this.tableSet = new TreeSet<CommonDataModelTable>();
		this._tableNameToTable = new HashMap<String, CommonDataModelTable>();
		this.tableOrderToTable = new HashMap<Integer, CommonDataModelTable>();
		this.tableNameToTableOrder = new HashMap<String, Integer>();
	}
	
	protected Calendar parseDateMmDdYyyy(String versionDateMmDdYyyy) {
		String[] flds =  versionDateMmDdYyyy.split(Pattern.quote("-"));
		if (flds.length == 1) {
			flds = versionDateMmDdYyyy.split(Pattern.quote("/"));
		}
		int year = Integer.parseInt(flds[2]);
		int month = Integer.parseInt(flds[0])-1;
		int dayOfMonth = Integer.parseInt(flds[1]);
		Calendar c = Calendar.getInstance();
		c.set(year, month, dayOfMonth, 0, 0, 0);
		return c;
	}
	
	public void addTable(CommonDataModelTable table) {
		if (table.getCdm() == null) table.setCommonDataModel(this);
		tableList.add(table);
		tableSet.add(table);
		//System.out.println("Adding table " + table.getName());
		_tableNameToTable.put(table.getName(), table);
		tableOrderToTable.put(table.getTableOrderInCdm(), table);
		tableNameToTableOrder.put(table.getName(), table.getTableOrderInCdm());
	}
	
	public String getCdmName() {
		return cdmName;
	}
	
	public String getCdmDescription() {
		return cdmDescription;
	}
	
	public String getCdmVersion() {
		return cdmVersionIdentifier;
	}
	
	public String getCreator() {
		return creator;
	}
	
	public Calendar getVersionReleaseDate() {
		return versionReleaseDate;
	}

	public CommonDataModelTable getTableByName(String cdmTableName) {
		return _tableNameToTable.get(cdmTableName.trim());
	}

	public int getTableOrderByName(String cdmTableName) {
		Integer i = tableNameToTableOrder.get(cdmTableName);
		return i.intValue();
	}

	public Iterator<CommonDataModelTable> getAllTables() {
		return tableList.iterator();
	}

	public Iterator<CommonDataModelTable> getAllTablesInOrder() {
		return tableSet.iterator();
	}

	public void normalizeFieldOrders() {
		/*
		 * For each table, iterate through its fields. If the fields are not numbered consecutively
		 * 	1 through n, where n is the total number of fields in the table, then make them
		 *  1 through n consecutive.  If there is a global CDM field order, we'll base the 
		 *  table-specific ordering on it.  If there is no ordering of fields at all, then
		 *  what a lousy CDM.  But we'll just insist on one.  In fact, already in the reading
		 *  in of the CDM, our code will have crashed without any ordering info.
		 *  
		 *  Similarly, if there's no global CDM field ordering, we'll use table-based ordering, 
		 *  	in combination with the ordering of tables, to impose one.  Same caveats apply.
		 */
		Iterator<CommonDataModelTable> i = tableSet.iterator();
		int cdmFieldCounter = 1, lastTableHighestCdmOrder = -1;
		while (i.hasNext()) {
			/*
			 * If we are on table #1 in the order, then global and table-specific field ordering
			 * 	are identical.  If they're not identical, then fix it.
			 */
			CommonDataModelTable t = i.next();
			Iterator<CommonDataModelField> j = t.getAllFieldsInOrder();
			int tableOrder = t.getTableOrderInCdm();
			// Specific to first table
			if (tableOrder == 1) {
				int fieldCounter = 1;
				while (j.hasNext()) {
					CommonDataModelField f = j.next();
					int fieldOrderInTable = f.getFieldOrderInTable();
					int fieldOrderInCdm = f.getFieldOrderInCdm();
					
					if (fieldOrderInTable != fieldOrderInCdm) {
						if (fieldOrderInTable < 0 && fieldOrderInCdm > 0)
							fieldOrderInTable = fieldOrderInCdm;
						else if (fieldOrderInCdm < 0 && fieldOrderInTable > 0)
							fieldOrderInCdm = fieldOrderInTable;
						else  
							fieldOrderInCdm = (fieldOrderInTable = fieldCounter);
					}
					fieldCounter++;
					cdmFieldCounter++;
				}
				lastTableHighestCdmOrder = cdmFieldCounter-1; 
			} else {
				//generic to remaining tables
				int fieldCounter = 1;
				while (j.hasNext()) {
					CommonDataModelField f = j.next();
					int fieldOrderInTable = f.getFieldOrderInTable();
					int fieldOrderInCdm = f.getFieldOrderInCdm();
					if (fieldOrderInTable == fieldOrderInCdm && fieldOrderInCdm > lastTableHighestCdmOrder) {
						int k = fieldOrderInTable - lastTableHighestCdmOrder;
						f.setFieldOrderInTable(k);
					} else if (fieldOrderInTable < 0) {
						f.setFieldOrderInTable(fieldCounter);
					} else if (fieldOrderInCdm < lastTableHighestCdmOrder) {
						f.setFieldOrderInCdm(lastTableHighestCdmOrder + fieldCounter);
					}
					fieldCounter++;
					cdmFieldCounter++;
				}
				lastTableHighestCdmOrder = cdmFieldCounter-1;
			}
			
		}
	}
}
