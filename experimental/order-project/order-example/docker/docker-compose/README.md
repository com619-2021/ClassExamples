# Docker Compose NGINX

This configuration is based upon an example given by Philipp Schmieder here

https://github.com/wmnnd/nginx-certbot

[Details of configuration](../docker-compose/nginx-readme.md)

## usage
rename init-letsencrypt.sh.template to init-letsencrypt.sh

You need to put the actual domain names of your server in init-letsencrypt.sh 
```
domains=(example.org www.example.org)
```