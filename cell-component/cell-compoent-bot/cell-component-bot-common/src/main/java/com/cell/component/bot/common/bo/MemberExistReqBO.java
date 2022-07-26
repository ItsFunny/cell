package com.cell.component.bot.common.bo;

import com.cell.base.common.utils.StringUtils;
import com.cell.node.core.context.CellContext;
import lombok.Data;

@Data
public class MemberExistReqBO extends BotType
{
    private String userKey;

    @Override
    public void validBasic(CellContext cellContext) throws IllegalArgumentException
    {
        super.validBasic(cellContext);
        if (StringUtils.isEmpty(this.userKey))
        {
            throw new IllegalArgumentException();
        }
    }
}
