package com.cell.concurrent.internal;

public final class NoOpTypeParameterMatcher extends TypeParameterMatcher {
    @Override
    public boolean match(Object msg) {
        return true;
    }
}
