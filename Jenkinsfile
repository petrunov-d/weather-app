pipeline {
	agent { label 'lite' }
	options {
		skipDefaultCheckout(false)
		disableConcurrentBuilds()
		withEnv(['DOCKER_HOST=unix:///var/run/docker.sock'])
	}
    tools {
        maven 'Maven 4.0.0'
    }
	stages {
		stage('Checkout') {
			steps {
				script {
					properties([[$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', daysToKeepStr: '', numToKeepStr: '10']]])
				}
			}
		}
		stage('Build') {
			steps {
				    script {
                        sh 'mvn -DargLine="-Xmx2048m -XX:MaxPermSize=512m" clean package -Pproduction'
                        sh 'sudo docker build --no-cache --tag weatherapp .'
				}
			}
		}
		stage('Run And Or Deploy Somewhere') {
        	 steps {
        			 script {
                         sh 'docker run --publish 8080:8080 weatherapp'
        		}
        	}
        }
	}
}