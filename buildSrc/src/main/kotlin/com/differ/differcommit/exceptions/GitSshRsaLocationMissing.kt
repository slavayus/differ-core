package com.differ.differcommit.exceptions

import com.differ.differcommit.tasks.DifferCommit

class GitSshRsaLocationMissing : GitCredentialsException(
    """There is no ssh private key location in properties.  
          |Please provide property with key '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_SSH_RSA_LOCATION}' and path as value. 
          |Or you can set property '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_PUSH_REQUIRED}' to false if push is not required.""".trimMargin()
)