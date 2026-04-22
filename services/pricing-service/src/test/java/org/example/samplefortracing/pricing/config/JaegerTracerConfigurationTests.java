package org.example.samplefortracing.pricing.config;

import io.opentracing.Tracer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Проверяет локальную конфигурацию tracer-а для сервиса расчёта цены.
 */
class JaegerTracerConfigurationTests {

    /**
     * Проверяет создание tracer-а для локального Jaeger agent.
     */
    @Test
    void jaegerTracerCreatesTracerBean() {
        JaegerTracerConfiguration configuration = new JaegerTracerConfiguration();

        Tracer tracer = configuration.jaegerTracer("pricing-service", "localhost", 6831, "http://localhost:14268/api/traces", true, "const", 1);

        assertThat(tracer).isNotNull();
    }
}
