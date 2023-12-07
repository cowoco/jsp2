package com.java.www.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import com.java.www.dto.BoardDto;
public class BoardDao {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	BoardDto bdto;
	ArrayList<BoardDto> list = new ArrayList<BoardDto>();
	int bno,bgroup,bstep,bindent,bhit;
	String btitle,bcontent,id,bfile,query;
	Timestamp bdate;
	int result,listCount;
	
	//커넥션풀에서 Connection객체 가져오기
	public Connection getConnection() {
		Connection connection = null;
		try {
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/Oracle18");
			connection = ds.getConnection();
		} catch (Exception e) { e.printStackTrace();}
		return connection;
	}//getConnection
	
	
	//전체게시글,검색 가져오기 - select
	public ArrayList<BoardDto> n_listSelect(String category, String sword, int startRow, int endRow) {
		try {
			conn = getConnection();
			if(category==null) {
				query = "select * from (select row_number() over (order by bgroup desc,bstep asc) rnum, a.* from board a) where rnum between ? and ?";
				pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, startRow);
				pstmt.setInt(2, endRow);
			}else if(category.equals("all")) {
				query = "select * from (select row_number() over (order by bgroup desc,bstep asc) rnum, a.* from board a where btitle like '%'||?||'%' or bcontent like '%'||?||'%') where rnum between ? and ?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, sword);
				pstmt.setString(2, sword);
				pstmt.setInt(3, startRow);
				pstmt.setInt(4, endRow);
			}else if(category.equals("btitle")) {
				query = "select * from (select row_number() over (order by bgroup desc,bstep asc) rnum, a.* from board a where btitle like '%'||?||'%') where rnum between ? and ?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, sword);
				pstmt.setInt(2, startRow);
				pstmt.setInt(3, endRow);
			}else {
				query = "select * from (select row_number() over (order by bgroup desc,bstep asc) rnum, a.* from board a where bcontent like '%'||?||'%') where rnum between ? and ?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, sword);
				pstmt.setInt(2, startRow);
				pstmt.setInt(3, endRow);
			}
			rs = pstmt.executeQuery();
			while(rs.next()) {
				bno = rs.getInt("bno");
				btitle = rs.getString("btitle");
				bcontent = rs.getString("bcontent");
				bdate = rs.getTimestamp("bdate");
				id = rs.getString("id");
				bgroup = rs.getInt("bgroup");
				bstep = rs.getInt("bstep");
				bindent = rs.getInt("bindent");
				bhit = rs.getInt("bhit");
				bfile = rs.getString("bfile");
				list.add(new BoardDto(bno,btitle,bcontent,bdate,id,bgroup,bstep,bindent,bhit,bfile));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (Exception e2) { e2.printStackTrace();}
		}//
		return list;
	}//n_listSelect
	
	//게시글 전체개수
	public int nListCount(String category, String sword) {
		try {
			conn = getConnection();
			if(category==null) { //
				query = "select count(*) listCount from board";
				pstmt = conn.prepareStatement(query);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					listCount = rs.getInt("listCount");
					System.out.println("dao nListCount : "+listCount);
				}
			}else if(category.equals("all")) {
				query = "select count(*) listCount from board where btitle like '%'||?||'%' or bcontent like '%'||?||'%' ";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, sword);
				pstmt.setString(2, sword);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					listCount = rs.getInt("listCount");
					System.out.println("dao nListCount : "+listCount);
				}
			}else if(category.equals("btitle")) {
				query = "select count(*) listCount from board where btitle like '%'||?||'%'";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, sword);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					listCount = rs.getInt("listCount");
					System.out.println("dao nListCount : "+listCount);
				}
			}else {
				query = "select count(*) listCount from board where bcontent like '%'||?||'%'";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, sword);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					listCount = rs.getInt("listCount");
					System.out.println("dao nListCount : "+listCount);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (Exception e2) { e2.printStackTrace();}
		}//
		return listCount;
	}//nListCount
	
	
	//게시글 1개 가져오기
	public BoardDto selectOne(int bno2) {
		try {
			conn = getConnection();
			query = "select * from board where bno=?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, bno2);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				//
				bno = rs.getInt("bno");
				btitle = rs.getString("btitle");
				bcontent = rs.getString("bcontent");
				bdate = rs.getTimestamp("bdate");
				id = rs.getString("id");
				bgroup = rs.getInt("bgroup");
				bstep = rs.getInt("bstep");
				bindent = rs.getInt("bindent");
				bhit = rs.getInt("bhit");
				bfile = rs.getString("bfile");
				bdto = new BoardDto(bno, btitle, bcontent, bdate, id, bgroup, bstep, bindent, bhit, bfile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (Exception e2) { e2.printStackTrace();}
		}//
		return bdto;
	}//selectOne
	
	
	
	
	//이전글 가져오기
	public BoardDto preSelectOne(int bno2) {
		try {
			conn = getConnection();
			query = "select * from (select row_number() over(order by bgroup desc, bstep asc) rnum, a.* from board a) where rnum = (select rnum from (select row_number() over(order by bgroup desc, bstep asc) rnum, a.* from board a) where bno=?)+1";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, bno2);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				//
				bno = rs.getInt("bno");
				btitle = rs.getString("btitle");
				bcontent = rs.getString("bcontent");
				bdate = rs.getTimestamp("bdate");
				id = rs.getString("id");
				bgroup = rs.getInt("bgroup");
				bstep = rs.getInt("bstep");
				bindent = rs.getInt("bindent");
				bhit = rs.getInt("bhit");
				bfile = rs.getString("bfile");
				bdto = new BoardDto(bno, btitle, bcontent, bdate, id, bgroup, bstep, bindent, bhit, bfile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (Exception e2) { e2.printStackTrace();}
		}//
		return bdto;
	}//preSelectOne
	
	
	//다음글 가져오기
	public BoardDto nextSelectOne(int bno2) {
		try {
			conn = getConnection();
			query = "select * from (select row_number() over(order by bgroup desc, bstep asc) rnum, a.* from board a) where rnum = (select rnum from (select row_number() over(order by bgroup desc, bstep asc) rnum, a.* from board a) where bno=?)-1";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, bno2);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				//
				bno = rs.getInt("bno");
				btitle = rs.getString("btitle");
				bcontent = rs.getString("bcontent");
				bdate = rs.getTimestamp("bdate");
				id = rs.getString("id");
				bgroup = rs.getInt("bgroup");
				bstep = rs.getInt("bstep");
				bindent = rs.getInt("bindent");
				bhit = rs.getInt("bhit");
				bfile = rs.getString("bfile");
				bdto = new BoardDto(bno, btitle, bcontent, bdate, id, bgroup, bstep, bindent, bhit, bfile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (Exception e2) { e2.printStackTrace();}
		}//
		return bdto;
	}//nextSelectOne
	
	
	
	
	//게시글 1개 저장
	public int insert(BoardDto bdto2) {
		try {
			conn = getConnection();
			query = "insert into board values(board_seq.nextval,?,?,sysdate,?,board_seq.currval,0,0,1,?)";
			pstmt = conn.prepareStatement(query);
			//1,2,
			pstmt.setString(1, bdto2.getBtitle());
			pstmt.setString(2, bdto2.getBcontent());
			pstmt.setString(3, bdto2.getId());
			pstmt.setString(4, bdto2.getBfile());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (Exception e2) { e2.printStackTrace();}
		}//
		return result;
	}//insert
	//답글달기를 위한 step증가
	public void stepUp(int bgroup2, int bstep2) {
		try {
			conn = getConnection();
			query = "update board set bstep=bstep+1 where bgroup=? and bstep>?";
			pstmt = conn.prepareStatement(query);
			//1,2,
			pstmt.setInt(1, bgroup2);
			pstmt.setInt(2, bstep2);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (Exception e2) { e2.printStackTrace();}
		}//
	}//stepUp
	//답글달기 저장
	public int replyInsert(BoardDto bdto2) {
		try {
			conn = getConnection();
			query = "insert into board values(board_seq.nextval,?,?,sysdate,?,?,?,?,1,?)";
			pstmt = conn.prepareStatement(query);
			//1,2
			pstmt.setString(1, bdto2.getBtitle());
			pstmt.setString(2, bdto2.getBcontent());
			pstmt.setString(3, bdto2.getId());
			pstmt.setInt(4, bdto2.getBgroup()); //부모 그대로 사용
			pstmt.setInt(5, bdto2.getBstep()+1); //부모보다 1증가
			pstmt.setInt(6, bdto2.getBindent()+1); //부모보다 1증가
			pstmt.setString(7, bdto2.getBfile());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (Exception e2) { e2.printStackTrace();}
		}//
		return result;
	}//replyInsert
	//게시글 수정 - update
	public int update(BoardDto bdto2) {
		try {
			conn = getConnection();
			query = "update board set btitle=?,bcontent=?,bfile=? where bno=?";
			pstmt = conn.prepareStatement(query);
			//1,2
			pstmt.setString(1, bdto2.getBtitle());
			pstmt.setString(2, bdto2.getBcontent());
			pstmt.setString(3, bdto2.getBfile());
			pstmt.setInt(4, bdto2.getBno()); //
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (Exception e2) { e2.printStackTrace();}
		}//
		return result;
	}//update
	//게시글 삭제
	public int delete(int bno2) {
		try {
			conn = getConnection();
			query = "delete board where bno=?";
			pstmt = conn.prepareStatement(query);
			//1,2
			pstmt.setInt(1, bno2);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (Exception e2) { e2.printStackTrace();}
		}//
		return result;
	}//delete


	//좋아요부분 - 내가 좋아요 누른 상태 - select
	public int myLikeSelect(String id2, int bno2) {
		int my_like_count =0;
		try {
			conn = getConnection();
			query = "select count(*) my_like_count from b_likes where bno=? and id=? and like_status=1";
			pstmt = conn.prepareStatement(query);
			//1,2
			pstmt.setInt(1, bno2);
			pstmt.setString(2, id2);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				my_like_count = rs.getInt("my_like_count");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (Exception e2) { e2.printStackTrace();}
		}//
		return my_like_count;
	}//myLikeSelect


	
	//좋아요부분 - 전체 좋아요 개수 - select
	public int allLikeSelect (int bno2) {
		int all_like_count = 0;
		try {
			conn = getConnection();
			query = "select count(*) all_like_count from b_likes where bno=? and like_status=1";
			pstmt = conn.prepareStatement(query);
			//1,2
			pstmt.setInt(1, bno2);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				all_like_count = rs.getInt("all_like_count");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (Exception e2) { e2.printStackTrace();}
		}//
		return all_like_count;
	}//allLikeSelect


	//좋아요 상태 변경 - update
	public int myLikeUpdate(String id2, int bno2, int like_status) {
		int all_like_count = 0;
		try {
			conn = getConnection();
			
			//내가 좋아요를 누른 적이 있는지 체크
			query = "select count(*) count from b_likes where bno=? and id=?";
			pstmt = conn.prepareStatement(query);
			//1,2
			pstmt.setInt(1, bno2);
			pstmt.setString(2, id2);
			rs = pstmt.executeQuery();
			int count = 0;
			if(rs.next()) {
				count = rs.getInt("count");
			}
			if(count==0) {
				//내가 좋아요 누른 적이 없는 경우 - insert
				query = "insert into b_likes values (b_likes_seq.nextval, ?,?,?)";
				pstmt = conn.prepareStatement(query);
				//1,2
				pstmt.setInt(1, bno2);
				pstmt.setString(2, id2);
				pstmt.setInt(3, like_status);
				pstmt.executeUpdate();
				
			}else {
				//내가 좋아요 누른 적이 있는 경우 - update
				query = "update b_likes set like_status=? where bno=? and id=?";
				pstmt = conn.prepareStatement(query);
				//1,2
				pstmt.setInt(1, like_status);
				pstmt.setInt(2, bno2);
				pstmt.setString(3, id2);
				pstmt.executeUpdate();
			}
			//좋아요 전체개수 가져오기
			all_like_count = allLikeSelect(bno2);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (Exception e2) { e2.printStackTrace();}
		}//
		return all_like_count;
	}//myLikeUpdate
	
	
	
	

	
	
}