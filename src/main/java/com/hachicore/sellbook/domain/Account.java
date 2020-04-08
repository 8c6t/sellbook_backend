package com.hachicore.sellbook.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOUNT_ID")
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    @OneToOne(mappedBy = "account")
    private Storage storage;

    // 연관관계 매핑용 메소드
    public void linkStorage(Storage storage) {
        this.storage = storage;
    }

}
