package com.xy.project.common;


import lombok.Data;

import java.io.Serializable;

@Data
public class DeleteRequest implements Serializable {
    private static final long serialVersionUID =123131231231L;
    private long id;
}
