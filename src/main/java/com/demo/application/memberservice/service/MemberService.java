package com.demo.application.memberservice.service;

import com.demo.application.memberservice.dto.MemberRequest;
import com.demo.application.memberservice.dto.MemberResponse;
import com.demo.application.memberservice.exception.ResourceNotFoundException;
import com.demo.application.memberservice.model.Member;
import com.demo.application.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    public void createMember(MemberRequest memberRequest) {
        Member member = new Member();
        member.setMemberNumber(memberRequest.getMemberNumber());
        member.setName(memberRequest.getName());
        member.setAddress(memberRequest.getAddress());
        memberRepository.save(member);
        log.info("Member [{}] saved successfully.", member.getId());
    }

    public List<MemberResponse> getAllMembers() {
        List<Member> memberList = memberRepository.findAll();
        log.info("Fetched [{}] members successfully.", memberList.size());
        return memberList.stream()
                .map(
                        this::fromMemberToMemberResponse
                )
                .toList();
    }

    public MemberResponse getMemberById(Long id) throws ResourceNotFoundException {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Member not found with id: %d", id))
        );

        return fromMemberToMemberResponse(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public MemberResponse updateMember(MemberRequest memberRequest, Long id) throws IllegalAccessException, ResourceNotFoundException {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Member not found with id: %d", id))
        );

        ArrayList<String> nullfieldsInMemberRequest = getNullFields(memberRequest);
        BeanUtils.copyProperties(memberRequest, member, nullfieldsInMemberRequest.toArray(String[]::new));
        memberRepository.save(member);
        return fromMemberToMemberResponse(member);
    }

    private MemberResponse fromMemberToMemberResponse(Member member) {
        MemberResponse memberResponse = new MemberResponse();
        memberResponse.setId(member.getId());
        memberResponse.setMemberNumber(member.getMemberNumber());
        memberResponse.setName(member.getName());
        memberResponse.setAddress(member.getAddress());
        return memberResponse;
    }

    private ArrayList<String> getNullFields(Object object) throws IllegalAccessException {
        ArrayList<String> nullFields = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field: fields) {
            field.setAccessible(true);
            if (field.get(object) == null) {
                nullFields.add(field.getName());
            }
        }
        return nullFields;
    }
}
