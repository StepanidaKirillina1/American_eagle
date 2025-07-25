# This is a test automation project for the website of the US clothing, footwear, and accessories brand American Eagle (https://www.ae.com/us/en).
##  Contents:


- [Used technologies](https://github.com/StepanidaKirillina1/American_eagle?tab=readme-ov-file#used-technologies)
- [How to run tests](https://github.com/StepanidaKirillina1/American_eagle?tab=readme-ov-file#how-to-run-tests)
- [View Allure Reports](https://github.com/StepanidaKirillina1/American_eagle?tab=readme-ov-file#view-allure-reports)
- [Test Coverage](https://github.com/StepanidaKirillina1/American_eagle?tab=readme-ov-file#test-coverage)


## Used technologies

Java 17, JUnit5, Gradle, Selenium (UI), RestAssured (API), Allure Reports, Lombok, GitHub Actions (CI/CD), Builder (patterns), PageFactory (patterns)


## How to run tests

### To run tests locally from the command line, please:

execute the following command via the command line ./gradlew clean {task.name}, for instance,

`./gradlew clean test`

The tasks' names can be found in build.gradle.

Please ensure all required properties are filled locally.

Also, the values of properties can be passed using the -D flag.

### To run tests on the remote server, please:
1. Navigate to the project’s page [American_eagle](https://github.com/StepanidaKirillina1/American_eagle)
2. Click on the Actions tab 

<img src="images/Actions.png" alt="ActionsTab"/>

3. Click on the job’s name in the left side menu (Run tests)

<img src="images/run tests.png" alt="runTestsJob" />

4. Click on the Run workflow button 

<img src="images/run workflow.png" alt="runWorkflowButton" />

5. Wait for the tests to complete

## View Allure Reports

### To generate Allure reports locally, please:
1.	Navigate to the reports directory: cd ./build/
2.	Run `allure serve`

Please verify that the path to /bin is set in your environment variables.

### See Allure Report overview

<img src="images/allure-results.png" alt="AllureReportOverview" />

### Example of Allure Report

<img src="images/allure-report-example.png" alt="AllureReportExample" />

### Example of Allure Report screenshot on failure

<img src="images/screenshot-on-failure.png" alt="FailureScreenshot" />

### To generate Allure reports in GitHub Actions, please:
1. Navigate to the project’s page [American_eagle](https://github.com/StepanidaKirillina1/American_eagle)
2. Click on the Actions tab after the CI build completion

<img src="images/Actions.png" alt="ActionsTab"/>

3. Click on the latest pages build and deployment link 

<img src="images/pages.png" alt="pagesBuildAndDeployment"/>

4. Click on the deploy link 

<img src="images/deploy.png" alt="deploy"/>

5. You will be redirected to the Allure Reports page

<img src="images/allure-results.png" alt="AllureReportOverview" />

## Test Coverage

The following scenarios were automated:
-	Adding items to the cart;
-	Quick Shop functionality (adding items via Quick Shop);
-	Increasing item quantity using the counter;
-	Editing the quantity of items in the cart;
-	Removing items from the cart;
-	Price verification on the cart and checkout pages;
-	Quantity verification on the cart and checkout pages;
-	The checkout form completion;
-	New user registration;
-	Existing user authorization;
-	Access token retrieval;
-	Free shipping option.


