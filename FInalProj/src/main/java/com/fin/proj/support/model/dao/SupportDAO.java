package com.fin.proj.support.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.fin.proj.common.model.vo.PageInfo;
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

	ArrayList<Support> selectSupportList(RowBounds rowBounds);

	int getWListCount();

	ArrayList<Support> selectApplyList(RowBounds rowBounds);

	int getDListCount(String devision);

	ArrayList<Support> selectApplyDevision(RowBounds rowBounds, String devision);

	int updateApplyStatus(Support s);


}
