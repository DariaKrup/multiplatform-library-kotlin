package pipelines

import jetbrains.buildServer.configs.kotlin.buildSteps.DotnetTestStep
import jetbrains.buildServer.configs.kotlin.buildSteps.dotnetTest
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
            dotnetTest {
                id = "netTests"

                projects = "money/money/Money.csproj"
                maxRetries = "2"

                sdk = "10"
                logging = DotnetTestStep.Verbosity.Diagnostic
            }
        }
        parallelism = 2

        requirements {
            startsWith("teamcity.agent.jvm.os.name", "Windows")
        }
    }
})