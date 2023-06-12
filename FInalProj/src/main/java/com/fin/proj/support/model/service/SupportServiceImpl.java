package com.fin.proj.support.model.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fin.proj.support.model.dao.SupportDAO;
import com.fin.proj.support.model.vo.Support;
import com.fin.proj.support.model.vo.SupportDetail;

@Service
public class SupportServiceImpl implements SupportService{

	@Autowired
	private SupportDAO suDAO;
	
	@Override
	public int supportApply(Support s) {
		return suDAO.supportApply(s);
	}

	@Override
	public int insertSupportDetail(SupportDetail supportDetail) {
		return suDAO.insertSupportDetail(supportDetail);
	}

	@Override
	public ArrayList<Support> selectApplyListUser(int uNo) {
		return suDAO.selectApplayListUser(uNo);
	}

	
}
