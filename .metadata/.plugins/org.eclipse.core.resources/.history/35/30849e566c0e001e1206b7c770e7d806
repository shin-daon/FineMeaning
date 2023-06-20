package com.fin.proj.board.model.service;

import java.util.ArrayList;

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
	public int getListCount(int i) {
		return bDAO.getListCount(i);
	}

	@Override
	public ArrayList<Board> selectBoardList(PageInfo pi, int i) {
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
	public int insertFruit(Board b) {
		return bDAO.insertFruit(b);
	}

	@Override
	public int deleteReply(int replyNo) {
		return bDAO.deleteReply(replyNo);
	}

}
