package com.lehuutri.studentmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

@SpringBootApplication
@Theme(value = "studentmanagement")
@PWA(name = "Student - Course Management", shortName = "SCM", offlineResources = {})
@NpmPackage(value = "line-awesome", version = "1.3.0")
@NpmPackage(value = "lumo-css-framework", version = "^4.0.10")
public class StudentmanagementApplication extends SpringBootServletInitializer implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(StudentmanagementApplication.class, args);
	}
}
