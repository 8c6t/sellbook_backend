package com.hachicore.sellbook.dto;

import com.hachicore.sellbook.domain.Account;
import lombok.Data;

@Data
public class AccountDto {

    private Long id;
    private String email;
    private String nickname;

    public AccountDto(Account account) {
        this.id = account.getId();
        this.email = account.getEmail();
        this.nickname = account.getNickname();
    }
}
