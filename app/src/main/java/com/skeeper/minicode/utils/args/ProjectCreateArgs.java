package com.skeeper.minicode.utils.args;

import com.skeeper.minicode.domain.enums.TemplateType;

public record ProjectCreateArgs(
        String name,
        String description,
        String[] tags,
        TemplateType templateType
) {}
