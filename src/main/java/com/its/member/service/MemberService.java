package com.its.member.service;

import com.its.member.dto.MemberDTO;
import com.its.member.entity.MemberEntity;
import com.its.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public Long save(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toEntity(memberDTO);
        Long id = memberRepository.save(memberEntity).getId();
        return id;
    }

    public MemberDTO login(MemberDTO memberDTO) {
        /**
         * login.html 에서 이메일, 비번을 받아오고
         * DB로 부터 해당 이메일의 정보를 가져와서
         * 입력받은 비번과 DB에서 조회한 비번의 일치여부를 판단하여
         * 일치하면 로그인 성공, 일치하지 않으면 로그인 실패로 처리
         */
        Optional<MemberEntity> member = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if(member.isPresent()){
            MemberEntity login = member.get();
            if(login.getMemberPassword().equals(memberDTO.getMemberPassword())){
                return MemberDTO.toMemberDTO(login);
            }else {
                return null;
            }
        }else {
            return null;
        }

    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntity = memberRepository.findAll();
        List<MemberDTO> memberList = new ArrayList<>();
        for (MemberEntity member : memberEntity){
//            MemberDTO memberDTO = MemberDTO.test(member);
//            memberList.add(memberDTO); 두줄용 밑에는 한줄
            memberList.add(MemberDTO.toMemberDTO(member));
        }
        return memberList;
    }

    public MemberDTO findById(Long id){
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if(optionalMemberEntity.isPresent()){
//            return MemberDTO.toMemberDTO(optionalMemberEntity.get()); 한줄용
            MemberEntity memberEntity = optionalMemberEntity.get();
            MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
            return memberDTO;
        }else {
            return null;
        }
    }
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    public void update(MemberDTO memberDTO){
        memberRepository.save(MemberEntity.toUpdateEntity(memberDTO));
    }

    public String check(String id) {
        Optional<MemberEntity> result = memberRepository.findByMemberEmail(id);
        if(result.isEmpty()){
            return "ok";
        }else {
            return "no";
        }
    }
//    public MemberDTO ajax() {
//
//    }
}
