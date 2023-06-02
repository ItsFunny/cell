package com.cell.base.core.wrapper;

import lombok.Data;

@Data
public class Box<T>
{
    private T data;

    public Box() {
    }

    public Box(T data) {
        this.data = data;
    }
}
