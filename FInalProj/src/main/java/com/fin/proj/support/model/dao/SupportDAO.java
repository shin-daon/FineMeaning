package com.fin.proj.support.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.fin.proj.support.model.vo.Support;
import com.fin.proj.support.model.vo.SupportDetail;

@Mapper
public interface SupportDAO {

	int supportApply(Support s);

	int insertSupportDetail(SupportDetail supportDetail);

}
