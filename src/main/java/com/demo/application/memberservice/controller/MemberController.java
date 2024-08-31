package com.demo.application.memberservice.controller;

import com.demo.application.memberservice.dto.MemberRequest;
import com.demo.application.memberservice.dto.MemberResponse;
import com.demo.application.memberservice.exception.ResourceNotFoundException;
import com.demo.application.memberservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createMember(@RequestBody MemberRequest memberRequest) {
        log.info("Member [{}] create request received.", memberRequest.getName());
        memberService.createMember(memberRequest);
        log.info("Member [{}] create request completed.", memberRequest.getName());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MemberResponse> getAllMembers() {
        log.info("Get all members request received.");
        return memberService.getAllMembers();
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable("id") Long id) {
        log.info("Get member by id [{}] request received.", id);
        try {
            MemberResponse memberResponse = memberService.getMemberById(id);
            log.info("Get member by id [{}] request completed.", id);
            return new ResponseEntity<>(memberResponse, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value="/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@PathVariable("id") Long id) {
        log.info("Delete member by id [{}] request received.", id);
        memberService.deleteMember(id);
    }

    @PatchMapping(value="/{id}")
    public ResponseEntity<MemberResponse> updateMember(@RequestBody MemberRequest memberRequest, @PathVariable("id") Long id) {
        log.info("Update member by id [{}] request received.", id);
        try {
            MemberResponse memberResponse = memberService.updateMember(memberRequest, id);
            log.info("Update member by id [{}] request completed.", id);
            return new ResponseEntity<>(memberResponse, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
