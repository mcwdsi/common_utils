package edu.ufl.bmi.util.cdm;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;

public class CdmReaderTest {

	public static void main(String[] args) {
		
		try {
			FileReader fr = new FileReader("./src/test/resources/pcornet_cdm_50_parseable_tab_delimited_text.txt");
			CommonDataModelReader cr = new CommonDataModelReader(fr);
			CommonDataModel cdm = cr.read();
			System.out.println("Name of CDM: " + cdm.getCdmName());
			System.out.println("Version of CDM: " + cdm.getCdmVersion());
			System.out.println("Release date of CDM: " + cdm.getVersionReleaseDate().toString());
			System.out.println("Creator of CDM: " + cdm.getCreator());
			System.out.println("Description: " + cdm.getCdmDescription());

			Iterator<CommonDataModelTable> i = cdm.getAllTablesInOrder();
			while (i.hasNext()) {
				CommonDataModelTable t = i.next();
				System.out.println(t.getName() + "\t" + t.getTableOrderInCdm());
				Iterator<CommonDataModelField> j = t.getAllFieldsInOrder();
				while (j.hasNext()) {
					CommonDataModelField f = j.next();
					System.out.println("\t" + f.getFieldName() + "\t" + f.getFieldOrderInTable() + "\t" +
											f.getFieldOrderInCdm());
				}
			}
		} catch (IOException ioe) {
			System.err.println("IO exception!");
			ioe.printStackTrace();
		} catch (ParseException pe) {
			System.err.println("Parse exception!");
			pe.printStackTrace();
		}

	}

}
