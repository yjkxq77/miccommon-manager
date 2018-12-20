#!/usr/bin/env bash

echo -e "close {jar-name} ..."
if [[ -f {jar-name}.pid ]]
    then
    PID=$(cat {jar-name}.pid)
    Result=$(ps -ax | awk '{print $1}' | grep -e ${PID})
    if [[ ${Result} == ${PID} ]]
    then
        kill -15 ${PID}
        [[ $? -eq 0 ]] && echo  "[{jar-name} closed success ]"
        echo  "delete {jar-name}.pid"
        rm -fr {jar-name}.pid
        [[ $? -eq 0 ]] && echo  "[{jar-name}.pid delete success ]"
        exit
    else
        echo "{jar-name} already closed..."
    fi
        rm -fr {jar-name}.pid
else
    echo "{jar-name} already closed..."
    echo "success"
fi