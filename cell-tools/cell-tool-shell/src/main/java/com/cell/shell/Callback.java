package com.cell.shell;


public interface Callback
{
    Callback sout = ((line, helper) -> System.out.println(line));

    void accept(String line, ProcessHelper helper);
}
