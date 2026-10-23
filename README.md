# ShadowOps: Red Team Engagement & Exploit Telemetry API (`shadowops-api`)

**Framework:** Java 17 / Spring Boot 3.4.3 (`spring-boot-starter-web`, `spring-boot-starter-validation`, `spring-boot-starter-aop`, `spring-boot-starter-test`)

---

## Project Overview

**ShadowOps** is a multi-layered RESTful backend API designed for Senior Red Team Operators (`RedTeamOperator`) to orchestrate authorized penetration testing engagements (`OperacionPentest`), catalog scoped target network hosts (`ObjetivoRed`), and record vulnerability exploitation telemetry (`HallazgoVulnerabilidad`) while enforcing **Anti-SOC OPSEC Throttling Rules** and **CVSS v3.1** structural validation.

---

## Key Architectural Pillars

1. **Inversion of Control (IoC) & Dependency Injection (DI):**
   - `CalculadoraRiesgoExploitStrategy` with two implementations (`CvssBaseRiskStrategy` and `StealthOpsecRiskStrategy`) injected via `@Qualifier("stealthOpsecRiskStrategy")`.
   - Explicit `@Bean` declarations in `ShadowOpsConfig`: `AuditoriaTelemetriaBuffer` and `GeneradorHuellaCriptograficaBean` (SHA-256 evidence hashing).
2. **Web Request Interceptors (`HandlerInterceptor`):**
   - `HttpTelemetryLoggingInterceptor` (`order = 1`): Logs HTTP request/response telemetry and latency in milliseconds.
   - `RedTeamClearanceInterceptor` (`order = 2`): Validates header `X-RedTeam-Clearance-Token` (`^RT-OPS-[A-Za-z0-9]{8,}$`, e.g., `RT-OPS-9948A7F2`), excluding `/api/v1/health/**` and `/error`.
3. **Aspect-Oriented Programming (AOP):**
   - `OperacionLoggingAspect` (`@Around`, `@Order(1)`): Measures service method execution time in ms.
   - `OpsecStealthRuleAspect` (`@Before`, `@Order(2)`): Enforces the **Anti-SOC OPSEC Throttling Rule** (max 2 simultaneous `EN_EJECUCION` operations per operator; max 3 `CRITICA` findings per operation within a 60-minute window).
   - `RedTeamAuditAspect` (`@AfterReturning`, `@Order(3)`): Appends immutable audit logs after state-mutating operations.
4. **Custom Bean Validation (`@ValidVectorCVSS`):**
   - `CvssVectorValidator` validates strict compliance with official **CVSS v3.1 Base Metrics** (`^CVSS:3\.1/AV:[NALP]/AC:[LH]/PR:[NLH]/UI:[NR]/S:[UC]/C:[NLH]/I:[NLH]/A:[NLH]$`).
5. **Global Exception Handling (`@RestControllerAdvice`):**
   - `GlobalExceptionHandler` maps validation (`400`), missing resources (`404`), OPSEC conflicts (`409`), and unexpected errors (`500`) into a uniform `ApiErrorResponse` JSON contract.

---

## Running & Testing the Project

```bash
# Run the complete automated test suite (17 passing tests, 0 failures)
mvn clean test

# Start the Spring Boot server on port 8080
mvn spring-boot:run
```
