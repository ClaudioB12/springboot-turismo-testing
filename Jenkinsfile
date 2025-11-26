pipeline {
    agent any

    tools {
        // Instala la versión de Maven configurada como "MAVEN_HOME"
        maven "MAVEN_HOME"
    }

    stages {
        stage('Clone') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {  // Aumentado a 5 minutos
                    git branch: 'main', credentialsId: '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', url: 'https://github.com/ClaudioB12/springboot-turismo-testing.git'
                }
            }
        }

        stage('Build') {
            steps {
                timeout(time: 12, unit: 'MINUTES') {  // Aumentado a 12 minutos
                    sh "mvn -DskipTests clean package -f turismo-spring-boot/pom.xml"
                }
            }
        }

        stage('Test') {
            steps {
                timeout(time: 15, unit: 'MINUTES') {  // Aumentado a 15 minutos
                    // Se cambia <test> por <install> para que se genere el reporte de JaCoCo
                    sh "mvn clean install -f turismo-spring-boot/pom.xml"
                }
            }
        }

        stage('Sonar') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {  // Aumentado a 10 minutos
                    withSonarQubeEnv('sonarqube') {
                        sh "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar -Pcoverage -f turismo-spring-boot/pom.xml"
                    }
                }
            }
        }

        stage('Quality gate') {
            steps {
                sleep(10) // segundos
                timeout(time: 5, unit: 'MINUTES') {  // Aumentado a 5 minutos
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Deploy') {
            steps {
                timeout(time: 12, unit: 'MINUTES') {  // Aumentado a 12 minutos
                    // Ejecutar mvn spring-boot:run
                    echo "mvn spring-boot:run -f turismo-spring-boot/pom.xml"
                }
            }
        }
    }
}
