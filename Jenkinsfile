pipeline {
    agent any

    parameters {
        string(
            name: 'Environment',
            defaultValue: 'uat2',
            description: 'Environment to run API tests against (dev, test, staging, prod)'
        )
        string(
            name: 'FEATURE_FILE',
            defaultValue: 'src/test/resources/features/phase1/turnOn',
            description: 'Feature file path or directory'
        )
        string(
            name: 'TEST_SUITE',
            defaultValue: 'smoke',
            description: 'Test suite to execute (smoke, regression, sanity, full)'
        )
        booleanParam(
            name: 'SKIP_CLEANUP',
            defaultValue: false,
            description: 'Skip test data cleanup after execution'
        )
    }

    tools {
        maven 'Maven-3.9.6'
        jdk 'JDK-22'
    }

    environment {
        MAVEN_HOME = tool('Maven-3.9.6')
        JAVA_HOME = tool('JDK-22')
        PATH = "${env.PATH};${MAVEN_HOME}\\bin;${JAVA_HOME}\\bin"
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 45, unit: 'MINUTES')
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Validate Parameters') {
            steps {
                script {
                    // Optional: Add parameter validation
                    echo "Validating input parameters..."

                    if (!params.Environment?.trim()) {
                        error("Environment parameter cannot be empty")
                    }

                    if (!params.FEATURE_FILE?.trim()) {
                        error("FEATURE_FILE parameter cannot be empty")
                    }

                    if (!params.TEST_SUITE?.trim()) {
                        error("TEST_SUITE parameter cannot be empty")
                    }

                    echo "✅ All parameters validated successfully"
                }
            }
        }

        stage('Environment Setup & Validation') {
            steps {
                bat '''
                    echo ==========================================
                    echo API Automation Environment Setup
                    echo ==========================================
                    echo MAVEN_HOME: %MAVEN_HOME%
                    echo JAVA_HOME: %JAVA_HOME%
                    echo Environment: %Environment%
                    echo Feature File: %FEATURE_FILE%
                    echo Test Suite: %TEST_SUITE%
                    echo Skip Cleanup: %SKIP_CLEANUP%
                    echo ==========================================

                    echo Validating Maven installation...
                    mvn --version

                    echo Validating Java installation...
                    java -version
                '''
            }
        }

        stage('Dependency Check') {
            steps {
                bat '''
                    echo Checking and downloading dependencies...
                    mvn dependency:resolve
                '''
            }
        }

        stage('Run API Tests') {
            steps {
                script {
                    def mvnCommand = "mvn clean test -Denv=${params.Environment} -D\"cucumber.features\"=${params.FEATURE_FILE}"

                    if (params.TEST_SUITE && params.TEST_SUITE != 'full') {
                        mvnCommand += " -Dcucumber.filter.tags=\"@${params.TEST_SUITE}\""
                    }

                    if (params.SKIP_CLEANUP) {
                        mvnCommand += " -DskipCleanup=true"
                    }

                    bat """
                        echo Running API tests with command: ${mvnCommand}
                        ${mvnCommand}
                    """
                }
            }
            post {
                always {
                    // Publish test results
                    publishTestResults testResultsPattern: 'target/surefire-reports/*.xml'

                    // Archive API response logs if they exist
                    archiveArtifacts artifacts: 'target/logs/**/*', allowEmptyArchive: true
                }
            }
        }

        stage('Generate Reports') {
            parallel {
                stage('Generate Donut Chart') {
                    steps {
                        bat '''
                            echo Generating Donut Chart Report...
                            mvn exec:java -D"exec.mainClass"="com.gng.api.util.DonutChartGenerator" -D"exec.classpathScope"="test"
                        '''
                    }
                }

                stage('Package Extent Reports') {
                    steps {
                        bat '''
                            echo Packaging Extent Reports...
                            cd target
                            if exist extent-reports (
                                "%JAVA_HOME%\\bin\\jar" -cf extent-report.zip extent-reports
                                echo Extent reports packaged successfully
                            ) else (
                                echo No extent-reports directory found
                            )
                        '''
                    }
                }

                stage('Generate Test Summary') {
                    steps {
                        script {
                            // Create a build summary for API tests
                            def summary = """
                            <h3>API Test Execution Summary</h3>
                            <table border="1" style="border-collapse: collapse; width: 100%;">
                                <tr><td style="padding: 8px;"><b>Environment</b></td><td style="padding: 8px;">${params.Environment}</td></tr>
                                <tr><td style="padding: 8px;"><b>Test Suite</b></td><td style="padding: 8px;">${params.TEST_SUITE}</td></tr>
                                <tr><td style="padding: 8px;"><b>Feature File</b></td><td style="padding: 8px;">${params.FEATURE_FILE}</td></tr>
                                <tr><td style="padding: 8px;"><b>Skip Cleanup</b></td><td style="padding: 8px;">${params.SKIP_CLEANUP}</td></tr>
                                <tr><td style="padding: 8px;"><b>Build Time</b></td><td style="padding: 8px;">${new Date()}</td></tr>
                                <tr><td style="padding: 8px;"><b>Jenkins Job</b></td><td style="padding: 8px;">${env.JOB_NAME}</td></tr>
                                <tr><td style="padding: 8px;"><b>Build Number</b></td><td style="padding: 8px;">${env.BUILD_NUMBER}</td></tr>
                            </table>
                            """

                            writeFile file: 'target/api-test-summary.html', text: summary
                        }
                    }
                }
            }
        }

        stage('Publish Reports') {
            steps {
                script {
                    // Publish Allure Reports
                    allure([
                        includeProperties: false,
                        jdk: '',
                        properties: [],
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: 'target/allure-results']]
                    ])

                    // Publish HTML Reports
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/extent-reports',
                        reportFiles: 'index.html',
                        reportName: 'API Test Report'
                    ])

                    // Publish Test Summary
                    publishHTML([
                        allowMissing: true,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target',
                        reportFiles: 'api-test-summary.html',
                        reportName: 'Test Summary'
                    ])
                }
            }
        }

        stage('API Health Check') {
            when {
                expression { params.Environment != 'prod' }
            }
            steps {
                script {
                    echo "Performing post-test API health checks for ${params.Environment} environment..."
                    // Add custom API health check logic here if needed
                    // For example, ping critical endpoints to ensure they're still responsive
                }
            }
        }
    }

    post {
        always {
            // Archive artifacts
            archiveArtifacts artifacts: '''
                target/extent-report.zip,
                target/extent-reports/**/*,
                target/allure-results/**/*,
                target/logs/**/*,
                target/api-test-summary.html
            ''', allowEmptyArchive: true
        }

        success {
            emailext (
                    subject: "✅ API Tests Passed - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """
                        <div style="font-family: Arial, sans-serif;">
                            <h2 style="color: green;">✅ API Test Execution Successful</h2>

                            <h3>Build Information</h3>
                            <table border="1" style="border-collapse: collapse; width: 100%;">
                                <tr style="background-color: #f2f2f2;"><td style="padding: 8px;"><b>Job Name</b></td><td style="padding: 8px;">${env.JOB_NAME}</td></tr>
                                <tr><td style="padding: 8px;"><b>Build Number</b></td><td style="padding: 8px;">${env.BUILD_NUMBER}</td></tr>
                                <tr style="background-color: #f2f2f2;"><td style="padding: 8px;"><b>Environment</b></td><td style="padding: 8px;">${params.Environment}</td></tr>
                                <tr><td style="padding: 8px;"><b>Test Suite</b></td><td style="padding: 8px;">${params.TEST_SUITE}</td></tr>
                                <tr style="background-color: #f2f2f2;"><td style="padding: 8px;"><b>Feature File</b></td><td style="padding: 8px;">${params.FEATURE_FILE}</td></tr>
                                <tr><td style="padding: 8px;"><b>Duration</b></td><td style="padding: 8px;">${currentBuild.durationString}</td></tr>
                            </table>

                            <h3>Quick Links</h3>
                            <ul>
                                <li><a href="${env.BUILD_URL}" style="color: blue;">Build Details</a></li>
                                <li><a href="${env.BUILD_URL}allure" style="color: blue;">Allure Report</a></li>
                                <li><a href="${env.BUILD_URL}API_Test_Report" style="color: blue;">API Test Report</a></li>
                                <li><a href="${env.BUILD_URL}Test_Summary" style="color: blue;">Test Summary</a></li>
                            </ul>

                            <p style="color: green;"><b>All API tests executed successfully!</b></p>
                        </div>
                    """,
                    mimeType: 'text/html',
                    to: '${DEFAULT_RECIPIENTS}',
                    attachmentsPattern: 'target/extent-report.zip'
                )
            }

        failure {
            emailext (
                    subject: "❌ API Tests Failed - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """
                        <div style="font-family: Arial, sans-serif;">
                            <h2 style="color: red;">❌ API Test Execution Failed</h2>

                            <h3>Build Information</h3>
                            <table border="1" style="border-collapse: collapse; width: 100%;">
                                <tr style="background-color: #f2f2f2;"><td style="padding: 8px;"><b>Job Name</b></td><td style="padding: 8px;">${env.JOB_NAME}</td></tr>
                                <tr><td style="padding: 8px;"><b>Build Number</b></td><td style="padding: 8px;">${env.BUILD_NUMBER}</td></tr>
                                <tr style="background-color: #f2f2f2;"><td style="padding: 8px;"><b>Environment</b></td><td style="padding: 8px;">${params.Environment}</td></tr>
                                <tr><td style="padding: 8px;"><b>Test Suite</b></td><td style="padding: 8px;">${params.TEST_SUITE}</td></tr>
                                <tr style="background-color: #f2f2f2;"><td style="padding: 8px;"><b>Feature File</b></td><td style="padding: 8px;">${params.FEATURE_FILE}</td></tr>
                                <tr><td style="padding: 8px;"><b>Duration</b></td><td style="padding: 8px;">${currentBuild.durationString}</td></tr>
                            </table>

                            <h3>Troubleshooting Links</h3>
                            <ul>
                                <li><a href="${env.BUILD_URL}" style="color: blue;">Build Details</a></li>
                                <li><a href="${env.BUILD_URL}console" style="color: blue;">Console Output</a></li>
                                <li><a href="${env.BUILD_URL}allure" style="color: blue;">Allure Report (if available)</a></li>
                            </ul>

                            <div style="background-color: #ffe6e6; padding: 10px; border-left: 5px solid red; margin: 10px 0;">
                                <p style="color: red; margin: 0;"><b>Action Required:</b> API tests have failed. Please check the console logs and reports for detailed error information.</p>
                            </div>
                        </div>
                    """,
                    mimeType: 'text/html',
                    to: '${DEFAULT_RECIPIENTS}',
                )
            }

        unstable {
            emailext (
                    subject: "⚠️ API Tests Unstable - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """
                        <div style="font-family: Arial, sans-serif;">
                            <h2 style="color: orange;">⚠️ API Test Execution Unstable</h2>

                            <h3>Build Information</h3>
                            <table border="1" style="border-collapse: collapse; width: 100%;">
                                <tr style="background-color: #f2f2f2;"><td style="padding: 8px;"><b>Job Name</b></td><td style="padding: 8px;">${env.JOB_NAME}</td></tr>
                                <tr><td style="padding: 8px;"><b>Build Number</b></td><td style="padding: 8px;">${env.BUILD_NUMBER}</td></tr>
                                <tr style="background-color: #f2f2f2;"><td style="padding: 8px;"><b>Environment</b></td><td style="padding: 8px;">${params.Environment}</td></tr>
                                <tr><td style="padding: 8px;"><b>Test Suite</b></td><td style="padding: 8px;">${params.TEST_SUITE}</td></tr>
                                <tr style="background-color: #f2f2f2;"><td style="padding: 8px;"><b>Feature File</b></td><td style="padding: 8px;">${params.FEATURE_FILE}</td></tr>
                                <tr><td style="padding: 8px;"><b>Duration</b></td><td style="padding: 8px;">${currentBuild.durationString}</td></tr>
                            </table>

                            <h3>Review Links</h3>
                            <ul>
                                <li><a href="${env.BUILD_URL}" style="color: blue;">Build Details</a></li>
                                <li><a href="${env.BUILD_URL}allure" style="color: blue;">Allure Report</a></li>
                                <li><a href="${env.BUILD_URL}API_Test_Report" style="color: blue;">API Test Report</a></li>
                            </ul>

                            <div style="background-color: #fff3cd; padding: 10px; border-left: 5px solid orange; margin: 10px 0;">
                                <p style="color: orange; margin: 0;"><b>Note:</b> Some API tests may have failed or been skipped. Please review the reports for details.</p>
                            </div>
                        </div>
                    """,
                    mimeType: 'text/html',
                    to: '${DEFAULT_RECIPIENTS}',
                    attachmentsPattern: 'target/extent-report.zip'
                )
            }
    }
}