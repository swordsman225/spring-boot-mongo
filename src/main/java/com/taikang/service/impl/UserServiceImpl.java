package com.taikang.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.taikang.service.IUserService;
import com.taikang.vo.UserVO;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	@Override
	public UserVO create(UserVO userVO) {
		mongoTemplate.save(userVO);
		
		return userVO;
	}

	@Override
	public UserVO findById(String id) {
		UserVO userVO = mongoTemplate.findById(id, UserVO.class);
		
		return userVO;
	}

	@Override
	public List<UserVO> queryList(UserVO userVO) {
		Criteria criteria = new Criteria();
		if (!StringUtils.isEmpty(userVO.getName())) {
			criteria.and("name").is(userVO.getName());
		}
		if (userVO.getAge() != null) {
			criteria.and("age").is(userVO.getAge());
		}
		if (userVO.getSex() != null) {
			criteria.and("sex").is(userVO.getSex());
		}
		
		Query query = new Query(criteria);
		List<UserVO> list = mongoTemplate.find(query, UserVO.class);
		
		return list;
	}
	
	
	@Override
	public PageImpl<UserVO> queryPaging(Integer pageIndex, Integer pageSize, UserVO userVO) {
		Criteria criteria = new Criteria();
		if (!StringUtils.isEmpty(userVO.getName())) {
			criteria.and("name").is(userVO.getName());
		}
		if (userVO.getAge() != null) {
			criteria.and("age").is(userVO.getAge());
		}
		if (userVO.getSex() != null) {
			criteria.and("sex").is(userVO.getSex());
		}
		
		Query query = new Query(criteria);
		Sort sort = Sort.by(Direction.DESC, "age");
		PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, sort);

		query.with(pageRequest);
		
		long total = mongoTemplate.count(query, UserVO.class);
		List<UserVO> list = mongoTemplate.find(query, UserVO.class);
		
		PageImpl<UserVO> pageImpl = new PageImpl<>(list, pageRequest, total);
		
		return pageImpl;
	}

	@Override
	public Long update(UserVO userVO) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(userVO.getId()));
		
		Update update = new Update();
		if (!StringUtils.isEmpty(userVO.getName())) {
			update.set("name", userVO.getName());
		}
		if (userVO.getSex() != null) {
			update.set("sex", userVO.getSex());
		}
		if (userVO.getAge() != null) {
			update.set("age", userVO.getAge());
		}
		
		UpdateResult result = mongoTemplate.updateFirst(query, update, UserVO.class);
		
		return result.getModifiedCount();
	}

	@Override
	public Boolean delete(String id) {
		Criteria criteria = Criteria.where("id").is(new ObjectId(id));
		Query query = new Query(criteria);
		DeleteResult deleteResult = mongoTemplate.remove(query, UserVO.class);
		
		return deleteResult.getDeletedCount() > 0;
	}
	
}
