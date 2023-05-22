package com.knotted.service.admin;


import com.knotted.dto.BoardFormDTO;
import com.knotted.entity.Board;
import com.knotted.entity.BoardImage;
import com.knotted.repository.admin.AdminBoardImageRepository;
import com.knotted.repository.admin.AdminBoardRepository;
import jakarta.persistence.EntityNotFoundException;
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

        if(!boardImageFile.isEmpty()) { // 게시글 이미지를 올렸을 때만
            adminBoardImageService.saveBoardImage(boardImage, boardImageFile);
        }

        // 여기까지 정상적으로 됐으면 게시글 및 게시글 이미지 업로드, 게시글 이미지 DB까지 저장 완료.
    }

    // 게시글 삭제 메소드
    public void deleteBoard(Long boardId) throws Exception {
        Board board = adminBoardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);

        // 일단 삭제 전 해당 이미지 파일도 같이 제거해야 함 (어차피 실제 이미지도 제거해야 하니까 굳이 양방향 매핑 하지 않았음)
        BoardImage boardImage = adminBoardImageRepository.findByBoardId(board.getId());

        // 게시글 이미지 파일 및 DB 먼저 제거
        adminBoardImageService.deleteBoardImage(boardImage);

        // 정상적으로 됐으면 게시글 제거
        adminBoardRepository.delete(board);
    }

}
