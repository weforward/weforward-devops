FROM openjdk:8

RUN groupadd boot -g 1008 && useradd boot -g boot -u 1008 -m

WORKDIR /home/boot/

ARG JAR_FILE

ARG USER

ARG PASS

ARG RES_URL

USER boot

RUN wget --user=${USER} --password=${PASS} ${JAR_FILE}

RUN wget ${RES_URL}boot.sh

RUN ["chmod","u+x","boot.sh"]

CMD ["./boot.sh"]