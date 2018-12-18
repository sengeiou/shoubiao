package com.bracelet.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bracelet.dto.HttpBaseDto;
import com.bracelet.dto.SocketLoginDto;
import com.bracelet.entity.Fence;
import com.bracelet.entity.Fencelog;
import com.bracelet.entity.OddShape;
import com.bracelet.entity.SensitivePoint;
import com.bracelet.entity.SensitivePointLog;
import com.bracelet.entity.WatchDevice;
import com.bracelet.entity.WatchDeviceAlarm;
import com.bracelet.entity.WatchDeviceHomeSchool;
import com.bracelet.entity.WatchFriend;
import com.bracelet.entity.WatchPhoneBook;
import com.bracelet.exception.BizException;
import com.bracelet.service.IDeviceService;
import com.bracelet.service.IFenceService;
import com.bracelet.service.IFencelogService;
import com.bracelet.service.ISensitivePointService;
import com.bracelet.service.ISensitivePointlogService;
import com.bracelet.service.IUserInfoService;
import com.bracelet.service.IWatchDeviceService;
import com.bracelet.service.WatchFriendService;
import com.bracelet.util.ChannelMap;
import com.bracelet.util.RadixUtil;
import com.bracelet.util.RespCode;
import com.bracelet.util.Utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/watchfriend")
public class WatchFriendController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	WatchFriendService watchFriendService;

	/* 获取 */
	@ResponseBody
	@RequestMapping(value = "/getList/{token}/{imei}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public String getFriendList(@PathVariable String token, @PathVariable String imei) {

		JSONObject bb = new JSONObject();

		String userId = checkTokenWatchAndUser(token);
		if ("0".equals(userId)) {
			bb.put("Code", -1);
			return bb.toString();
		}

		List<WatchFriend> FriendList = watchFriendService.getFriendByImei(imei);
		JSONArray jsonArray = new JSONArray();
		if(FriendList.size()>0 && FriendList != null){
			for(WatchFriend wfd : FriendList){
				JSONObject dataMap = new JSONObject();
				dataMap.put("DeviceFriendId", wfd.getDeviceFriendId());
				dataMap.put("Relationship", wfd.getRole_name()+"");
				dataMap.put("FriendDeviceId ", wfd.getDeviceFriendId());
				dataMap.put("Name", wfd.getRole_name()+"");
				dataMap.put("Phone", wfd.getPhone()+"");
				dataMap.put("id", wfd.getId());
				jsonArray.add(dataMap);
			}
		}
		bb.put("friendList", jsonArray);
		bb.put("Code", 1);
		return bb.toString();
	}

	/* 修改好友信息 */
	@ResponseBody
	@RequestMapping(value = "/updateFriendInfo", method = RequestMethod.POST)
	public String updateFriendInfo(@RequestBody String body) {
		JSONObject bb = new JSONObject();
		JSONObject jsonObject = (JSONObject) JSON.parse(body);
		String token = jsonObject.getString("token");

		String userId = checkTokenWatchAndUser(token);
		if ("0".equals(userId)) {
			bb.put("Code", -1);
			return bb.toString();
		}
		Long DeviceFriendId = jsonObject.getLong("DeviceFriendId");
		String phone = jsonObject.getString("phone");
		String imei = jsonObject.getString("imei");
		
		WatchFriend wtf = watchFriendService.getFriendByImeiAndPhone(imei,phone,DeviceFriendId);
		
		String nickname = jsonObject.getString("new_name");
		if(wtf !=null ){
			watchFriendService.updateFriendNameById(wtf.getId(), nickname);
		}else{
			watchFriendService.insertFriend(imei, nickname, phone, "0", "1",DeviceFriendId);
		}
		bb.put("Code", 1);
		// Long id = jsonObject.getLong("id");
		return bb.toString();
	}

	/* 获取 */
	@ResponseBody
	@RequestMapping(value = "/delete/{token}/{imei}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public String deleteFriendInfo(@PathVariable String token, @PathVariable Long id) {

		JSONObject bb = new JSONObject();

		String userId = checkTokenWatchAndUser(token);
		if ("0".equals(userId)) {
			bb.put("Code", -1);
			return bb.toString();
		}

		if(watchFriendService.deleteFriendById(id)){
			bb.put("Code", 1);
		}else{
			bb.put("Code", 0);
		}
		return bb.toString();
	}
	
}
