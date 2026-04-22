package org.example.samplefortracing.pricing.config;

import io.jaegertracing.Configuration.ReporterConfiguration;
import io.jaegertracing.Configuration.SamplerConfiguration;
import io.jaegertracing.Configuration.SenderConfiguration;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Регистрирует Jaeger tracer для локального стенда, если starter не создал его сам.
 */
@org.springframework.context.annotation.Configuration
public class JaegerTracerConfiguration {

    /**
     * Создаёт tracer с отправкой спанов в локальный Jaeger agent.
     *
     * @param serviceName имя сервиса
     * @param agentHost хост Jaeger agent
     * @param agentPort порт Jaeger agent
     * @param logSpans признак логирования спанов
     * @param samplerType тип sampler-а
     * @param samplerParam параметр sampler-а
     * @return tracer Jaeger/OpenTracing
     */
    @Bean
    @ConditionalOnMissingBean(Tracer.class)
    public Tracer jaegerTracer(@Value("${spring.application.name}") String serviceName,
                               @Value("${opentracing.jaeger.udp-sender.host:localhost}") String agentHost,
                               @Value("${opentracing.jaeger.udp-sender.port:6831}") int agentPort,
                               @Value("${opentracing.jaeger.http-sender.url:}") String collectorEndpoint,
                               @Value("${opentracing.jaeger.log-spans:true}") boolean logSpans,
                               @Value("${opentracing.jaeger.sampler.type:const}") String samplerType,
                               @Value("${opentracing.jaeger.sampler.param:1}") int samplerParam) {
        SenderConfiguration senderConfiguration = SenderConfiguration.fromEnv();
        if (collectorEndpoint != null && !collectorEndpoint.isBlank()) {
            senderConfiguration = senderConfiguration.withEndpoint(collectorEndpoint);
        } else {
            senderConfiguration = senderConfiguration.withAgentHost(agentHost).withAgentPort(agentPort);
        }

        Tracer tracer = new io.jaegertracing.Configuration(serviceName)
            .withSampler(SamplerConfiguration.fromEnv().withType(samplerType).withParam(samplerParam))
            .withReporter(
                ReporterConfiguration.fromEnv()
                    .withLogSpans(logSpans)
                    .withSender(senderConfiguration)
            )
            .getTracer();

        GlobalTracer.registerIfAbsent(tracer);
        return tracer;
    }
}
