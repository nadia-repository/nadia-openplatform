package com.nadia.openplatfrom.isv.manage.service.impl;

import com.nadia.openplatfrom.isv.account.domain.IsvInfo;
import com.nadia.openplatfrom.isv.account.service.AccountService;
import com.nadia.openplatfrom.isv.manage.dao.MenuInfoMapper;
import com.nadia.openplatfrom.isv.manage.dao.RoleMenuMapper;
import com.nadia.openplatfrom.isv.manage.domain.MenuInfo;
import com.nadia.openplatfrom.isv.manage.domain.RoleMenu;
import com.nadia.openplatfrom.isv.manage.domain.criteria.MenuInfoCriteria;
import com.nadia.openplatfrom.isv.manage.domain.criteria.RoleMenuCriteria;
import com.nadia.openplatfrom.isv.manage.dto.response.MenuResponse;
import com.nadia.openplatfrom.isv.manage.dto.response.Menus;
import com.nadia.openplatfrom.isv.manage.exception.MenuInfoException;
import com.nadia.openplatfrom.isv.manage.service.MenuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MenuInfoServiceImpl implements MenuInfoService {
    @Resource
    private AccountService accountService;
    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private MenuInfoMapper menuInfoMapper;

    @Override
    public MenuResponse getMenusByIsvId(Long isvId) {
        IsvInfo isvInfo = accountService.getIsvById(isvId);
        if(isvInfo == null){
            throw new MenuInfoException(1009L);
        }
        Long roleId = isvInfo.getRoleId();

        RoleMenuCriteria roleMenuExample = new RoleMenuCriteria();
        roleMenuExample.createCriteria().andRoleIdEqualTo(roleId);
        List<RoleMenu> roleMenus = roleMenuMapper.selectByExample(roleMenuExample);

        List<Long> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());

        if(CollectionUtils.isEmpty(menuIds)){
            throw new MenuInfoException(1010L);
        }

        MenuInfoCriteria menuInfoExample = new MenuInfoCriteria();
        menuInfoExample.createCriteria().andIdIn(menuIds);
        List<MenuInfo> menuInfos = menuInfoMapper.selectByExample(menuInfoExample);

        MenuResponse response = new MenuResponse();
        Map<Long, Menus> MenuMap = new HashMap<>();
        Map<Long, List<Menus>> childrenMenuMap = new HashMap<>();
        menuInfos.forEach(menu ->{
            Menus m = new Menus();
            BeanUtils.copyProperties(menu,m);
            if(menu.getParentId() == 0){
                MenuMap.put(menu.getId(),m);
            }else {
                List<Menus> menus = childrenMenuMap.get(menu.getParentId());
                if(menus==null){
                    menus = new LinkedList<Menus>();
                }
                menus.add(m);
                childrenMenuMap.put(menu.getParentId(), menus);
            }
        });

        MenuMap.values().forEach(menu ->{
            menu.setChildren(childrenMenuMap.get(menu.getId()));
        });

        List<Menus> menus = MenuMap.values().stream().sorted(Comparator.comparing(Menus::getId)).collect(Collectors.toList());
        response.setMenus(menus);
        return response;
    }
}
