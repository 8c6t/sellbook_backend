package com.hachicore.sellbook.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class Storage {

    @Id @GeneratedValue
    @Column(name = "STORAGE_ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @Builder.Default
    @OneToMany(mappedBy = "storage")
    private List<StorageBook> storageBooks = new ArrayList<>();

    public void addAccount(Account account) {
        this.account = account;
        account.linkStorage(this);
    }

}
