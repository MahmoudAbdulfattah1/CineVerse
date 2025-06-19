package com.cineverse.cineverse.controller;

import com.cineverse.cineverse.domain.entity.*;
import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.dto.ContentMetaDataDto;
import com.cineverse.cineverse.dto.CrewMemberDto;
import com.cineverse.cineverse.dto.CrewMemberSocialDto;
import com.cineverse.cineverse.mapper.ContentMetaDataMapper;
import com.cineverse.cineverse.mapper.CrewMemberMapper;
import com.cineverse.cineverse.mapper.CrewMemberSocialMapper;
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
    private CrewMemberSocialMapper crewMemberMapperSocial;
    private ContentMetaDataMapper contentMetaDataMapper;

    @GetMapping("/{crewMemberId}")
    public ResponseEntity<CrewMemberDto> getCrewMemberDetails(@PathVariable int crewMemberId) {
        CrewMember crewMember = crewMemberService.getCrewMemberDetails(crewMemberId);
        CrewMemberDto crewMemberDto = crewMemberMapper.toDto(crewMember);
        return ResponseEntity.ok(crewMemberDto);
    }

    @GetMapping("/{crewMemberId}/social")
    public ResponseEntity<CrewMemberSocialDto> getCrewMemberSocial(@PathVariable int crewMemberId) {
        CrewMemberSocialDto crewMemberSocialDto =
                crewMemberMapperSocial.toDto(crewMemberService.getCrewMemberSocial(crewMemberId));
        return ResponseEntity.ok(crewMemberSocialDto);
    }

    @GetMapping("/{crewMemberId}/contents")
    public ResponseEntity<Page<ContentMetaDataDto>> getCrewContent(
            @PathVariable int crewMemberId,
            @RequestParam(required = false) ContentType type,
            @PageableDefault(size = 5, sort = "releaseDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Content> contentPage = crewMemberService.getCrewMemberContents(crewMemberId, type, pageable);
        Page<ContentMetaDataDto> dtoPage = contentMetaDataMapper.toDto(contentPage);
        return ResponseEntity.ok(dtoPage);
    }

}
