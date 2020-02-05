package com.differ.differcommit.exceptions

import com.differ.differcommit.tasks.DifferCommit

class GitHttpPasswordMissing : GitCredentialsException(
    """There is no password for GIT account in properties. 
          |Please provide property with key '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_HTTP_PASSWORD}' and password as value. 
          |Or you can set property '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_PUSH_REQUIRED}' to false if push is not required.""".trimMargin()
)