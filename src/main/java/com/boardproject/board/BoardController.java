package com.boardproject.board;

import com.boardproject._core.security.CustomUserDetails;
import com.boardproject._core.utils.JsoupParser;
import com.boardproject.board.dto.BoardRequest;
import com.boardproject.board.dto.BoardResponse;
import com.boardproject.board.type.Category;
import com.boardproject.user.User;
import com.boardproject.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/board")
@Controller
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/create")
    public String create(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("loginUser", userDetails.getUser());
        return "board/boardCreateForm";
    }

    @PostMapping("/create")
    public String create(@Valid BoardRequest.CreateFormDTO createFormDTO, Errors errors) {
        BoardResponse.CreateDTO boradDTO = boardService.create(createFormDTO);
        return "redirect:/board/" + boradDTO.getId();
    }

    @GetMapping("/{id}")
    public String read(Model model, @PathVariable("id") Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        BoardResponse.DetailsDTO boardDTO = boardService.getDetailsById(id);

        model.addAttribute("board", boardDTO);
        model.addAttribute("loginUser", userDetails.getUser());
        return "board/boardDetails";
    }

    @PostMapping("/update/form")
    public String updateForm(Model model, @Valid BoardRequest.UpdateFormDTO updateDTO, Errors errors) {
        model.addAttribute("board", updateDTO);

        List<String> parsedImgName = JsoupParser.parseImgName(updateDTO.getContent());
        model.addAttribute("imgList", parsedImgName);
        return "board/boardUpdateForm";
    }

    @PostMapping("/update")
    public String update(@Valid BoardRequest.UpdateDTO updateDTO, Errors errors) {
        BoardResponse.UpdateDTO update = boardService.update(updateDTO);
        return "redirect:/board/" + update.getId();
    }

    @PostMapping("/delete")
    public String delete(@Valid BoardRequest.DeleteDTO deleteDTO, Errors errors) {
        boardService.delete(deleteDTO);
        return "redirect:/board/list";
    }

    @GetMapping("/list")
    public String list(
            Model model,
            @PageableDefault(size = 6, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    )
    {
        Page<Board> list = boardService.findAll(pageable);
        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        list.map(el -> el).forEach(System.out::println);

        model.addAttribute("boards", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "board/boardList";
    }

    @GetMapping("/{category}")
    public String generalList(
            Model model,
            @PageableDefault(size = 6, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable("category") String category
    )
    {
        BoardResponse.ListDTO listDTO = boardService.getDetailsByCategory(category);
        System.out.println(listDTO);
//        if (category != "genernal" || category != "vip") {
//            return "redirect:/board/list";
//        }

//        model.addAttribute()

        return "redirect:/board/list";
    }
}
