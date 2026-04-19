package springallinone.practice.jpa.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springallinone.practice.jpa.dto.MemberCreateReq;
import springallinone.practice.jpa.entity.Member;
import springallinone.practice.jpa.service.MemberService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> registerMember(@Valid MemberCreateReq req) {
        Long id = memberService.register(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMember(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMember(id));
    }

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @PatchMapping("/{id}/update-name")
    public ResponseEntity<Void> updateName(@PathVariable Long id, @RequestParam String name) {
        memberService.updateName(id, name);
        return ResponseEntity.noContent().build();
    }


}
