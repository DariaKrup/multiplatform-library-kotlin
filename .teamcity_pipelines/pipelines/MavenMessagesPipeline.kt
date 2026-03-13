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
                name = "Test and deploy"
                goals = "clean test deploy -DaltDeploymentRepository=local-repo::file://local-repo"
                mavenVersion = defaultProvidedVersion()

                pomLocation = "pom.xml"
                runnerArgs = "-Dmaven.test.skip=true"
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
            password = "%docker_io_password%"
        }
        dockerRegistry {
            id = "AzureRegistry"
            url = "https://dkrupkinacontainerregistry.azurecr.io"
            userName = "17c5afa7-698f-4afd-b518-0db1fb4f0984"
            password = "credentialsJSON:e705338c-c9c9-42d4-a4bc-be4160bb969c"
        }
    }

})

