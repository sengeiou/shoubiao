package com.bracelet.service.impl;

import com.bracelet.entity.DeviceCarrierInfo;
import com.bracelet.entity.MomentPwdInfo;
import com.bracelet.entity.PhoneCharge;
import com.bracelet.entity.Pushlog;
import com.bracelet.entity.PwdInfo;
import com.bracelet.entity.SmsInfo;
import com.bracelet.service.IPushlogService;
import com.bracelet.service.PageParam;
import com.bracelet.service.Pagination;
import com.bracelet.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

@Service
public class PushlogServiceImpl implements IPushlogService {
	@Autowired
	JdbcTemplate jdbcTemplate;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean insert(Long user_id, String imei, Integer type, String target, String title, String content) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update(
				"insert into pushlog (user_id, imei, type, target, title, content, createtime) values (?,?,?,?,?,?,?)",
				new Object[] { user_id, imei, type, target, title, content, now }, new int[] { Types.INTEGER, Types.VARCHAR,
						Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP });
		return i == 1;
	}

	@Override
	public Pagination<Pushlog> find(Long user_id, PageParam pageParam) {
		String sql = "select * from pushlog where user_id=?";
		return new Pagination<Pushlog>(sql, new Object[] { user_id }, pageParam, jdbcTemplate, Pushlog.class);
	}

	@Override
	public boolean insertPushMsg(String imei, String message, Integer status) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update(
				"insert into push_message (imei, msg, status, createtime, updatetime) values (?,?,?,?,?)",
				new Object[] {  imei, message, status, now, now }, new int[] {  Types.VARCHAR, Types.VARCHAR,
						Types.INTEGER, Types.TIMESTAMP , Types.TIMESTAMP });
		return i == 1;
	}

	@Override
	public List<SmsInfo> getSmsList(String deviceId) {
		String sql = "select * from watch_sms_info where deviceId=? ";
		List<SmsInfo> list = jdbcTemplate.query(sql, new Object[] {
				deviceId }, new BeanPropertyRowMapper<SmsInfo>(
						SmsInfo.class));
		return list;
	}

	@Override
	public PhoneCharge getCharge(String phone) {
		String sql = "select * from watch_phone_charge where phone=? order by id desc LIMIT 1";
		List<PhoneCharge> list = jdbcTemplate.query(sql, new Object[] { phone },
				new BeanPropertyRowMapper<PhoneCharge>(PhoneCharge.class));

		if (list != null && !list.isEmpty()) {
			return list.get(0);
		} else {
			logger.info("getCharge return null.user_id:" + phone);
		}
		return null;
	}

	@Override
	public DeviceCarrierInfo getDeviceCarrInfo(String deviceId) {
		String sql = "select * from watch_carrier where deviceId=?  LIMIT 1";
		List<DeviceCarrierInfo> list = jdbcTemplate.query(sql, new Object[] { deviceId },
				new BeanPropertyRowMapper<DeviceCarrierInfo>(DeviceCarrierInfo.class));

		if (list != null && !list.isEmpty()) {
			return list.get(0);
		} else {
			logger.info("getCharge return null.user_id:" + deviceId);
		}
		return null;
	}

	@Override
	public boolean updateCarrierById(Long id, String smsNumber, String smsBalanceKey, String smsFlowKey) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update(
				"update watch_carrier set smsNumber = ?, smsBalanceKey = ? , smsFlowKey = ? , updatetime = ? where id = ?",
				new Object[] { smsNumber, smsBalanceKey, smsFlowKey, now , id},
				new int[] { Types.VARCHAR, Types.VARCHAR,Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER });
		return i == 1;
	}

	@Override
	public boolean insertCarrier(String deviceId, String smsNumber, String smsBalanceKey, String smsFlowKey) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update(
				"insert into watch_carrier (deviceId, smsNumber, smsBalanceKey, smsFlowKey, updatetime, createtime) values (?,?,?,?,?,?)",
				new Object[] { deviceId, smsNumber, smsBalanceKey, smsFlowKey, now ,now}, new int[] {
						Types.VARCHAR, Types.VARCHAR,Types.VARCHAR, 
						Types.VARCHAR, Types.TIMESTAMP,  Types.TIMESTAMP });
		return i == 1;
	}

	@Override
	public boolean insertErrorInfo(Long userId, String content) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update(
				"insert into watch_errorinfo (user_id, content, updatetime, createtime) values (?,?,?,?)",
				new Object[] { userId, content, now ,now}, new int[] {
						Types.INTEGER, Types.VARCHAR, Types.TIMESTAMP,  Types.TIMESTAMP });
		return i == 1;
	}

}
