package com.fin.proj.support.model.service;

import java.util.ArrayList;

import com.fin.proj.common.model.vo.PageInfo;
import com.fin.proj.support.model.vo.Support;
import com.fin.proj.support.model.vo.SupportDetail;

public interface SupportService {

	int supportApply(Support s);

	int insertSupportDetail(SupportDetail supportDetail);

	ArrayList<Support> selectApplyListUser(int uNo);

	Support supportDetail(int supportNo);

	ArrayList<SupportDetail> supportUsageDetail(int supportNo);

	int getListCount();

	ArrayList<Support> selectSupportList(PageInfo pi);

	int getWListCount();

	ArrayList<Support> selectApplyList(PageInfo pi);

	int getDListCount(String devision);

	ArrayList<Support> applyDevision(PageInfo pi, String devision);




}
