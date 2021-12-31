FROM openjdk:8

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN echo 'Asia/Shanghai' >/etc/timezone

RUN groupadd boot -g 1008 && useradd boot -g boot -u 1008 -m

VOLUME /home/boot/data/

VOLUME /home/boot/log/

WORKDIR /home/boot/

ADD --chown=boot:boot docker-logback.xml ./logback.xml

ADD --chown=boot:boot docker-boot.sh ./boot.sh

ADD --chown=boot:boot *.class .

ADD --chown=boot:boot *.jar .

COPY --chown=boot:boot site/ ./site/

USER boot

RUN ["chmod","u+x","boot.sh"]

CMD ["./boot.sh"]