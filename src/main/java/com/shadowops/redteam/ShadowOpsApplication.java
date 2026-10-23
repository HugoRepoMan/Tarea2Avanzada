package com.shadowops.redteam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Punto de entrada principal para ShadowOps: Red Team Engagement & Exploit Telemetry API.
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class ShadowOpsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShadowOpsApplication.class, args);
    }
}
