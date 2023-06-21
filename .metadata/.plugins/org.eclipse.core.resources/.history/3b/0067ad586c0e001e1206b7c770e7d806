package com.fin.proj.board.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.fin.proj.board.model.vo.Board;
import com.fin.proj.board.model.vo.Reply;

@Mapper
public interface BoardDAO {

	int getListCount(int i);

	ArrayList<Board> selectBoardList(int i, RowBounds rowbounds);

	int countUp(int bNo);

	Board selectBoard(int bNo);

	ArrayList<Reply> selectReply(int bNo);

	int insertBoard(Board b);

	void insertReply(Reply r);

	int updateBoard(Board b);

	int deleteBoard(int bId);

	int insertFruit(Board b);

	int deleteReply(int replyNo);
}
