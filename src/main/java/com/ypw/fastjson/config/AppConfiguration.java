package com.ypw.fastjson.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yupengwu
 */
@Configuration
public class AppConfiguration {
    /**
     * 自定义fastJson转换器
     *
     * @return
     */
    @Bean
    @Primary
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        //1.需要定义一个convert转换消息的对象;
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        //2:添加fastJson的配置信息;
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        //fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.BrowserSecure,
                // 消除对同一对象重复引用的优化
                SerializerFeature.DisableCircularReferenceDetect,
                // 将中文都会序列化为\Uxxxx 格式
                // 超过 −9007199254740992 到 9007199254740992 区间使用字符串，如："9007199254740993"
                // FIXME: 序列化的时候会导致空指针。
                SerializerFeature.BrowserCompatible,
                // 不隐藏为空的字段
                SerializerFeature.IgnoreNonFieldGetter,
                // map为Null，置为{}
                SerializerFeature.WriteMapNullValue,
                // Long、Integer、Short等number类型为Null，置为0
                SerializerFeature.WriteNullNumberAsZero,
                // Boolean为Null，置为false
                SerializerFeature.WriteNullBooleanAsFalse,
                // List为Null，置为[]
                SerializerFeature.WriteNullListAsEmpty,
                // String为Null，置为""
                SerializerFeature.WriteNullStringAsEmpty
        );
        //3处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        //4.在convert中添加配置信息.
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        HttpMessageConverter<?> converter = fastJsonHttpMessageConverter;
        return new HttpMessageConverters(converter);
    }

}
