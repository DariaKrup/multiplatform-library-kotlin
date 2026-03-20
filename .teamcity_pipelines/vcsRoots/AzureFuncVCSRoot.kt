package vcsRoots

import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

object AzureFuncVCSRoot : GitVcsRoot({
    name = "AzureSDK Functions repository"
    url = "git@github.com:DariaKrup/azure-functions-vs-build-sdk.git"
    branch = "refs/heads/main"
    checkoutPolicy = AgentCheckoutPolicy.USE_MIRRORS
    authMethod = uploadedKey {
        uploadedKey = "id_rsa"
    }
})