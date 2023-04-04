package com.cell.component.httpsecurity.security.models;

import com.cell.base.common.utils.CollectionUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GroupNode
{
    private Integer groupId;
    private String groupName;
    private Integer groupLevel;
    private Integer groupWeight;
    private Integer parentGroupId;
    private String groupDesc;
    private Integer userAmount;
    private Integer weight;
    private Date createDate;
    private List<GroupNode> childGroup;

    public static Integer getUserAmount(GroupNode node)
    {
        int ret = 0;
        if (node.getUserAmount() != null)
        {
            ret += node.getUserAmount();
        }
        List<GroupNode> childGroup = node.getChildGroup();
        if (CollectionUtils.isNotEmpty(childGroup))
        {
            for (GroupNode groupNode : childGroup)
            {
                ret += getUserAmount(groupNode);
            }
        }
        return ret;
    }

}
