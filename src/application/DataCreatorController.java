package application;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.JSONObject;

public class DataCreatorController {
	int companyId;
	
	public void siteAdminUserCreator() {
		System.out.println("siteAdminUserCreator");
//		test();
		
	}

	public void siteMemberUserCreator() {
		System.out.println("siteMemberUserCreator");
	}
	
	public void basicWebContentCreator() {
		System.out.println("basicWebContentCreator");
	}
	
	public void plainStructureCreator() {
		System.out.println("plainStructureCreator");
	}
	
	public void fileCreator() {
		System.out.println("fileCreator");
	}
	
	public void categoryCreator() {
		System.out.println("categoryCreator");
	}
	
	public void formCreator() {
		System.out.println("formCreator");
	}
	
	public void deleteNonAdminUsers() {
		System.out.println("deleteNonAdminUsers");
		
		System.out.println(getCompanyId());
//		deleteNonAdminUsersForCompany(companyId);
	}

	private int getCompanyId() {
        Runtime rt = Runtime.getRuntime();
        Process p1;
        Process p2;
		StringBuilder output = new StringBuilder();
        try {
            String[] stringPost = {"curl", "http://localhost:8080/api/jsonws/company/get-companies",
                "-u", "test@liferay.com:test"
            };

            ProcessBuilder ps = new ProcessBuilder(stringPost);
            //ps.redirectErrorStream(true);
            Process pr = ps.start();
            pr.waitFor();

            BufferedReader reader2 = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            String line2 = "";
            while ((line2 = reader2.readLine()) != null) {
                output.append(line2 + "\n");
            }

            System.out.println("\n\n\noutput1====" + output + "===\n\n\n");

            String sbToStringOrig = output.toString();
            String sbToString = sbToStringOrig.substring(1, sbToStringOrig.length()-1);

            JSONObject jsonObject = new JSONObject(sbToString);

            System.out.println(jsonObject.toString());

            p1 = Runtime.getRuntime().exec("pwd");
            p1.waitFor();
            companyId = Integer.parseInt((String) jsonObject.get("companyId"));
//            BufferedReader reader1a
//                    = new BufferedReader(new InputStreamReader(p1.getInputStream()));
//
//            String line1a = "";
//            while ((line1a = reader1a.readLine()) != null) {
//                output.append(line1a + "\n");
//                System.out.println("output====" + output + "===");
//            }
        } catch (Exception e) {
            System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
        }
		return companyId;

	}
	
}
