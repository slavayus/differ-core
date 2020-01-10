package com.differ.differcore.controllers

import com.differ.differcore.models.Difference
import com.differ.differcore.models.Either
import com.differ.differcore.service.DiffService
import com.differ.differcore.trash.Application
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@WebMvcTest(DifferController::class)
@ContextConfiguration(classes = [Application::class])
internal class DifferControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var diffService: DiffService

    @Test
    fun getFullDiffJsonSuccessResult() {
        val difference = Difference(mapOf(), mapOf(), mapOf())
        `when`(diffService.difference("0001", "0002")).thenReturn(Either.Success(difference))
        mockMvc.perform(
            get("/v1/differ")
                .contentType(MediaType.APPLICATION_JSON)
                .param("left", "0001")
                .param("right", "0002")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andDo(print())
            .andExpect(MockMvcResultMatchers.jsonPath("$.value").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.value.full").isEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.value.onlyOnLeft").isEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.value.onlyOnRight").isEmpty)

        val penultimateCaptor = ArgumentCaptor.forClass(String::class.java)
        val lastCaptor = ArgumentCaptor.forClass(String::class.java)
        verify(diffService, times(1)).difference(penultimateCaptor.capture(), lastCaptor.capture())

        assertThat("0001", equalTo(penultimateCaptor.value))
        assertThat("0002", equalTo(lastCaptor.value))
    }

    @Test
    fun getFullDiffJsonErrorResult() {
        `when`(diffService.difference("0001", "0002")).thenReturn(Either.Error("error message"))
        mockMvc.perform(
            get("/v1/differ")
                .contentType(MediaType.APPLICATION_JSON)
                .param("left", "0001")
                .param("right", "0002")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andDo(print())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("error message"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cause").doesNotExist())

        val penultimateCaptor = ArgumentCaptor.forClass(String::class.java)
        val lastCaptor = ArgumentCaptor.forClass(String::class.java)
        verify(diffService, times(1)).difference(penultimateCaptor.capture(), lastCaptor.capture())

        assertThat("0001", equalTo(penultimateCaptor.value))
        assertThat("0002", equalTo(lastCaptor.value))
    }
}