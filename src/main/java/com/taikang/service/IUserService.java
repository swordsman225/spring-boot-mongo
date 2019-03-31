package com.taikang.service;

import java.util.List;

import org.springframework.data.domain.PageImpl;

import com.taikang.vo.UserVO;

public interface IUserService {
	
	public UserVO create(UserVO userVO);

	public UserVO findById(String id);
	
	public List<UserVO> queryList(UserVO userVO);
	
	public PageImpl<UserVO> queryPaging(Integer pageIndex, Integer pageSize, UserVO userVO);
	
	public Long update(UserVO userVO);
	
	public Boolean delete(String id);
	
}
