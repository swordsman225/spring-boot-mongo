package com.taikang.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.taikang.service.IUserService;
import com.taikang.vo.UserVO;

@RestController
@RequestMapping(value="/mongo")
public class MongoDBController {
	
	private static final Logger logger = LoggerFactory.getLogger(MongoDBController.class);
	
	@Autowired
	private IUserService iUserService;

	@PostMapping(value="/users")
	public UserVO create(@RequestBody UserVO userVO) {
		logger.info("User Create params: {}.", JSON.toJSONString(userVO));
		userVO.setId(UUID.randomUUID().toString());
		userVO = iUserService.create(userVO);
		
		return userVO;
	}
	
	
}
