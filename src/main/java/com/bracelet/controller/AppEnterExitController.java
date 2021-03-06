package com.bracelet.controller;

import com.alibaba.fastjson.JSONObject;
import com.bracelet.dto.SocketLoginDto;
import com.bracelet.service.WatchSetService;
import com.bracelet.util.ChannelMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/app")
public class AppEnterExitController extends BaseController {
	
	@Autowired
	WatchSetService watchSetService;	
	

	@ResponseBody
	@RequestMapping(value = "/enter/{token}/{imei}", method = RequestMethod.GET)
	public String enter(@PathVariable String token, @PathVariable String imei) {
		JSONObject bb = new JSONObject();
		String user_id = checkTokenWatchAndUser(token);
		if ("0".equals(user_id)) {
			bb.put("Code", -1);
			return bb.toString();
		}
		SocketLoginDto socketLoginDto = ChannelMap.getChannel(imei);
		if (socketLoginDto == null || socketLoginDto.getChannel() == null) {
			bb.put("Code", 2);
			return bb.toString();
		}
		if (socketLoginDto.getChannel().isActive()) {
			String reps = "[YW*"+imei+"*0001*0004*ET,1]";
			socketLoginDto.getChannel().writeAndFlush(reps);
			bb.put("Code", 1);
		} else {
			bb.put("Code", 0);
		}
		return bb.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/exit/{token}/{imei}", method = RequestMethod.GET)
	public String exit(@PathVariable String token, @PathVariable String imei) {
		JSONObject bb = new JSONObject();

		String user_id = checkTokenWatchAndUser(token);
		if ("0".equals(user_id)) {
			bb.put("Code", -1);
			return bb.toString();
		}
		SocketLoginDto socketLoginDto = ChannelMap.getChannel(imei);
		if (socketLoginDto == null || socketLoginDto.getChannel() == null) {
			bb.put("Code", 2);
			return bb.toString();
		}
		if (socketLoginDto.getChannel().isActive()) {
			String reps = "[YW*"+imei+"*0001*0004*ET,0]";
			socketLoginDto.getChannel().writeAndFlush(reps);
			bb.put("Code", 1);
		} else {
			bb.put("Code", 0);
		}
		return bb.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/setdialpad/{token}/{imei}/{type}", method = RequestMethod.GET)
	public String setDialPad(@PathVariable String token, @PathVariable String imei, @PathVariable Integer type) {
		JSONObject bb = new JSONObject();

		String user_id = checkTokenWatchAndUser(token);
		if ("0".equals(user_id)) {
			bb.put("Code", -1);
			return bb.toString();
		}
		SocketLoginDto socketLoginDto = ChannelMap.getChannel(imei);
		if (socketLoginDto == null || socketLoginDto.getChannel() == null) {
			bb.put("Code", 4);
			return bb.toString();
		}

		if (socketLoginDto.getChannel().isActive()) {
			
			String reps=null;
			if(type==1){
				reps = "[YW*"+imei+"*0001*0004*KB,1]";
			}else{
				reps = "[YW*"+imei+"*0001*0004*KB,0]";
			}
			socketLoginDto.getChannel().writeAndFlush(reps);
			bb.put("Code", 1);
			watchSetService.setdialpadbyImei(imei,type);
		} else {
			bb.put("Code", 0);
		}
		
		return bb.toString();
	}

}