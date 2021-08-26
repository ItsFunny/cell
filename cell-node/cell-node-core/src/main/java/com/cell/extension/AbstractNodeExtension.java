package com.cell.extension;

import com.cell.annotation.ActivePlugin;
import com.cell.annotation.Required;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-26 05:40
 */
public abstract class AbstractNodeExtension implements INodeExtension
{
    protected ActivePlugin anno;
    protected boolean required = true;
    protected Required requiredAnno;

    public AbstractNodeExtension()
    {
        anno = this.getClass().getAnnotation(ActivePlugin.class);
        requiredAnno = this.getClass().getAnnotation(Required.class);
        if (requiredAnno != null)
        {
            required = requiredAnno.necessary();
        }
    }

    @Override
    public void setUnrequired()
    {
        required = false;
    }

    @Override
    public boolean isRequired()
    {
        return this.required;
    }

    @Override
    public String getName()
    {
        if (anno != null && !anno.name().isEmpty())
        {
            return anno.name();
        }
        return this.getClass().getSimpleName();
    }
}
