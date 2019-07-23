# web-pc-oracle
Spring boot microservice for exposing REST API to Oracle Chatbot

# kubernetes configuration

`kubectl create secret generic atp-db-creds \
        --from-literal=db.user=admin \
        --from-literal=db.password=<DB_PASSWORD> -n namespace`
