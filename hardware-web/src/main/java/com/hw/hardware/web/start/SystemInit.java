package com.hw.hardware.web.start;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.jd.ump.profiler.proxy.Profiler;

@Component
public class SystemInit {
	//监控 key
	private static final String JONE_SYSHEARTBEAT = "jone_cmdb.systemHeartBeat";
	private static final String JONE_JVM = "jone_cmdb.jvmmonitor";
	
	@PostConstruct
	public void init() {
		//系统心跳
		Profiler.InitHeartBeats(JONE_SYSHEARTBEAT);
		Profiler.registerJVMInfo(JONE_JVM);
	}
}
