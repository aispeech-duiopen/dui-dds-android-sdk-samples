BUILD_HOME=$(cd $(dirname $0); pwd)

echo $BUILD_HOME

function copy_source(){
    rm -rf $BUILD_HOME/output/*
    mkdir -p     $BUILD_HOME/output
    rm -rf $BUILD_HOME/../dds_demo_output/*
    mkdir -p     $BUILD_HOME/../dds_demo_output
    cp -r $BUILD_HOME/* $BUILD_HOME/../dds_demo_output
}

function cut_source(){
    cd $BUILD_HOME/../dds_demo_output
    rm -rf ./.gradle
    rm -rf ./gradle
    rm -rf ./.idea
    rm -rf ./*.iml
    rm -rf ./local.properties
    rm -rf ./build
    rm -rf ./h5demo/build
    rm -rf ./nativedemo/build
    rm -rf ./dds_demo
    rm -rf ./build.sh
}

function zip_demo(){
    cd $BUILD_HOME/../dds_demo_output
    zip -r -q DdsDemo.zip *
}

function copy_demo(){
    mkdir -p     $BUILD_HOME/output
    cp -r $BUILD_HOME/../dds_demo_output/DdsDemo.zip $BUILD_HOME/output
    rm -rf $BUILD_HOME/../dds_demo_output
}

copy_source

cut_source

zip_demo

copy_demo

