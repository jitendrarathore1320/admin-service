package com.advantal.adminRoleModuleService.utils;

public class Constant {

//	 -------------------------- EMAIL AND PASSWORD -------------------------------
	public static final String EMAIL_ADDRESS = "navneet.patidar@advantal.net";
	//public static final String EMAIL_ADDRESS = "smscindia@outlook.com";
	public static final String PASSWORD = "Advantal@22";// "Smsc@123";

//	 ---------------------------- Admin Panel Link ---------------------------------
//	public static final String FORGET_PASSWORD_PAGE_LINK = "http://3.6.39.13:8080/AMWAL/#/auth/reset?";
//	public static final String CHANGE_PASSWORD_PAGE_LINK = "http://3.6.39.13:8080/AMWAL/#/auth/changePassword?";
	
	public static final String FORGET_PASSWORD_PAGE_LINK = "http://151.106.39.5:8080/AMWAL/#/auth/reset?";
	public static final String CHANGE_PASSWORD_PAGE_LINK = "http://151.106.39.5:8080/AMWAL/#/auth/changePassword?";
	
	

// -------------------------------------- TOKEN VARIABLE ---------------------------------------
	public static String HEADER_STRING = "Authorization";
	public static String TOKEN_PREFIX = "Bearer ";
	public static String AUTHORITIES_KEY = "roles";
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	public static String SECRETKEY = "javainuse";
// ------------------------------------ END TOKEN VARIABLE ------------------------------------

//	-------------------------------------- OTHER CONSTATNT -----------------------------------

	/* 1=active/unblocked, 0=deactive/delete, 2=blocked */
	public static final Short ZERO = 0;
	public static final Short ONE = 1;
	public static final Short TWO = 2;
	public static final Boolean FALSE = false;
	public static final Boolean TRUE = true;
	public static final String ACTIVATE = "activate";
	public static final String TYPE_ACTIVATE = "kHJIqVoRke6QPOT/nSXKrw==";
	public static final String SEND_MAIL_TIME = "2023-03-06 22:21:25";

//	------------------------------------------ End OTHER CONSTANT -------------------------------------------

//------------------------------------------ STATUS CODE -------------------------------------
	public static final String CREATE = "201";
	public static final String OK = "200";
	public static final String BAD_REQUEST = "400";
	public static final String NOT_AUTHORIZED = "401";
	public static final String FORBIDDEN = "403";
	public static final String WRONGEMAILPASSWORD = "402";
	public static final String NOT_FOUND = "404";
	public static final String SERVER_ERROR = "500";
	public static final String DB_CONNECTION_ERROR = "502";
	public static final String ENCRYPTION_DECRYPTION_ERROR = "503";
	public static final String NOT_EXIST = "405";
	public static final String CONFLICT = "409";

//--------------------------------------END STATUS CODE---------------------------------------

//--------------------------------------- RESPONSE KEY ---------------------------------------

	public static final String INVALID_REQUEST = "Invalid request!";//
	public static final String UNAUTHRISED = "UNAUTHRISED";//
	public static final String WRONG_INPUT_DATA = "Wrong input data!";//
	public static final String RESPONSE_CODE = "responseCode";
	public static final String OBJECT = "object";
	public static final String SUCCESS = "SUCCESS";
	public static final String ERROR = "ERROR";
	public static final String BLOCKED = "Blocked";
	public static final String NOT_VERIFIED = "Not verified";
	public static final String VERIFY_ACCOUNT = "Verify Account";
	public static final String RESET_PASSWORD = "Reset Password";
	public static final String AUTH_KEY = "authKey";
	public static final String MESSAGE = "message";
	public static final String DATA = "data";
	public static final String TOKEN = "token";
	public static final String AUTH_ID = "authId";
	public static final String TOTAL_USER = "totalUser";
	public static final String ISVALID = "isValid";

//-------------------------------------- END RESPONSE KEY ---------------------------------------	

//--------------------------------------- RESPONSE MESSAGES ----------------------------------------

	// ======================= Common message ==========================
	public static final String BAD_REQUEST_MESSAGE = "Bad request!!";
	public static final String ERROR_MESSAGE = "Please try again!!";
	public static final String RECORD_NOT_FOUND_MESSAGE = "Record not found!!";
	public static final String RECORD_FOUND_MESSAGE = "Record found!!";
	public static final String SERVER_MESSAGE = "Technical issue";
	public static final String NO_SERVER_CONNECTION = "The server was found but the connection to its local database was not possible.";
	public static final String PLEASE_LOGIN_FIRST_MESSAGE = "Please login again!!";
	public static final String PAGE_SIZE_MESSAGE = "Page size can't be zero, it should be more then zero!!";
	public static final String NOT_DOWNLOAD_FILE_MESSAGE = "Not able to download the file, because no record found on the database!!";
	public static final String ALREADY_DELETED_MESSAGE = "Already deleted!!";
	public static final String ALREADY_FIND_ADMIN = "Already find!!";
	public static final String DELETED_MESSAGE = "Deleted successfully!!";
	public static final String FIND_MESSAGE = "Find successfully!!";
	public static final String ID_NOT_FOUND_MESSAGE = "Given id not found into the database!!";
	public static final String GIVEN_ID_NOT_FOUND_MESSAGE = "This given Id is block or deleted in the database or not active";
	public static final String RECORD_BLOCKED_OR_DELETED_MESSAGE = "Record not found, because it may be blocked or deleted!!";
	public static final String PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE = "Page size and Page index can't be null!!";
	public static final String DATA_FOUND = "Data found";
	public static final String RECORD_NOT_UPDATED_MESSAGE = "Record not updated because, given id not found into the database!!";
	public static final String ID_CAN_NOT_NULL_MESSAGE = "Id can not null, it should be valid!!";
	public static final String INTERNAL_SERVER_ERROR_MESSAGE = "There is an error on the server-side. Try again later";
	public static final String DATA_NOT_FOUND = "Data not found !!";
//	public static final String FIREBASE_KEY = "AAAAm0FfB0c:APA91bF4aoisgK235fegLgnziDlGQC2fqu_f5ekmXjxQxYaoipX43RgFu5Qztm9vS04lVCdU0rmQzLOevY60zSE0nxn0BHSsVvJaXLMxxZDkgBboveS3hn3VhhrqfrLRxv9uLuJnWiYQ";

	// =============================== Module =============================
	public static final String MODULE_ADDED_SUCCESS_MESSAGE = "Module registered successfully!!";
	public static final String MODULE_NAME_CAN_NOT_EMPTY_MESSAGE = "Module name can't be null or blank!!";
	public static final String MODULE_LIST_FOUND_MESSAGE = "Modules list found!!";
	public static final String MODULE_LIST_EMPTY_MESSAGE = "Module list empty!!";
	public static final String MODULE_ID_NOT_FOUND_MESSAGE = "Module_id can not blank or null, it should be valid!!";
	public static final String MODULE_NOT_FOUND_MESSAGE = "Given module_id not found into the database!!";
	public static final String MODULE_NAME_AND_ID_NOT_FOUND_MESSAGE = "Given moduleId or moduleName not found into the database!!";
	public static final String MODULE_ID_CAN_NOT_BLANK_MESSAGE = "Module_id can not blank or null, it should be valid!!";

	// ========================== Role message ============================
	public static final String ROLE_ID_NOT_FOUND_MESSAGE = "Given role_id not found into the database!!";
	public static final String ROLENAME_EXISTS_MESSAGE = "Please provide another roleName, as this roleName already exist!!";
	public static final String ROLE_ADDED_SUCCESS_MESSAGE = "Role registration successfully!!";
	public static final String ROLE_ADDED_FAILED_MESSAGE = "Role registration failed, because no module selected!!";
	public static final String ROLE_UPDATED_SUCCESS_MESSAGE = "Role updated successfully!!";
	public static final String ROLE_CAN_NOT_DELETE_MESSAGE = "This role assigned someone, you can't delete!!";
	public static final String ROLE_FOUND_MESSAGE = "Role list found!!";
	public static final String ROLE_LIST_EMPTY_MESSAGE = "Role list empty!!";
	public static final String ROLE_NOT_ACTIVE_MESSAGE = "Role not active!!";
	public static final String MODULE_NOT_ACTIVE_MESSAGE = "Module not active!!";
	public static final String ROLE_NOT_ASSIGNED_MESSAGE = "You have not assigned role!!";
	public static final String NOT_ASSIGNED_ROLE_OR_MODULE_MESSAGE = "You have not assigned role and modules!!";
	public static final String ALREADY_BLOCKED_MESSAGE = "Already blocked!!";
	public static final String BLOCKED_SUCCESS_MESSAGE = "Blocked successfully!!";
	public static final String STATUS_INVALID_MESSAGE = "Status value invalid!!";
	public static final String UNBLOCKED_SUCCESS_MESSAGE = "Unblocked successfully!!";
	public static final String ALREADY_UNBLOCKED_MESSAGE = "Already unblocked!!";
	public static final String ROLE_CAN_NOT_BLOCK_MESSAGE = "This role assigned someone, you can't block!!";

	// ========================== Admin message ============================
	public static final String ADMIN_LOGIN_SUCCESSFULLY = "Admin login successfully!!";
	public static final String INVALID_CREDENTIAL = "Invalid credential!!";
	public static final String ROLE_NOT_FOUND_MESSAGE = "Given role not found into the database!!";
	public static final String EMAIL_EXISTS_MESSAGE = "Please provide another Email, as this Email already exist!!";
	public static final String MOBILE_EXISTS_MESSAGE = "Please provide another Mobile, as this Mobile already exist!!";
	public static final String ADMIN_ADDED_SUCCESS_MESSAGE = "Admin registered successfully!!";
	public static final String INVALID_EMAIL_AND_PASSWORD = "Invalid email and password!!";
	public static final String ADMIN_LOGIN_EMAIL = "Email not valid!!";
	public static final String ADMIN_LOGIN_PASSWORD = "password not valid!!";
	public static final String EMAIL_AND_PASSWORD_NOT_FOUND = "Email and password not found!!";
	public static final String ADMIN_UPDATED_SUCCESS_MESSAGE = "Admin updated successfully!!";
//	public static final String ID_CAN_NOT_NULL_MESSAGE = "Id can not null, it shoulsd be valid!!";
	public static final String PASSWORD_UPDATE = "Password changed successfully!!";
	public static final String PASSWORD_NOT_MATCH = "Password not matched";
	public static final String FIELD_NOT_EMPTY = "Email not valid";
	public static final int FORGET_PASSWORD_CUSTOMER_VERIFICATION_LINK_API = 0;
	public static final String EMAIL_SEND_SUCCESSFULLY = "Email send sucessfully";
	public static final String EMAIL_NOT_FOUND_MESSAGE = "Email not found";
	public static final String CONFIRM_PASSWORD_NOT_MATCH = " Confirm password not match";
	public static final String PASSWORD_FIELD_NOT_EMPTY = "Password field is not empty";
	public static final String PASSWORD_RESET = "Password reset sucessfully";
	public static final String USER_ID_PASSWORD_NOT_EMPTY_MESSAGE = "User id and password field should not empty!!";
	public static final String ACCOUNT_DELETED_MESSAGE = "Your account have deleted!!";
	public static final String ACCOUNT_BLOCKED_MESSAGE = "Your account have blocked!!";
	public static final String ADMIN_NOT_FOUND = "Admin not found !!";
	public static final String ADMIN_COUNT = "Admin found successfully !!";
	public static final Object RECORD_FOUND = "Data found !!";
	public static final Object ADMIN_HISTORY_FOUND_SUCCESSFULY = "Admin history found successfully !!";
	public static final Object TITLE_DESC_EXPIRATION_DAYS_CANT_BE_NULL = "title & description and expiration days can't be null.";
	public static final Object PAYLOAD_CANNOT_BE_NULL = "payload can't be null.";
	public static final Object PASSWORD_POLICY_CREATED = "Password Policy added successfully";
	public static final String FIREBASE_KEY = "AAAACyT_qHo:APA91bHTS6_Ellb35oRuoUNtOKOoSVTArX4PZjm1qEdGckcg3Z2ZPYHEFHJfKAn4PtF0hZhbhSShdhZQIau1UBo0aRnXCsujPtw7Xa64P61l1utKmVpBNyxmpUQSK5au5tsuOkh-olzg";

	// ========================== File message ============================
	public static final String FILE_UPLOAD_SUCCESS_MESSAGE = "New file uploaded sucessfully!!";
	public static final String UPDATED_FILE_UPLOAD_SUCCESS_MESSAGE = "Updated file uploaded sucessfully!!";
	public static final String FILE_UPLOAD_FAILED_MESSAGE = "File uploading failed! please try again!!";
	public static final String NO_CHANGES_FOUND_IN_FILE_MESSAGE = "No Changes found in local file! so you can't upload the file!!";
	public static final String NOT_A_EXCEL_FILE_MESSAGE = "This file is not an excel file!!";

	// ==========================News message ============================
	public static final String NEWS_NOT_UPDATED_MESSAGE = "News not updated because, given id not found into the database!!";
	public static final String NEWS_UPDATED_MESSAGE = "News updated successfully!!";
	public static final String NEWS_LIST_FOUND_MESSAGE = "News list found!!";
	public static final String NEWS_LIST_EMPTY_MESSAGE = "News list empty!!";
	public static final String NEWS_CREATED_MESSAGE = "News created successfully!!";

	// ==========================Otp message ============================
	public static final String OTP_SENT_MESSAGE = "OTP sent successfully to your email, it will be expired soon!!";
	public static final String OTP_ALREADY_SENT_MESSAGE = "OTP already sent to your email, it will be expired soon!!";

	// ==========================Support message ============================
	public static final String TICKET_NOT_GENERATED_MESSAGE = "You can not raise ticket because, given userId not found into the database!!";
	public static final String FAILED_TO_PERFORM_ACTION_MESSAGE = "Failed to perform an action because, given userId not found into the database!!";
	public static final String TICKET_GENERATED_SUCCESS_MESSAGE = "Ticket generated successfully!!";
	public static final String FAILED_TO_CLOSE_TICKET_MESSAGE = "Failed to close the ticket because, given id not found into the database!!";
	public static final String TICKET_CLOSED_SUCCESS_MESSAGE = "Ticket closed successfully!!";
	public static final String REPLIED_SUCCESS_MESSAGE = "Replied to the user successfully!!";
	public static final String FAILED_TO_REPLAY_MESSAGE = "Failed to replay to the user because, given id not found into the database!!";
	public static final String TICKET_ALREADY_CLOSED_MESSAGE = "This ticket already closed!!";
	public static final String TICKET_ALREADY_DELETED_MESSAGE = "This ticket already deleted!!";
	public static final String EMAIL_NOT_REGISTERED_MESSAGE = "You can not raise ticket because, your email not found into the database!!";
	public static final String FAILED_TO_PERFORM_ACTION_ID_STATUS_MESSAGE = "Failed to perform an action because, given id can't be zero or null and status should be 1 or 2 !!";
	public static final String ADMIN_ID_NOT_EMPTY = " Admin id can't be null !!";
	public static final String ROLE_ID_NOT_EMPTY = " Role id can't be null !!";
	public static final String MOBILE_NOT_EMPTY = " Mobile number can't be null or empty !!";
	public static final String IMAGE_NOT_NULL = " Image can't be null !!";
	public static final String ADDRESS_NOT_EMPTY = "Address can't be null !!";
	public static final String COUNTRY_NOT_EMPTY = "Conutry can't be null !!";
	public static final String CITY_NOT_EMPTY = "City can't be null !!";
	public static final String PINCODE_NOT_EMPTY = "PinCode can't be null !!";
	public static final String EMAIL_NOT_EMPTY = "Email can't be null !!";
	public static final String NAME_NOT_EMPTY = "Name can't be null !!";
	public static final String PASSWORD_NOT_EMPTY = "Password can't be null or empty !!";
	public static final String HTTP_STATUS = "status";
	public static final Object PLEASE_TRY_AGAIN = "Please Try Again !!";
	public static final String SEND_NOTIFICATION_SUCCESSFULLY = "Push Notification send succesfully";
	public static final String INCORRECT_OTP = "Invalid OTP";

	// ==========================vERSION message & downloadApp massage
	// ============================

	public static final String VERSION_CREATE_SUCESSFULLY_MESSAGE = "Version create successfully";
	public static final String VERSION_UPDATE_SUCESSFULLY_MESSAGE = "Version update successfully";
	public static final String VERSION_ALREADY_EXISTS = "Version already exists! No changes made";
	public static final String INCRIMENT_SUCESSFULLY_MESSAGE = "Incriment  successfully";
	public static final Object RETRIEVED_SUCCESSFULLY_MESSAGE = "Riterive  successfully";
	public static final String DOWNLOAD_COUNT_SUCCESSFULLY_MESSAGE = "Download counts retrieved successfully";
	public static final String DOWNLOAD_NOT_FOUND = "Failed to increment download counts";
	public static final String ANDROID = "Android";
	public static final String IOS = "Ios";
	public static final Object APP_DOWNLOAD_SUCCESSFULLY = "App download successfully";

	// ========================== Broker message ============================
	public static final String BROKER_ALREADY_EXIST_MESSAGE = "This broker already exist !!";
	public static final String BROKER_SAVED_MESSAGE = "Broker saved successfully !!";
	public static final String BROKER_UPDATED_MESSAGE = "Broker updated successfully !!";
	public static final String COUNTRY_NOT_FOUND_MESSAGE = "Given country not found !!";
	public static final String COUNTRY_CAN_NOT_NULL_MESSAGE = "Country can't be null or empty !!";
	public static final String BROKER_NOT_EMPTY = "Broker can't be null";
	public static final String BROKER_ID_NOT_EMPTY = "Broker id can't be null !!";
	public static final String TYPE_CANT_BE_NULL = "Type can't be null !!";


	// ========================== password policy massage ============================

	public static final String PASSWORD_POLICY = "Password policy uploaded successfully";
	public static final String AlREADY_UPDATED_PASSWORD_POLICY = "Already updated password policy";
	public static final String PASSWORD_POLICY_UPDATED = "Updated Password Policy successfully";
	public static final String PASSWORD_POLICY_NOT_FOUND = "Password Policy not found";
	public static final Object PASSWORD_POLICY_DATA_FOUND = "Password Policy Data found successfully";
	public static final String NO_DB_SERVER_CONNECTION = "The server was found but the connection to its local database was not possible.";
	public static final String MESSAGE_TYPE_NOT_NULL = "Name & description can't be null.";
	public static final String PASSWORD_POLICY_TITTLE = "Password_Policy";

//----------------------------------------- END RESPONSE MESSAGES ----------------------------------------	
}