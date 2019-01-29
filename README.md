# BlogRestApi

This REST app allow to create/update/delete posts.
Also user can get all posts or only own and retrieve post info by Id of post.

Application contain only one user role - PUBLISHER.

Authentication provided by Bearer access token 

## Need documentation?
 
    https://blogrestapi.docs.apiary.io/#

## What tools did I use?

The following tools I did used:

    Java 8 (1.8.0_192)
    Gradle (v4.9)
    PostgreSQL (42.2.5)
    SpringBoot (2.0.2.RELEASE)
    SpringSecurity (5.0.8.RELEASE)

## How to run?

You should have Java, Gradle, PostgreSQL. After installation, you need to clone this repository:

    git clone git@github.com:EduardVavrynchuk/BlogRestApi.git

You need create DB and user, also grant user access on DB, commands:

    1. sudo -u postgres createdb blog_db;
    2. sudo -u postgres createuser blog_user;
    3. sudo -u postgres psql blog_db;
    4. ALTER USER "blog_user" WITH PASSWORD 'qwAS123zx';
    5. GRANT ALL PRIVILEGES ON DATABASE blog_db TO blog_user;
    
## And run the program:

    gradle bootRun
