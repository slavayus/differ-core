package com.differ.differcommit.exceptions

import com.differ.differcommit.tasks.DifferCommit

class GitSshRsaPasswordMissing : GitCredentialsException(
    """There is no ssh private key password in properties. 
          |Please provide property with key '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_SSH_RSA_PASSWORD}' and password as value. 
          |Or you can set property '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_PUSH_REQUIRED}' to false if push is not required.""".trimMargin()
)