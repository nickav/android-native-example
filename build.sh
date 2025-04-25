#!/bin/bash
set -e

project_root="$(cd "$(dirname "$0")" && pwd -P)"

pushd $project_root

export ANDROID_NDK="$HOME/Library/Android/sdk/ndk/25.1.8937393"
export ANDROID_SDK="$HOME/Library/Android/sdk"
export JAVA_JDK="/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/"

./a.sh

popd