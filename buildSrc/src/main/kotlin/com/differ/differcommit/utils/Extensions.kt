package com.differ.differcommit.utils

import com.differ.differcommit.exceptions.GitCredentialsNotProvidedException
import com.differ.differcommit.tasks.DifferCommit
import java.util.*

fun String.isInt() = toIntOrNull() != null

fun allIsNull(vararg any: Any?) = any.filterNotNull().isEmpty()

fun requireNotNull(any: Any?, error: () -> Throwable) = run {
    if (Objects.isNull(any)) {
        throw error.invoke()
    }
}

fun usernameNotProvidedException() =
    GitCredentialsNotProvidedException(
        """There is no username for GIT account in properties. 
          |Please provide property with key '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_HTTP_USERNAME}' and username as value. 
          |Or you can set property '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_PUSH_REQUIRED}' to false if push is not required.""".trimMargin()
    )


fun passwordNotProvidedException() =
    GitCredentialsNotProvidedException(
        """There is no password for GIT account in properties. 
          |Please provide property with key '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_HTTP_PASSWORD}' and password as value. 
          |Or you can set property '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_PUSH_REQUIRED}' to false if push is not required.""".trimMargin()
    )

fun rsaLocationNotProvidedException() =
    GitCredentialsNotProvidedException(
        """There is no ssh private key location in properties.  
          |Please provide property with key '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_SSH_RSA_LOCATION}' and path as value. 
          |Or you can set property '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_PUSH_REQUIRED}' to false if push is not required.""".trimMargin()
    )

fun rsaPasswordNotProvidedException() =
    GitCredentialsNotProvidedException(
        """There is no ssh private key password in properties. 
          |Please provide property with key '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_SSH_RSA_PASSWORD}' and password as value. 
          |Or you can set property '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_PUSH_REQUIRED}' to false if push is not required.""".trimMargin()
    )