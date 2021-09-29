package com.encircle360.oss.receiptfox;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.encircle360.oss.receiptfox.dto.pagination.PageContainer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Testcontainers
@NoArgsConstructor
@AllArgsConstructor
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class AbstractTest {

    @Autowired
    private MockMvc mvc;

    private ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();

    @Container
    private static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer()
        .withDatabaseName("receiptfox")
        .withUsername("user")
        .withPassword("user");

    @DynamicPropertySource
    static void postgreSQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }


    protected MvcResult post(String uri, Object object, ResultMatcher expect) throws Exception {
        return post(uri, getObjectMapper().writeValueAsString(object), expect);
    }

    protected MvcResult post(@NonNull String uri, @NonNull String body, @NonNull ResultMatcher expect) throws Exception {
        return getMvc().perform(
                MockMvcRequestBuilders.post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(body))
            .andExpect(expect)
            .andReturn();
    }

    protected MvcResult put(String uri, Object object, ResultMatcher expect) throws Exception {
        return put(uri, getObjectMapper().writeValueAsString(object), expect);
    }

    protected MvcResult emptyPut(String uri, ResultMatcher expect) throws Exception {
        return getMvc().perform(
                MockMvcRequestBuilders.put(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(expect)
            .andReturn();
    }

    protected MvcResult put(String uri, String body, ResultMatcher expect) throws Exception {
        return getMvc().perform(
                MockMvcRequestBuilders.put(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(body))
            .andExpect(expect)
            .andReturn();
    }

    protected MvcResult emptyPost(String uri, ResultMatcher expect) throws Exception {
        return getMvc().perform(
                MockMvcRequestBuilders.post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(expect)
            .andReturn();
    }

    protected MvcResult delete(String uri, ResultMatcher expect) throws Exception {
        return getMvc().perform(
                MockMvcRequestBuilders.delete(uri)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(expect)
            .andReturn();
    }

    protected MvcResult get(String uri, ResultMatcher expect) throws Exception {
        return get(uri, expect, null, null);
    }

    protected MvcResult get(String uri, ResultMatcher expect, String name, String value) throws Exception {
        Map<String, String> params = null;
        if (name != null && !name.isBlank() && value != null && !value.isBlank()) {
            params = Map.of(name, value);
        }
        return get(uri, expect, params);
    }

    protected MvcResult get(String uri, ResultMatcher expect, Map<String, String> params) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri)
            .accept(MediaType.APPLICATION_JSON);

        if (params != null && !params.isEmpty()) {
            for (Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                requestBuilder = requestBuilder.param(key, value);
            }
        }

        return getMvc().perform(requestBuilder)
            .andExpect(expect)
            .andReturn();
    }

    protected <T> T mapResultToObject(MvcResult result, Class<T> type) throws Exception {
        String json = getJsonFromResponse(result);

        return this.getObjectMapper().readValue(json, type);
    }

    protected <T> PageContainer<T> mapResultToPageContainer(MvcResult result, Class<T> type) throws Exception {
        String json = getJsonFromResponse(result);
        JavaType reference = getObjectMapper().getTypeFactory().constructParametricType(PageContainer.class, type);

        return this.getObjectMapper().readValue(json, reference);
    }

    protected <T> List<T> mapResultToList(MvcResult result, Class<T> type) throws Exception {
        String json = getJsonFromResponse(result);
        JavaType reference = getObjectMapper().getTypeFactory().constructCollectionType(List.class, type);
        return this.getObjectMapper().readValue(json, reference);
    }

    protected String getJsonFromResponse(final MvcResult result) throws UnsupportedEncodingException {
        assertNotNull(result);
        assertNotNull(result.getResponse());
        String json = result.getResponse().getContentAsString();
        assertNotNull(json);

        return json;
    }

    protected static String loremBase64() {
        return "dGVzdA==";
    }

}
