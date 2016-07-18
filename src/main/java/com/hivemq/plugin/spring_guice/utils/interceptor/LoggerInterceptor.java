package com.hivemq.plugin.spring_guice.utils.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration("LoggerInterceptor")
public class LoggerInterceptor implements org.aopalliance.intercept.MethodInterceptor {
	private static Logger log = LoggerFactory.getLogger("guice_log");

	@Override
	public Object invoke(org.aopalliance.intercept.MethodInvocation invocation) throws Throwable {
		
		String m_name = invocation.getMethod().getName();
		String c_name = invocation.getMethod().getDeclaringClass().getName();
		String result = String.format("%-20s  %s",
				m_name != null ? m_name :"", c_name != null ? c_name :"");
		
		String e_label = "{}{}ENTERING";
		String l_label = "{}{}Leaving";
		
		log.info(e_label.toString(), result);		
        Object ret = invocation.proceed();
        log.info(l_label.toString(), result);

        return ret;
	}

//http://www.dotnetperls.com/format-java
}
