FROM openjdk:8

RUN groupadd boot -g 1008 && useradd boot -g boot -u 1008 -m

WORKDIR /home/boot/

ADD --chown=boot:boot *.jar .

ADD --chown=boot:boot *.class .

ADD --chown=boot:boot boot.sh .

USER boot

RUN ["chmod","u+x","boot.sh"]

CMD ["./boot.sh"]