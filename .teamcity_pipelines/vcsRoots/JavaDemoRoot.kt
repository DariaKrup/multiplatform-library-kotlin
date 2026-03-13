package vcsRoots

import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

object JavaDemoRoot : GitVcsRoot({
    name = "Java Demo Repository"
    url = "https://github.com/DariaKrup/java-maven-demo.git"
    branch = "refs/heads/main"
    branchSpec = "refs/heads/*"
    authMethod = token {
        userName = "oauth2"
        tokenId = "tc_token_id:CID_4b4df26346ed38498f51c0d6bee05baa:-1:cf81e969-97fc-4097-a44f-024090e5fd19"
    }
})