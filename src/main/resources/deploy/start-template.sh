#!/usr/bin/env bash
JAVA_HOME={java-bin}
JAVA_OPTS={java-param}
APP_HOME={app-home}

cd ${APP_HOME}
if [[ -f {jar-name}.pid ]]
then
    PID=$(cat {jar-name}.pid)
    Result=$(ps -ax | awk '{print $1}' | grep -e ${PID})
    if [[ ${Result} == ${PID} ]]
    then
    	echo "{jar-name} already-started !PID is $PID"
    	exit
    else
    	rm -fr {jar-name}.pid
    fi
fi
    D=`date +%Y%m%d`
    nohup ${JAVA_HOME} ${JAVA_OPTS} -jar {app-home}{jar-name}.jar > "{jar-name}-${D}.log" &
    sleep 1
    echo $! > {jar-name}.pid
    echo "checking {jar-name} status...."
if [[ -f {jar-name}.pid ]]
then
    PID=$(cat {jar-name}.pid)
    Result=$(ps -ax | awk '{print $1}' | grep -e ${PID})
    if [[ ${Result} == ${PID} ]]
    then
    	echo "start {jar-name} success!PID is $PID"
    else
    	rm -fr {jar-name}.pid
    	echo "start {jar-name} fail!"
    fi
else
    echo "start {jar-name} fail!"
    echo "failed"
fi