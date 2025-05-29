package com.jzo2o.customer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.ObjectUtils;
import com.jzo2o.customer.mapper.BankAccountMapper;
import com.jzo2o.customer.model.domain.BankAccount;
import com.jzo2o.customer.model.dto.request.BankAccountUpsertReqDTO;
import com.jzo2o.customer.model.dto.response.BankAccountResDTO;
import com.jzo2o.customer.service.IBankAccountService;
import com.jzo2o.mvc.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BankAccountServiceImpl extends ServiceImpl<BankAccountMapper, BankAccount> implements IBankAccountService {

    @Override
    public void upsert(BankAccountUpsertReqDTO bankAccountUpsertReqDTO) {
        if(ObjectUtils.isNull(bankAccountUpsertReqDTO.getId()) || ObjectUtils.isNull(bankAccountUpsertReqDTO.getType())){
            bankAccountUpsertReqDTO.setId(UserContext.currentUserId());
            bankAccountUpsertReqDTO.setType(UserContext.currentUser().getUserType());
            log.info("新增{}类型的用户{}",UserContext.currentUser().getUserType(),UserContext.currentUserId());
        }
        BankAccount bankAccount = BeanUtils.toBean(bankAccountUpsertReqDTO, BankAccount.class);
        super.saveOrUpdate(bankAccount);
    }

    @Override
    public BankAccountResDTO getBankAccount(Long userId) {
        BankAccount bankAccount = baseMapper.selectById(userId);
        return BeanUtils.toBean(bankAccount, BankAccountResDTO.class);
    }
}
