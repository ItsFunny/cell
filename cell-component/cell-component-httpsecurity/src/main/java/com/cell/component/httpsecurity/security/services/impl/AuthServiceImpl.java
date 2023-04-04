package com.cell.component.httpsecurity.security.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cell.base.common.utils.CollectionUtils;
import com.cell.base.common.utils.StringUtils;
import com.cell.sdk.log.LOG;
import com.mi.wallet.mange.common.utils.CommonUtil;
import com.mi.wallet.mange.context.*;
import com.mi.wallet.mange.entity.*;
import com.mi.wallet.mange.mapper.ManualDao;
import com.mi.wallet.mange.request.AuthGetAdminTypeRequest;
import com.mi.wallet.mange.request.GetUserMenuRequest;
import com.mi.wallet.mange.response.AuthGetAdminTypeResponse;
import com.mi.wallet.mange.response.GetUserMenuResponse;
import com.mi.wallet.mange.security.IAuthService;
import com.mi.wallet.mange.security.annotation.AuthAnnotation;
import com.mi.wallet.mange.security.constants.SecurityConstants;
import com.mi.wallet.mange.security.impl.SecurityUser;
import com.mi.wallet.mange.security.models.*;
import com.mi.wallet.mange.security.services.IEncryptService;
import com.mi.wallet.mange.security.utils.SecurityUtils;
import com.mi.wallet.mange.service.*;
import com.mi.wallet.mange.vo.AdminTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements IAuthService {

    @Override
    public GetUserAuthRespBO getUserAuth(CellContext context, GetUserAuthReqBO req) {
        GetUserAuthRespBO ret = new GetUserAuthRespBO();
        AuthBatchGetUserAuthReqBO reqBO = new AuthBatchGetUserAuthReqBO();
        reqBO.setUserIdList(Arrays.asList(req.getUserId()));
        reqBO.setDataPermission(req.isDataPermission());
        reqBO.setOperationPermission(req.isOperationPermission());
        AuthBatchGetUserAuthRespBO authBatchGetUserAuthRespBO = this.batchGetUserAuth(context, reqBO);
        if (CollectionUtils.isNotEmpty(authBatchGetUserAuthRespBO.getItems())) {
            ret.setItem(authBatchGetUserAuthRespBO.getItems().get(0));
        }
        return ret;
    }

    @Autowired
    private ManualDao manualDao;

    @Override
    public GetUserMenuResponse getUserMenus(CellContext context, GetUserMenuRequest req) {
        GetUserMenuResponse ret = new GetUserMenuResponse();
        SecurityUser user = SecurityUtils.getCurrentUser(context);
        List<Integer> roleIds = null;
        if (user.isSuperAdmin()) {
            List<SecurityRole> roles = securityRoleService.list();
            if (CollectionUtils.isNotEmpty(roles)) {
                roleIds = roles.stream().map(r -> r.getRoleId()).collect(Collectors.toList());
            }
        } else {
            // 1. 寻找用户所拥有的角色 + 用户所绑定的组所拥有的角色
            roleIds = manualDao.getUserRoleIds(Integer.parseInt(user.getUserId()));
        }
        if (CollectionUtils.isEmpty(roleIds)) {
            return ret;
        }
        List<SecurityRolePermission> permissionListByRoleIdList = this.getPermissionListByRoleIdList(context, roleIds);
        // 3. 根据permissionId 查询所有的 数据权限+操作权限
        Collection<Integer> permissionIdList = new HashSet<>();
        permissionListByRoleIdList.forEach(p ->
                permissionIdList.add(p.getPermissionId()));
        ret.setItems(this.getMenuResource(context, permissionIdList));
        return ret;
    }

    @Autowired
    private SecurityUserInfoService securityUserInfoService;

    public AuthBatchGetUserAuthRespBO batchGetUserAuth(CellContext context, AuthBatchGetUserAuthReqBO req) {
        AuthBatchGetUserAuthRespBO ret = null;
        List<Integer> userIdList = req.getUserIdList();

        Collection<SecurityUserInfo> users = securityUserInfoService.listByIds(userIdList);
        if (CollectionUtils.isEmpty(users) || users.size() != userIdList.size()) {
            throw new WrapContextException(context, new BusinessException(ErrorConstant.RECORD_NOT_EXISTS));
        }
        Map<Integer, UserNode> nodeMap = users.stream().collect(Collectors.toMap(p -> p.getUserId(), v ->
        {
            UserNode node = new UserNode();
            node.setUserId(v.getUserId());
            return node;
        }));
        ret = new AuthBatchGetUserAuthRespBO();
        ret.setItems(new ArrayList<>(nodeMap.values()));

        Map<Integer, Set<Integer>> userRoleIdList = this.getUserRoleIdList(context, userIdList);
        if (userRoleIdList.isEmpty()) {
            return ret;
        }

        Set<Integer> roleIdSet = new HashSet<>();
        // 建立roleId-> userNode 的映射
        Map<Integer, List<UserNode>> roleUserNodeMap = new HashMap<>();

        for (Integer userId : userRoleIdList.keySet()) {
            Set<Integer> userRoleIdSet = userRoleIdList.get(userId);
            roleIdSet.addAll(userRoleIdSet);
            UserNode node = nodeMap.get(userId);
            for (Integer roleId : userRoleIdSet) {
                List<UserNode> userNodes = roleUserNodeMap.get(roleId);
                if (CollectionUtils.isEmpty(userNodes)) {
                    userNodes = new ArrayList<>();
                    roleUserNodeMap.put(roleId, userNodes);
                }
                userNodes.add(node);
            }
        }

        // 4. 查询角色及其权限信息
        GetRoleReqBO getRoleReqBO = new GetRoleReqBO();
        getRoleReqBO.setRoleIdList(roleIdSet);
        getRoleReqBO.setDataPermission(req.isDataPermission());
        getRoleReqBO.setOperationPermission(req.isOperationPermission());
        GetRoleRespBO roles = this.getRoles(context, getRoleReqBO);
        Collection<RoleNode> items = roles.getItems();
        for (RoleNode item : items) {
            List<UserNode> userNodes = roleUserNodeMap.get(item.getRoleId());
            if (CollectionUtils.isEmpty(userNodes)) {
                continue;
            }
            for (UserNode usernode : userNodes) {
                if (CollectionUtils.isEmpty(usernode.getItems())) {
                    usernode.setItems(new ArrayList<>());
                }
                usernode.getItems().add(item);
            }
        }
        return ret;
    }

    @Autowired
    private SecurityUserGroupService securityUserGroupService;
    @Autowired
    private SecurityGroupRoleService groupRoleService;
    @Autowired
    private SecurityUserRoleService securityUserRoleService;

    private Map<Integer, Set<Integer>> getUserRoleIdList(CellContext context, List<Integer> userIdList) {
        Map<Integer, Set<Integer>> ret = new HashMap<>();
        // 2. 查询用户所属的组织
        QueryWrapper<SecurityUserGroup> SecurityUserGroupQueryWrapper = new QueryWrapper<>();
        SecurityUserGroupQueryWrapper.in("user_id", userIdList);
        List<SecurityUserGroup> groups = securityUserGroupService.list(SecurityUserGroupQueryWrapper);
        if (CollectionUtils.isNotEmpty(groups)) {
            Map<Integer, Integer> userGroupMap = new HashMap<>();
            List<Integer> groupIdList = new ArrayList<>();
            groups.stream().forEach(p ->
            {
                userGroupMap.put(p.getGroupId(), p.getUserId());
                groupIdList.add(p.getGroupId());
            });
            QueryWrapper<SecurityGroupRole> SecurityGroupRoleQueryWrapper = new QueryWrapper<>();
            // 根据组织id查询角色信息
            SecurityGroupRoleQueryWrapper.in("group_id", groupIdList);
            List<SecurityGroupRole> groupRoles = groupRoleService.list(SecurityGroupRoleQueryWrapper);
            groupRoles.stream().forEach(g ->
            {
                Integer userId = userGroupMap.get(g.getGroupId());
                Set<Integer> Integers = ret.get(userId);
                if (CollectionUtils.isEmpty(Integers)) {
                    Integers = new HashSet<>();
                    ret.put(userId, Integers);
                }
                Integers.add(g.getRoleId());
            });
        }

        // 3. 查询用户所绑定的角色
        QueryWrapper<SecurityUserRole> SecurityUserRoleQueryWrapper = new QueryWrapper<>();
        SecurityUserRoleQueryWrapper.in("user_id", userIdList);
        List<SecurityUserRole> userRoles = securityUserRoleService.list(SecurityUserRoleQueryWrapper);
        userRoles.stream().forEach(u ->
        {
            Set<Integer> Integers = ret.get(u.getUserId());
            if (CollectionUtils.isEmpty(Integers)) {
                Integers = new HashSet<>();
                ret.put(u.getUserId(), Integers);
            }
            Integers.add(u.getRoleId());
        });
        return ret;
    }

    @Autowired
    private SecurityRoleService securityRoleService;

    @Override
    public GetRoleRespBO getRoles(CellContext context, GetRoleReqBO req) {
        GetRoleRespBO ret = new GetRoleRespBO();
        // 1. 查询全部role
        QueryWrapper<SecurityRole> roleQueryWrapper = new QueryWrapper<>();

        if (req != null && CollectionUtils.isNotEmpty(req.getRoleIdList())) {
            roleQueryWrapper.in("role_id", req.getRoleIdList());
        }
        roleQueryWrapper.orderByAsc("parent_role_id");
        List<SecurityRole> roles = securityRoleService.list(roleQueryWrapper);
        if (CollectionUtils.isEmpty(roles)) {
            return ret;
        }
        List<Integer> roleIdList = new ArrayList<>();
        Map<Integer, RoleNode> roleNodeMap = new HashMap<>();
        List<RoleNode> parentRoles = new ArrayList<>();
        roles.stream().forEach(r ->
        {
            roleIdList.add(r.getRoleId());
            RoleNode node = new RoleNode();
            node.setRoleDesc(r.getRoleDesc());
            node.setRoleName(r.getRoleName());
            node.setRoleId(r.getRoleId());
            node.setMenuItems(new ArrayList<>());
            if (r.getParentRoleId() > 0) {
                RoleNode parent = roleNodeMap.get(r.getParentRoleId());
                if (parent != null) {
                    parent.addChildRole(node);
                } else {
                    LOG.erroring(ModuleEnums.PUFF, "野生的角色:{}", r);
                }
            } else {
                parentRoles.add(node);
            }
            roleNodeMap.put(r.getRoleId(), node);
        });
        ret.setItems(parentRoles);

        if (req == null || !req.isDataPermission()) {
            return ret;
        }

        // 2. 根据roleId查询其有的所有权限
        List<SecurityRolePermission> rolePermissionList = this.getPermissionListByRoleIdList(context, roleIdList);
        if (CollectionUtils.isEmpty(rolePermissionList)) {
            return ret;
        }
        // 3. 根据permissionId 查询所有的 数据权限+操作权限
        Map<Integer, RoleNode> permissionRoleMap = new HashMap<>();
        Collection<Integer> permissionIdList = new HashSet<>();
        rolePermissionList.stream().forEach(p ->
        {
            permissionIdList.add(p.getPermissionId());
            if (roleNodeMap.containsKey(p.getRoleId())) {
                permissionRoleMap.put(p.getPermissionId(), roleNodeMap.get(p.getRoleId()));
            }
        });

        GetMenuAuthReqBO getMenuAuthReqBO = new GetMenuAuthReqBO();
        getMenuAuthReqBO.setPermissionIdList(permissionIdList);
        getMenuAuthReqBO.setOperationPermission(req.isOperationPermission());
        GetMenuAuthRespBO menuAuthInfo = this.getMenuAuthInfo(context, getMenuAuthReqBO);
        Collection<MenuNode> items = menuAuthInfo.getItems();
        items.stream().forEach(r ->
        {
            RoleNode roleNode = permissionRoleMap.get(r.getPermissionId());
            if (roleNode != null) {
                roleNode.addMenu(r);
            }
        });

        return ret;
    }

    @Autowired
    private SecurityRolePermissionService securityRolePermissionService;

    private List<SecurityRolePermission> getPermissionListByRoleIdList(CellContext context, Collection<Integer> roleIdList) {
        QueryWrapper<SecurityRolePermission> rolePermissionQueryWrapper = new QueryWrapper<>();
        rolePermissionQueryWrapper.in("role_id", roleIdList);
        return securityRolePermissionService.list(rolePermissionQueryWrapper);
    }

    @Autowired
    private SecurityPermissionService securityPermissionService;
    @Autowired
    private SecurityMenuResourceService securityMenuResourceService;

    public Collection<MenuNode> getMenuResource(CellContext context, Collection<Integer> permissionIdList) {
        QueryWrapper<SecurityPermission> permissionQueryWrapper = new QueryWrapper<>();
        if (CollectionUtils.isNotEmpty(permissionIdList)) {
            permissionQueryWrapper.in("permission_id", permissionIdList);
        }
        List<SecurityPermission> permissions = securityPermissionService.list(permissionQueryWrapper);
        Map<String, List<SecurityPermission>> permissionCatagories = CommonUtil.groupBy(permissions, SecurityPermission::getPermissionType);
        List<SecurityPermission> resources = permissionCatagories.get(SecurityConstants.DB_PERMISSION_TYPE_MENU);
        if (CollectionUtils.isEmpty(resources)) {
            return null;
        }
        // 通过permissionId 获取得到数据权限(资源)
        List<Integer> resourcePermissionIdList = resources.stream().map(SecurityPermission::getPermissionId).collect(Collectors.toList());
        QueryWrapper<SecurityMenuResource> resourceQueryWrapper = new QueryWrapper<>();
        resourceQueryWrapper.in("permission_id", resourcePermissionIdList);
        resourceQueryWrapper.orderByAsc("parent_menu_id");
        List<SecurityMenuResource> resourceList = securityMenuResourceService.list(resourceQueryWrapper);
        AuthGetMenusReqBO authGetMenusReqBO = new AuthGetMenusReqBO();
        authGetMenusReqBO.setPermissionIdList(resourcePermissionIdList);
        AuthGetMenuRespBO menus = this.getMenus(context, authGetMenusReqBO);
        return menus.getItems();
//            resourceList.stream().forEach(r ->
//            {
//                Integer permissionId = r.getPermissionId();
//                if (nodeMap.containsKey(permissionId))
//                {
//                    throw new WrapContextException(context, new BusinessException(ErrorConstant.INTERNAL_SERVER_ERROR));
//                }
//                MenuNode node = new MenuNode();
//                node.setMenuName(r.getMenuName());
//                node.setMenuUrl(r.getMenuUrl());
//                node.setPermissionId(permissionId);
//                node.setItems(new ArrayList<>());
//                node.setMenuResourceId(r.getMenuResourceId());
//                nodeMap.put(permissionId, node);
//            });
//        return nodeMap;
    }

    @Override
    public AuthGetMenuRespBO getMenus(CellContext context, AuthGetMenusReqBO reqBO) {
        // 获取所有的菜单
        QueryWrapper<SecurityMenuResource> SecurityMenuResourceQueryWrapper = new QueryWrapper<>();
        SecurityMenuResourceQueryWrapper.orderByAsc("parent_menu_id");
        if (CollectionUtils.isNotEmpty(reqBO.getPermissionIdList())) {
            SecurityMenuResourceQueryWrapper.in("permission_id", reqBO.getPermissionIdList());
        }
        List<SecurityMenuResource> menus = securityMenuResourceService.list(SecurityMenuResourceQueryWrapper);
        AuthGetMenuRespBO ret = new AuthGetMenuRespBO();
        ret.setItems(this.groupMenus(menus).values());
        return ret;
    }

    @Override
    @ArgumentAnnotation
    public AddUserRespBO addUser(CellContext context, AddUserReqBO reqBO) {
        // 判断名称是否重复
        QueryWrapper<SecurityUserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", reqBO.getUserName());
        long count = securityUserInfoService.count(queryWrapper);
        if (count > 0) {
            throw new WrapContextException(context, new BusinessException(ErrorConstant.RECORD_ALREADY_EXISTS));
        }

        BatchAddUserReqBO batchAddUserReqBO = new BatchAddUserReqBO();
        BatchAddUserReqBO.BatchAddUserNode node = new BatchAddUserReqBO.BatchAddUserNode();
        node.from(reqBO, () -> {
            List<Integer> roleIdList = new ArrayList<>(1);
            if (CollectionUtils.isNotEmpty(reqBO.getRoleIdList())) {
                roleIdList.addAll(reqBO.getRoleIdList());
            }
            if (CollectionUtils.isNotEmpty(roleIdList)) {
                GetRoleReqBO roleReqBO = new GetRoleReqBO();
                roleReqBO.setRoleIdList(roleIdList);
                GetRoleRespBO roles = this.getRoles(context, roleReqBO);
                Collection<RoleNode> items = roles.getItems();
                Set<Integer> roleIdSet = new HashSet<>();
                items.forEach(p -> roleIdSet.addAll(p.getRoleIds()));
                node.setRoleIdList(roleIdSet);
            }
        });

        batchAddUserReqBO.setItems(Arrays.asList(node));
        BatchAddUserRespBO batchAddUserRespBO = this.batchAddUser(context, batchAddUserReqBO);
        if (batchAddUserRespBO != null) {
            AddUserRespBO ret = new AddUserRespBO();
            ret.setUserId(batchAddUserRespBO.getUserIdList().get(0));
            return ret;
        }
        return null;
    }

    @Autowired
    private SecurityGroupService securityGroupService;
    @Autowired
    private IEncryptService encryptService;
    @Autowired
    private IdWorkerService idWorkerService;

    private Map<Integer, MenuNode> groupMenus(List<SecurityMenuResource> menus) {
        Map<Integer, MenuNode> menuNodeMap = new HashMap<>();
        menus.stream().forEach(p ->
        {
            MenuNode menuNode = new MenuNode();
            menuNode.setMenuName(p.getMenuName());
            menuNode.setMenuUrl(p.getMenuUrl());
            menuNode.setMenuResourceId(p.getMenuResourceId());
            menuNode.setPermissionId(p.getPermissionId());
            menuNode.setParentMenuResourceId(p.getParentMenuId());
            menuNode.setOperationItems(new ArrayList<>(0));
            menuNode.setChildItems(new ArrayList<>(0));
            if (p.getParentMenuId() != null && p.getParentMenuId() != 0) {
                MenuNode parent = menuNodeMap.get(p.getParentMenuId());
                if (parent != null) {
                    parent.getChildItems().add(menuNode);
                }
            }
//            if (p.getParentMenuId() == null || p.getParentMenuId() == 0) {
            menuNodeMap.put(p.getMenuResourceId(), menuNode);
//            }
        });
        return menuNodeMap;
    }

    @Autowired
    private SecurityOperationResourceService securityOperationResourceService;

    @Override
    public GetMenuAuthRespBO getMenuAuthInfo(CellContext context, GetMenuAuthReqBO req) {
        GetMenuAuthRespBO ret = new GetMenuAuthRespBO();
        Collection<MenuNode> menuNodes = this.getMenuResource(context, req.getPermissionIdList());
        ret.setItems(menuNodes);

        if (!req.isOperationPermission()) {
            return ret;
        }
        List<Integer> resourcePermissionIdList = new ArrayList<>();
        Map<Integer, MenuNode> nodeMap = new HashMap<>();
        menuNodes.forEach(p ->
        {
            resourcePermissionIdList.add(p.getPermissionId());
            nodeMap.put(p.getPermissionId(), p);
            if (CollectionUtils.isNotEmpty(p.getChildItems())) {
                List<MenuNode> childItems = p.getChildItems();
                for (MenuNode childItem : childItems) {
                    nodeMap.put(childItem.getPermissionId(), childItem);
                    resourcePermissionIdList.add(childItem.getPermissionId());
                }
            }
        });
        // 获取操作权限
        QueryWrapper<SecurityOperationResource> SecurityOperationResourceQueryWrapper = new QueryWrapper<>();
        SecurityOperationResourceQueryWrapper.in("permission_id", resourcePermissionIdList);
        List<SecurityOperationResource> operationResources = securityOperationResourceService.list(SecurityOperationResourceQueryWrapper);
        operationResources.stream().forEach(o ->
        {
            Integer permissionId = o.getPermissionId();
            MenuNode menuNode = nodeMap.get(permissionId);
            if (menuNode != null) {
                OperationNode node = new OperationNode();
                node.setOperationName(o.getOpName());
                node.setOperationPermissionId(permissionId);
                node.setOperationRule(o.getOpRule());
                menuNode.getOperationItems().add(node);
            }
        });
        return ret;
    }

    // 查询用户admin 列表
    @Override
    @ArgumentAnnotation
    public AuthAdminListRespBO adminList(CellContext context, AuthAdminListReqBO req) {
        AuthAdminListRespBO ret = new AuthAdminListRespBO();
        IPage<SecurityUserInfo> azerUserIPage = new Page<>(req.getPageIndex(), req.getPageSize());
        QueryWrapper<SecurityUserInfo> azerUserQueryWrapper = new QueryWrapper<>();
        azerUserQueryWrapper.orderByDesc("user_id");
        IPage<SecurityUserInfo> page = securityUserInfoService.page(azerUserIPage, azerUserQueryWrapper);
        if (page.getTotal() == 0) {
            ret.setTotalCount(0);
            return ret;
        }
        ret.setTotalCount((int) page.getTotal());
        ret.setPages(Math.toIntExact(page.getPages()));
        ret.setSize(Math.toIntExact(page.getSize()));

        List<SecurityUserInfo> users = page.getRecords();
        List<Integer> userIdList = new ArrayList<>();
        Map<Integer, AuthAdminListRespBO.AdminListNode> userNodeMap = new HashMap<>();
        List<AuthAdminListRespBO.AdminListNode> retNodes = new ArrayList<>();
        for (SecurityUserInfo user : users) {
            AuthAdminListRespBO.AdminListNode node = new AuthAdminListRespBO.AdminListNode();
            node.from(user, null);
            userNodeMap.put(user.getUserId(), node);
            userIdList.add(user.getUserId());
            retNodes.add(node);
        }
        ret.setItems(retNodes);
        AuthBatchGetUserAuthReqBO batchGetUserAuthReqBO = new AuthBatchGetUserAuthReqBO();
        batchGetUserAuthReqBO.setUserIdList(userIdList);
        if (req.getPermissionReq() != null) {
            batchGetUserAuthReqBO.setDataPermission(req.getPermissionReq().isDataPermission());
            batchGetUserAuthReqBO.setOperationPermission(req.getPermissionReq().isOperationPermission());
        }
        AuthBatchGetUserAuthRespBO respBO = this.batchGetUserAuth(context, batchGetUserAuthReqBO);
        if (CollectionUtils.isEmpty(respBO.getItems())) {
            return ret;
        }
        List<UserNode> items = respBO.getItems();
        for (UserNode item : items) {
            AuthAdminListRespBO.AdminListNode node = userNodeMap.get(item.getUserId());
            node.setItems(item.getItems());
        }
        //
        return ret;
    }


    @Override
    @Transactional
    @ArgumentAnnotation
    public AuthUpdateUserResponse updateUser(CellContext context, AuthUpdateUserRequest req) {
        SecurityUserInfo info = this.mustGetById(context, req.getUserId());
         /*
            3. 如果角色有变更,则删除角色信息
         */
        SecurityUserInfo updateUser = new SecurityUserInfo();
        updateUser.setUserId(info.getUserId());
        boolean update = false;
        if (StringUtils.isNotEmpty(req.getMask()) && !req.getMask().equals(info.getMask())) {
            update = true;
            updateUser.setMask(req.getMask());
        }
        if (StringUtils.isNotEmpty(req.getUserName()) && !info.getUserName().equals(req.getUserName())) {
            update = true;
            updateUser.setUserName(req.getUserName());
            this.checkUserName(context, req.getUserName());
        }
        if (StringUtils.isNotEmpty(req.getPassword()) && !info.getPassword().equals(req.getPassword())) {
            update = true;
            updateUser.setPassword(req.getPassword());
        }
        if (req.getAdminType() != info.getType()) {
//            LOG.info(ModuleEnums.PUFF,"sequenceId:{},判断当前登录用户");
            update = true;
            updateUser.setType(req.getAdminType());
        }
        if (CollectionUtils.isNotEmpty(req.getRoleIdList())) {
            LOG.info(ModuleEnums.PUFF, "sequenceId:{},重新构建role信息,req:{},并且判断role是否存在", context.getSequenceId(), req);
            this.removeUserRole(Arrays.asList(req.getUserId()));
            List<SecurityRole> securityRoles = this.securityRoleService.listByIds(req.getRoleIdList());
            if (securityRoles.size() != req.getRoleIdList().size()) {
                throw new WrapContextException(context, new BusinessException(ErrorConstant.ARGUMENT_ILLEGAL));
            }
            List<SecurityUserRole> roles = new ArrayList<>();
            for (Integer id : req.getRoleIdList()) {
                SecurityUserRole securityUserRole = new SecurityUserRole();
                securityUserRole.setUserId(info.getUserId());
                securityUserRole.setRoleId(id);
                securityUserRole.setCreateDate(new Date());
                roles.add(securityUserRole);
            }
            this.securityUserRoleService.saveBatch(roles);
        }

        if (update) {
            this.securityUserInfoService.updateById(updateUser);
        }

        return null;
    }

    @Override
    @ArgumentAnnotation
    @Transactional
    public AuthRemoveUserResponse removeUser(CellContext context, AuthRemoveUserRequest req) {
        LOG.info(ModuleEnums.AUTH, "sequenceId:{},1.清楚用户角色信息:{}", context.getSequenceId(), req);
        this.removeUserRole(req.getUserIdList());
        LOG.info(ModuleEnums.AUTH, "sequenceId:{},2.清楚用户group信息:{}", context.getSequenceId(), req);
        this.removeUserGroup(req.getUserIdList());
        this.removeUser(req.getUserIdList());
        LOG.info(ModuleEnums.AUTH, "sequenceId:{},3.清楚用户信息:{}", context.getSequenceId(), req);
        return null;
    }

    private void checkUserName(CellContext context, String userName) {
        // 判断名称是否重复
        QueryWrapper<SecurityUserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName);
        long count = securityUserInfoService.count(queryWrapper);
        if (count > 0) {
            throw new WrapContextException(context, new BusinessException(ErrorConstant.RECORD_ALREADY_EXISTS));
        }
    }

    private void removeUserRole(List<Integer> userId) {
        QueryWrapper<SecurityUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", userId);
        this.securityUserRoleService.remove(queryWrapper);
    }

    private void removeUserGroup(List<Integer> userId) {
        QueryWrapper<SecurityUserGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", userId);
        this.securityUserGroupService.remove(queryWrapper);
    }

    private void removeUser(List<Integer> userId) {
        this.securityUserInfoService.removeByIds(userId);
    }


    @Override
    public AuthGetAdminTypeResponse getAdminType(CellContext context, AuthGetAdminTypeRequest req) {
        AuthGetAdminTypeResponse ret = new AuthGetAdminTypeResponse();
        List<AdminTypeVO> adminTypeVOS = new ArrayList<>();
        AdminTypeVO superAdmin = new AdminTypeVO();
        superAdmin.setAdminType(DBConstants.ADMIN_LEVEL_SUPER_ADMIN);
        superAdmin.setDesc(DBConstants.DB_AUTH_SUPER_ADAMIN);
        adminTypeVOS.add(superAdmin);

        AdminTypeVO normalAdmin = new AdminTypeVO();
        normalAdmin.setAdminType(DBConstants.ADMIN_LEVEL_NORMAL_ADMIN);
        normalAdmin.setDesc(DBConstants.DB_AUTH_ADMIN);
        adminTypeVOS.add(normalAdmin);
        ret.setAdminTypes(adminTypeVOS);
        return null;
    }

    private SecurityUserInfo mustGetById(CellContext context, Integer userId) {
        SecurityUserInfo info = this.securityUserInfoService.getById(userId);
        if (info == null) {
            throw new WrapContextException(context, new BusinessException(ErrorConstant.RECORD_NOT_EXISTS));
        }
        return info;
    }

    @Transactional
    @AuthAnnotation(allowRole = {DBConstants.DB_AUTH_SUPER_ADAMIN})
    public BatchAddUserRespBO batchAddUser(CellContext context, BatchAddUserReqBO req) {
        class Wrapper {
            SecurityUserInfo user;
            List<SecurityUserRole> roles = new ArrayList<>(1);
            SecurityUserGroup SecurityUserGroup;
        }
        Map<Long, Wrapper> wrapperMap = new HashMap<>();

        List<SecurityUserInfo> retUser = new ArrayList<>();
        /*
            1. 用户表插入信息
            2. 用户-组织表插入信息
            3. 如果角色也存在,则绑定角色信息
         */
        List<BatchAddUserReqBO.BatchAddUserNode> users = req.getItems();
        Set<String> userNames = new HashSet<>();
        List<SecurityUserInfo> userList = new ArrayList<>();
        List<SecurityUserRole> userRoleList = new ArrayList<>();
        Set<Integer> roleIdSet = new HashSet<>();


        Set<Integer> groupIdSet = users.stream().filter(u -> u.getGroupId() != null).map(BatchAddUserReqBO.BatchAddUserNode::getGroupId).collect(Collectors.toSet());
        final Map<Integer, SecurityGroup> groupMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(groupIdSet)) {
            QueryWrapper<SecurityGroup> groupQueryWrapper = new QueryWrapper<>();
            groupQueryWrapper.in("group_id", groupIdSet);

            List<SecurityGroup> groups = securityGroupService.listByIds(groupIdSet);
            if (groups.size() != groupIdSet.size()) {
                throw new WrapContextException(context, new BusinessException(ErrorConstant.BAD_REQUEST));
            }
            for (SecurityGroup group : groups) {
                groupMap.put(group.getGroupId(), group);
            }
        }

        long count = 0;
        List<SecurityUserGroup> userGroups = new ArrayList<>();


        users.stream().forEach(u ->
        {
            Wrapper wrapper = new Wrapper();
            long keyId = idWorkerService.nextId();
            wrapperMap.put(keyId, wrapper);

            userNames.add(u.getUserName());
            Date now = new Date();
            groupIdSet.add(u.getGroupId());
            SecurityUserInfo user = new SecurityUserInfo();
            retUser.add(user);
            user.setType(u.getAdminType());
            user.setUserName(u.getUserName());
            if (CollectionUtils.isNotEmpty(u.getRoleIdList()) || DBConstants.ADMIN_LEVEL_SUPER_ADMIN.equals(u.getAdminType())) {
                user.setStatus(DBConstants.DB_USER_STATUS_ENABLE);
            } else {
                user.setStatus(DBConstants.DB_USER_STATUS_DISABLE);
            }
            user.setCreateDate(now);
            IEncryptService.EncryptReq encryptReq = new IEncryptService.EncryptReq();
            encryptReq.setValue(u.getPassword());
            user.setPassword(encryptService.encrypt(encryptReq).getEncryptResp());
            userList.add(user);
            wrapper.user = user;


            if (u.getGroupId() != null) {
                SecurityUserGroup userGroup = new SecurityUserGroup();
                SecurityGroup group = groupMap.get(u.getGroupId());
                userGroup.setGroupId(group.getGroupId());
                userGroup.setGroupName(group.getGroupName());
                userGroup.setGroupLevel(group.getGroupLevel());
                userGroup.setCreateDate(now);
                userGroups.add(userGroup);
                wrapper.SecurityUserGroup = userGroup;
            }

            Set<Integer> roleIdList = u.getRoleIdList();
            if (CollectionUtils.isNotEmpty(roleIdList)) {
                for (Integer roleId : roleIdList) {
                    SecurityUserRole userRole = new SecurityUserRole();
                    userRole.setCreateDate(now);
                    userRole.setRoleId(roleId);
                    roleIdSet.add(roleId);
                    wrapper.roles.add(userRole);
                    userRoleList.add(userRole);
                }
            }
        });

        // 1. 先用户表插入信息
        QueryWrapper<SecurityUserInfo> SecurityUserQueryWrapper = new QueryWrapper<>();
        SecurityUserQueryWrapper.in("user_name", userNames);
        count = securityUserInfoService.count(SecurityUserQueryWrapper);
        if (count > 0) {
            LOG.erroring(ModuleEnums.PUFF, "重复插入数据,用户已经存在");
            throw new WrapContextException(context, new BusinessException(ErrorConstant.ACCOUNT_ALREADY_EXISTS));
        }
        // 用户表插入信息
        securityUserInfoService.saveBatch(userList);

        // 开始凑齐其他表
        List<Integer> retUserIdList = new ArrayList<>();
        Set<Long> keys = wrapperMap.keySet();
        for (Long key : keys) {
            Wrapper wrapper = wrapperMap.get(key);
            for (SecurityUserRole r : wrapper.roles) {
                r.setUserId(wrapper.user.getUserId());
            }
            if (wrapper.SecurityUserGroup != null) {
                wrapper.SecurityUserGroup.setUserId(wrapper.user.getUserId());
            }
            retUserIdList.add(wrapper.user.getUserId());
        }
        if (CollectionUtils.isNotEmpty(userGroups)) {
            securityUserGroupService.saveBatch(userGroups);
        }

        // 确保角色存在
        if (CollectionUtils.isNotEmpty(userRoleList)) {
            QueryWrapper<SecurityRole> SecurityRoleQueryWrapper = new QueryWrapper<>();
            SecurityRoleQueryWrapper.in("role_id", roleIdSet);
            count = securityRoleService.count(SecurityRoleQueryWrapper);
            if (count != roleIdSet.size()) {
                throw new WrapContextException(context, new BusinessException(ErrorConstant.RECORD_NOT_EXISTS));
            }
            securityUserRoleService.saveBatch(userRoleList);
        }

        BatchAddUserRespBO ret = new BatchAddUserRespBO();
        ret.setUserIdList(retUserIdList);
        return ret;
    }
}
