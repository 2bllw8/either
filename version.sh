#!/usr/bin/env sh
#
# Copyright (c) 2022 2bllw8
# SPDX-License-Identifier: Apache-2.0
#
# Update version script
# Usage:
#   ./version.sh "old.version" "new.version"

function usage {
  echo "Usage: ${0} \$OLD_VERSION \$NEW_VERSION"
}

function main {
  local OLD_VERSION="$1"
  local NEW_VERSION="$2"

  sed -i "s/version = '${OLD_VERSION}'/version = '${NEW_VERSION}'/" lib/build.gradle
  sed -i "s/${OLD_VERSION}/${NEW_VERSION}/" README.md

  git add README.md lib/build.gradle
  git commit -m "Version ${NEW_VERSION}"
  git tag "${NEW_VERSION}"
}

if [ "$#" -ne 2 ]; then
  usage
  exit 1
else
  main "${@}"
fi
