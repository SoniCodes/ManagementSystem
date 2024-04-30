package com.soni;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Task
{
    private UUID clientId;
    private String name;
    private String description;
}
