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
    fun difference_whenExistingFilesWithData_shouldReturnDifference() {
        val penultimate = tmpDir.resolve("0001.json")
        Files.write(penultimate.toPath(), listOf("{\"1\": 1 }"))
        doReturn(penultimate).`when`(versionService).getVersionFile("0001")

        val last = tmpDir.resolve("0002.json")
        Files.write(last.toPath(), listOf("{\"2\": 2 }"))
        doReturn(last).`when`(versionService).getVersionFile("0002")

        val actual: Either<Difference> = diffServiceImpl.difference("0001", "0002")
        val expected: Either.Success<Difference> =
            Either.Success(Difference(mapOf("1" to 1, "2" to 2), mapOf("1" to 1), mapOf("2" to 2)))


        assertThat("difference is success", actual is Either.Success<Difference>)
        actual as Either.Success<Difference>
        assertThat(expected, `is`(actual))
    }

    @Test
    fun difference_whenPenultimateFileAbsent_shouldReturnErrorMessage() {
        doReturn(null).`when`(versionService).getVersionFile("0001")

        val last = tmpDir.resolve("0002.json")
        Files.write(last.toPath(), listOf("{\"2\": 2 }"))
        doReturn(last).`when`(versionService).getVersionFile("0002")

        val actual: Either<Difference> = diffServiceImpl.difference("0001", "0002")
        val expected: Either.Error =
            Either.Error("No version 0001 file found")


        assertThat("difference is error", actual is Either.Error)
        actual as Either.Error
        assertThat(expected, `is`(actual))
    }

    @Test
    fun difference_whenLastFileAbsent_shouldReturnErrorMessage() {
        val penultimate = tmpDir.resolve("0001.json")
        Files.write(penultimate.toPath(), listOf("{\"1\": 1 }"))
        doReturn(penultimate).`when`(versionService).getVersionFile("0001")

        doReturn(null).`when`(versionService).getVersionFile("0002")

        val actual: Either<Difference> = diffServiceImpl.difference("0001", "0002")
        val expected: Either.Error =
            Either.Error("No version 0002 file found")


        assertThat("difference is error", actual is Either.Error)
        actual as Either.Error
        assertThat(expected, `is`(actual))
    }

    @Test
    fun difference_whenBothFilesPresentAndPenultimateParamIsNull_shouldReturnErrorMessage() {
        val penultimate = tmpDir.resolve("0001.json")
        Files.write(penultimate.toPath(), listOf("{\"1\": 1 }"))
        doReturn(penultimate).`when`(versionService).getVersionFile("0001")

        doReturn(penultimate).`when`(versionService).getPenultimateVersionFile()

        val last = tmpDir.resolve("0002.json")
        Files.write(last.toPath(), listOf("{\"2\": 2 }"))
        doReturn(last).`when`(versionService).getVersionFile("0002")

        val actual: Either<Difference> = diffServiceImpl.difference(null, "0002")
        val expected: Either.Success<Difference> =
            Either.Success(Difference(mapOf("1" to 1, "2" to 2), mapOf("1" to 1), mapOf("2" to 2)))


        assertThat("difference is success", actual is Either.Success<Difference>)
        actual as Either.Success<Difference>
        assertThat(expected, `is`(actual))
    }

    @Test
    fun difference_whenBothFilesPresentAndLastParamIsNull_shouldReturnErrorMessage() {
        val penultimate = tmpDir.resolve("0001.json")
        Files.write(penultimate.toPath(), listOf("{\"1\": 1 }"))
        doReturn(penultimate).`when`(versionService).getVersionFile("0001")

        val last = tmpDir.resolve("0002.json")
        Files.write(last.toPath(), listOf("{\"2\": 2 }"))
        doReturn(last).`when`(versionService).getVersionFile("0002")

        doReturn(last).`when`(versionService).getLastVersionFile()

        val actual: Either<Difference> = diffServiceImpl.difference("0001", null)
        val expected: Either.Success<Difference> =
            Either.Success(Difference(mapOf("1" to 1, "2" to 2), mapOf("1" to 1), mapOf("2" to 2)))


        assertThat("difference is success", actual is Either.Success<Difference>)
        actual as Either.Success<Difference>
        assertThat(expected, `is`(actual))
    }

    @Test
    fun difference_whenBothFilesPresentAndBothParamsAreNull_shouldReturnErrorMessage() {
        val penultimate = tmpDir.resolve("0001.json")
        Files.write(penultimate.toPath(), listOf("{\"1\": 1 }"))
        doReturn(penultimate).`when`(versionService).getVersionFile("0001")

        doReturn(penultimate).`when`(versionService).getPenultimateVersionFile()

        val last = tmpDir.resolve("0002.json")
        Files.write(last.toPath(), listOf("{\"2\": 2 }"))
        doReturn(last).`when`(versionService).getVersionFile("0002")

        doReturn(last).`when`(versionService).getLastVersionFile()

        val actual: Either<Difference> = diffServiceImpl.difference(null, null)
        val expected: Either.Success<Difference> =
            Either.Success(Difference(mapOf("1" to 1, "2" to 2), mapOf("1" to 1), mapOf("2" to 2)))


        assertThat("difference is success", actual is Either.Success<Difference>)
        actual as Either.Success<Difference>
        assertThat(expected, `is`(actual))
    }

    @Test
    fun difference_whenBothAbsentAndBothParamsAreNull_shouldReturnErrorMessage() {
        doReturn(null).`when`(versionService).getVersionFile("0001")
        doReturn(null).`when`(versionService).getPenultimateVersionFile()

        doReturn(null).`when`(versionService).getVersionFile("0002")
        doReturn(null).`when`(versionService).getLastVersionFile()

        val actual: Either<Difference> = diffServiceImpl.difference(null, null)
        val expected: Either.Error = Either.Error("No version files was found")


        assertThat("difference is error", actual is Either.Error)
        actual as Either.Error
        assertThat(expected, `is`(actual))
    }

    @Test
    fun difference_whenPenultimateFileAbsentAndPenultimateParamIsNull_shouldReturnErrorMessage() {
        doReturn(null).`when`(versionService).getPenultimateVersionFile()

        val last = tmpDir.resolve("0002.json")
        Files.write(last.toPath(), listOf("{\"2\": 2 }"))
        doReturn(last).`when`(versionService).getVersionFile("0002")

        val actual: Either<Difference> = diffServiceImpl.difference(null, "0002")
        val expected: Either.Error =
            Either.Error("No version null file found")


        assertThat("difference is error", actual is Either.Error)
        actual as Either.Error
        assertThat(expected, `is`(actual))
    }

    @Test
    fun difference_whenLastFileAbsentAndLastParamIsNull_shouldReturnErrorMessage() {
        val penultimate = tmpDir.resolve("0001.json")
        Files.write(penultimate.toPath(), listOf("{\"1\": 1 }"))
        doReturn(penultimate).`when`(versionService).getVersionFile("0001")

        doReturn(null).`when`(versionService).getLastVersionFile()

        val actual: Either<Difference> = diffServiceImpl.difference("0001", null)
        val expected: Either.Error =
            Either.Error("No version null file found")


        assertThat("difference is error", actual is Either.Error)
        actual as Either.Error
        assertThat(expected, `is`(actual))
    }
}