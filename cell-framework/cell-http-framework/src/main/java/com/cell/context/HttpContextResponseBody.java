package com.cell.context;

import com.cell.annotations.HttpCmdAnno;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-01 10:31
 */
@Data
@Builder
public class HttpContextResponseBody
{
    private HttpStatus status;
}
