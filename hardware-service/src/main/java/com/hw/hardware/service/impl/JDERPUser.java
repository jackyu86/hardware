package com.hw.hardware.service.impl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.hw.hardware.common.Constants;
import com.hw.hardware.domain.User;
import com.jd.ump.profiler.CallerInfo;
import com.jd.ump.profiler.proxy.Profiler;

/**
 * 
 */
class JDERPUser {
    private static final Logger LOG = LoggerFactory.getLogger(JDERPUser.class);
    private static final String WS_NS = "http://360buy.com/";// 命名空间
    private static final String VERIFY_METHOD = "Verify";
    private static final String GET_USER_METHOD = "GetUserByName";
    private static final String SOAP_ADDRESS_KEY = "login.soap.server";
    
    /**
     * 获取ERP账户信息
     * @param erpName
     * @return
     */
    public static User getERPUser(String erpName) {
    	CallerInfo info = Profiler.registerInfo("jone.JDERPUser.getERPUser", false, true);
        User user = null;
        try {
            Map<String, String> data = new HashMap<String, String>();
            data.put("name", erpName);
            Node node = doRequest(GET_USER_METHOD, data);// GetUserByNameResponse
            if (!node.hasChildNodes()) {// 不存在ERP账户
                return null;
            }
            Node userNode = node.getFirstChild();// GetUserByNameResult
            if (userNode.hasChildNodes()) {
                user = getUser(userNode);
            }
            if(user == null || user.getName() == null) {
                user = null;
            }
        } catch (Exception e) {
            LOG.error("获取[{}]的ERP账户信息失败:{}", erpName,e.getMessage());
            Profiler.functionError(info);
        } finally {
        	Profiler.registerInfoEnd(info);
        }
        return user;
    }
    
    
    /**
     * ERP认证
     * @param userName ERP账号
     * @param password ERP密码
     * @return
     */
    public static boolean verify_ERP(String userName, String password) {
    	CallerInfo info = Profiler.registerInfo("jone.JDERPUser.verify_ERP", false, true);
        boolean res = false;
        try {
            Map<String, String> data = new HashMap<String, String>();
            data.put("name", userName);
            data.put("password", password);
            Node node = doRequest(VERIFY_METHOD, data);// VerifyResponse
            res = node.hasChildNodes();// 认证失败没有返回任何节点信息,认证成功会返回用户信息
        } catch (Exception e) {
            LOG.error("ERP账号[{}]认证失败:{}", userName,e.getMessage());
            Profiler.functionError(info);
        } finally {
        	Profiler.registerInfoEnd(info);
        }
        return res;
    }
    
    /**
     * ERP登录WS请求
     * @param method 方法
     * @param data 数据
     * @return
     * @throws Exception
     */
    private static Node doRequest(String method, Map<String, String> data) throws Exception {
        SOAPMessage reqMsg = MessageFactory.newInstance().createMessage();
        SOAPEnvelope envelope = reqMsg.getSOAPPart().getEnvelope();
        SOAPBody body = envelope.getBody();
        SOAPBodyElement getMessage = body.addBodyElement(envelope.createName(method, null, WS_NS));
        // 设置参数
        if (data != null) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                SOAPElement param = getMessage.addChildElement(entry.getKey());
                param.addTextNode(entry.getValue());
            }
        }
        SOAPConnection con = SOAPConnectionFactory.newInstance().createConnection();
        SOAPMessage response = con.call(reqMsg, Constants.getSystemCfg(SOAP_ADDRESS_KEY));
        con.close();
        return response.getSOAPBody().getFirstChild();
    }
    
    //转换User对象
    private static User getUser(Node userNode) {
        User user = new User();
        Field[] fieldArray = user.getClass().getDeclaredFields();// 反射注入值
        NodeList list = userNode.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node userInfo = list.item(i);
            String nodeName = userInfo.getNodeName();
            try {
                if ("Dept".equals(nodeName)) {// 部门子节点
                    NodeList deptInfo = userInfo.getChildNodes();
                    for (int j = 0; j < deptInfo.getLength(); j++) {
                        Node dept = deptInfo.item(j);
                        if ("Name".equals(dept.getNodeName())) {
//                            user.setDeptmentName(dept.getTextContent());
                            break;
                        }
                    }
                } else {// 根节点
                    String fieldName = lowerFirstChar(nodeName);
                    for (Field field : fieldArray) {
                        if (field.getName().equals(fieldName)) {
                            field.setAccessible(true);
                            if (field.getType() == Integer.TYPE) {
//                                field.set(user, Integer.valueOf(userInfo.getTextContent()));
                            } else {
//                                field.set(user, userInfo.getTextContent());
                            }
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
        return user;
    }
    
    /**
     * 转换名称(首字母小写)
     * @param name
     * @return
     */
    private static String lowerFirstChar(String str) {
        char[] chars = str.toCharArray();
        StringBuffer sb = new StringBuffer(String.valueOf(chars[0]).toLowerCase());
        for (int i = 1; i < chars.length; i++) {
            sb.append(chars[i]);
        }
        return sb.toString();
    }
}