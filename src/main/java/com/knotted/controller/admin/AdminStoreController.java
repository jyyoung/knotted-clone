package com.knotted.controller.admin;

import com.knotted.dto.StoreDTO;
import com.knotted.dto.StoreFormDTO;
import com.knotted.dto.StoreImageDTO;
import com.knotted.dto.StoreItemDTO;
import com.knotted.service.admin.AdminStoreService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/admin/store")
@Controller
@RequiredArgsConstructor
public class AdminStoreController {

    private final AdminStoreService adminStoreService;

    // 매장 관리 메인 페이지. 매장 리스트도 뿌려준다.
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        List<StoreDTO> storeList = adminStoreService.getAllStores();
        model.addAttribute("storeList", storeList);

        return "admin/store/index";
    }

    // 매장 등록 페이지로 이동
    @GetMapping(value = "/new")
    public String storeForm(Model model){
        model.addAttribute("storeFormDTO", new StoreFormDTO());

        return "admin/store/storeForm";
    }

    // 매장 등록 처리
    @PostMapping(value = "/new")
    public String storeSubmit(@Valid StoreFormDTO storeFormDTO, BindingResult bindingResult, Model model, MultipartFile storeImageFile){

        if(bindingResult.hasErrors()){
            return "admin/store/storeForm";
        }

        if(storeImageFile.isEmpty()){ // 이미지가 없다면
            model.addAttribute("errorMessage", "매장 이미지가 없습니다");
            return "admin/store/storeForm";
        }
        
        if(!storeImageFile.getContentType().startsWith("image/")){ // 이미지 파일이 아니라면
            model.addAttribute("errorMessage", "이미지 파일이 아닙니다");
            return "admin/store/storeForm";
        }

        // 이미지가 있으면 매장 및 매장 이미지 저장 로직을 호출
        try{
            adminStoreService.saveStore(storeFormDTO, storeImageFile);
        }catch (Exception e){
            model.addAttribute("errorMessage", "매장 등록 중 에러가 발생했습니다");
            return "admin/store/storeForm";
        }

        // 성공 시 매장 관리 페이지로 이동
        return "redirect:/admin/store";
    }

    // 매장 삭제 처리
    @DeleteMapping(value = "/{storeId}")
    @ResponseBody
    public ResponseEntity<Void> deleteStore(@PathVariable("storeId") Long storeId){

        try {
            adminStoreService.deleteStore(storeId);
        } catch (Exception e) {
            return new ResponseEntity("매장 삭제 중 에러가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.noContent().build();
    }

    // 매장 수정 페이지로 이동
    @GetMapping(value = "/{storeId}")
    public String storeFormUpdate(@PathVariable("storeId") Long storeId, Model model){
        // StoreFormDTO를 넘겨준다

        try {
            StoreFormDTO storeFormDTO = adminStoreService.getStore(storeId);
            model.addAttribute("storeId", storeId);
            model.addAttribute("storeFormDTO", storeFormDTO);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 매장입니다");
            return "redirect:/admin/store";
        }

        return "admin/store/storeForm";
    }

    // 매장 수정 처리
    @PostMapping(value = "/{storeId}")
    public String storeUpdate(@PathVariable("storeId") Long storeId, String imageName, String imageId, @Valid StoreFormDTO storeFormDTO, BindingResult bindingResult, Model model, MultipartFile storeImageFile){

        if(bindingResult.hasErrors()){
            // 바인딩 에러 나면 itemImageDTO가 없어서 타임리프에서 NPE가 뜨는 것 방지
            StoreImageDTO storeImageDTO = new StoreImageDTO();
            storeImageDTO.setOriginalImageName(imageName);
            storeImageDTO.setOriginalImageName(imageId);
            storeImageDTO.setOriginalImageName(imageName);
            storeImageDTO.setId(Long.valueOf(imageId));
            storeFormDTO.setStoreImageDTO(storeImageDTO);

            return "admin/store/storeForm";
        }

        // 이미지 관련 작업부터 하기
        if(!storeImageFile.isEmpty()) { // 새로 올린 파일이 있다면
            // 이미지인지 확인
            if(!storeImageFile.getContentType().startsWith("image/")){ // 이미지 파일이 아니라면
                model.addAttribute("errorMessage", "이미지 파일이 아닙니다");
                return "admin/store/storeForm";
            }
        }

        try {
            adminStoreService.updateStore(storeFormDTO, storeImageFile);
        } catch(Exception e) {
            model.addAttribute("errorMessage", "매장 수정 중 에러가 발생했습니다");
            return "admin/store/storeForm";
        }
        
        // 성공 시 매장 관리 페이지로 이동
        return "redirect:/admin/store";
    }

    // 매장별 상품 관리 페이지로 이동
    @GetMapping(value = "/{storeId}/item")
    public String storeItem(@PathVariable("storeId") Long storeId, Model model){

        // 해당 storeId 있는지 확인
        try {
            StoreFormDTO storeFormDTO = adminStoreService.getStore(storeId);
            model.addAttribute("storeId", storeId);
            model.addAttribute("storeFormDTO", storeFormDTO);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 매장입니다");
            return "redirect:/admin/store";
        }

        return "admin/store/storeItem";
    }

    // 전체 상품 리스트 및 매장별 재고를 받아옴
    @GetMapping(value = "/{storeId}/item/list")
    @ResponseBody
    public ResponseEntity<List<StoreItemDTO>> storeItemList(@PathVariable("storeId") Long storeId){
        // 모든 상품 리스트 (이미지 포함한) 및 매장별 재고를 받아옴
        try{
            List<StoreItemDTO> storeItemList = adminStoreService.getStoreItemList(storeId);
            return new ResponseEntity<>(storeItemList, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 해당 매장의 해당 상품 재고를 추가함
    @PostMapping(value = "/{storeItemId}/add")
    @ResponseBody
    public ResponseEntity<Void> addStock(@PathVariable("storeItemId") Long storeItemId,
                                           @RequestParam("stock") Long stock){

        // 이것도 서비스단에 메소드 만들어서 처리하기
        try{
            adminStoreService.addStock(storeItemId, stock);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
