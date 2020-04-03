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

    @OneToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @OneToMany(mappedBy = "storage")
    private List<StorageBook> storageBooks = new ArrayList<>();

}
