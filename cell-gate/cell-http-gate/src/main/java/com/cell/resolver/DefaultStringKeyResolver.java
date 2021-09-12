package com.cell.resolver;


import lombok.Builder;
import lombok.Data;
import org.springframework.util.Assert;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-12 17:47
 */
public class DefaultStringKeyResolver implements IKeyResolver<DefaultStringKeyResolver.StringKeyResolver, String>
{

    private static final String separator = "_";

    @Override
    public String resolve(StringKeyResolver t)
    {
        return t.method + separator + t.uri;
    }

    @Override
    public boolean match(String res, StringKeyResolver req)
    {
        return res.equalsIgnoreCase(this.resolve(req));
    }

    @Override
    public StringKeyResolver before(String s)
    {
        String[] split = s.split(separator);
        return StringKeyResolver.builder()
                .method(split[0])
                .uri(split[1]).build();
    }

    @Data
    @Builder
    public static class StringKeyResolver
    {
        private String method;
        private String uri;
    }

//
////    @Override
//    public boolean match(String s, String[] strings)
//    {
//        // 0:method ,1:
//        return s.equalsIgnoreCase(strings[0] + separator + strings[1]);
//    }
//
//    @Override
//    public String[] before(String s)
//    {
//        return new String[0];
//    }
}
