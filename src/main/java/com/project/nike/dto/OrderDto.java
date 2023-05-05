package com.project.nike.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private long id;
    private List<OrderItemDto> itemDtoList;
    private String address;
    private long voucherId;


//    private long totalPayment;
// private Date createdDate;
//    @JsonIgnore
//    private User user;
//    private String orderStatus;
}



