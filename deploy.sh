#!/bin/bash

## 변수 설정
txtrst='\033[1;37m' # White
txtred='\033[1;31m' # Red
txtylw='\033[1;33m' # Yellow
txtpur='\033[1;35m' # Purple
txtgrn='\033[1;32m' # Green
txtgra='\033[1;30m' # Gray

CURRENT_PATH=$(pwd)
MAIN_PATH=${CURRENT_PATH}
LOG_PATH="${CURRENT_PATH}/logs"
BRANCH=master
PROFILE=prod

echo -e "${txtylw}=======================================${txtrst}"
echo -e "${txtgrn}  << Start deploy Triplog 🧐 >>${txtrst}"
echo -e "${txtylw}=======================================${txtrst}"

## github branch 변경 체크
function check_dff() {
  git fetch origin $BRANCH
  master=$(git rev-parse $BRANCH)
  remote=$(git rev-parse origin/$BRANCH)

  if [[ $master == $remote ]]; then
    echo 0
  else
    echo 1
  fi
}

## 저장소 pull
function pull() {
  echo -e "${txtylw}>> Pull Request 🏃${txtrst}"
  git pull origin $BRANCH
}

## build
function build() {
  echo -e ""
  echo -e "${txtylw}>> Build${txtrst}"
  ./gradlew clean build
}

# docker conatiner delete
function deleteDockerContainer() {
  echo -e ""
  echo -e "${txtylw}>> Delete docker container 🏃${txtrst}"
  echo -e "=> Remove previous container..."
  docker rm -f triplog_app
  docker rm -f triplog_db
  echo -e "=> Remove previous image..."
  docker rmi -f triplog-application
#  docker rmi -f mysql:8.0.34
}

## docker image build & docker compose up
function dockerComposeUp() {
  echo -e ""
  echo -e "${txtylw}>> Docker build & compose up 🏃${txtrst}"
  docker compose up -d
}

## main
MAIN_DFF=$(check_dff); ## github branch 변경 체크
if [[ $MAIN_DFF == 1 ]]; then
  echo -e "Repository is changed."
  pull; ## 저장소 pull
else
  echo -e "Repository is not changed."
fi
echo -e ""

# git repository 변경사항 없으면 배포 스크립트 종료.
#if [[ $MAIN_DFF == 0 ]]; then
#  exit 0
#fi
#echo -e ""

## gradle build
build;

# docker conatiner delete
deleteDockerContainer;

## docker image build & docker compose up
dockerComposeUp;

echo -e "${txtylw}=======================================${txtrst}"
echo -e "${txtgrn}  << End deploy Triplog >>${txtrst}"
echo -e "${txtylw}=======================================${txtrst}"

exit 0
