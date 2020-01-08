package com.differ.differcore.service

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.Mockito.doReturn
import org.mockito.MockitoAnnotations
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import java.io.File
import java.nio.file.Files


class VersionServiceImplTest {
    @Mock
    lateinit var resourceLoader: ResourceLoader

    @Mock
    lateinit var resource: Resource

    @InjectMocks
    lateinit var versionServiceImpl: VersionServiceImpl

    @TempDir
    lateinit var tmpDir: File

    @BeforeEach
    fun initMocks() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getAllVersions_whenExistsOnlyOneVersion_shouldReturnVersion() {
        Files.createFile(tmpDir.resolve("0001.json").toPath())

        doReturn(tmpDir).`when`(resource).file
        doReturn(resource).`when`(resourceLoader).getResource(anyString())

        versionServiceImpl.init()

        val allVersions = versionServiceImpl.getAllVersions()

        assertThat(allVersions, containsInAnyOrder("0001"))
    }

    @Test
    fun getAllVersions_whenThereIsNoVersion_shouldReturnEmptyList() {
        doReturn(tmpDir).`when`(resource).file
        doReturn(resource).`when`(resourceLoader).getResource(anyString())

        versionServiceImpl.init()

        val allVersions = versionServiceImpl.getAllVersions()

        assertThat(allVersions, empty())
    }

    @Test
    fun getAllVersions_whenThereIsMore_shouldReturnAllVersions() {
        Files.createFile(tmpDir.resolve("0001.json").toPath())
        Files.createFile(tmpDir.resolve("0004.json").toPath())
        Files.createFile(tmpDir.resolve("0005.json").toPath())

        doReturn(tmpDir).`when`(resource).file
        doReturn(resource).`when`(resourceLoader).getResource(anyString())

        versionServiceImpl.init()

        val allVersions = versionServiceImpl.getAllVersions()

        assertThat(allVersions, containsInAnyOrder("0001", "0004", "0005"))
    }

    @Test
    fun getLastVersionFile_whenThereIsMore_shouldReturnMaxVersion() {
        Files.createFile(tmpDir.resolve("0001.json").toPath())
        Files.createFile(tmpDir.resolve("0004.json").toPath())
        Files.createFile(tmpDir.resolve("0005.json").toPath())

        doReturn(tmpDir).`when`(resource).file
        doReturn(resource).`when`(resourceLoader).getResource(anyString())

        versionServiceImpl.init()

        val lastVersion = versionServiceImpl.getLastVersionFile()

        assertThat(lastVersion, notNullValue())
        assertThat(lastVersion!!.name, equalTo("0005.json"))
    }

    @Test
    fun getLastVersionFile_whenThereIsOneFile_shouldReturnTheSameVersion() {
        Files.createFile(tmpDir.resolve("0004.json").toPath())

        doReturn(tmpDir).`when`(resource).file
        doReturn(resource).`when`(resourceLoader).getResource(anyString())

        versionServiceImpl.init()

        val lastVersion = versionServiceImpl.getLastVersionFile()

        assertThat(lastVersion, notNullValue())
        assertThat(lastVersion!!.name, equalTo("0004.json"))
    }

    @Test
    fun getLastVersionFile_whenThereIsNoFile_shouldReturnNull() {
        doReturn(tmpDir).`when`(resource).file
        doReturn(resource).`when`(resourceLoader).getResource(anyString())

        versionServiceImpl.init()

        val penultimateVersion = versionServiceImpl.getLastVersionFile()

        assertThat(penultimateVersion, nullValue())
    }

    @Test
    fun getPenultimateVersionFile_whenThereIsNoFile_shouldReturnNull() {
        doReturn(tmpDir).`when`(resource).file
        doReturn(resource).`when`(resourceLoader).getResource(anyString())

        versionServiceImpl.init()

        val penultimateVersion = versionServiceImpl.getPenultimateVersionFile()

        assertThat(penultimateVersion, nullValue())
    }

    @Test
    fun getPenultimateVersionFile_whenThereIsOneFile_shouldReturnNull() {
        Files.createFile(tmpDir.resolve("0004.json").toPath())

        doReturn(tmpDir).`when`(resource).file
        doReturn(resource).`when`(resourceLoader).getResource(anyString())

        versionServiceImpl.init()

        val penultimateVersion = versionServiceImpl.getPenultimateVersionFile()

        assertThat(penultimateVersion, nullValue())
    }

    @Test
    fun getPenultimateVersionFile_whenThereIsTwoFile_shouldReturnPenultimate() {
        Files.createFile(tmpDir.resolve("0004.json").toPath())
        Files.createFile(tmpDir.resolve("0001.json").toPath())

        doReturn(tmpDir).`when`(resource).file
        doReturn(resource).`when`(resourceLoader).getResource(anyString())

        versionServiceImpl.init()

        val penultimateVersion = versionServiceImpl.getPenultimateVersionFile()

        assertThat(penultimateVersion, notNullValue())
        assertThat(penultimateVersion!!.name, equalTo("0001.json"))
    }

    @Test
    fun getPenultimateVersionFile_whenThereIsMoreFile_shouldReturnPenultimate() {
        Files.createFile(tmpDir.resolve("0004.json").toPath())
        Files.createFile(tmpDir.resolve("0005.json").toPath())
        Files.createFile(tmpDir.resolve("0001.json").toPath())

        doReturn(tmpDir).`when`(resource).file
        doReturn(resource).`when`(resourceLoader).getResource(anyString())

        versionServiceImpl.init()

        val penultimateVersion = versionServiceImpl.getPenultimateVersionFile()

        assertThat(penultimateVersion, notNullValue())
        assertThat(penultimateVersion!!.name, equalTo("0004.json"))
    }

    @Test
    fun getVersionFile_whenExistFile_shouldReturnFile() {
        Files.createFile(tmpDir.resolve("0004.json").toPath())
        Files.createFile(tmpDir.resolve("0005.json").toPath())
        Files.createFile(tmpDir.resolve("0001.json").toPath())

        doReturn(tmpDir).`when`(resource).file
        doReturn(resource).`when`(resourceLoader).getResource(anyString())

        versionServiceImpl.init()

        val versionFile = versionServiceImpl.getVersionFile("0004")

        assertThat(versionFile, notNullValue())
        assertThat(versionFile!!.name, equalTo("0004.json"))
    }

    @Test
    fun getVersionFile_whenAbsentFile_shouldReturnNull() {
        Files.createFile(tmpDir.resolve("0005.json").toPath())
        Files.createFile(tmpDir.resolve("0001.json").toPath())

        doReturn(tmpDir).`when`(resource).file
        doReturn(resource).`when`(resourceLoader).getResource(anyString())

        versionServiceImpl.init()

        val versionFile = versionServiceImpl.getVersionFile("0004")

        assertThat(versionFile, nullValue())
    }

    @Test
    fun getVersionFile_whenEmptyDirectory_shouldReturnNull() {
        doReturn(tmpDir).`when`(resource).file
        doReturn(resource).`when`(resourceLoader).getResource(anyString())

        versionServiceImpl.init()

        val versionFile = versionServiceImpl.getVersionFile("0004")

        assertThat(versionFile, nullValue())
    }

}