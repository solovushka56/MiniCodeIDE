package com.skeeper.minicode.core.helpers;

import java.util.ArrayList;
import java.util.List;

public class ProjectRectColorBindings {

    public static ProjectRectColorBinding orangeBinding = new ProjectRectColorBinding(
            "#ED6663", "#CC425E");
    public static ProjectRectColorBinding violetBinding = new ProjectRectColorBinding(
            "#4C3BCF", "#2D246D");
    public static ProjectRectColorBinding greenBinding = new ProjectRectColorBinding(
            "#2BBD97", "#0D6D80");

    public static List<ProjectRectColorBinding> bindingsList = new ArrayList<>() {{
        add(orangeBinding);
        add(violetBinding);
        add(greenBinding);
    }};
}
