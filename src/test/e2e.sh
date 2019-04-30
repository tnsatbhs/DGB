#!/bin/bash

function separate () {
    echo -ne "\n------------\n\n"
}


A="http://127.0.0.1:8080"
B="http://127.0.0.1:8090"


echo "Create gb1"
curl -s -XPOST "$A/gradebook/gb1"
separate


#echo "Should fail: "
#curl -XPOST "$A/gradebook/gb1"
#separate


echo "List gradebooks on 8080"
curl -s "$A/gradebook"
separate


echo "Create student on 8080"
curl -s -XPOST "$A/gradebook/1/student/alex/grade/F"
separate


echo "List gradebook #1 on 8080"
curl -s "$A/gradebook/1"
separate


echo "List gradebooks on 8090"
curl -s "$B/gradebook"
separate


echo "Create secondary on 8090"
curl -XPOST "$B/secondary/1"
separate




echo "List gradebook #1 on 8090"
curl -s "$B/gradebook/1"
separate


echo "Delete id=1"
curl -XDELETE "$A/gradebook/1"
separate


echo "List gradebooks on 8090"
curl -s "$B/gradebook"
separate


echo "List gradebooks on 8080"
curl -s "$A/gradebook"
separate
