package com.its.member.controller;

import com.its.member.dto.MemberDTO;
import com.its.member.entity.MemberEntity;
import com.its.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/member")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/save")
    public String saveForm() {
        return "save";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        memberService.save(memberDTO);
        return "redirect:/";
    }
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO member = memberService.login(memberDTO);
        if(member != null) {
            session.setAttribute("email", member.getMemberEmail());
            session.setAttribute("id",member.getId());
            return "index";
        }else {
            return "login";
        }
    }
    @GetMapping("/")
    public String findAll(Model model){
        List<MemberDTO> memberList = memberService.findAll();
        model.addAttribute("member",memberList);
        return "list";
    }
    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        System.out.println("memberDTO = " + memberDTO);
        model.addAttribute("member",memberDTO);
        return "detail";
    }


    @GetMapping("/ajax/{id}")
    public @ResponseBody MemberDTO findByIdAjax(@PathVariable Long id){
        MemberDTO memberDTO = memberService.findById(id);
        return memberDTO;
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        memberService.delete(id);
        if (session.getAttribute("loginEmail") != "관리자") {
            session.invalidate();
        }
        return "index";
    }

    @DeleteMapping("/{id}")
    public @ResponseBody String deleteMapping(@PathVariable Long id, HttpSession session) {
        memberService.delete(id);
        if (session.getAttribute("loginEmail") != "관리자") {
            session.invalidate();
        }
        return "ok";
    }
    // 수정화면 요청
    @GetMapping("/update")
    public String updateFrom(HttpSession session, Model model){
        Long id = (Long)session.getAttribute("id");
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("updateMember", memberDTO);
        return "update";
    }
    // 수정처리
    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return "redirect:/member/" + memberDTO.getId();
    }
    @PutMapping("/{id}")
    public ResponseEntity updateByAjax(@RequestBody MemberDTO memberDTO){
        memberService.update(memberDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/emailCheck")
    public @ResponseBody String check(@RequestParam String id) {
        String result = memberService.check(id);
        return result;
    }
}
