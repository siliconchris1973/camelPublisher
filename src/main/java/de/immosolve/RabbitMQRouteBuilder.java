// *********************************************************************************************************************
// * (C) 2016 Immosolve GmbH, Tegelbarg 43, 24576 Bad Bramstedt
// *********************************************************************************************************************

package de.immosolve;

import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.camel.rabbitmq")
@Setter
public class RabbitMQRouteBuilder extends SpringRouteBuilder {

	private static final Log LOG = LogFactory.getLog(RabbitMQRouteBuilder.class);

	private String host;
	private String port;
	private String username;
	private String password;
	private boolean confirm;

	@Override
	public void configure() throws Exception {

		errorHandler(deadLetterChannel("direct:error")
			//.maximumRedeliveries(3).redeliveryDelay(1000)
		);

		from("direct:error")
			.process(
				new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						LOG.info("Message delivered: " + (exchange.getOut().getHeader("rabbitmq.DELIVERY_TAG") != null));
					}
				}
			);
		//.to("log:de.immosolve?level=DEBUG&multiline=true&showAll=true");


		from("timer://foo?fixedRate=true&period=1000&repeatCount=1")
			.routeId("direct")
			.id("to-rabbit-mq")
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					exchange.getIn().setBody("test");
					exchange.getIn().getHeaders().put("rabbitmq.DELIVERY_MODE", 2);
					exchange.getIn().getHeaders().put("rabbitmq.ROUTING_KEY", "test");

				}
			})
			.setExchangePattern(ExchangePattern.InOut)
			.to("rabbitmq://" + host + ":" + port + "/testExchange" //
					+ "?username=" + username //
					+ "&password=" + password //
					+ "&autoAck=" + !confirm
					+ "&exchangeType=topic"
					+ "&queue=test"
					+ "&autoDelete=false"
					+ "&requestTimeout=0")
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					LOG.info(exchange.getIn().getHeader("rabbitmq.DELIVERY_TAG"));
				}
			})
			.to("log:de.immosolve?level=INFO&multiline=true&showAll=true");

	}

}
