package infoauto.entity;

import lombok.Data;

import java.util.List;

//设备报警实体类
@Data
public class EquipmentAlarms {
    //请求ID
    private String reqId;
    //工厂编号
    private String facility="5500";
    //系统编码
    private String reqSys="PLC";
    //订单编号
    private String wipOrderNo;
    //顺序号
    private String sequenceNo;
    //工序号
    private String oprSequenceNo;
    //产品序列号
    private String serialNo;
    //设备编码
    private String deviceID;
    //员工号
    private String employeeNo;
    //预留参数1
    private String paramRsrv1;
    //预留参数2
    private String paramRsrv2;
    //异常信息列表
    private List<Exception> exceptionList;

    public static class Exception {
        //故障代码
        private String reasonCode;
        //异常描述
        private String exceptionDesc;
        //发生时间
        private String exceptionTime;
        //上传时间
        private String uploadTime;
        //预留参数3
        private String paramRsrv3;


        public String getReasonCode() {
            return reasonCode;
        }

        public void setReasonCode(String reasonCode) {
            this.reasonCode = reasonCode;
        }

        public String getExceptionDesc() {
            return exceptionDesc;
        }

        public void setExceptionDesc(String exceptionDesc) {
            this.exceptionDesc = exceptionDesc;
        }

        public String getExceptionTime() {
            return exceptionTime;
        }

        public void setExceptionTime(String exceptionTime) {
            this.exceptionTime = exceptionTime;
        }

        public String getUploadTime() {
            return uploadTime;
        }

        public void setUploadTime(String uploadTime) {
            this.uploadTime = uploadTime;
        }

        public String getParamRsrv3() {
            return paramRsrv3;
        }

        public void setParamRsrv3(String paramRsrv3) {
            this.paramRsrv3 = paramRsrv3;
        }
    }
}
