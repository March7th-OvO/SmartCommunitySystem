package com.neusoft.smartcommunity.community.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.community.entity.CommPropertyBill;
import com.neusoft.smartcommunity.community.service.CommPropertyBillService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/** 物业费：用户我的账单/缴纳 + 管理端查询 */
@RestController
@RequestMapping("/community/property-bill")
public class PropertyBillController {

    private final CommPropertyBillService propertyBillService;

    public PropertyBillController(CommPropertyBillService propertyBillService) {
        this.propertyBillService = propertyBillService;
    }

    private Long getUserId(HttpServletRequest request) {
        String id = request.getHeader("X-User-Id");
        if (id == null) throw new BusinessException(ResultCode.UNAUTHORIZED, "未登录");
        return Long.parseLong(id);
    }

    @GetMapping("/my")
    public Result<Page<CommPropertyBill>> my(HttpServletRequest request,
                                            @RequestParam(defaultValue = "1") int pageNum,
                                            @RequestParam(defaultValue = "10") int pageSize,
                                            @RequestParam(required = false) Integer status) {
        return Result.success(propertyBillService.pageByUser(getUserId(request), pageNum, pageSize, status));
    }

    @PostMapping("/{billId}/pay")
    public Result<Void> pay(HttpServletRequest request, @PathVariable Long billId) {
        propertyBillService.pay(getUserId(request), billId);
        return Result.success();
    }

    @PostMapping("/admin")
    public Result<Void> create(@RequestBody CommPropertyBill bill) {
        propertyBillService.createBill(bill);
        return Result.success();
    }

    @GetMapping("/admin/page")
    public Result<Page<CommPropertyBill>> pageAdmin(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long communityId,
            @RequestParam(required = false) String billPeriod,
            @RequestParam(required = false) Integer status) {
        return Result.success(propertyBillService.pageAdmin(pageNum, pageSize, communityId, billPeriod, status));
    }
}
