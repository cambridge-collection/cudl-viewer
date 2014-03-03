#!/bin/sh

check() {
    dir="$1"
    chsum1=""

    while [[ true ]]
    do
        chsum2=`find src/ -type f -exec md5 {} \;`
        if [[ $chsum1 != $chsum2 ]] ; then           
            eval $2
            chsum1=`find src/ -type f -exec md5 {} \;`
        fi
        sleep 2
    done
}

check $*