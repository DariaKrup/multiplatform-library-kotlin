package pipelines

import jetbrains.buildServer.configs.kotlin.buildSteps.DotnetTestStep
import jetbrains.buildServer.configs.kotlin.buildSteps.dotnetTest
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.pipelines.Pipeline
import vcsRoots.*

object PipelineMultipleRoots : Pipeline({
    name = "Pipeline Multiple Roots"

    repositories {
        repository(MavenUnbalancedRoot)
        repository(NetVcsRoot, enabledByDefault = false)
        repository(GradleConnectionRoot, enabledByDefault = false)
    }

    job {
        id = "firstVCSRoot"
        name = "First VCS Root: Gradle"

        repositories {
            repository(GradleConnectionRoot)
        }

        steps {
            gradle {
                name = "Gradle Build"
                tasks = "clean build"
            }
        }
    }

    job {
        id = "secondVCSRoot"
        name = "Second VCS Root: .NET"

        repositories {
            repository(NetVcsRoot)
        }
        steps {
            dotnetTest {
                id = "netTests"
                name = ".NET Tests"

                projects = "money/Money.csproj"
            }
        }
    }

    job {
        id = "thirdVCSRoot"
        name = "Third VCS Root: Maven"

        repositories {
            repository(MavenUnbalancedRoot)
        }
        steps {
            maven {
                name = "Maven Tests"

                goals = "clean test"
                mavenVersion = defaultProvidedVersion()
            }
        }
    }

    job {
        id = "fourthVCSRoot"
        name = "Fourth VCS Root: Maven Demo"

        repositories {
            repository(JavaDemoRoot)
        }
        steps {
            maven {
                name = "Maven Tests"

                goals = "clean test"
                mavenVersion = defaultProvidedVersion()
            }
        }
    }
})