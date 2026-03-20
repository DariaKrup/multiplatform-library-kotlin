package pipelines

import jetbrains.buildServer.configs.kotlin.buildSteps.*
import jetbrains.buildServer.configs.kotlin.pipelines.Pipeline
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import vcsRoots.*

object NunitTestsPipeline : Pipeline({

    id("NunitTestsPipeline")
    name = "NUnit: restore + VCS tests"

    repositories {
        repository(NUnitTestsVCSRoot, enabledByDefault = true)
    }

    triggers {
        vcs {  }
    }

    job {
        id = "netRestore"
        name = "Restore"

        steps {
            dotnetRestore {
                name = "Build"

                projects = "NUnitTests.sln"
            }
            dotnetMsBuild {
                name = "Build"
                projects = "NUnitTests.sln"
                version = DotnetMsBuildStep.MSBuildVersion.CrossPlatform
            }
            dotnetVsTest {
                name = "VSTest"
                assemblies = "%system.teamcity.build.checkoutDir%/NUnitTests/bin/Debug/*/NUnitTests.dll"
                version = DotnetVsTestStep.VSTestVersion.CrossPlatform
                platform = DotnetVsTestStep.Platform.Auto

                coverage = dotcover {
                }
            }
        }
    }
})