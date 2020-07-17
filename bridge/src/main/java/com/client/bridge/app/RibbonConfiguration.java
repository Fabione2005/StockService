package com.client.bridge.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.WeightedResponseTimeRule;

@Configuration
public class RibbonConfiguration {
//	@Bean
//	public IRule ribbonRule() { //permite aplicar otras reglas de balanceo
//		//return new RetryRule(new WeightedResponseTimeRule(),500);
//		//return new RandomRule();
//		return new WeightedResponseTimeRule();
//	}
}