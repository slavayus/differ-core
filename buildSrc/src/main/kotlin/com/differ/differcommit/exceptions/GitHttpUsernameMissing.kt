package com.differ.differcommit.exceptions

import com.differ.differcommit.tasks.DifferCommit

class GitHttpUsernameMissing : GitCredentialsException(
    """There is no username for GIT account in properties. 
          |Please provide property with key '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_HTTP_USERNAME}' and username as value. 
          |Or you can set property '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_PUSH_REQUIRED}' to false if push is not required.""".trimMargin()
)