package com.differ.differcommit.plugins;

import org.gradle.api.Project;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.function.BiConsumer;

public class Main {
    public static void main(String[] args) {

    }

    private void addPropertiesToProject(Project project) {
        if (Files.exists(Paths.get("${project.rootDir}/src/main/resources/application.properties"))) {
            try {
                Properties localProperties = new Properties();
                localProperties.load(new FileInputStream("${project.rootDir}/src/main/resources/application.properties"));
                localProperties.forEach((key, value) -> project.getProperties().get("asfd"));
                localProperties.forEach(new BiConsumer<Object, Object>() {
                    @Override
                    public void accept(Object s, Object s2) {
//                        project.getProperties().put(s.toString(), s.toString());
                    }
                });
//                localProperties.forEach(prop -> System.out.println("${prop.key}, ${prop.value}"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
