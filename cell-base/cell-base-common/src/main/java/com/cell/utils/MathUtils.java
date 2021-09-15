package com.cell.utils;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-15 21:05
 */
public class MathUtils
{

    public static double standardDeviation(double[] nums)
    {
        if (nums == null || nums.length == 0)
        {
            return 0D;
        }

        double sum = 0;
        for (int i = 0; i < nums.length; i++)
        {
            sum += nums[i];
        }
        double average = sum / nums.length;
        double total = 0;
        for (int i = 0; i < nums.length; i++)
        {
            total += (nums[i] - average) * (nums[i] - average);
        }

        return Math.sqrt(total / nums.length);
    }
}
