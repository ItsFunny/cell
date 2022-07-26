package com.cell.component.bot.common.bo;

import com.cell.component.bot.common.constants.BotConstants;
import com.cell.node.core.context.CellContext;
import com.cell.node.core.valid.IValidBasic;
import lombok.Data;

@Data
public class BotType implements IValidBasic
{
    private int type;

    @Override
    public void validBasic(CellContext cellContext) throws IllegalArgumentException
    {
        if (this.type == 0 ||
                (this.type != BotConstants.TELEGRAM_BOT && this.type != BotConstants.TWITTER_BOT &&
                        this.type != BotConstants.DISCORD_BOT))
        {
            throw new IllegalArgumentException();
        }
    }
}
