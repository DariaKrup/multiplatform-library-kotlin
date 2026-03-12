package vcsRoots

import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

object GradleConnectionRoot : GitVcsRoot({
    name = "GHConnection Repository"
    url = "https://github.com/DariaKrup/gradle-simple.git"
    branch = "refs/heads/master"
    branchSpec = "refs/heads/*"
    authMethod = token {
        userName = "oauth2"
        tokenId = "tc_token_id:CID_4b4df26346ed38498f51c0d6bee05baa:-1:9aeb4114-69a4-4eba-8c5a-8fafca765e79"
    }
})