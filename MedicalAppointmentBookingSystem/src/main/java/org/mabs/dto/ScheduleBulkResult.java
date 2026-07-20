package org.mabs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleBulkResult {
    private final int created;
    private final int skipped;
}
