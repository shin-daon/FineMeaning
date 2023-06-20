package com.fin.proj.support.model.service;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fin.proj.common.model.vo.PageInfo;
import com.fin.proj.support.model.dao.SupportDAO;
import com.fin.proj.support.model.vo.Support;
import com.fin.proj.support.model.vo.SupportDetail;
import com.fin.proj.support.model.vo.SupportHistory;

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

	@Override
	public Support supportDetail(int supportNo) {
		return suDAO.supportDetail(supportNo);
	}

	@Override
	public ArrayList<SupportDetail> supportUsageDetail(int supportNo) {
		return suDAO.supportUsageDetail(supportNo);
	}

	@Override
	public int getListCount() {
		return suDAO.getListCount();
	}

	@Override
	public ArrayList<Support> selectSupportList(PageInfo pi) {
		int offset = (pi.getCurrentPage() -1 ) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		return suDAO.selectSupportList(rowBounds);
	}

	@Override
	public int getWListCount() {
		return suDAO.getWListCount();
	}

	@Override
	public ArrayList<Support> selectApplyList(PageInfo pi) {
		int offset = (pi.getCurrentPage() -1 ) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		return suDAO.selectApplyList(rowBounds);
	}

	@Override
	public int getDListCount(String devision) {
		return suDAO.getDListCount(devision);
	}

	@Override
	public ArrayList<Support> applyDevision(PageInfo pi, String devision) {
		int offset = (pi.getCurrentPage() -1 ) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return suDAO.selectApplyDevision(rowBounds, devision);
	}

	@Override
	public int updateApplyStatus(Support s) {
		return suDAO.updateApplyStatus(s);
	}

	@Override
	public int getEListCount() {
		return suDAO.getEListCount();
	}

	@Override
	public ArrayList<Support> selectEndSupportList(PageInfo pi) {
		int offset = (pi.getCurrentPage() -1 ) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return suDAO.selectEndSupportList(rowBounds);
	}

	@Override
	public int getSeachListCount(String searchWord) {
		return suDAO.getSearchListCount(searchWord);
	}

	@Override
	public ArrayList<Support> selectSearchListAdmin(PageInfo pi, String searchWord) {
		int offset = (pi.getCurrentPage() -1 ) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		return suDAO.selectSearchListAdmin(rowBounds, searchWord);
	}

	@Override
	public int getSupporterListCount(int supportNo) {
		return suDAO.getSupporterListCount(supportNo);
	}

	@Override
	public ArrayList<SupportHistory> selectSupporterListEach(PageInfo pi, int supportNo) {
		int offset = (pi.getCurrentPage() -1 ) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		return suDAO.selectSupporterListEach(rowBounds, supportNo);
	}

	@Override
	public int getSupporterListAllCount() {
		return suDAO.getSupporterListAllCount();
	}

	@Override
	public ArrayList<SupportHistory> selectSupporterList(PageInfo pi) {
		int offset = (pi.getCurrentPage() -1 ) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		return suDAO.selectSupporterList(rowBounds);
	}






	
}
