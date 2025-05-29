package com.jzo2o.customer.controller.worker;

import com.jzo2o.customer.model.dto.request.BankAccountUpsertReqDTO;
import com.jzo2o.customer.model.dto.response.BankAccountResDTO;
import com.jzo2o.customer.service.IBankAccountService;
import com.jzo2o.mvc.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController("bankAccountController")
@RequestMapping("/worker/bank-account")
@Api(value = "服务端银行账户信息")
public class BankAccountController {

    @Resource
    private IBankAccountService bankAccountService;

    @PostMapping
    @ApiOperation("新增或更新银行账号信息")
    public void add(@RequestBody @Validated BankAccountUpsertReqDTO bankAccountUpsertReqDTO) {
        bankAccountService.upsert(bankAccountUpsertReqDTO);
    }

    @GetMapping("/currentUserBankAccount")
    @ApiOperation("获取当前用户银行账号")
    public BankAccountResDTO getBankAccount() {
        return bankAccountService.getBankAccount(UserContext.currentUserId());
    }

}
