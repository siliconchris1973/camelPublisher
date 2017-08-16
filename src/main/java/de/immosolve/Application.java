package de.immosolve;// *********************************************************************************************************************
// * (C) 2017 Immosolve GmbH, Tegelbarg 43, 24576 Bad Bramstedt
// *********************************************************************************************************************

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.support.GenericMessage;

@SpringBootApplication
@EnableBinding(Source.class)
public class Application {

	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	@InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "10000", maxMessagesPerPoll = "1"))
	public MessageSource<String> testSource() {
		return () -> new GenericMessage<>("test");
	}

	@Bean
	public AlwaysSampler defaultSampler() {

		return new AlwaysSampler();

	}
}
