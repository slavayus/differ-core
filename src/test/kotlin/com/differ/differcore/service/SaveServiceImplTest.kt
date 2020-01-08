package com.differ.differcore.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.io.FileMatchers.anExistingFile
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.spy
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import springfox.documentation.spring.web.DocumentationCache
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper
import java.io.File
import java.nio.file.Files


class SaveServiceImplTest {
    @Spy
    lateinit var objectMapper: ObjectMapper

    @Mock
    private lateinit var documentationCache: DocumentationCache

    @Spy
    private lateinit var mapper: ServiceModelToSwagger2Mapper

    lateinit var saveServiceImpl: SaveServiceImpl

    @TempDir
    lateinit var tmpDir: File

    @BeforeEach
    fun initMocks() {
        MockitoAnnotations.initMocks(this)
        saveServiceImpl = spy(SaveServiceImpl(documentationCache, objectMapper, mapper))
    }

    @Test
    fun saveTest_whenExistsDocument_shouldSave() {
        val documentation = mapOf("1" to null)
        doReturn(documentation).`when`(documentationCache).all()
        val differ = tmpDir.resolve("differ-doc.json")
        doReturn(differ).`when`(saveServiceImpl).provideSaveFile()

        saveServiceImpl.save()

        assertThat(differ, anExistingFile())

        val filesData = Files.readAllLines(differ.toPath()).joinToString()

        assertThat(filesData, `is`("{\"1\":null}"))
    }

    @Test
    fun saveTest_whenThereIsNoDocument_shouldSaveEmptyJsonObject() {
        val differ = tmpDir.resolve("differ-doc.json")
        doReturn(differ).`when`(saveServiceImpl).provideSaveFile()

        saveServiceImpl.save()

        assertThat(differ, anExistingFile())

        val filesData = Files.readAllLines(differ.toPath()).joinToString()

        assertThat(filesData, `is`("{}"))
    }
}