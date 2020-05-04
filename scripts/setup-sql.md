# Setup PostgreSQL

```
[root ]# su - postgres
[postgres ]$ psql
psql (12.2)
Type "help" for help.

postgres=# CREATE DATABASE vascloud_sms_campaign;
CREATE DATABASE
postgres=#
postgres=# CREATE ROLE vascloud PASSWORD 'vascloud' LOGIN;
CREATE ROLE
postgres=#
postgres=# GRANT ALL PRIVILEGES ON DATABASE vascloud_sms_campaign TO vascloud;
GRANT
postgres=#
```

Test connection:

```
$ psql -U vascloud -h localhost vascloud_sms_campaign
Password for user vascloud:
psql (12.2)
Type "help" for help.

vascloud_sms_campaign=>
```
