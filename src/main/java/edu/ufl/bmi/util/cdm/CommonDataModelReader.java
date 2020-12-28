package edu.ufl.bmi.util.cdm;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.regex.Pattern;


public class CommonDataModelReader {

	LineNumberReader r;
	CommonDataModel cdm;
	
	public CommonDataModelReader(Reader r) {
		this.r = new LineNumberReader(r);
	}
	
	public CommonDataModel read() throws IOException, ParseException {
		readCdmInfo();
		readTableInfo();
		readFieldInfo();
		cdm.normalizeFieldOrders();
		return cdm;
	}

	private void readCdmInfo() throws IOException, ParseException {
		String line = r.readLine();
		if (!line.equals("@CDM"))
			throw new ParseException("file must begin with @CDM", 0);
		
		String cdmName=null, cdmDescription=null, cdmVersion=null, cdmCreator=null, cdmVersionDate=null;
		while(!(line=r.readLine()).trim().equals("")) {
			String[] flds = line.split(Pattern.quote("="));
			switch (flds[0]) {
				case "name":
					cdmName = flds[1];
					break;
				case "description":
					cdmDescription = flds[1];
					break;
				case "version":
					cdmVersion = flds[1];
					break;
				case "versionReleaseDate":
					cdmVersionDate = flds[1];
					break;
				case "creator":
					cdmCreator = flds[1];
					break;
				default:
					throw new ParseException("Do not understand CDM attribute: " + flds[0], 1);
			}
		}
		
		cdm = new CommonDataModel(cdmName, cdmVersion, cdmCreator, cdmDescription, cdmVersionDate);
		
	}

	private void readTableInfo() throws IOException, ParseException {
		String line = r.readLine();
		if (!line.equals("@TABLE"))
			throw new ParseException("Table section must begin with @TABLE", 2);
		
		line = r.readLine();
		String[] flds = line.split(Pattern.quote("\t"));
		int iName = -1, iDesc = -1, iOrder = -1;
		for (int i=0; i<flds.length; i++) {
			String fld = flds[i].toLowerCase();
			if (fld.contains("name")) {
				iName = i;
			} else if (fld.contains("descr") || fld.contains("defin")) {
				iDesc = i;
			} else if (fld.contains("order") || fld.contains("seq")) {
				iOrder = i;
			}
		}
		
		while(!(line=r.readLine()).trim().equals("")) {
			flds = line.split(Pattern.quote("\t"));
			
			String name = flds[iName];
			String desc = flds[iDesc];
			String seqTxt = (iOrder > 0) ? flds[iOrder] : null;
			int seq = (seqTxt != null) ? Integer.parseInt(seqTxt) : -1;
			
			CommonDataModelTable t = new CommonDataModelTable(cdm, name);
			if (desc != null) t.setDescription(desc);
			if (seqTxt != null) t.setTableOrderInCdm(seq);
			cdm.addTable(t);
		}
	}

	private void readFieldInfo() throws IOException, ParseException {
		String line = r.readLine();
		if (!line.equals("@FIELD"))
			throw new ParseException("Field section must begin with @FIELD", 3);
		
		/* read header row */
		line = r.readLine();
		String[] flds = line.split(Pattern.quote("\t"));
		int iName = -1, iDesc = -1, iOrder = -1, iTable = -1;
		for (int i=0; i<flds.length; i++) {
			String fld = flds[i].toLowerCase();
			if (fld.contains("field") && fld.contains("name")) {
				iName = i;
			} else if (fld.contains("descr") || fld.contains("defin")) {
				iDesc = i;
			} else if (fld.contains("order") || fld.contains("seq")) {
				iOrder = i;
			} else if (fld.contains("table") && fld.contains("name")) {
				iTable = i;
			}
		}
		System.out.println(iName);
		
		//System.out.println("reading fields.");
		while(!(line=r.readLine()).trim().equals("")) {
			//System.out.println("line " + r.getLineNumber() + ": " + line);
			//System.out.println("reading next field.");
			flds = line.split(Pattern.quote("\t"), -1);
		
			int fldOrder = Integer.parseInt(flds[iOrder]);
			//System.out.println(flds[iTable]);
			CommonDataModelTable t = cdm.getTableByName(flds[iTable]);
			//System.out.println(t);
			CommonDataModelField f = new CommonDataModelField(t, flds[iName], 
					flds[iDesc], fldOrder, fldOrder);
			t.addField(f);
		}
		
		
	}
	
}
