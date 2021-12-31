package com.example.board;

import com.example.exception.Constants;
import com.example.member.MemberService;
import com.example.member.MemberVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Controller
@Slf4j
public class BoardController {

    @Autowired
    public MemberService memberService;

    @Autowired
    private PostService postService;

    @Autowired
    private AttachService attachService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CommentServiceImpl commentService;

    // ########## 게시글 ########## //
    // 게시글 작성 폼
    @GetMapping("/post/write")
    public String writeForm(Model model) {
        int defaultListNo = 1;
        List<String> boardNames = this.postService.retrieveBoardName();
        HashMap<Integer, String> boardList = new HashMap<Integer, String>();
        int i = 1;
        for (String string : boardNames) {
            boardList.put(i, string);
            i++;
        }

        List<RoomVo> roomList = this.roomService.retrieveRoomList();

        model.addAttribute("roomList", roomList);
        // request 영역에 디폴트 게시판 정보를 저장한다.
        model.addAttribute("defaultListNo", defaultListNo);
        // request 영역에 게시판 리스트 정보를 저장한다.
        model.addAttribute("boardList", boardList);

        return "page/post_write";
    }

    // 게시글 목록
    @GetMapping("/board/{boardNo}")
    public String list(@PathVariable("boardNo") int boardNo, Model model,
                       HttpServletRequest request) {
        List<PostVo> posts = postService.retrieveAllPosts(boardNo);
        model.addAttribute("posts", posts);
        model.addAttribute("boardNo", boardNo);

        return "page/post_list";
    }

    // 내가 작성한 게시글 목록
    @GetMapping("/member/room")
    public String myList(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        MemberVo member = (MemberVo) session.getAttribute("member");
        int MemNo = member.getMemNo();
        List<PostVo> posts = this.postService.retrieveMyPosts(MemNo);
        model.addAttribute("posts", posts);


        return "page/member_room";
    }

    // 회원이 작성한 게시글 목록 (회원 정보 확인/회원이 작성한 글)
    @GetMapping("/member/{memNo}")
    public String memberWriteList(@PathVariable("memNo") int memNo, Model model) {

        List<PostVo> posts = this.postService.retrieveMyPosts(memNo);
        model.addAttribute("posts", posts);

        return "page/member_post_list";
    }

    // 게시글 상세보기
    @GetMapping("/post/{postNo}")
    public String read(@PathVariable("postNo") int postNo, Model model) {

        // 게시글 상세정보
        PostVo post = this.postService.retrieveDetailBoard(postNo);
        if (post == null) {
            throw new RuntimeException(Constants.ExceptionMsgClass.NOTPOST.getExceptionMsgClass());
        }
        model.addAttribute("post", post);

        // 댓글 상세정보
        List<CommentVo> comments = this.commentService.retrieveCommentList(postNo);
        for (CommentVo commentVo : comments) {
            // DB에서 대댓글의 댓글인 경우 대댓글 작성자의 닉네임 가져오기
            int parentMemNo = commentVo.getParentMemNo();
            if (parentMemNo > 0) {
                String parentMemNick = memberService.retrieveMember(parentMemNo).getNick();
                commentVo.setParentMemNick(parentMemNick);
            }
        }
        model.addAttribute("comments", comments);


        return "page/post_detail";
    }


    // 게시글 수정폼
    @GetMapping("/post/modify/{postNo}")
    public String modifyFrom(@PathVariable("postNo") int postNo, HttpServletRequest request, Model model) {
        //권한체크


        //게시글 정보 가져오기
        PostVo post = this.postService.retrieveDetailBoard(postNo);

        //현존하지 않은 게시글인 경우
        if (post == null) {
            throw new RuntimeException(Constants.ExceptionMsgClass.NOTPOST.getExceptionMsgClass());
        }

        //게시판 목록 정보 가져오기
        List<String> boardNames = this.postService.retrieveBoardName();

        //HashMap 데이터 형에 게시판 목록 담기
        HashMap<Integer, String> boardList = new HashMap<Integer, String>();
        int i = 1;
        for (String string : boardNames) {
            boardList.put(i, string);
            i++;
        }
        //게시판 목록 model셋팅
        model.addAttribute("boardList", boardList);

        //게시글 정보 model셋팅
        model.addAttribute("post", post);

        return "page/post_modify";
    }

    // 게시글 수정
    @PostMapping("/post/update")
    public String update(@Valid PostVo post) {

        // 값 셋팅
        PostVo postVo = new PostVo();
        postVo.setPostNo(post.getPostNo());
        postVo.setBoardNo(post.getBoardNo());
        postVo.setSubject(post.getSubject());
        postVo.setContent(post.getContent());
        postVo.setTag(post.getTag());

        // 수정 쿼리 실행
        this.postService.modifyPost(postVo);

        return "redirect:/post/" + post.getPostNo();
    }

    // 게시글 삭제
    @GetMapping("/post/delete/{postNo}")
    public String delete(@PathVariable("postNo") int postNo) {

        //본인ㅇ글인지? 확인할 것

        //현존하는 게시글인지 확인
        PostVo post = this.postService.retrieveDetailBoard(postNo);

        //현존하지 않은 게시글인 경우
        if (post == null) {
            throw new RuntimeException(Constants.ExceptionMsgClass.NOTPOST.getExceptionMsgClass());
        }

        // 해당 게시글의 board pk값 받아옴 (삭제 후 목록이로 이동하기 위함)
        int boardNo = post.getBoardNo();

        // 삭제 쿼리 실행
        this.postService.removePost(postNo);

        return "redirect:/board/" + boardNo;
    }
}
