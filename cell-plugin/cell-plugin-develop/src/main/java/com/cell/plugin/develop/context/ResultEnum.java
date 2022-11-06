package com.cell.plugin.develop.context;

public enum ResultEnum {
    /*
     * 操作成功
     */
    SUCCESS(200,"success"),
    /*
     * 无法判断
     */
    ERROR_PARAMS(402,"params error"),

    /**
     * 用户地址为null
     */
    ACCOUNT_ERROR(405,"account is null"),
    /*
     * 提示信息
     */
	ERROR_MSG(502,"system error"),

    USER_EXIST(601, "user exist"),

    USER_NOT_EXIT(602,"USER_NOT_EXIT"),
    USER_PARENT_NOT_EXIST(603, "user parent not exist"),

    NO_Commodity(604, "NO Commodity"),

    Stage_error(605,"Stage_error"),

    KEY_ERROR(606,"key error"),

    SIGN_ERROR(607,"sign error"),

    COMM_BIND(608,"commodity is  bind"),

    CONTRACT_EXIST(609, "contract already exists"),


    Authorization_failure(610,"Authorization failure"),

    Is_using(611,"Is using"),

    NFT_OWNER_ERROR(612,"Nft owner error"),

    NFT_NOT_SIGN_OK(613,"Nft not sign ok"),

    SCORE_TOO_LOW(613,"score too low"),


    NFT_Award_Error(615,"NFT_Award_Error"),


    SETTLEMENT(614,"settlement"),

    NOT_KNOCKING_DOOR_ENOUGH(614, "not knocking enough"),


    BUSINESS_EXCEPTION_PARAMETER_UPLOAD_FILE_IS_EMPTY(51012, "上传文件不能为空"),

    BUSINESS_EXCEPTION_UPLOAD_FILE_IS_NOT_IMAGE(52001, "上传文件并非图片类型"),

    BUSINESS_EXCEPTION_UPLOAD_FILE_FAILED(52002, "上传文件失败，请重试");

    public int code;

    public String message;

    private ResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
