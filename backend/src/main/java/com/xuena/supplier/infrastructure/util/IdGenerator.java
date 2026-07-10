package com.xuena.supplier.infrastructure.util;

import cn.hutool.core.lang.Snowflake;
import org.springframework.stereotype.Component;

@Component
public class IdGenerator {

    private final Snowflake snowflake;

    public IdGenerator(Snowflake snowflake) {
        this.snowflake = snowflake;
    }

    public String generateId() {
        return String.valueOf(snowflake.nextId());
    }

    public long generateLongId() {
        return snowflake.nextId();
    }
}