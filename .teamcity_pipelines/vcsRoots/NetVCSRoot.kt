package vcsRoots

import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

object NetVcsRoot : GitVcsRoot({
    name = ".NET repository"
    url = "git@github.com:DariaKrup/nunit-csharp-tests-project.git"
    branch = "refs/heads/master"
    checkoutPolicy = AgentCheckoutPolicy.USE_MIRRORS
    authMethod = uploadedKey {
        uploadedKey = "id_rsa"
    }
})