package com.xy.project.common;


import lombok.Data;

import java.io.Serializable;

@Data
public class PageRequest implements Serializable {
    private static final long serialVersionUID =123131231231L;
    protected int pageSize =10;
    protected int pageNum =1;
}
