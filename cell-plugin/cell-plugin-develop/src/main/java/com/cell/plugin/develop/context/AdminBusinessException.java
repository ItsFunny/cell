package com.cell.plugin.develop.context;


public class AdminBusinessException extends BusinessException {

  private static final long serialVersionUID = 4952562300194820990L;
  /**业务code*/
  private static final String BUSINESS_CODE = "1";
  /**异常code*/
  private static final String EXCEPTION_CODE = "2";
  
  AdminBusinessException(){
    super();
  }

  public AdminBusinessException(ErrorEntity error) {
    super(error);
  }
  public AdminBusinessException(ErrorEntity error,Throwable t) {
    super(error);
  }
  
  public AdminBusinessException(String code, String message) {
    super(code,message);
  }
  
  public AdminBusinessException(String code, String message, Throwable t) {
    super(code,message,t);
  }

  public static AdminBusinessException throwBusinessMsg(String message){
    return new AdminBusinessException(BUSINESS_CODE, message);
  }
  
  public static AdminBusinessException throwExceptionMsg(String message, Throwable t){
    return new AdminBusinessException(EXCEPTION_CODE, message, t);
  }
}
