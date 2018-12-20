#!/usr/bin/env bash


echo "check status {jar-name} ..."
if [[ -f {jar-name}.pid ]]
then
    PID=$(cat {jar-name}.pid)
    # shell 指令不能带有空格，大爷的
    Result=$(ps -ax | awk '{print $1}' | grep -e ${PID})
    if [[ ${Result} == ${PID} ]]
    then
        echo {jar-name} PID:${PID}
        echo "alvie"
    else
        echo "close"
    fi
else
    echo "close"
fi
