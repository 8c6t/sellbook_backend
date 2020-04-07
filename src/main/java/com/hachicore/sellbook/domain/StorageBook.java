package com.hachicore.sellbook.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class StorageBook {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORAGE_ID")
    private Storage storage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID")
    private Book book;

}
