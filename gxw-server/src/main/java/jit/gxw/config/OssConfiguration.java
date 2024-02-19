package jit.gxw.config;

import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyuncs.exceptions.ClientException;
import jit.gxw.properties.AliOssProperties;
import jit.gxw.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于创建AliOssUtil对象
 */
@Configuration
@Slf4j
public class OssConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) throws ClientException {
        log.info("开始创建alioss工具对象：{}",aliOssProperties);

        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getBucketName(),
                CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider());
    }
}
