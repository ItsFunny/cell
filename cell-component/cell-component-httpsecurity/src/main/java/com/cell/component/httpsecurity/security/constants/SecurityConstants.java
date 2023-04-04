package com.cell.component.httpsecurity.security.constants;

public interface SecurityConstants {
    String DB_PERMISSION_OPERATION_UPDATE = "UPDATE";
    String DB_PERMISSION_OPERATION_ADD = "ADD";
    String DB_PERMISSION_OPERATION_DELETE = "DELETE";

    String DB_PERMISSION_TYPE_MENU = "MENU";
    String DB_PERMISSION_TYPE_OPERATION = "OP";

    Integer ANY = 1 << 0;

    String DB_AUTH_SUPER_ADAMIN = "superAdmin";
    String DB_AUTH_ADMIN = "admin";
    Integer ADMIN_LEVEL_SUPER_ADMIN = 0;
    Integer ADMIN_LEVEL_NORMAL_ADMIN = 1;

    String SECRETKEY="cell";

    Integer DB_USER_STATUS_DISABLE = 0;
    Integer DB_USER_STATUS_ENABLE = 1;
    String ContextUserKey="security-user";
}
