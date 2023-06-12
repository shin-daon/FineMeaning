package com.fin.proj.support.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.fin.proj.support.model.vo.Support;
import com.fin.proj.support.model.vo.SupportDetail;

@Mapper
public interface SupportDAO {

	int supportApply(Support s);

	int insertSupportDetail(SupportDetail supportDetail);

	ArrayList<Support> selectApplayListUser(int uNo);

	Support supportDetail(int supportNo);

	ArrayList<SupportDetail> supportUsageDetail(int supportNo);

	int getListCount();

}
