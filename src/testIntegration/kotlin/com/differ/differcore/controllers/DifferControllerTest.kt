package com.differ.differcore.controllers

import com.differ.differcore.models.Difference
import com.differ.differcore.models.Either
import com.differ.differcore.service.DiffService
import com.differ.differcore.trash.Application
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@WebMvcTest(DifferController::class)
@ExtendWith(RestDocumentationExtension::class)
@ContextConfiguration(classes = [Application::class])
@AutoConfigureRestDocs
internal class DifferControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var diffService: DiffService

    @Test
    fun getFullDiffJsonSuccessResult() {
        val difference = Difference(
            mapOf(
                "home" to mapOf(
                    "info" to mapOf(
                        "description" to
                                "API for fetching user related information"
                    )
                )
            ), mapOf(), mapOf()
        )
        `when`(diffService.difference("0001", "0002")).thenReturn(Either.Success(difference))
        val actions = mockMvc.perform(
            get("/v1/differ")
                .contentType(MediaType.APPLICATION_JSON)
                .param("left", "0001")
                .param("right", "0002")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.full").isNotEmpty)
            .andExpect(jsonPath("$.onlyOnLeft").isEmpty)
            .andExpect(jsonPath("$.onlyOnRight").isEmpty)

        actions.andDo(print())
            .andDo(
                document(
                    "{class-name}/{method-name}",
                    preprocessResponse(prettyPrint()),
                    requestParameters(
                        parameterWithName("left").description("An old version of API"),
                        parameterWithName("right").description("A new version of API")
                    ),
                    relaxedResponseFields(
                        fieldWithPath("full").type(mapOf<String, Any>()).description("Entities presented in both versions"),
                        fieldWithPath("onlyOnLeft").type(mapOf<String, Any>()).description("Entities only on left version"),
                        fieldWithPath("onlyOnRight").type(mapOf<String, Any>()).description("Entities only on right version")
                    )
                )
            )

        val penultimateCaptor = ArgumentCaptor.forClass(String::class.java)
        val lastCaptor = ArgumentCaptor.forClass(String::class.java)
        verify(diffService, times(1)).difference(penultimateCaptor.capture(), lastCaptor.capture())

        assertThat("0001", equalTo(penultimateCaptor.value))
        assertThat("0002", equalTo(lastCaptor.value))
    }

    @Test
    fun getFullDiffJsonErrorResult() {
        `when`(diffService.difference("0001", "0002")).thenReturn(Either.Error("No version 0002 file was found"))
        val actions = mockMvc.perform(
            get("/v1/differ")
                .contentType(MediaType.APPLICATION_JSON)
                .param("left", "0001")
                .param("right", "0002")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.message").value("No version 0002 file was found"))
            .andExpect(jsonPath("$.cause").doesNotExist())

        actions.andDo(print())
            .andDo(
                document(
                    "{class-name}/{method-name}",
                    preprocessResponse(prettyPrint()),
                    requestParameters(
                        parameterWithName("left").description("An old version of API"),
                        parameterWithName("right").description("A new version of API")
                    ),
                    responseFields(
                        fieldWithPath("message").description("Error description, can show to user"),
                        fieldWithPath("cause").optional().type(Exception()).description("If any error occurred")
                    )
                )
            )

        val penultimateCaptor = ArgumentCaptor.forClass(String::class.java)
        val lastCaptor = ArgumentCaptor.forClass(String::class.java)
        verify(diffService, times(1)).difference(penultimateCaptor.capture(), lastCaptor.capture())

        assertThat("0001", equalTo(penultimateCaptor.value))
        assertThat("0002", equalTo(lastCaptor.value))
    }
}