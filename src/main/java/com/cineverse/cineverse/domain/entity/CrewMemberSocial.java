package com.cineverse.cineverse.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CrewMemberSocial {
    @Id
    private Long crewMemberId;
    @OneToOne
    @MapsId
    private CrewMember crewMember;
    private String facebookId;
    private String instagramId;
    private String tiktokId;
    private String twitterId;
    private String youtubeId;

    public CrewMemberSocial(CrewMember crewMember, String facebookId, String instagramId, String tiktokId,
                            String twitterId, String youtubeId) {
        this.crewMember = crewMember;
        this.facebookId = facebookId;
        this.instagramId = instagramId;
        this.tiktokId = tiktokId;
        this.twitterId = twitterId;
        this.youtubeId = youtubeId;
    }
}
