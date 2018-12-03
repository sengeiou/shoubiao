package com.bracelet.util;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

import com.bracelet.dto.SocketLoginDto;
import com.bracelet.dto.TianQiLatest;
import com.bracelet.dto.WatchLatestLocation;

public class ChannelMap {
	private ChannelMap() {
	}

	/**
	 * key:imei,value:SocketLoginDto
	 */
	private static Map<String, SocketLoginDto> channelMap2 = new HashMap<String, SocketLoginDto>();

	/**
	 * key:channel,value:SocketLoginDto
	 */
	private static Map<Channel, SocketLoginDto> channelMap3 = new HashMap<Channel, SocketLoginDto>();
	
	
	private static Map<String, WatchLatestLocation> channelMap4 = new HashMap<String, WatchLatestLocation>();
	
	private static Map<String, TianQiLatest> channelMap5 = new HashMap<String, TianQiLatest>();

	/**
	 * 
	 * add channelInfo
	 * 
	 * @param imei
	 * @param dto
	 * @return void
	 * @exception
	 */
	public static void addChannel(String imei, SocketLoginDto dto) {
		channelMap2.put(imei, dto);
	}

	public static void addChannel(Channel channel, SocketLoginDto dto) {
		channelMap3.put(channel, dto);
	}

	public static SocketLoginDto getChannel(Channel channel) {
		return channelMap3.get(channel);
	}

	public static void removeChannel(String imei) {
		SocketLoginDto dto = channelMap2.get(imei);
		if (dto != null) {
			channelMap3.remove(dto.getChannel());
		}
		channelMap2.remove(imei);
	}

	public static SocketLoginDto getChannel(String imei) {
		return channelMap2.get(imei);
	}

	public static void removeChannel(Channel channel) {
		SocketLoginDto dto = channelMap3.get(channel);
		if (dto != null) {
			channelMap2.remove(dto.getImei());
		}

		channelMap3.remove(channel);
	}
	
	
	public static void addlocation(String imei, WatchLatestLocation dto) {
		channelMap4.put(imei, dto);
	}


	public static WatchLatestLocation getlocation(String imei) {
		return channelMap4.get(imei);
	}
	
	
	public static void addCityQianQi(String city, TianQiLatest dto) {
		channelMap5.put(city, dto);
	}


	public static TianQiLatest getCityTianQi(String city) {
		return channelMap5.get(city);
	}
}
