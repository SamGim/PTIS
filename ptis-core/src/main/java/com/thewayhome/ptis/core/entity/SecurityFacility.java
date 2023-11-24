package com.thewayhome.ptis.core.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "security_facility")
public class SecurityFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    // 사설 경비원
    @Column(name = "private_security_guard", columnDefinition = "boolean default false")
    private Boolean privateSecurityGuard;

    // CCTV
    @Column(name = "cctv", columnDefinition = "boolean default false")
    private Boolean cctv;

    // 공동현관보안
    @Column(name = "entrance_security", columnDefinition = "boolean default false")
    private Boolean entranceSecurity;

    // 경비원
    @Column(name = "security_guard", columnDefinition = "boolean default false")
    private Boolean securityGuard;

    // 비디어폰
    @Column(name = "video_phone", columnDefinition = "boolean default false")
    private Boolean videoPhone;

    // 인터폰
    @Column(name = "inter_Phone", columnDefinition = "boolean default false")
    private Boolean interPhone;

    // 원룸도어락
    @Column(name = "one_room_door_lock", columnDefinition = "boolean default false")
    private Boolean oneRoomDoorLock;

    // 창문 방법창
    @Column(name = "window_method_window", columnDefinition = "boolean default false")
    private Boolean windowMethodWindow;

}
