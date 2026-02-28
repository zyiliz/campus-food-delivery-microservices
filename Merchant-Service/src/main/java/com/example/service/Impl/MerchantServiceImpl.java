package com.example.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.entity.pojo.Merchant;
import com.example.entity.request.MerchantRequest;
import com.example.entity.request.MerchantUpdateRequest;
import com.example.mapper.MerchantMapper;
import com.example.result.DataRespond;
import com.example.result.DataSuccessRespond;
import com.example.result.MsgRespond;
import com.example.service.MerchantService;
import com.example.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantMapper merchantMapper;

    @Transactional
    @Override
    public MsgRespond insertMerchant(MerchantRequest merchantRequest) {
        LambdaQueryWrapper<Merchant> lqw = new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getName,merchantRequest.getName())
                .eq(Merchant::getPhone,merchantRequest.getPhone());
        Merchant merchant = merchantMapper.selectOne(lqw);
        if (ObjectUtil.isNotNull(merchant)){
            return MsgRespond.fail("该商家已经存在！");
        }
        merchant = new Merchant();
        BeanUtil.copyProperties(merchantRequest,merchant);
        merchant.setStatus("关店中！");
        int insert = merchantMapper.insert(merchant);
        return insert>0?MsgRespond.success("创建成功！")
                :MsgRespond.fail("创建失败！");
    }
    @Transactional
    @Override
    public MsgRespond updateMerchant(MerchantUpdateRequest merchantUpdateRequest) {
        LambdaQueryWrapper<Merchant> lqw = new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getMerchantId,merchantUpdateRequest.getMerchantId());
        boolean exists = merchantMapper.exists(lqw);
        if (!exists){
            return MsgRespond.fail("该商店不存在");
        }
        LambdaUpdateWrapper<Merchant> luw = new LambdaUpdateWrapper<Merchant>()
                .eq(Merchant::getMerchantId,merchantUpdateRequest.getMerchantId());

        if (merchantUpdateRequest.getName() != null){
            luw.set(Merchant::getName,merchantUpdateRequest.getName());
        }
        if (merchantUpdateRequest.getAddress() != null){
            luw.set(Merchant::getAddress,merchantUpdateRequest.getAddress());
        }
        if (merchantUpdateRequest.getPhone() != null){
            luw.set(Merchant::getPhone,merchantUpdateRequest.getPhone());
        }
        int update = merchantMapper.update(luw);
        return update>0?MsgRespond.success("更新成功！")
                :MsgRespond.fail("更新失败！");
    }

    @Override
    public DataRespond MerchantExist(String id) {
        LambdaQueryWrapper<Merchant> lqw = new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getMerchantId,id);
        boolean exist = merchantMapper.exists(lqw);
        return new DataSuccessRespond("获取成功！",exist);
    }
}
