package pipelines

import jetbrains.buildServer.configs.kotlin.buildSteps.DotnetMsBuildStep
import jetbrains.buildServer.configs.kotlin.buildSteps.dotnetMsBuild
import jetbrains.buildServer.configs.kotlin.pipelines.Pipeline
import vcsRoots.*

object MSBuildPipeline : Pipeline({

    id("MSBuildPipeline")
    name = "Azure Functions: MSBuild"

    repositories {
        repository(AzureFuncVCSRoot, enabledByDefault = true)
    }

    job {
        id = "MSBuild"
        name = "MSBuild project"

        steps {
            dotnetMsBuild {
                name = "Build"

                projects = "FunctionsSdk.sln"
                sdk = "6"

                version = DotnetMsBuildStep.MSBuildVersion.V17
            }
        }
    }
})