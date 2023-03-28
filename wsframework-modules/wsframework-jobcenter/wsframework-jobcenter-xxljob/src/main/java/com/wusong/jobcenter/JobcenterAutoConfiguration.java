package com.wusong.jobcenter;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 *
 * @author p14
 */
@Slf4j
@Import(XxlJobConfig.class)
public class JobcenterAutoConfiguration {

}
