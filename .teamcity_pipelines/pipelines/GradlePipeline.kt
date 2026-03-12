package pipelines

import vcsRoots.MavenUnbalancedRoot
import jetbrains.buildServer.configs.kotlin.DslContext
import jetbrains.buildServer.configs.kotlin.buildSteps.MavenBuildStep
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.pipelines.Pipeline
import jetbrains.buildServer.configs.kotlin.triggers.vcs


object GradlePipeline : Pipeline({
    name = "Gradle Pipeline"

    repositories {
        repository(DslContext.settingsRoot)
        repository(MavenUnbalancedRoot, enabledByDefault = false)
    }

    params {
        text("parameter", "parameterName")
    }


    triggers {
        vcs {  }
    }

    job {
        id = "GradleErrors"
        name = "Gradle: GradlePluginConfigurationErrors"
        repositories {
            repository(DslContext.settingsRoot)
        }
        steps {
            gradle {
                id = "gradle_runner"
                tasks = "clean :convention-plugins:checkKotlinGradlePluginConfigurationErrors"
                gradleParams = "-x :library:extractDebugAnnotations"
            }
        }
    }

    job {
        id = "MavenTests"
        name = "Maven Tests (unbalanced messages)"
        repositories {
            repository(MavenUnbalancedRoot, enabled = true, checkoutPath = "messages-repo")
        }
        steps {
            maven {
                name = "Test and deploy"
                goals = "clean test"
                mavenVersion = defaultProvidedVersion()
                
                pomLocation = "pom.xml"
                runnerArgs = "-Dmaven.test.skip=true"
            }
        }
    }
})