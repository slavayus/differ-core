package com.differ.differcommit.exceptions

import com.differ.differcommit.tasks.DifferCommit

class GitAllParametesMissing : GitCredentialsException(
    """There is no GIT credentials in properties. 
      |There are some options:
      |You can set property '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_PUSH_REQUIRED}' to false if push is not required.
      |If you use http protocol to push data to remote branch then provide properties '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_HTTP_USERNAME}' and '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_HTTP_PASSWORD}'.
      |If you use ssh protocol to push data to remote branch then provide properties '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_SSH_RSA_LOCATION}' and '${DifferCommit.DIFFERCOMMIT_VERSIONS_GIT_SSH_RSA_PASSWORD}'.""".trimMargin()
)