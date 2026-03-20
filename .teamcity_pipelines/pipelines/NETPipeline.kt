package pipelines

import jetbrains.buildServer.configs.kotlin.buildSteps.*
import jetbrains.buildServer.configs.kotlin.pipelines.Pipeline
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import vcsRoots.NetVcsRoot


object NETPipeline : Pipeline({
    name = ".NET pipeline"
    repositories {
        repository(NetVcsRoot)
    }

    triggers {
        vcs {  }
    }

    job {
        id = "NETtests"
        name = ".NET tests"

        steps {
            dotnetRestore {
                id = "netRestore"

                projects = "./Samples.sln"

                packagesDir = "./Packages"
                sdk = "10"
            }
            dotnetTest {
                id = "netTests"

                projects = "money/Money.csproj"
                maxRetries = "2"

                sdk = "10"
                logging = DotnetTestStep.Verbosity.Diagnostic
            }
            dotnetTest {
                id = "netTestsSecond"

                projects = "syntax/AssertSyntax.csproj"
                maxRetries = "3"

                sdk = "10"
                logging = DotnetTestStep.Verbosity.Diagnostic
            }
            dotnetBuild {
                id = "netBuild"

                projects = "./Samples.sln"
                sdk = "10"
                versionSuffix = "beta"
            }
            dotnetPublish {
                id = "netPublish"

                projects = "./Samples.sln"
                sdk = "10"
                versionSuffix = "beta"
            }
            dotnetClean {
                id = "netClean"

                projects = "./Samples.sln"
                sdk = "10"
            }

        }
        parallelism = 2
        allowReuse = false

        requirements {
            startsWith("teamcity.agent.jvm.os.name", "Windows")
        }
    }
})