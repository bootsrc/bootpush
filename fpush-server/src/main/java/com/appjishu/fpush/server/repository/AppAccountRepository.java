package com.appjishu.fpush.server.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.appjishu.fpush.server.entity.AppAccount;

@Repository
public class AppAccountRepository {
	private static final Logger log = LoggerFactory.getLogger(AppAccountRepository.class);
	
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private static final String COLUMNS = " app_id, app_key, app_secret_key, key_token, secret_token " +
            ", create_time, update_time, key_token_expire, secret_token_expire, mobile_phone ";
    private static final String TABLE_NAME = "app_account";
    
    public int add(AppAccount appAccount) {
    	String sql = " INSERT INTO " + TABLE_NAME + " (" + COLUMNS + ") VALUES ("
    			+ ":appId, :appKey, :appSecretKey, :keyToken, :secretToken, :createTime, :updateTime"
    			+ ", :keyTokenExpire, :secretTokenExpire, :mobilePhone"
    			+ " )";
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("appId", appAccount.getAppId());
    	params.put("appKey", appAccount.getAppKey());
      	params.put("appSecretKey", appAccount.getAppSecretKey());
      	params.put("keyToken", appAccount.getKeyToken());
      	params.put("secretToken", appAccount.getSecretToken());
      	params.put("createTime", appAccount.getCreateTime());
      	params.put("updateTime", appAccount.getUpdateTime());
      	params.put("keyTokenExpire", appAccount.getKeyTokenExpire());
      	params.put("secretTokenExpire", appAccount.getSecretTokenExpire());
      	params.put("mobilePhone", appAccount.getMobilePhone());
      	
    	int updatedCount = namedParameterJdbcTemplate.update(sql, params);
    	return updatedCount;
    }
    
    public AppAccount findByMobilePhone(String mobilePhone) {
    	String sql = "SELECT * FROM " + TABLE_NAME + " WHERE mobile_phone=:mobilePhone ";
    	
    	Map<String, Object> params = new HashMap<String, Object>();
     	params.put("mobilePhone", mobilePhone);
     	List<AppAccount> list = namedParameterJdbcTemplate.query(sql, 
     			params, new BeanPropertyRowMapper<>(AppAccount.class));
     	
     	if (list == null || list.size() == 0) {
     		return null;
     	} else {
     		return list.get(0);
     	}
    }
    
    public AppAccount findBySecretKey(long appId, String appSecretKey) {
    	String sql = "SELECT * FROM " + TABLE_NAME + " WHERE app_id=:appId"
    			+ " AND app_secret_key=:appSecretKey ";
    	
    	Map<String, Object> params = new HashMap<String, Object>();
     	params.put("appId", appId);
     	params.put("appSecretKey", appSecretKey);
     	List<AppAccount> list = namedParameterJdbcTemplate.query(sql, 
     			params, new BeanPropertyRowMapper<>(AppAccount.class));
     	
     	if (list == null || list.size() == 0) {
     		return null;
     	} else {
     		return list.get(0);
     	}
    }
    
    public AppAccount findByAppKey(long appId, String appKey) {
    	String sql = "SELECT * FROM " + TABLE_NAME + " WHERE app_id=:appId"
    			+ " AND app_key=:appKey ";
    	
    	Map<String, Object> params = new HashMap<String, Object>();
     	params.put("appId", appId);
     	params.put("appKey", appKey);
     	List<AppAccount> list = namedParameterJdbcTemplate.query(sql, 
     			params, new BeanPropertyRowMapper<>(AppAccount.class));
     	
     	if (list == null || list.size() == 0) {
     		return null;
     	} else {
     		return list.get(0);
     	}
    }
}
