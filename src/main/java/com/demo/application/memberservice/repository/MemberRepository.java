package com.demo.application.memberservice.repository;

import com.demo.application.memberservice.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
