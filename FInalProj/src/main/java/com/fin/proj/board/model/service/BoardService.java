package com.fin.proj.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fin.proj.board.model.vo.Board;
import com.fin.proj.board.model.vo.Reply;
import com.fin.proj.common.model.vo.PageInfo;

public interface BoardService {
	
	int getListCount(String string);

	ArrayList<Board> selectBoardList(PageInfo pi, String i);

	Board selectBoard(int bNo, boolean countYN);

	ArrayList<Reply> selectReply(int bNo);

	int insertBoard(Board b);

	void insertReply(Reply r);

	int updateBoard(Board b);

	int deleteBoard(int bId);

	int insertBoardWithCategory(Board b);

	int deleteReply(int replyNo);

	int deleteReplyAll(int boardNo);

	int replyCount(int boardNo);

	ArrayList<Board> searchByTitle(PageInfo pageInfo, HashMap<String, Object> map);

	ArrayList<Board> searchByTitleAndCategory(PageInfo pi, HashMap<String, Object> params);

	int searchListCount(HashMap<String, Object> params);

	ArrayList<Reply> selectReplyList(PageInfo pageInfo, int bNo);

	ArrayList<Reply> selectMyReply(PageInfo pi, int uNo);

	int myReplyCount(int uNo);

	int myBoardCount(int uNo);

	ArrayList<Board> selectMyBoard(PageInfo pageInfo, int uNo);

	ArrayList<Board> searchByFpName(PageInfo pageInfo, HashMap<String, Object> map);

	int finePeopleCount(HashMap<String, Object> map);
	
	List<Reply> findAllComment(int boardNo);

	int saveComment(Reply params);

	void updateComment(Reply params);

	Reply findCommentById(int replyNo);

	int deleteComment(int replyNo);

	Reply findCommentById(int id);
}
