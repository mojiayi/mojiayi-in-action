package com.mojiayi.action.commission.service.impl;

import com.google.common.base.Preconditions;
import com.mojiayi.action.commission.domain.CommissionDetail;
import com.mojiayi.action.commission.integration.MemberApiClient;
import com.mojiayi.action.commission.service.ICommissionService;
import com.mojiayi.action.commission.service.mock.MockCommissionData;
import com.mojiayi.action.common.tool.response.CommonResp;
import com.mojiayi.action.member.response.MemberDetailResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mojiayi
 */
@Service
@Slf4j
public class CommissionServiceImpl implements ICommissionService {
    @Autowired
    private MemberApiClient memberServiceIntegration;

    @Override
    public CommissionDetail queryCommissionDetailByOrderId(Long orderId) {
        Preconditions.checkArgument(orderId != null && orderId > 0, "订单id不能为空且必须大于0");
        CommissionDetail commissionDetail = MockCommissionData.mockDataMap.get(orderId);
        if (commissionDetail == null) {
            return null;
        }
        CommonResp<MemberDetailResp> memberDetailResp = memberServiceIntegration.getDetailByMemberId(commissionDetail.getMemberId());
        String memberName = "-";
        if (memberDetailResp.isSuccess() && memberDetailResp.getData() != null) {
            memberName = memberDetailResp.getData().getMemberName();
        }
        commissionDetail.setMemberName(memberName);
        return commissionDetail;
    }
}
