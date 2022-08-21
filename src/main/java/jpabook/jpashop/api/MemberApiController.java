package jpabook.jpashop.api;

import jpabook.jpashop.Service.MemberService;
import jpabook.jpashop.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /** Entity를 여기까지 가져와서 사용하지 않기
     * '@RequestBody @Valid Member member'를 파라미터에 넣으면
     * Json 타입의 데이터를 member 객체에 다 넣어준다.
     *
     * :: Entity 객체라서 이렇게 하지말고 별도의 DTO를 만들어주기 ::
     *
     * v2 참고! ++ 이렇게 해야지 안전함.
     */

    /** DTO를 만들 때 설계방법
     * API 스펙에 맞는 DTO를 만들어서 사용해라.
     * 예) DTO기준으로 name에 NotEmpty(필수값)를 해야한다면 Entity에 말고
     * DTO에 설정하는게 좋다.
     * 왜냐? Entity를 다른 곳에서 사용할 때 name이 필수값이 아닐수도 있기 때문에!!
     */

    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members") //PostMan으로 실행..
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();

        // List<Member>를 List<MemberDto>로 바꿈.
        // 이렇게 하는이유는 Json배열 타입으로 나가면 유연성이 떨어져서 Json Object타입으로 개선.
        List<memberDto> collect = findMembers.stream().map(m -> new memberDto(m.getName())).collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }
    // 절대 절대 선택이 아닌 필수! : Entity를 직접 부르지 말고, API스펙에 맞춘 DTO를 만들어서 사용해라.
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T date;
    }

    @Data
    @AllArgsConstructor
    static class memberDto {
        private String name;
    }



    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMeberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * PutMappring는 RestAPI.. Update..
     */
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberRespones updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberRespones(findMember.getId(), findMember.getName());
    }

    // ====================================================================

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberRespones {
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

}
