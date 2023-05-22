package com.knotted.service.admin;


import com.knotted.dto.BoardFormDTO;
import com.knotted.entity.Board;
import com.knotted.entity.BoardImage;
import com.knotted.repository.admin.AdminBoardImageRepository;
import com.knotted.repository.admin.AdminBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminBoardService {

    private final AdminBoardRepository adminBoardRepository;
    private final AdminBoardImageRepository adminBoardImageRepository;
    private final AdminBoardImageService adminBoardImageService;

    // 게시글 등록 메소드
    public void saveBoard(BoardFormDTO boardFormDTO, MultipartFile boardImageFile) throws Exception{

        // 게시글을 먼저 등록한다
        Board board = boardFormDTO.createBoard(); // 받은 boardFormDTO 객체를 엔티티로 변환 후 저장
        adminBoardRepository.save(board);

        // 이미지 엔티티를 생성하고 해당 엔티티 안에 FK로 둔 엔티티를 설정한다
        BoardImage boardImage = new BoardImage();
        boardImage.setBoard(board); // 위에서 저장한 엔티티를 FK로 넣어준다

        adminBoardImageService.saveBoardImage(boardImage, boardImageFile);

        // 여기까지 정상적으로 됐으면 게시글 및 게시글 이미지 업로드, 게시글 이미지 DB까지 저장 완료.
    }
}
