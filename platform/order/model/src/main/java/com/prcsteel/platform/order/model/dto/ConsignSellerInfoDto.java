package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.account.model.dto.DepartmentDto;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;

import java.util.List;

public class ConsignSellerInfoDto extends ConsignOrderItems{
    private List<DepartmentDto> departments;

    public ConsignSellerInfoDto() {
    }

    public ConsignSellerInfoDto(ConsignOrderItems items) {
        super(items);
    }

    public List<DepartmentDto> getDepartments() {
        return departments;
    }

    public void setDepartments(List<DepartmentDto> departments) {
        this.departments = departments;
    }
}