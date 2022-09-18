package com.test.intuit.demo.integrationtest;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.MySQLContainer;

public class DemoApplicationIntegrationTestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:5.7");

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        mySQLContainer.withDatabaseName("democomment")
                .withUsername("root")
                .withPassword("root")
                .start();
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext, "DB_URL=" + mySQLContainer.getJdbcUrl());
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext, "DB_USER=" + mySQLContainer.getUsername());
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext, "DB_PASSWORD=" + mySQLContainer.getPassword());
    }

}
