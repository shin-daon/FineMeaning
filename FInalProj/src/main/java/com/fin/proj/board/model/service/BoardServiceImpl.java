package com.fin.proj.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fin.proj.board.model.dao.BoardDAO;
import com.fin.proj.board.model.vo.Board;
import com.fin.proj.board.model.vo.Reply;
import com.fin.proj.common.model.vo.PageInfo;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardDAO bDAO;
	
	@Override
	public int getListCount(String i) {
		return bDAO.getListCount(i);
	}

	@Override
	public ArrayList<Board> selectBoardList(PageInfo pi, String i) {
		int offset = (pi.getCurrentPage() -1 ) * pi.getBoardLimit();
		RowBounds rowbounds = new RowBounds(offset, pi.getBoardLimit());
		
		return bDAO.selectBoardList(i, rowbounds);
	}

	@Override
	@Transactional
	public Board selectBoard(int bNo, boolean countYN) {
		int result = 0;
		if(countYN) {
			result = bDAO.countUp(bNo);
//			System.out.println(result);
		}
		Board board = bDAO.selectBoard(bNo);
//		System.out.println(board);
		return board;
	}

	@Override
	public ArrayList<Reply> selectReply(int bNo) {
		return bDAO.selectReply(bNo);
	}

	@Override
	public int insertBoard(Board b) {
		return bDAO.insertBoard(b);
	}

	@Override
	public void insertReply(Reply r) {
		bDAO.insertReply(r);
	}

	@Override
	public int updateBoard(Board b) {
		return bDAO.updateBoard(b);
	}

	@Override
	public int deleteBoard(int boardNo) {
		return bDAO.deleteBoard(boardNo);
	}

	@Override
	public int insertBoardWithCategory(Board b) {
		return bDAO.insertBoardWithCategory(b);
	}

	@Override
	public int deleteReply(int replyNo) {
		return bDAO.deleteReply(replyNo);
	}

	@Override
	public int deleteReplyAll(int boardNo) {
		return bDAO.deleteReplyAll(boardNo);
	}

	@Override
	public int replyCount(int boardNo) {
		return bDAO.replyCount(boardNo);
	}

	@Override
	public ArrayList<Board> searchByTitle(PageInfo pi, HashMap<String, Object> map) {
		int offset = (pi.getCurrentPage() -1 ) * pi.getBoardLimit();
		RowBounds rowbounds = new RowBounds(offset, pi.getBoardLimit());
		return bDAO.searchByTitle(map, rowbounds);
	}

	@Override
	public ArrayList<Board> searchByTitleAndCategory(PageInfo pi, HashMap<String, Object> params) {
		int offset = (pi.getCurrentPage() -1 ) * pi.getBoardLimit();
		RowBounds rowbounds = new RowBounds(offset, pi.getBoardLimit());
		
		return bDAO.searchByTitleAndCategory(params, rowbounds);
	}

	@Override
	public int searchListCount(HashMap<String, Object> params) {
//		System.out.println(params);
		return bDAO.searchListCount(params);
	}

	@Override
	public ArrayList<Reply> selectReplyList(PageInfo pi, int bNo) {
		int offset = (pi.getCurrentPage() -1 ) * pi.getBoardLimit();
		RowBounds rowbounds = new RowBounds(offset, pi.getBoardLimit());
		
		return bDAO.selectReply(bNo, rowbounds);
	}

	@Override
	public ArrayList<Reply> selectMyReply(PageInfo pi, int uNo) {
		int offset = (pi.getCurrentPage() -1 ) * pi.getBoardLimit();
		RowBounds rowbounds = new RowBounds(offset, pi.getBoardLimit());
		return bDAO.selectMyReply(uNo, rowbounds);
	}

	@Override
	public int myReplyCount(int uNo) {
		return bDAO.myReplyCount(uNo);
	}

	@Override
	public int myBoardCount(int uNo) {
		return bDAO.myBoardCount(uNo);
	}

	@Override
	public ArrayList<Board> selectMyBoard(PageInfo pi, int uNo) {
		int offset = (pi.getCurrentPage() -1 ) * pi.getBoardLimit();
		RowBounds rowbounds = new RowBounds(offset, pi.getBoardLimit());
		return bDAO.selectMyBoard(uNo, rowbounds);
	}

	@Override
	public ArrayList<Board> searchByFpName(PageInfo pi, HashMap<String, Object> map) {
		int offset = (pi.getCurrentPage() -1 ) * pi.getBoardLimit();
		RowBounds rowbounds = new RowBounds(offset, pi.getBoardLimit());
		return bDAO.searchByFpName(map, rowbounds);
	}

	@Override
	public int finePeopleCount(HashMap<String, Object> map) {
		return bDAO.finePeopleCount(map);
	}
	
	public List<Reply> findAllComment(int boardNo) {
		return bDAO.findAllComment(boardNo);
	}

	@Override
	public int saveComment(Reply params) {
		return bDAO.saveComment(params);
	}

	@Override
	public Reply findCommentById(int replyNo) {
		return bDAO.findCommentById(replyNo);
	}

	@Override
	public int deleteComment(int replyNo) {
		return bDAO.deleteComment(replyNo);
	}

	@Override
	public int updateComment(int replyNo) {
		return bDAO.updateComment(replyNo);
	}

	@Override
	public ArrayList<Board> searchByMyBoard(PageInfo pi, HashMap<String, Object> map) {
		int offset = (pi.getCurrentPage() -1 ) * pi.getBoardLimit();
		RowBounds rowbounds = new RowBounds(offset, pi.getBoardLimit());
		return bDAO.searchByMyBoard(map, rowbounds);
	}

	@Override
	public int searchMyBoardListCount(HashMap<String, Object> params) {
		return bDAO.searchMyBoardListCount(params);
	}

}
