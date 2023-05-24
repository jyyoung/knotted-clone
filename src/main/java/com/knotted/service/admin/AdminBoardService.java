package com.knotted.service.admin;


import com.knotted.dto.BoardFormDTO;
import com.knotted.dto.BoardImageDTO;
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
        if(boardImage != null){ // 있으면 제거
            adminBoardImageService.deleteBoardImage(boardImage);
        }

        // 정상적으로 됐으면 게시글 제거
        adminBoardRepository.delete(board);
    }


    // 게시글 읽는 메소드 (이미지까지 포함)
    public BoardFormDTO getBoard(Long boardId){
        Board board = adminBoardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);

        // 애초에 정상적으로 찾아졌으면 여기로 넘어옴
        BoardFormDTO boardFormDTO = BoardFormDTO.of(board);
        BoardImage boardImage = adminBoardImageRepository.findByBoardId(board.getId());

        if(boardImage != null){
            BoardImageDTO boardImageDTO = BoardImageDTO.of(boardImage);
            boardFormDTO.setBoardImageDTO(boardImageDTO);
        }

        return boardFormDTO;
    }


    // 게시글 수정 메소드
    public void updateBoard(BoardFormDTO boardFormDTO, MultipartFile boardImageFile) throws Exception{

        // 게시글을 먼저 수정한다
        Board board = adminBoardRepository.findById(boardFormDTO.getId())
                .orElseThrow(EntityNotFoundException::new);
        board.updateBoard(boardFormDTO);

        // 새로 올라온 이미지가 있으면 기존 거 삭제 후 등록
        if(!boardImageFile.isEmpty()){
            // 기존 이미지를 찾는다
            BoardImage boardImage = adminBoardImageRepository.findByBoardId(board.getId()); // 게시글 이미지 엔티티 조회

            // 이미지가 있으면 기존 이미지부터 제거해준다
            if(boardImage != null){
                adminBoardImageService.deleteBoardImage(boardImage);
            }

            // BoardImage를 생성 후 엔티티만 넣어주고,
            BoardImage newBoardImage = new BoardImage(); // BoardImage 엔티티 생성
            newBoardImage.setBoard(board); // 위에서 저장한 엔티티를 FK로 넣어준다

            // 새 이미지 등록
            adminBoardImageService.saveBoardImage(newBoardImage, boardImageFile);
        }
    }

}
