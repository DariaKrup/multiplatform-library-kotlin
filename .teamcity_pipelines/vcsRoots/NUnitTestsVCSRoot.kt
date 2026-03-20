package vcsRoots

import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

object NUnitTestsVCSRoot : GitVcsRoot({
    name = "NUnit Tests repository"
    url = "git@github.com:DariaKrup/NUnitTests.git"
    branch = "refs/heads/main"
    checkoutPolicy = AgentCheckoutPolicy.USE_MIRRORS
    authMethod = uploadedKey {
        uploadedKey = "id_rsa"
    }
}) {
}