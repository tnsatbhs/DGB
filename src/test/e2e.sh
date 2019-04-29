#!/bin/bash


echo "Create gb1"
curl -s -XPOST "http://127.0.0.1:8080/gradebook/gb1"
echo ""


echo "Should fail: "
curl -XPOST "http://127.0.0.1:8080/gradebook/gb1"
echo ""


echo "List gradebooks on 8080"
curl -s "http://127.0.0.1:8080/gradebook"
echo ""


echo "List gradebooks on 8090"
curl -s "http://127.0.0.1:8090/gradebook"
echo ""


echo "Create secondary on 8090"
curl -XPOST "http://127.0.0.1:8080/secondary/1"
echo ""


echo "List gradebooks on 8090"
curl -s "http://127.0.0.1:8090/gradebook"
echo ""


echo "Delete id=1"
curl -XDELETE "http://127.0.0.1:8080/gradebook/1"
echo ""


echo "List gradebooks on 8090"
curl -s "http://127.0.0.1:8090/gradebook"
echo ""


echo "List gradebooks on 8080"
curl -s "http://127.0.0.1:8080/gradebook"
echo ""
