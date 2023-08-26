# aws-cognito-auth

In case you do not wish to utilize the Login and Authorization flow features provided by AWS Cognito, this code allows you to generate bearer tokens and perform token validation/authorization on receiving requests.<br>


#### Information required
There are two user variables that must be edited. Locate src/main/resources/application.properties. Enter the **UserPool ID** and **Client ID** from your created User Pool in AWS Cognito. You will find them here-<br>
![Image](ReadmeScreens/UserPoolID.png?raw=true)
![Image](ReadmeScreens/ClientDetails.png?raw=true)
<br>


#### User Groups
Create User groups named "admin" and "user". These Groups discriminate user access. An admin account has access to both adminPage and userPage, however accounts belonging to user group cannot access adminPage. They should look like this-<br>
![Image](ReadmeScreens/GroupsList.png?raw=true)
![Image](ReadmeScreens/AdminGroup.png?raw=true)
<br>


#### Accessing the endpoints
Hit **http://localhost:8080/loginPage** with username and password to generate a bearer token. Note this is a POST request.
Sample Request-<br>
{
    "username": "user",
    "password": "pass"
}

Use this bearer token as an Authorization Header to hit **http://localhost:8080/adminPage**. No body is necessary as this is a GET request.

There is a AWS location where it maintains the Key ID for all generated Bearer Tokens. To access it send a GET Request to **https://cognito-idp.<YOUR REGION NAME>.amazonaws.com/<YOUR COGNITO USER POOL>/.well-known/jwks.json**