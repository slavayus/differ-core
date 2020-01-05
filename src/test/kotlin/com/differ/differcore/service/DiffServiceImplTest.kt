package com.differ.differcore.service

import com.differ.differcore.models.Difference
import com.differ.differcore.models.Either
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import java.io.File
import java.nio.file.Files


class DiffServiceImplTest {
    @Spy
    lateinit var objectMapper: ObjectMapper

    @Mock
    lateinit var versionService: VersionService

    @Spy
    lateinit var mapTransformerService: MapTransformerServiceImpl

    @InjectMocks
    lateinit var diffServiceImpl: DiffServiceImpl

    @TempDir
    lateinit var tmpDir: File

    @BeforeEach
    fun initMocks() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun test() {
        val penultimate = tmpDir.resolve("0001.json")
        Files.write(penultimate.toPath(), listOf("{\"1\": 1 }"))
        doReturn(penultimate).`when`(versionService).getVersionFile("0001")

        val last = tmpDir.resolve("0002.json")
        Files.write(last.toPath(), listOf("{\"2\": 2 }"))
        doReturn(last).`when`(versionService).getVersionFile("0002")

        val difference: Either<Difference> = diffServiceImpl.difference("0001", "0002")
        val matcher: Either<Difference> =
            Either.Success(Difference(mapOf("1" to "1", "2" to "2"), mapOf("1" to "1"), mapOf("2" to "2")))
        assertThat(difference, `is`(matcher))
    }
}