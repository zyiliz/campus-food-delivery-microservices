package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.convert.AddressConvent;
import com.example.mapper.AddressMapper;
import com.example.pojo.DTO.AddressDTO;
import com.example.pojo.DTO.AddressUpdateDTO;
import com.example.pojo.VO.AddressVO;
import com.example.pojo.entity.Address;
import com.example.result.Result;
import com.example.service.AddressService;
import com.example.utils.UserContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressMapper addressMapper;

    private final AddressConvent addressConvent;


    @Transactional
    @Override
    public Result<String> insertAddress(AddressDTO addressDTO) {
        Long userId = UserContext.getId();
        log.info("【用户添加地址】用户id：{}",userId);
        LambdaQueryWrapper<Address> lqw = new LambdaQueryWrapper<Address>()
                .eq(Address::getUserId,userId);
        Long aLong = addressMapper.selectCount(lqw);
        if (aLong>=5){
            return Result.fail("地址数量不能超过五个！");
        }
        Address address = addressConvent.ToAddress(addressDTO);
        address.setUserId(userId);
        int insert = addressMapper.insert(address);
        if (insert>0){
            log.info("【用户添加地址】添加成功，用户id:{},新增地址：{}",userId,addressDTO.getDetail());
            return Result.success("添加地址成功！");
        }else {
            log.error("【用户添加地址】添加失败");
            return Result.fail("添加地址失败，请稍后再试！");
        }
    }

    @Override
    public Result<List<AddressVO>> getAddressList() {
        Long userId = UserContext.getId();
        LambdaQueryWrapper<Address> lqw = new LambdaQueryWrapper<Address>()
                .eq(Address::getUserId,userId);
        List<Address> addresses = addressMapper.selectList(lqw);
        List<AddressVO> addressVOS = addressConvent.toAddressVoList(addresses);
        return Result.success(addressVOS);
    }

    @Transactional
    @Override
    public Result<String> updateAddress(AddressUpdateDTO updateDTO) {
        Long userID = UserContext.getId();
        log.info("【用户修改地址】用户id：{},请求信息：{}",userID,updateDTO);
        if (updateDTO.getIsDefault() != null && updateDTO.getIsDefault() == 1){
            LambdaUpdateWrapper<Address> clearDefault = new LambdaUpdateWrapper<Address>()
                    .eq(Address::getUserId,userID)
                    .set(Address::getIsDefault,false);
            addressMapper.update(null,clearDefault);
            log.info("【用户修改地址】清空默认地址，用户id：{}",userID);
        }
        LambdaUpdateWrapper<Address> luw = new LambdaUpdateWrapper<Address>()
                .eq(Address::getId,updateDTO.getId())
                .eq(Address::getUserId,userID)
                .set(updateDTO.getConsignee()!= null,Address::getConsignee,updateDTO.getConsignee())
                .set(updateDTO.getSex() != null,Address::getSex,updateDTO.getSex())
                .set(updateDTO.getPhone() != null,Address::getPhone,updateDTO.getPhone())
                .set(updateDTO.getDetail() != null,Address::getDetail,updateDTO.getDetail() )
                .set(updateDTO.getLabel() != null,Address::getLabel,updateDTO.getLabel())
                .set(updateDTO.getIsDefault() != null,Address::getIsDefault,updateDTO.getIsDefault());

        int update = addressMapper.update(null,luw);
        if (update>0){
            log.info("【用户修改地址】修改成功！用户id：{},修改后地址：{}",userID,updateDTO.getDetail());
            return Result.success("更新成功！");
        }else {
            log.error("【用户修改地址】修改失败！");
            return Result.fail("更新失败，请稍后再试！");
        }

    }

    @Override
    public Result<String> deleteAddress(Long id) {
        Long userId = UserContext.getId();
        log.info("【删除地址】用户id：{}",userId);
        LambdaQueryWrapper<Address> lqw = new LambdaQueryWrapper<Address>()
                .eq(Address::getUserId,userId)
                .eq(Address::getId,id);
        int delete = addressMapper.delete(lqw);
        if (delete>0){
            log.info("【删除地址】删除成功,用户id：{}",userId);
            return Result.success("删除成功");
        }else {
            log.error("【删除地址】删除失败,用户id：{}",userId);
            return Result.fail("删除失败，请稍后再试！");
        }
    }

    @Override
    public Result<AddressVO> getAddressById(Long id) {
        LambdaQueryWrapper<Address> lqw = new LambdaQueryWrapper<Address>()
                .eq(Address::getId,id);
        Address address = addressMapper.selectOne(lqw);
        if (address != null){
            AddressVO addressVO = addressConvent.toAddressVo(address);
            return Result.success(addressVO);
        }
        return null;
    }

    @Override
    public Result<AddressVO> getDefaultAddress() {
        Long userId = UserContext.getId();
        LambdaQueryWrapper<Address> lqw = new LambdaQueryWrapper<Address>()
                .eq(Address::getUserId,userId)
                .eq(Address::getIsDefault,1);
        Address address = addressMapper.selectOne(lqw);
        AddressVO addressVo = addressConvent.toAddressVo(address);
        return Result.success(addressVo);
    }
}
