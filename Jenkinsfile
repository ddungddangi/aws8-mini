pipeline {
    agent any

    parameters {
        string(name: 'VERSION', defaultValue: 'v0.2', description: '도커 이미지 태그')
    }

    environment {
        BACKEND_IMAGE = "gaemineunttungttung/backend:${params.VERSION}"
        FRONTEND_IMAGE = "gaemineunttungttung/frontend:${params.VERSION}"
    }

    stages {
        stage('Git Clone') {
            steps {
                git branch: 'master', credentialsId: 'github-credentials', url: 'https://github.com/ddungddangi/aws8-mini'
            }
        }

        stage('Build Backend Image') {
            steps {
                dir('chat-service') {
                    sh '''
                        echo "Building Backend Docker Image..."
                        docker build -t $BACKEND_IMAGE .
                    '''
                }
            }
        }

        stage('Push Backend Image') {
            steps {
                withDockerRegistry(credentialsId: 'docker-hub-credentials', url: '') {
                    sh '''
                        echo "Pushing Backend Docker Image..."
                        docker push $BACKEND_IMAGE
                    '''
                }
            }
        }

        stage('Build Frontend Image') {
            steps {
                dir('chat-service-front') {
                    sh '''
                        echo "Building Frontend Docker Image..."
                        docker build -t $FRONTEND_IMAGE .
                    '''
                }
            }
        }

        stage('Push Frontend Image') {
            steps {
                withDockerRegistry(credentialsId: 'docker-hub-credentials', url: '') {
                    sh '''
                        echo "Pushing Frontend Docker Image..."
                        docker push $FRONTEND_IMAGE
                    '''
                }
            }
        }
    }
}

