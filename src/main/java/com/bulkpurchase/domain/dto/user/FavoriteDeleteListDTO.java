package com.bulkpurchase.domain.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FavoriteDeleteListDTO {

    private List<Long> productIDs;
}
