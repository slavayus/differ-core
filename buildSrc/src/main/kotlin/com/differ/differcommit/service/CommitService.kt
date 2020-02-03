package com.differ.differcommit.service

import com.differ.differcommit.naming.generator.VersionGenerator

interface CommitService {
    fun processNewApiVersion(versionGenerator: VersionGenerator)
}