package com.test.plugin;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

public class MethodPlugin implements Plugin<Project> {

    @Override
    public void apply(@NotNull Project project) {
        AppExtension appExtension = project.getExtensions().findByType(AppExtension.class);
        assert appExtension != null;
        appExtension.registerTransform(new MethodTransform(project));
    }
}
