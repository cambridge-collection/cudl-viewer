FROM ubuntu:18.04

COPY docker/deploy-data/deploy.py /usr/local/deploy-data/deploy.py
COPY docker/deploy-data/config.ini /usr/local/deploy-data/config.ini

RUN chmod u+x /usr/local/deploy-data/deploy.py
RUN apt update && apt install -y python3 python3-pip libpq-dev cron openssh-client git
RUN pip3 install psycopg2 gitpython glob3

# Setup SSH Keys (set in ENV Variables ssh_prv_key and ssh_pub_key)
ARG SSH_PUB_KEY
ARG SSH_PRV_KEY

# Authorize SSH Host
RUN mkdir -p /root/.ssh && \
    chmod 0700 /root/.ssh && \
    ssh-keyscan bitbucket.org > /root/.ssh/known_hosts

# Add the keys and set permissions
RUN echo "${SSH_PRV_KEY}" > /root/.ssh/id_rsa && \
    echo "${SSH_PUB_KEY}" > /root/.ssh/id_rsa.pub && \
    chmod 600 /root/.ssh/id_rsa && \
    chmod 600 /root/.ssh/id_rsa.pub

# Setup cron
COPY docker/deploy-data/deploy-cron /etc/cron.d/deploy-cron
RUN chmod 0644 /etc/cron.d/deploy-cron
RUN crontab /etc/cron.d/deploy-cron

# Create the log file to be able to run tail
RUN touch /var/log/cron.log


COPY ./docker/deploy-data/startup.sh /usr/local/deploy-data/startup.sh
RUN chmod u+x /usr/local/deploy-data/startup.sh

CMD /usr/local/deploy-data/startup.sh
