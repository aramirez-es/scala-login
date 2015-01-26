# Login with Spray
This project is just a test using Spray Framework.

## Requirements
- Scala
- sbt

## Install and Run
First, clone the repo and cd in

```bash
$ git clone git@github.com:aramirez-es/scala-login.git
$ cd scala-login
```

Then, run the project

```bash
$ sbt run
```

Check that server is started:

```bash
Starting application on 8080
[DEBUG] [01/25/2015 21:10:06.861] [on-spray-can-akka.actor.default-dispatcher-4] [akka://on-spray-can/user/IO-HTTP/listener-0] Binding to localhost/127.0.0.1:8080
[DEBUG] [01/25/2015 21:10:06.969] [on-spray-can-akka.actor.default-dispatcher-3] [akka://on-spray-can/system/IO-TCP/selectors/$a/0] Successfully bound to /127.0.0.1:8080
[INFO] [01/25/2015 21:10:06.973] [on-spray-can-akka.actor.default-dispatcher-4] [akka://on-spray-can/user/IO-HTTP/listener-0] Bound to localhost/127.0.0.1:8080
```

Go to your browser and open **http://127.0.0.1:8080**

## Test Cases
These are users and their privileges:
- user_1 => can access to page1
- user_2 => can access to page2
- user_3 => can access to page3
- user_13 => can access to both page1 and page3
- user_12 => can access to both page1 and page2
- user_23 => can access to both page2 and page3
- user_123 => can access to all pages: page1, page2 and page3.

Password is the same for all users **123456**.

## Tests
The number of tests is really low, but you can launch them as follow

```bash
$ sbt test
[info] LoginServiceSpec
[info]
[info] LoginService should
[info] + return a greeting for GET requests to the root path
[info] + return 200 and form page when GET /login page
[info] + return Bad Request when POST to /login without mandatory fields
[info] + return ValidationRejection when POST to /login with invalid credentials
[info] + return redirection and set Cookie when POST to /login with valid credentials
[info]
[info] Total for specification LoginServiceSpec
[info] Finished in 728 ms
[info] 5 examples, 0 failure, 0 error
[info] Passed: Total 5, Failed 0, Errors 0, Passed 5
[success] Total time: 27 s, completed 25-ene-2015 21:17:21
```