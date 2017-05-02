#!/bin/bash
set -e

git push
mvn test deploy -X
