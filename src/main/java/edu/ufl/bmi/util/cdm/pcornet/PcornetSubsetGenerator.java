package edu.ufl.bmi.util.cdm.pcornet;

import java.io.*;
import java.util.HashSet;

public class PcornetSubsetGenerator {
	
	static final double DEFAULT_PROB = 0.01D; 

	static File[] files;
	static HashSet<String> patids;
	static HashSet<String> providerids;
	static File outDir;
	static double prob;

	public static void main(String[] args) {
		patids = new HashSet<String>();
		providerids = new HashSet<String>();
		File f = new File(args[0]);
		outDir = new File(args[1]);
		if (args.length < 3) prob = DEFAULT_PROB;
		if (f.isDirectory() && outDir.isDirectory()) {
			files = f.listFiles();
			/*
			 * Select random rows from demographic table, saving off the patids 
			 *   from each row selected for use in subsequent processing
			 */ 
			subsetDemographic();

			/*
			 *  Specialized handling of encounter table, so save off facility
			 *    information and generate a separate facility table, which 
			 *    will initially be non-unique.  We'll just use shell scripts
			 *    to create the unique facility information.
			 */
			subsetEncounter();

			/* Generic handling by table name, patid field index, and provider id
				field index.  Bascially, for each row, if the patid is one we
				saved during demographic file handling, then write that row,
				else, skip it.  In the meantime, save all provider ids used
				in rows written to file. */
			subsetDiagnosis();
			subsetProcedures();
			subsetPrescribing();
			subsetDeath();
			subsetCondition();
			subsetDispensing();
			subsetAddressHistory();
			subsetMedAdmin();
			subsetLab();
			subsetPro();
			subsetVital();
			subsetObsClin();
			subsetObsGen();
			subsetEnrollment();
			subsetImmunization();

			/*
			 * Lastly, handle the provider file, writing out only the rows
			 *  with a providerid that we saved during processing of all 
			 *  the other tables.
			 */
			subsetProvider();

		} else {
			System.err.println("First argument must be a directory.");
		}
	}

	public static void subsetDemographic() {
		/*
		 *  First time through, just demographic file, not our own UF specific
		 *   demographic_history or demographic_lookup.
		 */
		for (File f : files) {
			String fNameCompare = f.toString().toLowerCase();
			if (fNameCompare.contains("demographic") && !fNameCompare.contains("lookup")
				  && !fNameCompare.contains("history")) {
				try {
					FileReader fr = new FileReader(f);
					LineNumberReader lnr = new LineNumberReader(fr);
					File fOut = new File(outDir, "demographic.tsv");
					FileWriter fw = new FileWriter(fOut);
					String line;
					int cLine = 0;
					while ((line=lnr.readLine())!=null) {
						if (Math.random() < prob) {
							cLine++;
							fw.write(line);
							fw.write("\n");
							String[] flds = line.split("\t");
							patids.add(flds[0]);
						}
					}
					System.out.println("Selected " + cLine + " patients at random.");
					lnr.close();
					fr.close();
					fw.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}

		/*
		 *  Scond time through, now we can process our own UF specific
		 *   demographic_history and demographic_lookup.
		 */
		for (File f : files) {
			String fNameCompare = f.toString().toLowerCase();
			if (fNameCompare.contains("demographic") && (fNameCompare.contains("lookup")
				  || fNameCompare.contains("history"))) {

				String outFname = (fNameCompare.contains("history")) ? 
										"demographic_history.tsv" : "demographic_lookup.tsv";

				try {
					FileReader fr = new FileReader(f);
					LineNumberReader lnr = new LineNumberReader(fr);
					File fOut = new File(outDir, outFname);
					FileWriter fw = new FileWriter(fOut);
					String line;
					int cLine = 0;
					while ((line=lnr.readLine())!=null) {
						String[] flds = line.split("\t");
						if (patids.contains("flds[0]")) {
							cLine++;
							fw.write(line);
							fw.write("\n");
						}
					}
					System.out.println("\t" + cLine + " records into " + outFname);
					lnr.close();
					fr.close();
					fw.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}			
		}
	}

	public static void subsetEncounter() {
		for (File f : files) {
			if (f.toString().toLowerCase().contains("encounter")) {
				try {
					FileReader fr = new FileReader(f);
					LineNumberReader lnr = new LineNumberReader(fr);
					File fOut = new File(outDir, "encounter.tsv");
					File fFac = new File(outDir, "facility-non-unique.tsv");
					File fOutNormFac = new File(outDir, "encounter-facility-normalized.tsv");
					File fFacNorm = new File(outDir, "facility-normalized-non-unique.tsv");
					FileWriter fw = new FileWriter(fOut);
					FileWriter fwFac = new FileWriter(fFac);
					FileWriter fwNorm = new FileWriter(fOutNormFac);
					FileWriter fwFacNorm = new FileWriter(fFacNorm);
					String line;
					while ((line=lnr.readLine())!=null) {
						String[] flds = line.split("\t");
						if (patids.contains(flds[1])) {
							String normFacId = (flds[9].equals("NULL")) ? "NULL" : ( (flds[17].equals("NULL")) ? flds[9] : flds[9] + "_" + flds[17]);
							fw.write(line);
							fw.write("\n");
							/* Write encounter row with normalized facility ID to 
							 *  separate file. 
							 */
							for (int i=0; i<9; i++) {
								fwNorm.write(flds[i]);
								fwNorm.write("\t");
							}
							fwNorm.write(normFacId);
							for (int i=10; i<34; i++) {
								if (i<flds.length) {
									fwNorm.write("\t");
									fwNorm.write(flds[i]);										
								}
							}
							fwNorm.write("\n");
							/* 
							 * Write facility info, both usual and normalized by facility type.
							 *  Both files contain non-unique information, however, the normalized
							 *   version will have one unique facility id per row once sorted unique,
							 *   whereas that often will not be the case for the non-normalized file.
							 */
							if (!flds[9].equals("NULL")) {
								String facInfo = flds[7] + "\t" + flds[9] + "\t" + flds[17] + "\n";
								fwFac.write(facInfo);
								fwFacNorm.write(normFacId);
								fwFacNorm.write("\t");
								fwFacNorm.write(facInfo);
							}
							providerids.add(flds[6]);
						}
					}
					lnr.close();
					fr.close();
					fw.close();
					fwFac.close();
					fwNorm.close();
					fwFacNorm.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}	
	}

	public static void subsetDiagnosis() {
		subsetFile("diagnosis", 1, 5);
	}
	public static void subsetProcedures() {
		subsetFile("procedures", 1, 5);
	}
	public static void subsetPrescribing() {
		subsetFile("procedures", 1, 3);
	}
	// -1 means that there is no providerid field.
	public static void subsetDeath() {
		subsetFile("death", 0, -1);
	}
	public static void subsetCondition() {
		subsetFile("condition", 1, -1);
	}
	public static void subsetDispensing() {
		subsetFile("dispensing", 1, -1);
	}
	public static void subsetAddressHistory() {
		subsetFile("lds_address_history", 1, -1);
	}
	public static void subsetMedAdmin() {
		subsetFile("med_admin", 1, 4);
	}
	public static void subsetLab() {
		subsetFile("lab_result_cm", 1, -1);
	}
	public static void subsetPro() {
		subsetFile("pro_cm", 1, -1);
	}
	public static void subsetVital() {
		subsetFile("vital", 1, -1);
	}
	public static void subsetObsClin() {
		subsetFile("obs_clin", 1, 3);
	}
	public static void subsetObsGen() {
		subsetFile("obs_gen", 1, 3);
	}
	public static void subsetEnrollment() {
		subsetFile("enrollment", 0, -1);
	}
	public static void subsetImmunization() {
		subsetFile("immunization", 1, 4);
	}

	public static void subsetFile(String tableNameLower, int patIdIndex, int providerIdIndex) {
		for (File f : files) {
			if (f.toString().toLowerCase().contains(tableNameLower)) {
				try {
					FileReader fr = new FileReader(f);
					LineNumberReader lnr = new LineNumberReader(fr);
					File fOut = new File(outDir, tableNameLower + ".tsv");
					FileWriter fw = new FileWriter(fOut);
					String line;
					while ((line=lnr.readLine())!=null) {
						String[] flds = line.split("\t");
						if (patids.contains(flds[patIdIndex])) {
							fw.write(line);
							fw.write("\n");
						}
						if (providerIdIndex > 0) {
							providerids.add(flds[providerIdIndex]);
						}
					}
					lnr.close();
					fr.close();
					fw.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}	
	}

	public static void subsetProvider() {
		System.out.println("Filtering provider for " + providerids.size() + " PROVIDERID(s).");
		for (File f : files) {
			if (f.toString().toLowerCase().contains("provider")) {
				try {
					FileReader fr = new FileReader(f);
					LineNumberReader lnr = new LineNumberReader(fr);
					File fOut = new File(outDir, "provider.tsv");
					FileWriter fw = new FileWriter(fOut);
					String line;
					while ((line=lnr.readLine())!=null) {
						String[] flds = line.split("\t");
						if (providerids.contains(flds[0])) {
							fw.write(line);
							fw.write("\n");
						}
					}
					lnr.close();
					fr.close();
					fw.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}
}