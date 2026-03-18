package pipelines

import jetbrains.buildServer.configs.kotlin.BuildStep
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.pipelines.Pipeline
import jetbrains.buildServer.configs.kotlin.projectFeatures.dockerRegistry
import jetbrains.buildServer.configs.kotlin.triggers.schedule
import jetbrains.buildServer.configs.kotlin.triggers.vcs

import vcsRoots.*


object MavenMessagesPipeline : Pipeline ({
    name = "Maven: Messages Pipeline"

    repositories {
        repository(MavenUnbalancedRoot)
    }

    triggers {
        vcs {  }
        schedule {
            schedulingPolicy = daily {
                hour = 15
                minute = 25
                timezone = "Europe/Amsterdam"
            }
            triggerBuild = always()
        }
    }

    job {
        id = "MavenJob"
        name = "Maven Job: parallel tests"
        steps {
            maven {
                name = "Tests"
                goals = "clean test"
                mavenVersion = defaultProvidedVersion()

                pomLocation = "pom.xml"
            }
            script {
                scriptContent = "echo 'Success'"
                dockerImage = "dkrupkinacontainerregistry.azurecr.io/app:latest"

                executionMode = BuildStep.ExecutionMode.RUN_ONLY_ON_FAILURE
            }
            script {
                scriptContent = "echo %project_parameter% >> file.txt"
            }
        }
        parallelism = 2
        allowReuse = false

        integration("Docker", "DockerRegistry")
        integration("Docker", "AzureRegistry")

        outputFiles {
            pipelineArtifacts("./file.txt")
        }

        requirements {
            equals("teamcity.agent.jvm.os.name", "Linux")
        }
    }

    integrations {
        dockerRegistry {
            id = "DockerRegistry"
            userName = "dariakrup"
            password = "credentialsJSON:82cbcea7-18a1-4a18-9e08-c383d88d5f4f"
        }
        dockerRegistry {
            id = "AzureRegistry"
            url = "https://dkrupkinacontainerregistry.azurecr.io"
            userName = "dkrupkinacontainerregistry"
            password = "credentialsJSON:76a85c3b-ca64-43a3-858f-bs"
        }
    }

})

