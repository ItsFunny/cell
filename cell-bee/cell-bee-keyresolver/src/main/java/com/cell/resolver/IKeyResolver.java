package com.cell.resolver;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-12 17:45
 */
public interface IKeyResolver<BEFORE, AFTER>
{
    AFTER resolve(BEFORE t);

    boolean match(AFTER res, BEFORE req);

    BEFORE before(AFTER after);
}
