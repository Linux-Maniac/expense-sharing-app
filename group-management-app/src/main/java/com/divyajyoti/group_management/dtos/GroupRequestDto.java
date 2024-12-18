package com.divyajyoti.group_management.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupRequestDto {

    private String name;

    private List<UserDto> members;

    private String createdByContact;

}
