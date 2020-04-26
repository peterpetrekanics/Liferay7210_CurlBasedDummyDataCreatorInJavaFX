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
	public Spinner<Integer> siteMemberCount;

	public void siteAdminUserCreator() throws InterruptedException {
		System.out.println("siteAdminUserCreator method starts");
		int companyId;
		companyId = getCompanyId();

		String newAdminUserName = "siteadmin";
		String siteName = "Guest";
		int groupId = getGroupIdForSite(companyId, siteName);
		String siteAdminRoleName = "Site Administrator";
		int siteAdminRoleId = getRoleIdOfSiteRole(companyId, siteAdminRoleName);
		int userCount = siteAdminCount.getValue();
		long currentUserId = 0;

		if(userCount>1) {
		
			for(int i=1; i<userCount+1; i++){
	
				currentUserId = createUser(companyId, newAdminUserName, i, groupId, siteAdminRoleId);
//				Thread.sleep(1000);
				assignSiteRole(currentUserId, groupId, siteAdminRoleId);
			}
		}
	}

	public void siteMemberUserCreator() {
//		System.out.println("siteMemberUserCreator method starts");
//		int companyId;
//		companyId = getCompanyId();
//
//		String newAdminUserName = "sitemember";
//		String siteName = "Guest";
//		int groupId = getGroupIdForSite(companyId, siteName);
//		String siteAdminRoleName = "Site Administrator";
//		int siteAdminRoleId = getRoleIdOfSiteRole(companyId, siteAdminRoleName);
//		int userCount = siteAdminCount.getValue();
//
//		createUser(companyId, newAdminUserName, userCount, groupId, siteAdminRoleId);
	}

	private int getRoleIdOfSiteRole(int inputCompanyId, String siteRoleName) {
		int siteRoleId = 0;
		String roleIdJsonString = "";
		Runtime rt = Runtime.getRuntime();
		StringBuilder output = new StringBuilder();
		try {
			String[] stringPost = { "curl", "http://localhost:8080/api/jsonws/role/get-role",
				"-u", "test@liferay.com:test",
				"-d", "companyId=" + inputCompanyId,
				"-d", "name=" + siteRoleName
				};

			ProcessBuilder ps = new ProcessBuilder(stringPost);
			Process pr = ps.start();

			InputStreamReader isReader = new InputStreamReader(pr.getInputStream());
			BufferedReader reader = new BufferedReader(isReader);
			StringBuffer sb = new StringBuffer();
			String str;
			while ((str = reader.readLine()) != null) {
				sb.append(str);
			}
			roleIdJsonString = sb.toString();

			JSONObject jsonObject = new JSONObject(roleIdJsonString);

			siteRoleId = Integer.parseInt((String) jsonObject.get("roleId"));
		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
		}

		System.out.println("siteAdminRoleId: " + siteRoleId);
		resultWindow.appendText("siteAdminRoleId: " + siteRoleId + "\n");
		return siteRoleId;
	}

	private int getGroupIdForSite(int inputCompanyId, String siteName) {
		int groupId = 0;
		String groupJsonString = "";
		Runtime rt = Runtime.getRuntime();
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

			JSONObject jsonObject = new JSONObject(groupJsonString);

			groupId = Integer.parseInt((String) jsonObject.get("groupId"));
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
		StringBuilder output = new StringBuilder();
		String adminIdString = "";
		try {
			String[] stringPost = { "curl", "http://localhost:8080/api/jsonws/user/get-user-id-by-email-address",
					"-u","test@liferay.com:test",
					"-d", "companyId=" + inputCompanyId,
					"-d","emailAddress=test@liferay.com" };

			ProcessBuilder ps = new ProcessBuilder(stringPost);
			Process pr = ps.start();

			InputStreamReader isReader = new InputStreamReader(pr.getInputStream());
			BufferedReader reader = new BufferedReader(isReader);
			StringBuffer sb = new StringBuffer();
			String str;
			while ((str = reader.readLine()) != null) {
				sb.append(str);
			}
			adminIdString = sb.toString();
		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
		}
		adminUserId = Integer.parseInt(adminIdString.substring(1, adminIdString.length() - 1));
		System.out.println("adminUserId: " + adminUserId);
		resultWindow.appendText("adminUserId: " + adminUserId + "\n");
		return adminUserId;
	}

	private long createUser(int companyId, String newUserName, int userNumber, int groupId, int siteRoleId) {
		String currentUserIdString = "";
		long currentUserId = 0;
		resultWindow.appendText("companyId: " + companyId + "\n");
		resultWindow.appendText("newUserName: " + newUserName + "\n");
		resultWindow.appendText("userCount: " + userNumber + "\n");

		Runtime rt = null;
		rt = Runtime.getRuntime();
		try {
			String[] stringPost = { "curl", "http://localhost:8080/api/jsonws/user/add-user",
				"-u", "test@liferay.com:test",
				"-d", "companyId=" + companyId,
				"-d", "autoPassword=false",
				"-d", "password1=" + newUserName,
				"-d", "password2=" + newUserName,
				"-d", "autoScreenName=true",
				"-d", "screenName=" + newUserName + userNumber,
				"-d", "emailAddress=" + newUserName + userNumber + "@liferay.com",
				"-d", "facebookId=0",
				"-d", "openId=",
				"-d", "locale=",
				"-d", "firstName="+ newUserName + userNumber,
				"-d", "middleName=",
				"-d", "lastName="+ newUserName + userNumber,
				"-d", "prefixId=0",
				"-d", "suffixId=0",
				"-d", "male=true",
				"-d", "birthdayMonth=1",
				"-d", "birthdayDay=1",
				"-d", "birthdayYear=1970",
				"-d", "jobTitle=",
				"-d", "groupIds=[" + groupId + "]",
				"-d", "organizationIds=",
//					"-d", "roleIds=[" + siteAdminRoleId + "]",  // <- this is only applicable for Regular Roles
				"-d", "roleIds=",
				"-d", "userGroupIds=",
				"-d", "sendEmail=false"
			};
	
			ProcessBuilder ps = new ProcessBuilder(stringPost);
			Process pr = ps.start();

			InputStreamReader isReader = new InputStreamReader(pr.getInputStream());
			BufferedReader reader = new BufferedReader(isReader);
			StringBuffer sb = new StringBuffer();
			String str;
			while ((str = reader.readLine()) != null) {
				sb.append(str);
			}
			currentUserIdString = sb.toString();

			JSONObject jsonObject = new JSONObject(currentUserIdString);

			currentUserId = Integer.parseInt((String) jsonObject.get("userId"));

		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
		}
		System.out.println("currentUserId: " + currentUserId);
		resultWindow.appendText("currentUserId: " + currentUserId + "\n");

		resultWindow.appendText("User creation process finished\n");
		return currentUserId;
	}

	private void assignSiteRole(long currentUserId, int groupId, int siteRoleId) {
		Runtime rt = Runtime.getRuntime();
		StringBuilder output = new StringBuilder();
		try {
			String[] stringPost = { "curl", "http://localhost:8080/api/jsonws/usergrouprole/add-user-group-roles",
					"-u","test@liferay.com:test",
					"-d", "userId=" + currentUserId,
					"-d", "groupId=" + groupId,
					"-d", "roleIds=" + siteRoleId
					};

			ProcessBuilder ps = new ProcessBuilder(stringPost);
			Process pr = ps.start();
			resultWindow.appendText("Site role assigned to the user\n");
			
		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
		}
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

		String siteName = "Guest";
		int companyId = getCompanyId();
		int companyUsersCount = 0;
		companyUsersCount = getCompanyUsersCount(companyId);
		System.out.println("companyUsersCount"+companyUsersCount);
		if (companyUsersCount < 2) return;

		// This code part will only run if the portal has more than one user
		int groupId = getGroupIdForSite(companyId, siteName);
		
		long[] nonAdminUserIds = getUserIdsForLiferaydefaultSite(companyId, groupId);
//		nonAdminUserIds
		int i=0;
		for (i = 0; i < nonAdminUserIds.length; i++) { 
			  
            // accessing each element of array 
            long actualNonAdminUserId = nonAdminUserIds[i]; 
//            System.out.print("nonAdminUserIds: "+x + " "); 
            deleteNonAdminUsersForCompany(actualNonAdminUserId);
        } 
	}

	private void deleteNonAdminUsersForCompany(long actualNonAdminUserId) {
		Runtime rt = Runtime.getRuntime();
		StringBuilder output = new StringBuilder();
		try {
			String[] stringPost = { "curl", "http://localhost:8080/api/jsonws/user/delete-user",
					"-u","test@liferay.com:test",
					"-d", "userId=" + actualNonAdminUserId
					};

			ProcessBuilder ps = new ProcessBuilder(stringPost);
			Process pr = ps.start();
			resultWindow.appendText("Non-admin user with the userId: " + actualNonAdminUserId + " deleted\n");
			
		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
		}
	}

	private int getCompanyUsersCount(int companyId) {
		int companyUsersCount = 0;
		Runtime rt = Runtime.getRuntime();
		StringBuilder output = new StringBuilder();
		try {
			String[] stringPost = { "curl", "http://localhost:8080/api/jsonws/user/get-company-users-count",
					"-u","test@liferay.com:test",
					"-d", "companyId=" + companyId
					};

			ProcessBuilder ps = new ProcessBuilder(stringPost);
			Process pr = ps.start();
			pr.waitFor();

			BufferedReader reader2 = new BufferedReader(new InputStreamReader(pr.getInputStream()));

			String line2 = "";
			while ((line2 = reader2.readLine()) != null) {
				output.append(line2);
			}

			String sbToStringOrig = output.toString();
			companyUsersCount = Integer.parseInt((String) sbToStringOrig);
		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
		}
		resultWindow.appendText("companyUsersCount: " + companyUsersCount + "\n");
		return companyUsersCount;
	}



	private long[] getUserIdsForLiferaydefaultSite(int companyId, int groupId) {
		long[] nonAdminUserIds = null;
		String userIdsForLiferaydefaultSiteString = "";
		String nonAdminUserIdsString = "";
		Runtime rt = Runtime.getRuntime();
		StringBuilder output = new StringBuilder();
		try {
			String[] stringPost = {
					"curl", "http://localhost:8080/api/jsonws/user/get-group-user-ids",
					"-u", "test@liferay.com:test",
					"-d", "groupId=" + groupId
					};

			ProcessBuilder ps = new ProcessBuilder(stringPost);
			Process pr = ps.start();

			InputStreamReader isReader = new InputStreamReader(pr.getInputStream());
			BufferedReader reader = new BufferedReader(isReader);
			StringBuffer sb = new StringBuffer();
			String str;
			while ((str = reader.readLine()) != null) {
				sb.append(str);
			}
			userIdsForLiferaydefaultSiteString = sb.toString();
		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
		}
		nonAdminUserIdsString = userIdsForLiferaydefaultSiteString.substring(1, userIdsForLiferaydefaultSiteString.length() - 1);
		
		int adminUserId = getAdminUserId(companyId);
		nonAdminUserIdsString = removeAdminIdfromUserIdString(nonAdminUserIdsString, adminUserId);
		System.out.println("+nonAdminUserIdsString: " + nonAdminUserIdsString);
		String[] nonAdminUserIdsStringArray = nonAdminUserIdsString.split(",");
		nonAdminUserIds = convertStringArrayToLongArray(nonAdminUserIdsStringArray);

		resultWindow.appendText("nonAdminUserIdsString: " + nonAdminUserIdsString + "\n");
		
		return nonAdminUserIds;
	}

	private long[] convertStringArrayToLongArray(String[] numbersString) {
		long[] result = null;
		
		System.out.println("numbersString.length: "+numbersString.length);
		result = new long[numbersString.length];
		for (int i = 0; i < numbersString.length; i++){
			result[i] = Integer.parseInt(numbersString[i]);
		}
		return result;
		}

	private String removeAdminIdfromUserIdString(String nonAdminUserIdsString, int adminUserId) {
		String adminUserIdString = adminUserId + "";
		String new_string = nonAdminUserIdsString.replace(adminUserIdString + ",", "");
//		String new_string_without_commas = new_string.replace(",", "");
		nonAdminUserIdsString = new_string;
		return nonAdminUserIdsString;
	}

	private int getCompanyId() {
		int companyId = 0;
		Runtime rt = Runtime.getRuntime();
		StringBuilder output = new StringBuilder();
		try {
			String[] stringPost = { "curl", "http://localhost:8080/api/jsonws/company/get-companies", "-u",
					"test@liferay.com:test" };

			ProcessBuilder ps = new ProcessBuilder(stringPost);
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

			companyId = Integer.parseInt((String) jsonObject.get("companyId"));
		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
		}
		resultWindow.appendText("companyId: " + companyId + "\n");
		return companyId;
	}
}
