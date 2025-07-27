package com.cineverse.cineverse.controller;

import com.cineverse.cineverse.domain.entity.*;
import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.dto.ApiResponse;
import com.cineverse.cineverse.dto.content.ContentMetaDataDto;
import com.cineverse.cineverse.dto.crew.CrewMemberDto;
import com.cineverse.cineverse.dto.crew.CrewMemberSocialDto;
import com.cineverse.cineverse.mapper.ContentMapper;
import com.cineverse.cineverse.mapper.CrewMemberMapper;
import com.cineverse.cineverse.service.CrewMemberService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/artists")
@AllArgsConstructor
public class ArtistController {
    private CrewMemberService crewMemberService;
    private CrewMemberMapper crewMemberMapper;
    private ContentMapper contentMapper;

    @GetMapping("/{crewMemberId}")
    public ResponseEntity<ApiResponse> getCrewMemberDetails(@PathVariable int crewMemberId) {
        CrewMember crewMember = crewMemberService.getCrewMemberDetails(crewMemberId);
        CrewMemberDto crewMemberDto = crewMemberMapper.toCrewMemberDto(crewMember);
        return ResponseEntity.ok(
                ApiResponse.success(crewMemberDto, "Crew member details fetched successfully")
        );
    }

    @GetMapping("/{crewMemberId}/social")
    public ResponseEntity<ApiResponse> getCrewMemberSocial(@PathVariable int crewMemberId) {
        CrewMemberSocialDto crewMemberSocialDto =
                crewMemberMapper.toCrewMemberSocialDto(crewMemberService.getCrewMemberSocial(crewMemberId));
        return ResponseEntity.ok(
                ApiResponse.success(crewMemberSocialDto, "Crew member social links fetched successfully")
        );
    }

    @GetMapping("/{crewMemberId}/contents")
    public ResponseEntity<ApiResponse> getCrewContent(
            @PathVariable int crewMemberId,
            @RequestParam(required = false) ContentType type,
            @PageableDefault(size = 5, sort = "releaseDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Content> contentPage = crewMemberService.getCrewMemberContents(crewMemberId, type, pageable);
        Page<ContentMetaDataDto> contentDtoPage = contentMapper.toContentMetaDataDto(contentPage);
        return ResponseEntity.ok(
                ApiResponse.success(contentDtoPage, "Crew member contents fetched successfully")
        );
    }

}
