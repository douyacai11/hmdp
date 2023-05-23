package com.hmdp.service.impl;

import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */

@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

/** 给shoptype的查询添加缓存*/
    @Override
    public Result queryList() {

        //1.从缓存中查找需要查询的list集合
        String key= RedisConstants.CACHE_TYPE_KEY+"list";
        String typeJson = stringRedisTemplate.opsForValue().get(key);

        //2.若缓存命中,则直接返回
        if (typeJson !=null){
            List<ShopType> shopTypeList = JSONUtil.toList(typeJson, ShopType.class);
            return Result.ok(shopTypeList);
        }
        //2.1缓存没命中,需要查找数据库
        List<ShopType> list = query().orderByAsc("sort").list();

        //3若查询到的list不存在 直接返回错误信息
        if (list==null){
            return Result.fail("分类不存在！");
        }

        //3.1若查询到的list存在,存入redis缓存中 并且返回给前端
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(list));
        return Result.ok(list);
    }



}
