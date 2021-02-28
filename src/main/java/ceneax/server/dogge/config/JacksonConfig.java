package ceneax.server.dogge.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
public class JacksonConfig extends WebMvcConfigurationSupport {

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
        jacksonConverter.getObjectMapper().setSerializerFactory(jacksonConverter.getObjectMapper().getSerializerFactory().withSerializerModifier(
                new MyBeanSerializerModifier()
        ));

        converters.add(jacksonConverter);
    }

//    @Bean
//    @Primary
//    @ConditionalOnMissingBean(ObjectMapper.class)
//    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
//        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
//
//        // 为objectMapper注册一个带有SerializerModifier的Factory
//        objectMapper.setSerializerFactory(objectMapper.getSerializerFactory()
//                .withSerializerModifier(new MyBeanSerializerModifier()));
//
//        objectMapper.getSerializerProvider().setNullValueSerializer(new CustomizeNullJsonSerializer.NullObjectJsonSerializer());
//
//        return objectMapper;
//    }

    /**
     * 自定义null值序列化处理器
     */
    public static class CustomizeNullJsonSerializer {
        /**
         * 处理数组集合类型的null值
         */
        public static class NullArrayJsonSerializer extends JsonSerializer<Object> {
            @Override
            public void serialize(Object value, JsonGenerator jsonGenerator,
                                  SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeStartArray();
                jsonGenerator.writeEndArray();
            }
        }

        /**
         * 处理字符串类型的null值
         */
        public static class NullStringJsonSerializer extends JsonSerializer<Object> {
            @Override
            public void serialize(Object value, JsonGenerator jsonGenerator,
                                  SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString("");
            }
        }

        /**
         * 处理数值类型的null值
         */
        public static class NullNumberJsonSerializer extends JsonSerializer<Object> {
            @Override
            public void serialize(Object value, JsonGenerator jsonGenerator,
                                  SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeNumber(0);
            }
        }

        /**
         * 处理boolean类型的null值
         */
        public static class NullBooleanJsonSerializer extends JsonSerializer<Object> {
            @Override
            public void serialize(Object value, JsonGenerator jsonGenerator,
                                  SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeBoolean(false);
            }
        }

        /**
         * 处理实体对象类型的null值
         */
        public static class NullObjectJsonSerializer extends JsonSerializer<Object> {
            @Override
            public void serialize(Object value, JsonGenerator jsonGenerator,
                                  SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeEndObject();
            }
        }
    }

    /**
     * 此modifier主要做的事情为：
     * 1.当序列化类型为数组集合时，当值为null时，序列化成[]
     * 2.String类型值序列化为""
     */
    public static class MyBeanSerializerModifier extends BeanSerializerModifier {
        @Override
        public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                         BeanDescription beanDesc,
                                                         List<BeanPropertyWriter> beanProperties) {
            // 循环所有的beanPropertyWriter
            for (BeanPropertyWriter writer : beanProperties) {
                // 判断字段的类型，如果是数组或集合则注册nullSerializer
                if (isArrayType(writer)) {
                    // 给writer注册一个自己的nullSerializer
                    writer.assignNullSerializer(new CustomizeNullJsonSerializer.NullArrayJsonSerializer());
                } else if (isStringType(writer)) {
                    writer.assignNullSerializer(new CustomizeNullJsonSerializer.NullStringJsonSerializer());
                } else if (isNumberType(writer)) {
                    writer.assignNullSerializer(new CustomizeNullJsonSerializer.NullNumberJsonSerializer());
                } else if (isBooleanType(writer)) {
                    writer.assignNullSerializer(new CustomizeNullJsonSerializer.NullBooleanJsonSerializer());
                } else {
                    writer.assignNullSerializer(new CustomizeNullJsonSerializer.NullObjectJsonSerializer());
                }
            }

            return beanProperties;
        }

        /**
         * 是否是数组
         */
        private boolean isArrayType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
        }

        /**
         * 是否是String
         */
        private boolean isStringType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return CharSequence.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz);
        }

        /**
         * 是否是数值类型
         */
        private boolean isNumberType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return Number.class.isAssignableFrom(clazz);
        }

        /**
         * 是否是boolean
         */
        private boolean isBooleanType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return clazz.equals(Boolean.class);
        }
    }

}
