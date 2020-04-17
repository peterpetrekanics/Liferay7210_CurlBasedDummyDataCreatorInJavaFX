package application;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import java.net.Socket;

public class DataCreatorController {
//	int companyId;
	@FXML
	public TextArea resultWindow;
	public Spinner<Integer> siteAdminCount;
//	public Spinner<Double> siteMemberCount;

	public void siteAdminUserCreator() {
		System.out.println("siteAdminUserCreator method starts");
		int companyId;
		companyId = getCompanyId();

//		int adminUserCount = 1;
		String newAdminUserName = "siteadmin";
		int adminUserId = getAdminUserId(companyId);
		String siteName = "Guest";
		int groupId = getGroupIdForSite(companyId, siteName);
		String siteAdminRoleName = "Site Administrator";
		int siteAdminRoleId = getRoleIdOfSiteAdminRole(companyId, siteAdminRoleName);

		//TODO:
		// assign id numbers to the spinners and username fields, so we can reference them here
		// retrieve the spinner and username values
		// create the createUser method below

		String newUserName = "";
//		userCount = Double.parseDouble(siteAdminCount.getValue());
		int userCount = siteAdminCount.getValue();

		createUser(companyId, adminUserId, newUserName, userCount, siteAdminRoleId);
//		System.out.println(userCount);
	}

	

	private int getRoleIdOfSiteAdminRole(int inputCompanyId, String siteAdminRoleName) {
		int siteAdminRoleId = 0;
		String roleIdJsonString = "";
		Runtime rt = Runtime.getRuntime();
		Process p1;
		Process p2;
		StringBuilder output = new StringBuilder();
		try {
			String[] stringPost = { "curl", "http://localhost:8080/api/jsonws/role/get-role",
				"-u", "test@liferay.com:test",
				"-d", "companyId=" + inputCompanyId,
				"-d", "name=" + siteAdminRoleName
				};

			ProcessBuilder ps = new ProcessBuilder(stringPost);
			// ps.redirectErrorStream(true);
			Process pr = ps.start();
			pr.waitFor();

			InputStreamReader isReader = new InputStreamReader(pr.getInputStream());
			BufferedReader reader = new BufferedReader(isReader);
			StringBuffer sb = new StringBuffer();
			String str;
			while ((str = reader.readLine()) != null) {
				sb.append(str);
			}
			roleIdJsonString = sb.toString();

			JSONObject jsonObject = new JSONObject(roleIdJsonString);

			// System.out.println(jsonObject.toString());

			p1 = Runtime.getRuntime().exec("pwd");
			p1.waitFor();
			siteAdminRoleId = Integer.parseInt((String) jsonObject.get("roleId"));
			// BufferedReader reader1a
			// = new BufferedReader(new InputStreamReader(p1.getInputStream()));
			//
			// String line1a = "";
			// while ((line1a = reader1a.readLine()) != null) {
			// output.append(line1a + "\n");
			// System.out.println("output====" + output + "===");
			// }
		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
		}

		System.out.println("siteAdminRoleId: " + siteAdminRoleId);
		resultWindow.appendText("siteAdminRoleId: " + siteAdminRoleId + "\n");
		return siteAdminRoleId;
	}

	private int getGroupIdForSite(int inputCompanyId, String siteName) {
		int groupId = 0;
		String groupJsonString = "";
		Runtime rt = Runtime.getRuntime();
		Process p1;
		Process p2;
		StringBuilder output = new StringBuilder();
		try {
			String[] stringPost = { "curl", "http://localhost:8080/api/jsonws/group/get-group", "-u",
					"test@liferay.com:test", "-d", "companyId=" + inputCompanyId, "-d", "groupKey=" + siteName };

			ProcessBuilder ps = new ProcessBuilder(stringPost);
			// ps.redirectErrorStream(true);
			Process pr = ps.start();
			pr.waitFor();

			InputStreamReader isReader = new InputStreamReader(pr.getInputStream());
			BufferedReader reader = new BufferedReader(isReader);
			StringBuffer sb = new StringBuffer();
			String str;
			while ((str = reader.readLine()) != null) {
				sb.append(str);
			}
			groupJsonString = sb.toString();

			// curl
			// http://localhost:8080/api/jsonws/user/get-user-id-by-email-address
			// \
			// -u test@liferay.com:test \
			// -d companyId=20101 \
			// -d emailAddress=test@liferay.com

			JSONObject jsonObject = new JSONObject(groupJsonString);

			// System.out.println(jsonObject.toString());

			p1 = Runtime.getRuntime().exec("pwd");
			p1.waitFor();
			groupId = Integer.parseInt((String) jsonObject.get("groupId"));
			// BufferedReader reader1a
			// = new BufferedReader(new InputStreamReader(p1.getInputStream()));
			//
			// String line1a = "";
			// while ((line1a = reader1a.readLine()) != null) {
			// output.append(line1a + "\n");
			// System.out.println("output====" + output + "===");
			// }
		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
		}

		System.out.println("groupId: " + groupId);
		resultWindow.appendText("groupId: " + groupId + "\n");
		return groupId;

	}

	private int getAdminUserId(int inputCompanyId) {
		int adminUserId = 0;
		Runtime rt = Runtime.getRuntime();
		Process p1;
		Process p2;
		StringBuilder output = new StringBuilder();
		String adminIdString = "";
		try {
			String[] stringPost = { "curl", "http://localhost:8080/api/jsonws/user/get-user-id-by-email-address", "-u",
					"test@liferay.com:test", "-d", "companyId=" + inputCompanyId, "-d",
					"emailAddress=test@liferay.com" };

			ProcessBuilder ps = new ProcessBuilder(stringPost);
			// ps.redirectErrorStream(true);
			Process pr = ps.start();
			pr.waitFor();

			InputStreamReader isReader = new InputStreamReader(pr.getInputStream());
			BufferedReader reader = new BufferedReader(isReader);
			StringBuffer sb = new StringBuffer();
			String str;
			while ((str = reader.readLine()) != null) {
				sb.append(str);
			}
			adminIdString = sb.toString();

			// curl
			// http://localhost:8080/api/jsonws/user/get-user-id-by-email-address
			// \
			// -u test@liferay.com:test \
			// -d companyId=20101 \
			// -d emailAddress=test@liferay.com
		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
		}
		adminUserId = Integer.parseInt(adminIdString.substring(1, adminIdString.length() - 1));
		System.out.println("adminUserId: " + adminUserId);
		resultWindow.appendText("adminUserId: " + adminUserId + "\n");
		return adminUserId;
	}

	private void createUser(int companyId, int adminUserId, String newUserName, int userCount, int siteAdminRoleId) {
		resultWindow.appendText("companyId: " + companyId + "\n");
		resultWindow.appendText("adminUserId: " + adminUserId + "\n");
		resultWindow.appendText("newUserName: " + newUserName + "\n");
		resultWindow.appendText("userCount: " + userCount + "\n");
		
		
		Runtime rt = Runtime.getRuntime();
		Process p1;
		Process p2;
		StringBuilder output = new StringBuilder();
		try {
			String[] stringPost = { "curl", "http://localhost:8080/api/jsonws/role/get-role",
				"-u", "test@liferay.com:test",
				"-d", "companyId=" + companyId,
				"-d", "autoPassword=true",
				"-d", "password1=''",
				"-d", "password2=''",
				"-d", "autoScreenName=true",
				"-d", "screenName=''",
				"-d", "emailAddress=''",
				"-d", "facebookId=",
				"-d", "openId=''",
				"-d", "locale=",
				"-d", "firstName=''",
				"-d", "middleName=''",
				"-d", "lastName=''",
				"-d", "prefixId=",
				"-d", "suffixId=",
				"-d", "male=true",
				"-d", "birthdayMonth=",
				"-d", "birthdayDay=",
				"-d", "birthdayYear=",
				"-d", "jobTitle=''",
				"-d", "groupIds=",
				"-d", "organizationIds=",
				"-d", "roleIds=",
				"-d", "userGroupIds=",
				"-d", "sendEmail=true"

			};

			ProcessBuilder ps = new ProcessBuilder(stringPost);
			// ps.redirectErrorStream(true);
			Process pr = ps.start();
			pr.waitFor();


			p1 = Runtime.getRuntime().exec("pwd");
			p1.waitFor();
		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
		}

		resultWindow.appendText("User creation process finished\n");
		
	}

	public void siteMemberUserCreator() {
		System.out.println("siteMemberUserCreator method starts");
	}

	public void basicWebContentCreator() {
		System.out.println("basicWebContentCreator method starts");
	}

	public void plainStructureCreator() {
		System.out.println("plainStructureCreator method starts");
	}

	public void fileCreator() {
		System.out.println("fileCreator method starts");
	}

	public void categoryCreator() {
		System.out.println("categoryCreator method starts");
	}

	public void formCreator() {
		System.out.println("formCreator method starts");
	}

	public void deleteNonAdminUsers() {
		System.out.println("deleteNonAdminUsers method starts");

		System.out.println(getCompanyId());
		// deleteNonAdminUsersForCompany(companyId);
	}

	private int getCompanyId() {
		int companyId = 0;
		Runtime rt = Runtime.getRuntime();
		Process p1;
		Process p2;
		StringBuilder output = new StringBuilder();
		try {
			String[] stringPost = { "curl", "http://localhost:8080/api/jsonws/company/get-companies", "-u",
					"test@liferay.com:test" };

			ProcessBuilder ps = new ProcessBuilder(stringPost);
			// ps.redirectErrorStream(true);
			Process pr = ps.start();
			pr.waitFor();

			BufferedReader reader2 = new BufferedReader(new InputStreamReader(pr.getInputStream()));

			String line2 = "";
			while ((line2 = reader2.readLine()) != null) {
				output.append(line2 + "\n");
			}

			// System.out.println("\n\n\noutput1====" + output + "===\n\n\n");

			String sbToStringOrig = output.toString();
			String sbToString = sbToStringOrig.substring(1, sbToStringOrig.length() - 1);

			JSONObject jsonObject = new JSONObject(sbToString);

			// System.out.println(jsonObject.toString());

			p1 = Runtime.getRuntime().exec("pwd");
			p1.waitFor();
			companyId = Integer.parseInt((String) jsonObject.get("companyId"));
			// BufferedReader reader1a
			// = new BufferedReader(new InputStreamReader(p1.getInputStream()));
			//
			// String line1a = "";
			// while ((line1a = reader1a.readLine()) != null) {
			// output.append(line1a + "\n");
			// System.out.println("output====" + output + "===");
			// }
		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
		}
		resultWindow.appendText("companyId: " + companyId + "\n");
		return companyId;

	}

}
